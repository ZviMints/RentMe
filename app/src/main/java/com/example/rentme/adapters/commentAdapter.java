package com.example.rentme.adapters;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rentme.R;
import java.util.ArrayList;


public class commentAdapter extends BaseAdapter {
    private ArrayList<String> comments;
    private Context context;

    public commentAdapter(ArrayList<String> items, Context context) {
        this.comments = new ArrayList<String>(items);
        this.context = context;

    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;// use holder
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.comment_row, parent, false);

            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            convertView.setLayoutParams(new ViewGroup.LayoutParams(size.x, size.y/14));
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.details.setText(comments.get(position));
//        holder.details.setText("not working well");

        return convertView;
    }


    public class Holder {
        private TextView details;


        public Holder(View view) {
            details = view.findViewById(R.id.comment_details);
        }


    }

}

