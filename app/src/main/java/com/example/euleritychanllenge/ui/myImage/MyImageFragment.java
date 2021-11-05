package com.example.euleritychanllenge.ui.myImage;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.euleritychanllenge.R;
import com.example.euleritychanllenge.api.ApiCallBack;
import com.example.euleritychanllenge.api.ApiHandler;
import com.example.euleritychanllenge.databinding.FragmentMyimageBinding;
import com.example.euleritychanllenge.databinding.FragmentSlideshowBinding;
import com.example.euleritychanllenge.model.ImageData;
import com.example.euleritychanllenge.ui.adapter.MyImageAdapter;
import com.example.euleritychanllenge.ui.selectionpage.SelectionPageFragment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyImageFragment extends Fragment {

    private MyImageViewModel myImageViewModel;
    private FragmentMyimageBinding binding;
    private File[] files = new File[]{};
    private Boolean isGranted = false;
    private MyImageAdapter myImageAdapter;
    private TextView tv_errorMsg;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myImageViewModel =
                new ViewModelProvider(this).get(MyImageViewModel.class);

        binding = FragmentMyimageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setHasOptionsMenu(true);

        final ListView lv_editedImage = binding.lvEditedImage;
        tv_errorMsg = binding.tvErrorMsg;

        myImageAdapter = new MyImageAdapter(this.getActivity().getApplicationContext());
        lv_editedImage.setAdapter(myImageAdapter);
        myImageAdapter.setData(files);

        if (!isGranted) {
            requestPermissions();
        } else {
            getFileData();
        }

        if (files.length == 0) {
            tv_errorMsg.setVisibility(View.VISIBLE);
        } else {
            tv_errorMsg.setVisibility(View.GONE);
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.myimage_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            if (!isGranted) {
                requestPermissions();
            } else {
                Navigation.findNavController(getView()).navigate(R.id.action_nav_myImage_to_nav_selection);
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void requestPermissions() {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            isGranted = true;
                            getFileData();
                            if (files.length == 0) {
                                tv_errorMsg.setVisibility(View.VISIBLE);
                            } else {
                                tv_errorMsg.setVisibility(View.GONE);
                            }
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                Toast.makeText(getContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
            }
        })
                .onSameThread().check();
    }

    private void showSettingsDialog() {
        // we are displaying an alert dialog for permissions
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // below line is the title
        // for our alert dialog.
        builder.setTitle("Need Permissions");

        // below line is our message for our dialog
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // this method is called on click on positive
                // button and on clicking shit button we
                // are redirecting our user from our app to the
                // settings page of our app.
                dialog.cancel();
                // below is the intent from which we
                // are redirecting our user.
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getApplicationContext().getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // this method is called when
                // user click on negative button.
                dialog.cancel();
            }
        });
        // below line is used
        // to display our dialog
        builder.show();
    }

    private void getFileData(){
        File dcimPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera");
        if (dcimPath.exists() && dcimPath.listFiles() != null) {
            files = dcimPath.listFiles();
            myImageAdapter.setData(files);
        }
    }

}