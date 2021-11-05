package com.example.euleritychanllenge.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.euleritychanllenge.R;
import com.example.euleritychanllenge.model.ImageData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyImageAdapter extends BaseAdapter {
    private Context mContext;

    private File[] files = new File[]{};
    private LayoutInflater mLayoutInflater;
    private ViewHolder holder;

    public MyImageAdapter(Context context){
        super();
        this.mContext = context;
        mLayoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(File[] list){
        this.files = list;
        this.notifyDataSetChanged();
    }

    private class ViewHolder {
        private TextView tv_description;
        private ImageView img_edited;
    }


    @Override
    public int getCount() {
        return files.length;
    }

    @Override
    public Object getItem(int i) {
        return files[i];
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            mLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.adapter_myimage, null);

            holder.img_edited = (ImageView) convertView.findViewById(R.id.img_edited);
            holder.tv_description = (TextView) convertView.findViewById(R.id.tv_description);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }


        //base on UI design, what kind of data need to show for
        holder.tv_description.setText("name : " + files[position].getName());
        Glide.with(mContext)
                .load(new File(files[position].getPath())) // Uri of the picture
                .into(holder.img_edited);
        return convertView;
    }

}
