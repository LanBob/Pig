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
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

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

public class Login extends AppCompatActivity {

    ActionBar actionBar;

    @BindView(R.id.acount)
    EditText acount;

    @BindView(R.id.passWord)
    EditText passWord;

    @BindView(R.id.login)
    Button button;

    @BindView(R.id.changePassWord)
    TextView changePassWord;

//    @BindView(R.id.response)
//    TextView responseText;

//    Observer<String> observer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        actionBar = getSupportActionBar();
        actionBar.setTitle("登录");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @OnClick({ R.id.login, R.id.changePassWord})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.acount:
                break;
            case R.id.passWord:
                break;
            case R.id.login:

                String userNameText = acount.getText().toString();
                String passWordText = passWord.getText().toString();
                if (StringUtil.isEmpty(userNameText) || StringUtil.isEmpty(passWordText)) {
//                    Toast.makeText(Login.this, "登录", Toast.LENGTH_SHORT).show();
                } else {

                    Call<ResponseBody> call = HttpMethods.getInstance()
                            .checkLogin(userNameText, passWordText);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String user = response.body().string();
                                if (user != null && !"".equals(user)) {
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, "请检查账号或密码", Toast.LENGTH_SHORT).show();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(Login.this, "请稍后", Toast.LENGTH_SHORT).show();
                        }
                    });

//                    if ("1158".equals(passWordText)) {
//                        Intent intent = new Intent(Login.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Toast.makeText(Login.this, "请检查输入", Toast.LENGTH_SHORT).show();
//                    }
                }

                break;
            case R.id.changePassWord:
//                Toast.makeText(Login.this, "修改密码", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, ChangePassWord.class);
                startActivity(intent);
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
