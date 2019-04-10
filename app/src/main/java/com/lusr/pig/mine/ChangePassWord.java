package com.lusr.pig.mine;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lusr.pig.MainActivity;
import com.lusr.pig.R;
import com.lusr.pig.Util.StringUtil;
import com.lusr.pig.Util.netWork.HttpMethods;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassWord extends AppCompatActivity {

    ActionBar actionBar;

    @BindView(R.id.acount)
    EditText userName;

    @BindView(R.id.passWord)
    EditText passWord;

    @BindView(R.id.rePassWord)
    EditText rePassWord;

    @BindView(R.id.oldpassWord)
    EditText oldpassWord;


    @BindView(R.id.change)
    Button change;

    Observer<String> observer;


//    @BindView(R.id.changePassWord)
//    TextView changePassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_word);
        ButterKnife.bind(this);
        actionBar = getSupportActionBar();
        actionBar.setTitle("修改密码");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        initData();
    }

    private void initData() {

//        observer = new Observer<String>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//                String json = new Gson().toJson(s);
//                Log.e("data",json);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.e("data",e.getMessage());
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        };

    }

    @OnClick({R.id.acount, R.id.passWord, R.id.change})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.acount:
                break;
            case R.id.passWord:
                break;
            case R.id.change:

                final String userNameText = userName.getText().toString();
                final String passWordText = passWord.getText().toString();
                final String rePassWordText = rePassWord.getText().toString();
                String oldpassWordText = oldpassWord.getText().toString();

                if (StringUtil.isEmpty(userNameText) || StringUtil.isEmpty(passWordText) || StringUtil.isEmpty(rePassWordText)) {
                    Toast.makeText(ChangePassWord.this, "请检查输入", Toast.LENGTH_SHORT).show();
                } else {

                    Call<ResponseBody> call = HttpMethods.getInstance()
                            .checkLogin(userNameText, oldpassWordText);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String user = response.body().string();
                                if (user != null && !"".equals(user)) {

                                    if (passWordText.equals(rePassWordText)) {

                                        Call<ResponseBody> calls = HttpMethods.getInstance()
                                                .changePassWord(userNameText,rePassWordText);

                                        calls.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                try {
                                                    String ss = response.body().string();
                                                    Log.e("data",response.body().string());
                                                    if("fail".equals(ss)){
                                                        Toast.makeText(ChangePassWord.this,"请检查账号和密码",Toast.LENGTH_SHORT).show();
                                                    }else if("success".equals(ss)){
                                                        Toast.makeText(ChangePassWord.this,"修改成功",Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }else {
                                                        Toast.makeText(ChangePassWord.this,"请稍后",Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                Toast.makeText(ChangePassWord.this,"请稍后",Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        Toast.makeText(ChangePassWord.this, "确认修改", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ChangePassWord.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(ChangePassWord.this, "请检查账号或密码", Toast.LENGTH_SHORT).show();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(ChangePassWord.this, "请稍后", Toast.LENGTH_SHORT).show();
                        }
                    });

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
