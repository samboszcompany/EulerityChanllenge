package com.example.euleritychanllenge.ui.imageEditor;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.euleritychanllenge.GPUImageUtil;
import com.example.euleritychanllenge.R;
import com.example.euleritychanllenge.api.ApiCallBack;
import com.example.euleritychanllenge.api.ApiHandler;
import com.example.euleritychanllenge.databinding.FragmentEditorBinding;
import com.example.euleritychanllenge.databinding.FragmentHomeBinding;
import com.example.euleritychanllenge.model.ImageData;
import com.example.euleritychanllenge.ui.home.HomeViewModel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import ja.burhanrashid52.photoeditor.PhotoEditorView;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImage3x3ConvolutionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;

public class EditorFragment extends Fragment {

    private FragmentEditorBinding binding;
    String imageUrl = "";
    String fileName = "";
    Bitmap theBitmap;
    String uploadUrl = "";
    private ImageView img_Display;
    ApiHandler apiHandler = new ApiHandler();
    GPUImage gpuImage;
    private GPUImageUtil gpuImageUtil = new GPUImageUtil();
    private int filterFlag = 0;
    Button btn_AddFilter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentEditorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btn_AddFilter = binding.btnAddFilter;
        final Button btn_AddText = binding.btnAddText;
        final Button btn_Upload = binding.btnUpload;
        img_Display = binding.imgDisplay;
        gpuImage = new GPUImage(getContext());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            imageUrl = bundle.getString("url");
        }

        apiHandler.getUploadUrl(new ApiCallBack() {
            @Override
            public void success(ArrayList result) {
                ArrayList<ImageData> posts = result;
                if (posts.size() > 0) {
                    uploadUrl = posts.get(0).getUrl();
                }
            }

            @Override
            public void failure(Throwable t) {
            }
        });

        new Thread() {
            @Override
            public void run() {
                try {
                    theBitmap = Glide.
                            with(getActivity()).
                            asBitmap().
                            load(imageUrl)
                            .submit()
                            .get();

                    theBitmap = Bitmap.createScaledBitmap(theBitmap,(int)(theBitmap.getWidth()*0.3), (int)(theBitmap.getHeight()*0.3), true);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            img_Display.setImageBitmap(theBitmap);
                        }
                    });


                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();

        btn_Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBtnSavePng();
            }
        });

        btn_AddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText taskEditText = new EditText(getContext());
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("Add Text")
                        .setMessage("Currently only can add on center")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                drawTextToBitmap(getContext(),task);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();

            }
        });

        btn_AddFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorFilter();
            }
        });

        return root;
    }

    public void onBtnSavePng() {
        new Thread() {
            @Override
            public void run() {
                try {
                    theBitmap = gpuImageUtil.getGPUImageFromAssets(theBitmap,gpuImage,filterFlag);
                    fileName = Calendar.getInstance().getTime() + ".jpg";
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                    File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    File file = new File(directory + "/Camera", fileName);
                    values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());

                    Uri uri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    try (OutputStream output = getContext().getContentResolver().openOutputStream(uri)) {
                        theBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                    }

                    callToast("Image saved into " + file.getAbsolutePath());

                    uploadImage(file);
                } catch (Exception e) {
                    callToast("Image save fail");
                }
            }
        }.start();

    }

    public void uploadImage(File file) {
        callToast("Uploading the Image");

        apiHandler.postImage(uploadUrl, file, imageUrl, new ApiCallBack() {
            @Override
            public void success(ArrayList result) {
                callToast("Image was Uploaded");
                Navigation.findNavController(getView()).navigate(R.id.action_nav_imageeditor_to_nav_myImage);
            }

            @Override
            public void failure(Throwable t) {

            }
        });
    }

    public void drawTextToBitmap(Context gContext,String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = theBitmap;

        android.graphics.Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(61, 61, 61));
        paint.setTextSize((int) (20 * scale));
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width())/2;
        int y = (bitmap.getHeight() + bounds.height())/2;

        canvas.drawText(gText, x, y, paint);

        theBitmap = bitmap;

        Bitmap bmOverlay = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas overLayCanvas = new Canvas(bmOverlay);
        overLayCanvas.drawBitmap(bitmap, new Matrix(), null);
        overLayCanvas.drawBitmap(gpuImageUtil.getGPUImageFromAssets(theBitmap,gpuImage,filterFlag), new Matrix(), null);

        img_Display.setImageBitmap(bmOverlay);
    }

    public void colorFilter(){
        AssetManager as = getActivity().getAssets();
        filterFlag = filterFlag < 11 ? filterFlag + 1 : 0;
        img_Display.setImageBitmap(gpuImageUtil.getGPUImageFromAssets(theBitmap,gpuImage,filterFlag));
        btn_AddFilter.setText(gpuImageUtil.getFilterName());
    }

    public void callToast(String message){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getContext(),  message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}