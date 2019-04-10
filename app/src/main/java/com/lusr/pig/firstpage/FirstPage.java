package com.lusr.pig.firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.lusr.pig.R;
import com.lusr.pig.Util.StringUtil;
import com.lusr.pig.Util.commonAdapter.Com_Adapter;
import com.lusr.pig.Util.commonAdapter.Com_ViewHolder;
import com.lusr.pig.Util.netWork.HttpMethods;
import com.lusr.pig.bean.Home;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FirstPage extends Fragment {
    //设置View对象
    private View mRootView;

    private RecyclerView roomList;
    private List<Home> roomListBean;
    private RecyclerView.Adapter roomListAdapter;
    private Button addHome;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            //任意一个layout对象，比如让message的layout对象，MineFragmendt的layout对象
            mRootView = inflater.inflate(R.layout.firstpage, container, false);
            roomList = mRootView.findViewById(R.id.roomList);
            addHome = mRootView.findViewById(R.id.addHome);
            roomList.setNestedScrollingEnabled(false);

            init();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    private void init() {
        roomListBean = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        roomList.setLayoutManager(linearLayoutManager);
        addHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddHome.class);
                getActivity().startActivity(intent);
            }
        });

        Call<ResponseBody> responseBodyCall = HttpMethods.getInstance()
                .QueryHomeList();
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String json = null;
                try {
                    json = response.body().string();
                    if (json != null || "".equals(json.trim())) {

                        List<Home> homes = StringUtil.getObjectList(json, Home.class);
                        for (Home h :
                                homes) {
                            roomListBean.add(h);
                        }

                    }
                    roomListAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "请稍后", Toast.LENGTH_SHORT).show();
            }
        });

//        for (int i = 0; i < 10; ++i) {
//            Home home = new Home();
//            home.setHomeLink(R.drawable.home_nine_explore);
//            home.setHomeNum(i + "房间");
//            home.setHomeType("母猪");
//            home.setPigNum(i * 3 + "");
//            roomListBean.add(home);
//        }

        roomListAdapter = new Com_Adapter<Home>(getContext(), R.layout.roomlistitem, roomListBean) {

            @Override
            public void convert(Com_ViewHolder holder, final Home home) {
                if (home != null) {
                    holder.setText(R.id.roomId, "房间号:"+home.getHomeNum());
                    holder.setText(R.id.pigNum, "猪数量："+home.getPigNum() + "");
//                    holder.setText(R.id.roomName,"房间名称:" + home.ge)
                    if(home.getHomeType() == 1){
                        holder.setText(R.id.roomType, "类型：公猪");
                    }else{
                        holder.setText(R.id.roomType, "类型：母猪");
                    }

                    int f = holder.getAdapterPosition() % 3;
                    if( f== 0)
                    holder.setImageResource(R.id.roomLink, R.drawable.a_0);
                    else if(f == 1){
                        holder.setImageResource(R.id.roomLink, R.drawable.a_1);
                    }else {
                        holder.setImageResource(R.id.roomLink, R.drawable.a_3);
                    }

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), RoomBeanScan.class);
                            Log.e("data",home.getId());
                            intent.putExtra("roomId", home.getId());
                            intent.putExtra("roomNum",home.getHomeNum());
                            getActivity().startActivity(intent);
                        }
                    });
                }
            }
        };

        roomList.setAdapter(roomListAdapter);
    }

}
