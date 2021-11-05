package com.example.euleritychanllenge.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.euleritychanllenge.GlobleValue;
import com.example.euleritychanllenge.model.ImageData;
import com.example.euleritychanllenge.model.Upload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

public class ApiHandler {

    private GlobleValue globleValue = new GlobleValue();
    private Retrofit retrofit;

    public Retrofit getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(globleValue.getApiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public JsonPlaceHolderApi getJsonPlaceHolderApi (){
        return getRetrofit().create(JsonPlaceHolderApi.class);
    }

    public void getImageData(final ApiCallBack apiCallBack) {
        //calling api
        Call<ArrayList<ImageData>> call = getJsonPlaceHolderApi().getImageData();
        call.enqueue(new Callback<ArrayList<ImageData>>() {
            @Override
            public void onResponse(Call<ArrayList<ImageData>> call, Response<ArrayList<ImageData>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                ArrayList<ImageData> posts = response.body();

                apiCallBack.success(posts);
            }

            @Override
            public void onFailure(Call<ArrayList<ImageData>> call, Throwable t) {
                apiCallBack.failure(t);
            }
        });
    }

    public void getUploadUrl(final ApiCallBack apiCallBack) {
        //calling api
        Call<ImageData> call = getJsonPlaceHolderApi().getUploadUrl();
        call.enqueue(new Callback<ImageData>() {
            @Override
            public void onResponse(Call<ImageData> call, Response<ImageData> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                ImageData imageData = response.body();

                ArrayList<ImageData> posts = new ArrayList<>();
                posts.add(imageData);

                apiCallBack.success(posts);
            }

            @Override
            public void onFailure(Call<ImageData> call, Throwable t) {
                apiCallBack.failure(t);
            }
        });
    }

    public void postImage(String url , File editedFile, String originalUrl ,final ApiCallBack apiCallBack) {
        File file = editedFile;


        RequestBody appID = RequestBody.create( MediaType.parse("multipart/form-data"), "samboszcompany@gmail.com");
        RequestBody original = RequestBody.create( MediaType.parse("multipart/form-data"), originalUrl);

        MultipartBody.Part imagenPerfil = null;
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        imagenPerfil = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Call<ResponseBody> call = getJsonPlaceHolderApi().editUser(url,appID,original,imagenPerfil);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

               apiCallBack.success(new ArrayList());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }


}
