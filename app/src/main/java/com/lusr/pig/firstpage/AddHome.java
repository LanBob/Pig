package com.lusr.pig.firstpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lusr.pig.R;
import com.lusr.pig.Util.StringUtil;
import com.lusr.pig.Util.netWork.HttpMethods;
import com.lusr.pig.bean.Home;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddHome extends AppCompatActivity {

//    @BindView(R.id.addName)
//    EditText addName;
    @BindView(R.id.addPigType)
    EditText addPigType;
    @BindView(R.id.pigNum)
    EditText pigNum;
    @BindView(R.id.homeSize)
    EditText homeSize;


    @BindView(R.id.add)
    Button add;

    ActionBar actionBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_home);
        actionBar = getSupportActionBar();
        actionBar.setTitle("添加房间");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.add})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.add:
//                String name = addName.getText().toString();
                String type = addPigType.getText().toString();
                String pignum = pigNum.getText().toString();
                String size = homeSize.getText().toString();
                if ( StringUtil.isEmpty(type) || StringUtil.isEmpty(pignum) || StringUtil.isEmpty(size) || !StringUtil.isInteger(size) || !StringUtil.isInteger(pignum)) {
                    Toast.makeText(AddHome.this, "请检查输入", Toast.LENGTH_SHORT).show();
                }else {
                    if (!"公猪".equals(type) && !"母猪".equals(type)) {
                        Toast.makeText(AddHome.this, "请检查猪的类型", Toast.LENGTH_SHORT).show();
                    }else {
                        Home home = new Home();
                        home.setHomeStatus(0);
                        home.setPigNum(Integer.valueOf(pignum));
                        home.setHomeSize(Integer.valueOf(size));
                        home.setHomeLink("");
                        if ("公猪".equals(type)){
                            home.setHomeType(1);
                        }else {
                            home.setHomeType(0);
                        }
                        home.setHomeNum(StringUtil.getRandom());
                        home.setId(StringUtil.getRandom());

                        Call<ResponseBody> responseBodyCall = HttpMethods.getInstance()
                                .addHome(home);

                        responseBodyCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                String result = null;
                                try {
                                    result = response.body().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if("Success".equals(result )){
                                    try {
                                        String data = response.body().string();
                                        Log.e("data",data);
                                        Toast.makeText(AddHome.this, "成功", Toast.LENGTH_LONG).show();
                                        finish();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    Toast.makeText(AddHome.this, "出错啦，请联系管理员...", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(AddHome.this, "请稍后", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
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
}
