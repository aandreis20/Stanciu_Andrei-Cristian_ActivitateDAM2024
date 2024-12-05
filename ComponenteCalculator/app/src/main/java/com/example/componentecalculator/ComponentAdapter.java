package com.example.componentecalculator;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.room.Room;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ComponentAdapter extends BaseAdapter {
    private List<Component> componentList = null;
    private Context ctx;
    private int resourceLayout;
    private ComponentDatabase componentDatabase = null;

    public ComponentAdapter(List<Component> componentList, Context ctx, int resourceLayout) {
        this.componentList = componentList;
        this.ctx = ctx;
        this.resourceLayout = resourceLayout;
    }

    @Override
    public int getCount() {
        return componentList.size();
    }

    @Override
    public Object getItem(int position) {
        return componentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        componentDatabase = Room.databaseBuilder(ctx, ComponentDatabase.class, "ComponentsDB").build();

        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(resourceLayout, parent, false);
        TextView componentName = view.findViewById(R.id.componentName);
        TextView componentCategory = view.findViewById(R.id.componentCategory);
        TextView componentPrice = view.findViewById(R.id.componentPrice);
        CheckBox componentDiscount = view.findViewById(R.id.componentDiscount);
        TextView componentQuantity = view.findViewById(R.id.componentQuantity);
        Button deleteButton = view.findViewById(R.id.componentDelete);

        Component component = (Component)getItem(position);
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        componentName.setText(component.getName());
        componentCategory.setText(component.getCategory());
        componentPrice.setText(String.valueOf(component.getPrice()));
        componentDiscount.setChecked(component.isDiscount());
        componentQuantity.setText(String.valueOf(component.getQuantity()));
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            componentDatabase.getDaoObject().deleteComponent(component);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        } finally {
                            handler.post(() -> {
                                componentList.remove(component);
                                notifyDataSetChanged();
                            });
                        }
                    }
                });
            }
        });
        return view;
    }
}
