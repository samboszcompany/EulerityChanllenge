package com.example.euleritychanllenge.api;

import com.example.euleritychanllenge.model.ImageData;

import java.util.ArrayList;

public interface ApiCallBack {
    void success(ArrayList result);
    void failure(Throwable t);
}
