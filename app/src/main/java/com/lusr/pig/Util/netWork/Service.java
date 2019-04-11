package com.lusr.pig.Util.netWork;

import com.lusr.pig.bean.Home;
import com.lusr.pig.bean.HomeData;
import com.lusr.pig.bean.HomeParam;
import com.lusr.pig.bean.Notice;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {
//=======================================================对外方法

    @POST("user/checklogin")
    @FormUrlEncoded
    Call<ResponseBody> checkLogin(@Field("account")String account,@Field("password")String password);

    @FormUrlEncoded
    @POST("user/changePwd")
    Call<ResponseBody> changePassWord(@Field("account")String account, @Field("newPwd")String password);



    @GET("Notice/getNoticeList")
    Call<ResponseBody> queryNoticeList();

    @GET("Notice/getNotice/{id}")
    Call<ResponseBody> queryNoticeById(@Path("id")String id);

//    @FormUrlEncoded
    @POST("Notice/saveNotice/{id}")
    @Headers({"Content-Type:application/json;charset=UTF-8"})
    Call<ResponseBody> saveNotice(@Path("id")String id,@Body Notice notice);



//    房间
 //   /Home/getHomeList
    @GET("Home/getHomeList")
    Call<ResponseBody> QueryHomeList();

    @GET("Home/getHome")
    Call<ResponseBody> queryHomeById(@Query("id")String id);

//    @FormUrlEncoded
    @POST("Home/addHome")
    @Headers({"Content-Type:application/json;charset=utf-8"})
    Call<ResponseBody> addHome(@Body Home home);

    @GET("Home/getHomeAllById/{id}")
    Call<ResponseBody> getHomeAllById(@Path("id") String id);


    @FormUrlEncoded
    @POST("Home/addHomeParam")
    @Headers({"Content-Type:application/json;charset=UTF-8"})
    Call<ResponseBody> addHomeParam(@Query("homeId")String homeId,@Field("homeparam")String homeparam);
    //http://120.78.177.14:8099/Home/getHomeAllById/311


//    新增参数

//    @FormUrlEncoded
    @POST("Home/addHomeDataList/{homeId}")
    @Headers({"Content-Type:application/json;charset=utf-8"})
    Call<ResponseBody> addHomeDataList(@Path("homeId")String homeId,@Body List<HomeParam> homedataList);
//=======================================================对外方法

    @GET("Home/getAlarmList")
    Call<ResponseBody> getAlarmList();

//    @FormUrlEncoded
    @POST("Home/deleteAlarm/{homeId}")
    @Headers({"Content-Type:application/json;charset=UTF-8"})
    Call<ResponseBody> deleteAlarm(@Path("homeId") String homeId);


    @POST("Home/deleteHome/{id}")
    @Headers({"Content-Type:application/json;charset=UTF-8"})
    Call<ResponseBody> delete(@Path("id") String id);

}
