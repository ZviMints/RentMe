package com.example.rentme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rentme.R;
import com.example.rentme.model.CategoryWarper;

import java.util.ArrayList;
import java.util.List;

public class AdminCategoryManagmentAdapter extends BaseAdapter implements ListAdapter {
    private List<CategoryWarper> list;
    private Context context;

    public AdminCategoryManagmentAdapter(ArrayList<CategoryWarper> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.admin_category_magment_adapter, null);
        }

        //Handle TextView and display string from your list
        TextView tvCategoryName = view.findViewById(R.id.tvCategoryName);
        tvCategoryName.setText(list.get(position).getCategoryName());

        TextView tvNumberOfProducts = view.findViewById(R.id.tvNumberOfProducts);
        tvNumberOfProducts.setText(list.get(position).getNumberOfProducts() + "");

        return view;
    }

}