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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.rentme.R;
import com.example.rentme.adapters.ProductListAdapter;
import com.example.rentme.adapters.commentAdapter;
import com.example.rentme.model.Product;
import com.example.rentme.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

public class InItemFragment extends Fragment {
    InsideCategoryFragment insideCategoryFragment;
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
    ListView commentsListView;
    Button sendCommentBtn;
    EditText writeComment;
    ProgressBar progressBar_sendComment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_in_item, container, false);

        //init product and user
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
        //end init product and user

        productTitle =  view.findViewById(R.id.product_title);
        categoryName = view.findViewById(R.id.category_name);
        moreDetails = view.findViewById(R.id.info);
        condition = view.findViewById(R.id.condition);
        price = view.findViewById(R.id.price);
        nameOfTheSeller = view.findViewById(R.id.nameOfTheSeller);
        cityOfTheSeller = view.findViewById(R.id.cityOfTheSeller);
        uploadTime = view.findViewById(R.id.upload_time);
        productPicture = view.findViewById(R.id.product_picture);
        commentsListView = view.findViewById(R.id.comments_list_view);
        sendCommentBtn = view.findViewById(R.id.sendBtn);
        writeComment = view.findViewById(R.id.writeComment);
        progressBar_sendComment = view.findViewById(R.id.progressBar_send_comment);


        sendCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar_sendComment.setVisibility(View.VISIBLE);
                String newComment = writeComment.getText().toString();
                if (newComment.length() > 0) {
                    ArrayList<String> newCommentArrayList;
                    try {
                        newCommentArrayList = new ArrayList<String>(product.getComments());
//                    if (newCommentArrayList.get(0)== "0")
//                        newCommentArrayList.clear();
                        newCommentArrayList.add(newComment);
                    }catch (Exception e){
                        newCommentArrayList = new ArrayList<String>();
                        newCommentArrayList.add(newComment);
                    }

                    String productFather = product.getUtc() + ": " + product.getTitle();
                    FirebaseDatabase.getInstance().getReference("Categories")
                            .child(product.getCategory()).child(productFather).child("comments").setValue(newCommentArrayList)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar_sendComment.setVisibility(View.GONE);
                                        writeComment.setText("");
                                        Toast.makeText(getContext(), "העלאת התגובה בוצעה בהצלחה", Toast.LENGTH_SHORT).show();
                                        //update the comments
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories").child(product.getCategory()).
                                                child(product.getUtc()+ ": " + product.getTitle()).child("comments").getRef();
                                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                GenericTypeIndicator<ArrayList<String>> tmp = new GenericTypeIndicator<ArrayList<String>>() {};
                                                ArrayList<String> updatedComments = dataSnapshot.getValue(tmp);

                                               // ArrayList<String> updatedComments = dataSnapshot.getValue(ArrayList.class);
                                                if(updatedComments == null) throw new NoSuchElementException("Cant Retrieve comments ArrayList from database");
                                                commentAdapter adapter;
                                                adapter = new commentAdapter(updatedComments, getContext());
                                                commentsListView.setAdapter(adapter);
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {}
                                        });
                                        //end update commits
                                    } else {
                                        progressBar_sendComment.setVisibility(View.GONE);
                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });



                }
                else {
                    Toast.makeText(getContext(),"תגובה לא חוקית", Toast.LENGTH_LONG).show();
                    progressBar_sendComment.setVisibility(View.GONE);
                }

            }
        });

        backBtn = view.findViewById(R.id.backToLastPage);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (insideCategoryFragment == null)
                    insideCategoryFragment = new InsideCategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Categories",product.getCategory());
                insideCategoryFragment.setArguments(bundle);
                outerTransaction(insideCategoryFragment);
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
        updateImageFromUrl(product, this.productPicture);

        //comment
        try {
            commentAdapter adapter;
            adapter = new commentAdapter(this.product.getComments(), getContext());
            commentsListView.setAdapter(adapter);
        }catch (Exception e){};
        //end comment


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

    private void outerTransaction(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.OuterFragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

//    private String dateFormat2UtcTime(String productDateFormat){
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        String strDate = (productDateFormat);
//        Date uploadDate = null;
//        try {
//            uploadDate = dateFormat.parse(strDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return uploadDate.getTime()+"";
//    }
}
