package com.lusr.pig.mine;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itingchunyu.badgeview.BadgeTextView;
import com.itingchunyu.badgeview.BadgeViewUtil;
import com.lusr.pig.MainActivity;
import com.lusr.pig.R;

import com.lusr.pig.Util.commonAdapter.Com_Adapter;
import com.lusr.pig.Util.commonAdapter.Com_ViewHolder;
import com.lusr.pig.Util.netWork.HttpMethods;
import com.lusr.pig.bean.Home;
import com.lusr.pig.bean.RoomAllBean;
import com.lusr.pig.firstpage.RoomBeanScan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Warn extends AppCompatActivity {

    private RecyclerView warnList;
    private List<Home> homeList;
    private RecyclerView.Adapter adapter;
    ActionBar actionBar;
    private Button sysNews;
    BadgeTextView badgeTextView;
    BadgeViewUtil badgeViewUtil;
    TextView count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn);

        actionBar = getSupportActionBar();
        actionBar.setTitle("我的通知");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        warnList = findViewById(R.id.warnList);
        sysNews = findViewById(R.id.sysNews);
        badgeTextView = new BadgeTextView(this);
        init();

    }

    private void init() {
        homeList = new ArrayList<>();
        count = findViewById(R.id.count);
//        homeList = new ArrayList<>();
//        for (int i = 0; i < 10; ++i) {
//            Home home = new Home();
//            home.setHomeNum(i + "房间");
//            home.setHomeType(1);
//            home.setPigNum(i * 3);
//            home.setHomeStatus(0);
//            homeList.add(home);
//        }
        badgeTextView.setTargetView(count);
//        badgeTextView.setBadgeCount(10);
        badgeViewUtil = badgeTextView.setmDefaultTopPadding(5).setmDefaultRightPadding(5);
//        badgeViewUtil.setCount(10);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Warn.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        warnList.setLayoutManager(linearLayoutManager);
        warnList.setNestedScrollingEnabled(false);

        adapter = new Com_Adapter<Home>(Warn.this, R.layout.warnitem, homeList) {

            @Override
            public void convert(Com_ViewHolder holder, final Home home) {
                if (home != null) {
                    holder.setText(R.id.roomName, "房间号：" + home.getHomeNum());
                    holder.setText(R.id.pigType, "类型：" + home.getHomeType());
                    holder.setText(R.id.pigNum, "数量：" + home.getPigNum());
                    holder.setText(R.id.roomState, "状态：" + home.getHomeStatus());

                    holder.itemView.findViewById(R.id.removeWarn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(badgeViewUtil.getCount() > 0){
                                badgeViewUtil.setCount(badgeViewUtil.getCount() - 1);
                            }
                            Call<ResponseBody> call = HttpMethods.getInstance()
                                    .deleteAlarm(home.getId());
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        String res = response.body().string();
                                        if(res != null && !"".equals(res)){
                                            homeList.remove(home);
//                                消除警报

                                            adapter.notifyDataSetChanged();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(Warn.this, "请稍后", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    holder.itemView.findViewById(R.id.detil).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                                查看详情
                            Intent intent = new Intent(Warn.this, RoomBeanScan.class);
                            intent.putExtra("roomId", home.getId());
                            startActivity(intent);
                        }
                    });
                }
            }
        };
        warnList.setAdapter(adapter);


        //        通过roomId进行加载
        Call<ResponseBody> responseBodyCall = HttpMethods.getInstance()
                .getAlarmList();

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String res = response.body().string();
                    GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
                    Gson gson = gsonBuilder.create();
                    RoomAllBean[] roomAllBean = gson.fromJson(res, RoomAllBean[].class);
                    int len = roomAllBean.length;
                    badgeViewUtil.setCount(len);

                    for (int i = 0; i < roomAllBean.length; ++i) {
                        Home home = roomAllBean[i].getHome();
                        homeList.add(home);
                    }
                    if(len != 0)
                    adapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Warn.this, "请稍后", Toast.LENGTH_LONG).show();
            }
        });


    }

//    @Override
//    public void onBackPressed() {
//        int i = badgeViewUtil.getCount();
//        Intent intent1 = new Intent();
//        intent1.putExtra("count",i+"");
//        setResult(RESULT_OK,intent1);
//        super.onBackPressed();
//        finish();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //对用户按home icon的处理，本例只需关闭activity，就可返回上一activity，即主activity。

                finish();

                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
