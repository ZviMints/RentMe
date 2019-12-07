package com.example.rentme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rentme.R;
import com.example.rentme.model.Category;

import java.util.List;

public class MainAdapter extends BaseAdapter  {

    private Context context;
    private LayoutInflater inflater;
    private List<Category> categories;

    public MainAdapter(Context c, List<Category> categories) {
        this.context = c;
        this.categories = categories;
    }


    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null) {
            inflater = (LayoutInflater) context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
        }
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.category_square,null);
        }
        ImageView imageView = convertView.findViewById(R.id.image_view);
        TextView textView = convertView.findViewById(R.id.text_view);

        Glide.with(context)
                .load(categories.get(position).getImg())
                .into(imageView);

        textView.setText(categories.get(position).getTitle());
        return convertView;
    }
}
