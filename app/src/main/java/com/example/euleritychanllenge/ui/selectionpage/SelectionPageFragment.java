package com.example.euleritychanllenge.ui.selectionpage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.euleritychanllenge.R;
import com.example.euleritychanllenge.api.ApiCallBack;
import com.example.euleritychanllenge.api.ApiHandler;
import com.example.euleritychanllenge.databinding.FragmentGalleryBinding;
import com.example.euleritychanllenge.databinding.FragmentSelectionBinding;
import com.example.euleritychanllenge.databinding.FragmentSlideshowBinding;
import com.example.euleritychanllenge.model.ImageData;
import com.example.euleritychanllenge.ui.adapter.ListImageDisplayAdapter;
import com.example.euleritychanllenge.ui.adapter.MyImageAdapter;
import com.example.euleritychanllenge.ui.gallery.GalleryViewModel;
import com.example.euleritychanllenge.ui.slideshow.SlideshowViewModel;

import java.util.ArrayList;

public class SelectionPageFragment extends Fragment {

    private FragmentSelectionBinding binding;
    private ArrayList<ImageData> imageData = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding = FragmentSelectionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final GridView gv_image = binding.gvImage;

        ListImageDisplayAdapter listImageDisplayAdapter = new ListImageDisplayAdapter(this.getActivity().getApplicationContext());
        gv_image.setAdapter(listImageDisplayAdapter);
        listImageDisplayAdapter.setData(imageData);

        ApiHandler apiHandler = new ApiHandler();
        apiHandler.getImageData( new ApiCallBack() {
            @Override
            public void success(ArrayList result) {
                ArrayList<ImageData> posts = result;
                imageData = posts;
                listImageDisplayAdapter.setData(posts);
            }

            @Override
            public void failure(Throwable t) {

            }
        });

        gv_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                ImageData imageData = (ImageData) adapterView.getItemAtPosition(i);
                bundle.putString("url", imageData.getUrl());

                Navigation.findNavController(getView()).navigate(R.id.action_nav_selection_to_nav_imageeditor,bundle);
            }
        });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}