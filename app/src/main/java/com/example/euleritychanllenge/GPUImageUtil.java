package com.example.euleritychanllenge;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageAddBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageAlphaBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBilateralFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBoxBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageCGAColorspaceFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBalanceFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;

public class GPUImageUtil {

    private static GPUImageFilter filter;
    private static String filterName;

    public static GPUImageFilter getFilter(int GPUFlag){
        switch (GPUFlag) {
            case 1:
                filter = new GPUImageGrayscaleFilter();
                filterName = "Gray Scale";
                break;

            case 2:
                filter = new GPUImageAddBlendFilter();
                filterName = "Add Blend";
                break;

            case 3:
                filter = new GPUImageAlphaBlendFilter();
                filterName = "Alpha Blend";
                break;

            case 4:
                filter = new GPUImageBilateralFilter();
                filterName = "Bilateral";
                break;

            case 5:
                filter = new GPUImageBoxBlurFilter();
                filterName = "Box Blur";
                break;

            case 6:
                filter = new GPUImageBrightnessFilter();
                filterName = "Brightness";
                break;

            case 7:
                filter = new GPUImageBulgeDistortionFilter();
                filterName = "Bulge Distortion";
                break;

            case 8:
                filter = new GPUImageColorBalanceFilter();
                filterName = "Color Balance";
                break;

            case 9:
                filter = new GPUImageCGAColorspaceFilter();
                filterName = "CGA Colorspace";
                break;

            case 10:
                filter = new GPUImageColorBlendFilter();
                filterName = "Color Blend";
                break;
            default:
                filter = new GPUImageGrayscaleFilter();
                filterName = "";
        }


        return filter;
    }


    public Bitmap getGPUImageFromAssets(Bitmap bitmap, GPUImage gpuImage,int FilterFlag){
        gpuImage.setImage(bitmap);
        if (FilterFlag != 0) {
            gpuImage.setFilter(getFilter(FilterFlag));
        }

        bitmap = gpuImage.getBitmapWithFilterApplied();

        return bitmap;
    }

    public String getFilterName(){
        return filterName;
    }
}
