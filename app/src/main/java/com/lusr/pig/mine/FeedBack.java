package com.lusr.pig.mine;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lusr.pig.R;
import com.lusr.pig.Util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedBack extends AppCompatActivity {

    @BindView(R.id.feedback_title)
    EditText feedback_title;
    @BindView(R.id.feedback_body)
    EditText feedback_body;
    @BindView(R.id.feedback_buttom)
    Button feedback_buttom;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
        actionBar = getSupportActionBar();
        actionBar.setTitle("反馈");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @OnClick({R.id.feedback_buttom})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.feedback_buttom:
                String feedback_titleText = feedback_title.getText().toString();
                String feedback_bodyText = feedback_body.getText().toString();
                if (StringUtil.isEmpty(feedback_titleText) || StringUtil.isEmpty(feedback_bodyText)) {
                    Toast.makeText(FeedBack.this, "请检查输入", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FeedBack.this, "已经收到您的返回！感谢您的反馈!", Toast.LENGTH_SHORT).show();
                    finish();
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
