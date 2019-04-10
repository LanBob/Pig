package com.lusr.pig.anno;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lusr.pig.R;
import com.lusr.pig.Util.Roll.ColorPointHintView;
import com.lusr.pig.Util.Roll.LoopPagerAdapter;
import com.lusr.pig.Util.Roll.RollPagerView;
import com.lusr.pig.Util.StringUtil;
import com.lusr.pig.Util.commonAdapter.Com_Adapter;
import com.lusr.pig.Util.commonAdapter.Com_ViewHolder;
import com.lusr.pig.Util.netWork.HttpMethods;
import com.lusr.pig.bean.Notice;
import com.lusr.pig.bean.PageContent;
import com.lusr.pig.mine.Login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Anno extends Fragment {


    //设置View对象
    private View mRootView;
    private List<Notice> noticeList;
    private RecyclerView noticeRecyclerview;
    private RecyclerView.Adapter noticeAdapter;
    public RollPagerView mViewPager;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            //任意一个layout对象，比如让message的layout对象，MineFragmendt的layout对象
            mRootView = inflater.inflate(R.layout.announce, container, false);

            noticeRecyclerview = mRootView.findViewById(R.id.pageList);
            noticeRecyclerview.setNestedScrollingEnabled(false);
            mViewPager = mRootView.findViewById(R.id.rollview_pager);
            init();
            //得到一个View，指定的Item的View
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    private void init() {
        noticeList = new ArrayList<>();
        Call<ResponseBody> responseBodyCall = HttpMethods.getInstance()
                .queryNoticeList();

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    List<Notice> list = StringUtil.getObjectList(json, Notice.class);
                    for (Notice no :
                            list) {
                        noticeList.add(no);
                    }
                    noticeAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "请稍后", Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        noticeRecyclerview.setLayoutManager(linearLayoutManager);


//        初始化
//        for (int i = 0; i < 10; ++i) {
//            Notice notice = new Notice();
//            notice.setTitle("震惊！他竟然是马儿儿子");
//            notice.setContent("他竟然他竟然是他竟然他竟然是马他他竟然是马儿儿子他竟然是马儿儿子他竟然是马儿儿子他竟然是马儿儿子竟然是马儿儿子他竟然是马儿儿子儿他竟然是马儿儿子儿子他竟然是马儿儿子是马儿儿子马他他竟然是马儿儿子他竟然是马儿儿子他竟然是马儿儿子他竟然是马儿儿子竟然是马儿儿子他竟然是马儿儿子儿他竟然是马儿儿子儿子他竟然是马儿儿子是马儿儿子");
//            notice.setCreateDate("1997-11-01 18:00:00");
//            notice.setReadNum(0);
//            noticeList.add(notice);
//        }

        noticeAdapter = new Com_Adapter<Notice>(getContext(), R.layout.pageitem, noticeList) {


            @Override
            public void convert(Com_ViewHolder holder, final Notice notice) {
                if (notice != null) {

                    holder.setText(R.id.pageTitle, notice.getTitle());
                    holder.setText(R.id.pageIntro, notice.getContent());
                    holder.setText(R.id.pageDate, notice.getCreateDate());
                    holder.setText(R.id.pageReaderCount, "阅读量 ： " + notice.getReadNum() + "");
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), Page.class);
                            intent.putExtra("pageId", notice.getId());
                            Log.e("pageId",notice.getId());
                            getActivity().startActivity(intent);
                        }
                    });

                }
            }
        };
        noticeRecyclerview.setAdapter(noticeAdapter);
//        Rool

        final ImageLoopAdapter loopAdapter = new ImageLoopAdapter(mViewPager);
        mViewPager.setAdapter(loopAdapter);
        mViewPager.setHintView(new ColorPointHintView(getContext(), Color.YELLOW, Color.WHITE));

//        Rool

    }


    ///=================Search
    private class ImageLoopAdapter extends LoopPagerAdapter {
        int[] imgs = new int[]{
                R.drawable.top
//                R.drawable.top3,
//                R.drawable.home_nine_explore
        };

        public ImageLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            view.setImageResource(imgs[position]);
            return view;
        }

        @Override
        public int getRealCount() {
            return imgs.length;
        }

    }
}