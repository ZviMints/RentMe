package com.example.rentme.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.rentme.R;
import com.example.rentme.adapters.ProductListAdapter;
import com.example.rentme.model.Product;
import com.example.rentme.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class InItemFragment extends Fragment {
    Button backBtn;
    Product product;
    User user;
    FirebaseDatabase firebaseDatabase;

    TextView productTitle;
    TextView categoryName;
    TextView moreDetails;
    TextView condition;
    TextView price;
    TextView nameOfTheSeller;
    TextView cityOfTheSeller;
    TextView uploadTime;
    ImageView productPicture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        product = (Product) getArguments().getSerializable("product");

        // Initialize User//Toast.makeText(getContext(), userUid , Toast.LENGTH_SHORT).show();
        String userUid = product.getUserUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference("Users").child(userUid).getRef();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if(user == null) throw new NoSuchElementException("Cant Retrieve user from database");
                setTextByCurrProduct();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_in_item, container, false);



        productTitle =  view.findViewById(R.id.product_title);
        categoryName = view.findViewById(R.id.category_name);
        moreDetails = view.findViewById(R.id.info);
        condition = view.findViewById(R.id.condition);
        price = view.findViewById(R.id.price);
        nameOfTheSeller = view.findViewById(R.id.nameOfTheSeller);
        cityOfTheSeller = view.findViewById(R.id.cityOfTheSeller);
        uploadTime = view.findViewById(R.id.upload_time);
        productPicture = view.findViewById(R.id.product_picture);



        backBtn = view.findViewById(R.id.backToLastPage);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    private void setTextByCurrProduct() {
        this.productTitle.setText(product.getTitle());
        this.categoryName.setText(product.getCategory());
        this.moreDetails.setText(product.getDetails());
        this.condition.setText(product.getCondition());
        this.price.setText(product.getPrice());
        this.nameOfTheSeller.setText(user.getName());
        this.cityOfTheSeller.setText(user.getArea());
        this.uploadTime.setText(product.getUploadTime());
        updateImageFromUrl(product, productPicture);

    }

    private void updateImageFromUrl(Product currProduct,final ImageView image){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(currProduct.getImage());
        httpsReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful())
                {
                    Glide.with(getContext())
                            .load(task.getResult())
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .into(image);


                }
                else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    //  Log.d("Firebase id",user.getUid());
                }

            }
        });
    }
}
