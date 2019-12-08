package com.example.rentme.adapters;

import android.content.Context;
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
import com.example.rentme.comperators.sortByLastUploaded;
import com.example.rentme.interfaces.MoreDetailsButtonListener;
import com.example.rentme.interfaces.UpdateMyProductList;
import com.example.rentme.model.Product;
import com.example.rentme.R;
import com.example.rentme.model.Relation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.NoSuchElementException;


public class myProductListAdapter extends BaseAdapter {

    private MoreDetailsButtonListener listener;
    private ArrayList<Product> items = new ArrayList<>();
    private Context context;

    public myProductListAdapter(ArrayList<Product> items, Context context) {
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
        Holder holder;
        final Product currProduct = items.get(position);
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
        holder.title.setText(currProduct.getProductDetails().getTitle());
        holder.category.setText(currProduct.getProductDetails().getCategory());
        String detailsText = currProduct.getProductDetails().getDetails();
        detailsText = (detailsText.length()>21) ? detailsText.substring(0,21)+"..." : detailsText;
        holder.details.setText(detailsText);
        holder.price.setText(currProduct.getProductDetails().getPrice());
        holder.productPriceTime.setText(currProduct.getProductDetails().getRentPeriod());

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String strDate = (currProduct.getProductDetails().getUploadTime());
        Date uploadDate = null;
        try {
            uploadDate = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = new Date().getTime() - uploadDate.getTime();

        holder.publishTime.setText(diff / (1000 * 60 * 60 * 24)+"");
        //holder.image.setImageResource(R.drawable.chairs);
        updateImageFromUrl(currProduct, holder.image);

        holder.MoreDetailsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(currProduct == null) throw new NoSuchElementException("showMoreDetails() Going to get null input");
                listener.showMoreDetails(currProduct);
            }
        });

        holder.RemoveProductBtm.setVisibility(View.VISIBLE);
        holder.RemoveProductBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete from category
                FirebaseDatabase.getInstance().getReference("Categories").child(currProduct.getProductDetails().getCategory())
                        .child(currProduct.getPRODUCT_UID()).removeValue();
                //delete from last product
                FirebaseDatabase.getInstance().getReference("Last Products")
                        .child(currProduct.getPRODUCT_UID()).removeValue();
//                //delete from user "my list"
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(currProduct.getAuthor().getUserUid())
                        .child("posts_list").getRef();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            ArrayList<Relation> myProductsId = new ArrayList<>();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Relation productId = ds.getValue(Relation.class);
                                if (productId.getProductUid().compareTo(currProduct.getPRODUCT_UID())!=0)
                                    myProductsId.add(productId);
                            }
                            // FirebaseDatabase.getInstance().getReference("Users").child(currProduct.getAuthor()
                             //       .getUserUid()).child("posts_list").removeValue();
                            FirebaseDatabase.getInstance().getReference("Users").child(currProduct.getAuthor()
                                    .getUserUid()).child("posts_list").setValue(myProductsId);

                            UpdateMyProductList updateProductList = (UpdateMyProductList)context;
                            updateProductList.updateMyPublishedProducts();

                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        });



        return convertView;
    }

    public void addProduct(Product product){
        items.add(product);
        Collections.sort(items, new sortByLastUploaded());
    }

    public void addProductFromProfile(Product product){
        items.add(product);
    }

    private void updateImageFromUrl(Product currProduct,final ImageView image){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(currProduct.getProductDetails().getImage());
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
        private TextView price;
        private TextView productPriceTime;
        private TextView publishTime;
        private Button MoreDetailsBtn;
        private Button RemoveProductBtm;


        public Holder(View view) {
            title = view.findViewById(R.id.product_title);
            category = view.findViewById(R.id.product_category);;
            details = view.findViewById(R.id.product_details);;
            image = view.findViewById(R.id.product_image);
            price = view.findViewById(R.id.product_price);
            productPriceTime = view.findViewById(R.id.product_price_time);
            publishTime = view.findViewById(R.id.Publish_time);
            MoreDetailsBtn = view.findViewById(R.id.more_product_details);
            RemoveProductBtm = view.findViewById(R.id.remove_product);
        }


    }

}