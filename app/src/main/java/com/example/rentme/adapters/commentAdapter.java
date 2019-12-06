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
import com.example.rentme.model.Comment;

import java.util.ArrayList;
import java.util.Date;


public class commentAdapter extends BaseAdapter {
    private ArrayList<Comment> comments;
    private Context context;

    public commentAdapter(ArrayList<Comment> comments, Context context) {
        this.comments = comments;
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
        String msg = comments.get(position).getMsg();
        String name = comments.get(position).getFullname();

        holder.details.setText(msg);

        return convertView;
    }


    public class Holder {
        private TextView details;


        public Holder(View view) {
            details = view.findViewById(R.id.comment_details);
        }


    }

}

