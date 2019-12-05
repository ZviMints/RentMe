package com.example.rentme.fragments;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rentme.R;
import com.example.rentme.adapters.commentAdapter;
import com.example.rentme.model.Favorites;
import com.example.rentme.model.Product;
import com.example.rentme.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
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
    TextView renterDetails;
    ImageView productPicture;
    ListView commentsListView;
    Button sendCommentBtn;
    Button makeReservationBtn;
    Button addToFavoritesBtm;
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
        final String userUid = product.getUserUid();
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
        backBtn = view.findViewById(R.id.backToLastPage);
        makeReservationBtn = view.findViewById(R.id.make_reservation);
        renterDetails = view.findViewById(R.id.renter_details);
        addToFavoritesBtm = view.findViewById(R.id.addToFavorites);

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
                    //upload the new comment
                    String productFather = product.getUtc() + ": " + product.getTitle();
                    FirebaseDatabase.getInstance().getReference("Categories")
                            .child(product.getCategory()).child(productFather).child("comments").setValue(newCommentArrayList)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {//successful uploading
                                        progressBar_sendComment.setVisibility(View.GONE);
                                        writeComment.setText("");
                                        Toast.makeText(getContext(), "העלאת התגובה בוצעה בהצלחה", Toast.LENGTH_SHORT).show();
                                        //download the updated comments
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories").child(product.getCategory()).
                                                child(product.getUtc()+ ": " + product.getTitle()).child("comments").getRef();
                                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                GenericTypeIndicator<ArrayList<String>> tmp = new GenericTypeIndicator<ArrayList<String>>() {};
                                                ArrayList<String> updatedComments = dataSnapshot.getValue(tmp);

                                                if(updatedComments == null) throw new NoSuchElementException("Cant Retrieve comments ArrayList from database");
                                                commentAdapter adapter;
                                                adapter = new commentAdapter(updatedComments, getContext());
                                                commentsListView.setAdapter(adapter);
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {}
                                        });
                                        //end download the updated comments
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

        makeReservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeReservationBtn.setVisibility(View.GONE);
                renterDetails.setVisibility(View.VISIBLE);
                renterDetails.setText(user.getName() + " " +user.getLastname()
                        +" " +user.getEmail()+ " "+ user.getNumber());
            }
        });

        addToFavoritesBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable img = getContext().getResources().getDrawable( R.drawable.ic_star_yellow_24dp );
                addToFavoritesBtm.setCompoundDrawablesWithIntrinsicBounds( null, null, img, null);

                String productFather = product.getUtc() + ": " + product.getTitle();
               final  Favorites newFavorite = new Favorites(productFather,product.getCategory());
                //download the favorites arrayList
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("Favorites").getRef();
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<Favorites> favorites = new ArrayList<>();
                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                Favorites favorite = ds.getValue(Favorites.class);
                                favorites.add(favorite);
                            }

//                              ArrayList<Favorites> favorites = new ArrayList<Favorites>();
//                              favorites = dataSnapshot.getValue(ArrayList.class);

                            if (favorites == null) {
                                favorites = new ArrayList<Favorites>();
                            }
                            favorites.add(newFavorite);
                            //upload the new favorites arrayList
                            updateFavorites(favorites);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

            }
        });


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

    private void updateFavorites(ArrayList<Favorites> favorites) {
        //upload the new favorite
        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Favorites")
                .setValue(favorites)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {//successful uploading
                            Toast.makeText(getContext(), "נוסף למועדפים שלך" + product.getTitle(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
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
