package com.example.euleritychanllenge.ui.adapter;

import android.content.Context;
import android.util.Log;
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

import java.util.ArrayList;

public class ListImageDisplayAdapter extends BaseAdapter {
    private Context mContext;

    private ArrayList<ImageData> mArrayList = new ArrayList<ImageData>();
    private LayoutInflater mLayoutInflater;
    private ListImageDisplayAdapter.ViewHolder holder;

    public ListImageDisplayAdapter(Context context){
        super();
        this.mContext = context;
        mLayoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<ImageData> list){
        this.mArrayList.clear();
        this.mArrayList = list;
        this.notifyDataSetChanged();
    }

    private class ViewHolder {
        private ImageView img_display;
    }


    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return mArrayList.get(i);
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

            holder = new ListImageDisplayAdapter.ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.adapter_imagedisplay, null);

            holder.img_display = (ImageView) convertView.findViewById(R.id.img_display);

            convertView.setTag(holder);
        }else{
            holder = (ListImageDisplayAdapter.ViewHolder) convertView.getTag();
        }

        ImageData imageData = mArrayList.get(position);

        //base on UI design, what kind of data need to show for
        Glide.with(mContext).load(imageData.getUrl()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.NONE).dontAnimate().into(holder.img_display);

        return convertView;
    }

}
