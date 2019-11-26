package com.example.rentme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rentme.R;

public class MainAdapter extends BaseAdapter  {

    private Context context;
    private LayoutInflater inflater;
    private String[] titles;
    private int[] numberImages;

    public MainAdapter(Context c, String[] titles, int[] numberImages) {
        this.context = c;
        this.numberImages = numberImages;
        this.titles = titles;
    }


    @Override
    public int getCount() {
        return Math.min(titles.length,numberImages.length);
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

        imageView.setImageResource(numberImages[position]);
        textView.setText(titles[position]);
        return convertView;
    }
}
