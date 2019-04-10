package com.lusr.pig.anno;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lusr.pig.R;
import com.lusr.pig.Util.StringUtil;
import com.lusr.pig.Util.netWork.HttpMethods;
import com.lusr.pig.bean.Notice;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Page extends AppCompatActivity {

    ActionBar actionBar;
    Notice notice;
    String pageId = "";
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.readCount)
    TextView readCount;


    @BindView(R.id.content)
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        ButterKnife.bind(this);
        actionBar = getSupportActionBar();
        actionBar.setTitle("咨询详情");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        notice = new Notice();
        Intent intent = getIntent();
        pageId = intent.getStringExtra("pageId");
        Log.e("pageId",pageId);
        if(pageId == null || "".equals(pageId.trim())){
            Toast.makeText(Page.this,"请稍后",Toast.LENGTH_SHORT).show();
        }else {

            Call<ResponseBody> responseBodyCall = HttpMethods
                    .getInstance().queryNoticeById(pageId);

            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String json = response.body().string();
                        notice = new Gson().fromJson(json,Notice.class);
                        addRead();
                        content.setText(""+notice.getContent());
                        title.setText(notice.getTitle());
                        time.setText("发布时间："+notice.getCreateDate());
                        readCount.setText("阅读量："+notice.getReadNum());
                        Log.e("data",json);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(Page.this,"请稍后",Toast.LENGTH_SHORT).show();
                }
            });



        }
    }

    private void addRead() {

        notice.setReadNum(notice.getReadNum() + 1);
        Log.e("data last",notice.getReadNum() + notice.getContent() + notice.getStatus() + notice.getCreateDate());
        Call<ResponseBody> addReadNumCall = HttpMethods.getInstance()
                .addReadNum(pageId,notice);

        addReadNumCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Page.this,"请稍后",Toast.LENGTH_SHORT).show();
            }
        });
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
