package com.example.componentecalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ComponentAdapter extends BaseAdapter {
    private List<Component> componentList = null;
    private final Context ctx;
    private final int resourceLayout;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        componentDatabase = Room.databaseBuilder(ctx, ComponentDatabase.class, "ComponentsDB").build();

        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            convertView = inflater.inflate(resourceLayout, parent, false);
            holder = new ViewHolder();
            holder.componentName = convertView.findViewById(R.id.componentName);
            holder.componentCategory = convertView.findViewById(R.id.componentCategory);
            holder.componentPrice = convertView.findViewById(R.id.componentPrice);
            holder.componentDiscount = convertView.findViewById(R.id.componentDiscount);
            holder.componentQuantity = convertView.findViewById(R.id.componentQuantity);
            holder.deleteButton = convertView.findViewById(R.id.componentDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Component component = (Component)getItem(position);

        holder.componentName.setText(component.getName());
        holder.componentCategory.setText(component.getCategory());
        holder.componentPrice.setText(String.valueOf(component.getPrice()));
        holder.componentDiscount.setChecked(component.isDiscount());
        holder.componentQuantity.setText(String.valueOf(component.getQuantity()));
        holder.deleteButton.setOnClickListener(v -> {
            Executor executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executor.execute(() -> {
                componentDatabase.getDaoObject().deleteComponent(component);
                handler.post(() -> {
                    componentList.remove(component);
                    notifyDataSetChanged();
                });
            });
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                saveToFavorites(component);
                return false;
            }
        });

        return convertView;
    }

    private void saveToFavorites(Component component) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("FavoritesList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = sharedPreferences.getString("FavoritesList", null);
        Type type = new TypeToken<ArrayList<Component>>() {}.getType();
        ArrayList<Component> favoritesList = gson.fromJson(json, type);

        if (favoritesList == null) {
            favoritesList = new ArrayList<>();
        }

        favoritesList.add(component);

        String updatedJson = gson.toJson(favoritesList);
        editor.putString("FavoritesList", updatedJson);
        editor.apply();

        Toast.makeText(ctx, "favorite component", Toast.LENGTH_SHORT).show();
    }

    static class ViewHolder {
        TextView componentName;
        TextView componentCategory;
        TextView componentPrice;
        CheckBox componentDiscount;
        TextView componentQuantity;
        Button deleteButton;
    }
}
