package com.example.euleritychanllenge.api;

import android.database.Observable;

import com.example.euleritychanllenge.model.ImageData;
import com.example.euleritychanllenge.model.Upload;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi {


    @GET("image")
    Call<ArrayList<ImageData>> getImageData();

    @GET("upload")
    Call<ImageData> getUploadUrl();

    @POST("posts")
    Call<Upload> createPost(@Body Upload upload);

    @Multipart
    @POST
    Call<ResponseBody> editUser(
                        @Url String url,
                        @Part("appid") RequestBody appid,
                        @Part("original") RequestBody original,
                        @Part MultipartBody.Part image);

}
