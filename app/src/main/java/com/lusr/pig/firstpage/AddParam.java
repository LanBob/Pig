package com.lusr.pig.firstpage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lusr.pig.R;
import com.lusr.pig.Util.StringUtil;
import com.lusr.pig.Util.WheelListDialog;
import com.lusr.pig.Util.commonAdapter.Com_Adapter;
import com.lusr.pig.Util.commonAdapter.Com_ViewHolder;
import com.lusr.pig.Util.netWork.HttpMethods;
import com.lusr.pig.bean.HomeParam;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddParam extends AppCompatActivity {

    @BindView(R.id.inputName)
    EditText inputName;
    @BindView(R.id.inputValue)
    EditText inputValue;
    @BindView(R.id.roomNames)
    TextView roomNames;



    @BindView(R.id.conform)
    Button conform;
    @BindView(R.id.send)
    Button send;

//    List<String> list;
    List<HomeParam> list1;

    @BindView(R.id.ParamList)
    RecyclerView ParamList;

    RecyclerView.Adapter adapter;
    String homeId;
    String roomNum;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_param);
        ButterKnife.bind(this);

        actionBar = getSupportActionBar();
        actionBar.setTitle("添加参数");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        list1 = new ArrayList<>();
        init();

        Intent intent = getIntent();
        homeId = intent.getStringExtra("homeId");
        roomNum = intent.getStringExtra("roomNum");
        roomNames.setText("房间号:  " + roomNum);
        String data = intent.getStringExtra("data");
        if(!"-1".equals(data)){
            list1.clear();
            list1.addAll(StringUtil.getObjectList(data,HomeParam.class));
            adapter.notifyDataSetChanged();
        }
    }

    private void init() {

        adapter = new Com_Adapter<HomeParam>(AddParam.this, R.layout.add_room_item, list1) {

            @Override
            public void convert(final Com_ViewHolder holder, HomeParam homeParam) {

                if (homeParam != null) {
                    holder.setText(R.id.paramName, homeParam.getConditionName());
                    if(homeParam.getConditionLimit() == null){
                        holder.setText(R.id.ParamLimitValue, "无");
                    }else {
                        holder.setText(R.id.ParamLimitValue, homeParam.getConditionLimit());
                    }
                    holder.itemView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = holder.getAdapterPosition();
                            list1.remove(pos);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        };
        ParamList.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddParam.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ParamList.setNestedScrollingEnabled(false);
        ParamList.setLayoutManager(linearLayoutManager);

    }

    @OnClick({R.id.conform,R.id.send})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.conform:
                String name = inputName.getText().toString();
                String value = inputValue.getText().toString();

                if(name == null || value == null || "".equals(name) || "".equals(value) || !StringUtil.isInteger(value)){
                    Toast.makeText(AddParam.this,"请检查输入",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AddParam.this,"新增" + name +"，限制值为" + value,Toast.LENGTH_SHORT).show();
                    int flag = 0 ;
                    for(int i = 0;i<list1.size();++i){
                        if(name.equals(list1.get(i).getConditionName())){
                            list1.get(i).setConditionLimit(Integer.valueOf(value)+"");
                            flag = 1;
                            break;
                        }
                    }
                    if(flag != 1){
                        HomeParam homeParam = new HomeParam();
                        homeParam.setConditionData("0");
                        homeParam.setConditionName(name);
                        homeParam.setHomeId(homeId);
                        homeParam.setStatus(0);
                        homeParam.setConditionLimit(Integer.valueOf(value) + "");
                        list1.add(homeParam);
                    }

                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.send:
                Call<ResponseBody> call = HttpMethods.getInstance()
                        .addHomeDataList(homeId,list1);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(AddParam.this,"上传成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
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
