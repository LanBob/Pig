package com.lusr.pig.firstpage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lusr.pig.R;
import com.lusr.pig.Util.StringUtil;
import com.lusr.pig.Util.commonAdapter.Com_Adapter;
import com.lusr.pig.Util.commonAdapter.Com_ViewHolder;
import com.lusr.pig.Util.netWork.HttpMethods;
import com.lusr.pig.bean.Home;
import com.lusr.pig.bean.HomeParam;
import com.lusr.pig.bean.Param;
import com.lusr.pig.bean.RoomAllBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomBeanScan extends AppCompatActivity {

    private String roomId;

    @BindView(R.id.roomNum)
    public TextView roomNum;

    @BindView(R.id.pigType)
    public TextView pigType;

    @BindView(R.id.pigNum)
    public TextView pigNum;

    @BindView(R.id.date)
    public TextView date;

    @BindView(R.id.addPatam)
    public Button addPatam;

    @BindView(R.id.paramData)
    RecyclerView paramData;

    RecyclerView.Adapter adapter;
    List<HomeParam> homeParams;

    HomeParam[] homeDetailList;

    ActionBar actionBar;
    String roomNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_scan);

        actionBar = getSupportActionBar();
        actionBar.setTitle("房间详情");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        ButterKnife.bind(this);
        homeParams = new ArrayList<>();

        Intent intent = getIntent();
        roomId = intent.getStringExtra("roomId");
        roomNumber = intent.getStringExtra("roomNum");
        initData();

//        通过roomId进行加载
        Call<ResponseBody> responseBodyCall = HttpMethods.getInstance()
                .getHomeAllById(roomId);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String res = response.body().string();
                    GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
                    Gson gson = gsonBuilder.create();
                    RoomAllBean roomAllBean = gson.fromJson(res, RoomAllBean.class);
                    Home home = roomAllBean.getHome();
                    if(home != null){

                        roomNum.setText("房间号:"+home.getHomeNum());

                        if(home.getHomeType() == 0){
                            pigType.setText("类型： 母猪");
                        }else{
                            pigType.setText("类型： 公猪");
                        }
                        pigNum.setText("数量: "+home.getPigNum() +"/" + home.getHomeSize());
                        date.setText(StringUtil.getCreateDate());
                    }


                    homeDetailList = roomAllBean.getHomedataList();
                    if (homeDetailList != null)
                        for (int i = 0; i < homeDetailList.length; ++i) {
                            homeParams.add(homeDetailList[i]);
                        }
                    adapter.notifyDataSetChanged();
                    if (homeDetailList== null || homeDetailList.length == 0) {
                        Toast.makeText(RoomBeanScan.this, "当前房间未参数设置", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RoomBeanScan.this, "请稍后", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void initData() {
        adapter = new Com_Adapter<HomeParam>(RoomBeanScan.this, R.layout.sacn_item, homeParams) {

            @Override
            public void convert(Com_ViewHolder holder, HomeParam homeParam) {

                if (homeParam != null) {
                    holder.setText(R.id.paramName, homeParam.getConditionName());
                    if(homeParam.getConditionLimit() == null){
                        holder.setText(R.id.ParamLimitValue, "无");
                    }else {
                        holder.setText(R.id.ParamLimitValue, homeParam.getConditionLimit());
                    }
                    holder.setText(R.id.ParamCurrentValue, homeParam.getConditionData());
                }
            }
        };
        paramData.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RoomBeanScan.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        paramData.setLayoutManager(linearLayoutManager);
    }

    @OnClick({R.id.addPatam})
    public void click(View view) {
        switch (view.getId()) {

            case R.id.addPatam:
                Intent intent1 = new Intent(RoomBeanScan.this, AddParam.class);
                intent1.putExtra("homeId",roomId);
                GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
                Gson gson = gsonBuilder.create();
                if(homeDetailList != null){
                    String json = gson.toJson(homeDetailList);
                    intent1.putExtra("data",json);
                    intent1.putExtra("roomNum",roomNumber);
                }else {
                    intent1.putExtra("data","-1");
                }
                startActivity(intent1);
//                finish();
                break;
        }
    }

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

    @Override
    protected void onRestart() {
        super.onRestart();

//        通过roomId进行加载
        Call<ResponseBody> responseBodyCall = HttpMethods.getInstance()
                .getHomeAllById(roomId);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String res = response.body().string();
                    GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
                    Gson gson = gsonBuilder.create();
                    RoomAllBean roomAllBean = gson.fromJson(res, RoomAllBean.class);
                    Home home = roomAllBean.getHome();
                    if(home != null){

                        roomNum.setText("房间号:"+home.getHomeNum());

                        if(home.getHomeType() == 0){
                            pigType.setText("类型： 母猪");
                        }else{
                            pigType.setText("类型： 公猪");
                        }
                        pigNum.setText("数量: "+home.getPigNum() +"/" + home.getHomeSize());
                        date.setText(StringUtil.getCreateDate());

                    }


                    homeDetailList = roomAllBean.getHomedataList();
                    homeParams.clear();
                    if (homeDetailList != null)
                        for (int i = 0; i < homeDetailList.length; ++i) {
                            homeParams.add(homeDetailList[i]);
                        }
                    adapter.notifyDataSetChanged();
                    if (homeDetailList== null || homeDetailList.length == 0) {
                        Toast.makeText(RoomBeanScan.this, "当前房间未参数设置", Toast.LENGTH_LONG).show();
                    }
                    Log.e("restart","size" + homeParams.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RoomBeanScan.this, "请稍后", Toast.LENGTH_LONG).show();
            }
        });
    }
}

