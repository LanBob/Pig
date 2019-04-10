package com.lusr.pig.Util.netWork;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lusr.pig.bean.Home;
import com.lusr.pig.bean.HomeParam;
import com.lusr.pig.bean.Notice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class HttpMethods {

    public static final String BASE_URL = MyUrl.getUrl();

    private static final int DEFAULT_TIMEOUT = 5000;
    private Retrofit retrofit;
    private Service service;

    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            //打印retrofit日志
            Log.i("RetrofitLog","retrofitBack = "+message);
        }
    });


    //构造方法私有
    private HttpMethods() {

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        httpClientBuilder
                .addInterceptor(loggingInterceptor)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        service = retrofit.create(Service.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    //对外的方法=====================================================================================================

    public Call<ResponseBody> checkLogin(String account,String passwrod){
        return service.checkLogin(account,passwrod);
    }

    public Call<ResponseBody> changePassWord(String account, String newPassWord){
        return service.changePassWord(account,newPassWord);
    }

    public Call<ResponseBody> queryNoticeList(){
        return service.queryNoticeList();
    }


    public Call<ResponseBody> queryNoticeById(String id){
        return service.queryNoticeById(id);
    }


    public Call<ResponseBody> addReadNum(String pageId, Notice notice){
//        String json = new Gson().toJson(notice);
        return service.saveNotice(pageId,notice);
    }


//    房间信息
    public Call<ResponseBody> QueryHomeList(){
        return service.QueryHomeList();
    }

    public Call<ResponseBody> queryHomeById(String id){
        return service.queryHomeById(id);
    }

//    获取房间参数
    public Call<ResponseBody> getHomeAllById(String id){
        return service.getHomeAllById(id);
    }

    public Call<ResponseBody> addHomeDataList(String homeId,List<HomeParam> list){
//        GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
//        Gson gson = gsonBuilder.create();
//        String json = gson.toJson(list);
//        Log.e("data",json);
        return service.addHomeDataList(homeId,list);
    }


//    增加房间

    public Call<ResponseBody> addHome(Home home) {
//        String json = new Gson().toJson(home);
        GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(home);
        String jj = null;
        Log.e("json",json);
//{"homeLink":"","homeNum":"311","homeSize":55,"homeStatus":0,"homeType":0,"id":"311","pigNum":5}
//        try {
//             jj = new String(json.getBytes(),"utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        return service.addHome(home);
    }


    //    获取房间参数
    public Call<ResponseBody> getAlarmList(){
        return service.getAlarmList();
    }


//    /**
//     * 新增参数
//     * @param id
//     * @param homeParam
//     * @return
//     */
//    public Call<ResponseBody> addHomeParam(String id, HomeParam homeParam){
//        String json = new Gson().toJson(homeParam);
//        return service.addHomeParam(id,json);
//    }





//
//
//    /**
//     * 登录
//     *
//     * @param map
//     * @param observer
//     */
//    public void login_check(Map map, Observer observer) {
//        String json = new Gson().toJson(map);
//        service.login_check(json, 1)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
//    }
//
//    public void changePassword(Map map, Observer observer) {
//        String json = new Gson().toJson(map);
//        service.login_check(json, 3)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
//    }
//
//    public void loginByMessageCode(Map map, Observer observer) {
//        String json = new Gson().toJson(map);
//        service.login_check(json, 2)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
//    }
//



    //对外的方法end=====================================================================================================


    public static MultipartBody.Part[] filesMultipartBody(List<File> fileList) {
        MultipartBody.Part[] partArray = new MultipartBody.Part[fileList.size()];
        for (int i = 0; i < fileList.size(); ++i) {
            File file = fileList.get(i);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file",
                    file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            partArray[i] = filePart;
        }
        return partArray;
    }


    /**
     * 图片，构造MultipartBody
     *
     * @param fileList
     * @return
     */
    public static MultipartBody filesToMultipartBody(List<File> fileList) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        for (File file : fileList) {
            //这里自动设置了图片的名称
            String fileName = file.getName();
            // TODO: 16-4-2  这里为了简单起见，没有判断file的类型
            RequestBody requestBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
            //TODO 根据文件名设置contentType
            builder.addPart(Headers.of("Content-Disposition",
                    "form-data; name=\"image\"; filename=\"" + fileName + "\""),
                    requestBody);
        }
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }

    //判断文件类型
    private static String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


}
