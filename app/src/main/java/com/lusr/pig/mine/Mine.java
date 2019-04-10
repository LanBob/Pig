package com.lusr.pig.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itingchunyu.badgeview.BadgeTextView;
import com.itingchunyu.badgeview.BadgeViewUtil;
import com.lusr.pig.R;
import com.lusr.pig.Util.StringUtil;
import com.lusr.pig.Util.netWork.HttpMethods;
import com.lusr.pig.bean.Home;
import com.lusr.pig.bean.RoomAllBean;
import com.lusr.pig.firstpage.RoomBeanScan;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Mine extends Fragment implements View.OnClickListener {
    //设置View对象
    private View mRootView;
    private RelativeLayout news;
    private RelativeLayout changePassWord;
    private RelativeLayout feedBack;
    private Button logOut;
    private TextView sysNews;
    BadgeTextView badgeTextView;
    BadgeViewUtil badgeViewUtil;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            //任意一个layout对象，比如让message的layout对象，MineFragmendt的layout对象
            mRootView = inflater.inflate(R.layout.mine, container, false);
            news = mRootView.findViewById(R.id.news);
            changePassWord = mRootView.findViewById(R.id.changePassWord);
            feedBack = mRootView.findViewById(R.id.feedBack);
            logOut = mRootView.findViewById(R.id.logOut);
            sysNews = mRootView.findViewById(R.id.sysNews);
            badgeTextView = new BadgeTextView(getActivity());
            badgeTextView.setTargetView(sysNews);
            badgeViewUtil = badgeTextView.setmDefaultTopPadding(5).setmDefaultRightPadding(5);
//            badgeViewUtil.setCount(10);

            news.setOnClickListener(this);
            changePassWord.setOnClickListener(this);
            feedBack.setOnClickListener(this);
            logOut.setOnClickListener(this);
            getData();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    private void getData() {
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "请稍后", Toast.LENGTH_LONG).show();
            }
        });

    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.news:
                Toast.makeText(getContext(),"新消息通知",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),Warn.class);
                getActivity().startActivity(intent);
                break;
            case R.id.changePassWord:
                Toast.makeText(getContext(),"修改密码",Toast.LENGTH_SHORT).show();
                Intent intent1 =  new Intent(getActivity(),ChangePassWord.class);
                getActivity().startActivity(intent1);
                break;
            case R.id.feedBack:
                Toast.makeText(getContext(),"用户反馈",Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(),"修改密码",Toast.LENGTH_SHORT).show();
                Intent intent3 =  new Intent(getActivity(),FeedBack.class);
                getActivity().startActivity(intent3);
                break;
            case R.id.logOut:
                Toast.makeText(getContext(),"退出登录",Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(getActivity(),Login.class);
                getActivity().startActivity(intent2);
                getActivity().finish();
                break;
        }
    }
}
