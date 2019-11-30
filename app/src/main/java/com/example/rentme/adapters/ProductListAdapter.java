package com.example.rentme.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.rentme.model.Product;
import com.example.rentme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
        Holder holder;// use holder
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
        //holder.image.setImageResource(R.drawable.chairs);
        updateImageFromUrl(items.get(position), holder.image);

        holder.MoreDetailsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.showMoreDetails();
            }
        });

        return convertView;
    }

//    private void updateImageFromUrl(Product currProduct){
//
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference httpsReference = storage.getReferenceFromUrl(currProduct.getImage());
//
//
//        final long ONE_MEGABYTE = 1024 * 1024;
//        httpsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//
//                holder.image.setImageBitmap(Bitmap.createScaledBitmap(bmp, holder.image.getWidth(),
//                        holder.image.getHeight(), false));
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });
//
//    }

    private void updateImageFromUrl(Product currProduct,final ImageView image){
//        storageReference = storage.getReference().child("images/").child(user.getUid());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(currProduct.getImage());
        httpsReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful())
                {
                    Glide.with(context)
                            .load(task.getResult())
                            .apply(RequestOptions.circleCropTransform())
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .into(image);


                }
                else {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    //  Log.d("Firebase id",user.getUid());
                }

            }
        });
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

