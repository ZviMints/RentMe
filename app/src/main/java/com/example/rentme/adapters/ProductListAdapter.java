package com.example.rentme.adapters;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.rentme.model.Product;
import com.example.rentme.R;

import java.util.ArrayList;


public class ProductListAdapter extends BaseAdapter {

    public interface MoreDetailsButtonListener{
        void showMoreDetails();
    }

    private MoreDetailsButtonListener listener;
    private ArrayList<Product> items = new ArrayList<>();
    private Context context;

    public ProductListAdapter(ArrayList<Product> items, Context context) {
        this.items = items;
        this.context = context;


        try {
           this.listener = (MoreDetailsButtonListener) context;

        } catch (ClassCastException e) {

            throw new ClassCastException(context.toString() + " must implement showButtons");
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder; // use holder
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.product_row, parent, false);

            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            convertView.setLayoutParams(new ViewGroup.LayoutParams(size.x, size.y/7));
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.title.setText(items.get(position).getTitle());
        holder.category.setText(items.get(position).getCategory());
        holder.details.setText(items.get(position).getDetails());
        holder.image.setImageResource(R.drawable.chairs);

        holder.MoreDetailsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.showMoreDetails();
            }
        });

        return convertView;
    }

    public class Holder {
        private TextView title;
        private TextView category;
        private TextView details;
        private ImageView image;
        private Button MoreDetailsBtn;

        public Holder(View view) {
            title = view.findViewById(R.id.product_title);
            category = view.findViewById(R.id.product_category);;
            details = view.findViewById(R.id.product_details);;
            image = view.findViewById(R.id.product_image);
            MoreDetailsBtn = view.findViewById(R.id.more_product_details);
        }

    }

}

