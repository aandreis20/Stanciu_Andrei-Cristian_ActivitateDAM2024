package com.example.componentecalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private List<ComponentImage> imagesList;
    private Context ctx;
    private int resourceLayout;

    public ImageAdapter(List<ComponentImage> imagesList, Context ctx, int resourceLayout) {
        this.imagesList = imagesList;
        this.ctx = ctx;
        this.resourceLayout = resourceLayout;
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public Object getItem(int position) {
        return imagesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(resourceLayout, parent, false);
        ImageView imageView = view.findViewById(R.id.imageItem);

        ComponentImage image = (ComponentImage)getItem(position);

        imageView.setImageBitmap(image.getImage());

        return view;
    }
}
