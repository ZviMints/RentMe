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
import com.example.rentme.model.Comment;
import com.example.rentme.model.RelationCategoryProduct;
import com.example.rentme.model.Product;
import com.example.rentme.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    ProgressBar SendCommentProgressBar;

    commentAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_in_item, container, false);

        productTitle = view.findViewById(R.id.product_title);
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
        SendCommentProgressBar = view.findViewById(R.id.progressBar_send_comment);
        backBtn = view.findViewById(R.id.backToLastPage);
        makeReservationBtn = view.findViewById(R.id.make_reservation);
        renterDetails = view.findViewById(R.id.renter_details);
        addToFavoritesBtm = view.findViewById(R.id.addToFavorites);

        // init product and user
        product = (Product) getArguments().getSerializable("Product");

        // Initialize User//Toast.makeText(getContext(), userUid , Toast.LENGTH_SHORT).show();
        final String userUid = product.getUserUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference("Users").child(userUid).getRef();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user == null)
                    throw new NoSuchElementException("Cant Retrieve user from database");
                gotUserFromFireBase(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        return view;
    }

    private void updateFavorites(RelationCategoryProduct favorites) {
        //upload the new favorite
        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Favorites")
                .child(new Date().getTime() + "").setValue(favorites)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {//successful uploading
                            Toast.makeText(getContext(), " נוסף למועדפים שלך" + product.getTitle(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void gotUserFromFireBase(User user) {
        this.productTitle.setText(product.getTitle());
        this.categoryName.setText(product.getCategory());
        this.moreDetails.setText(product.getDetails());
        this.condition.setText(product.getCondition());
        this.price.setText(product.getPrice());
        this.nameOfTheSeller.setText(this.user.getName());
        this.cityOfTheSeller.setText(this.user.getArea());

        Date date = product.getDate();
        SimpleDateFormat sm = new SimpleDateFormat("MM-dd-yyyy");
        String strDate = sm.format(date);

        this.uploadTime.setText("בתאריך " + strDate);
        updateImageFromUrl(product, this.productPicture);

        InitializeComments(user);

    }

    private void InitializeComments(final User user) {

        //Read Comments From FireBase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        // Initialize User
        DatabaseReference ref = firebaseDatabase.getReference("Categories")
                .child(product.getCategory())
                .child(product.getProductIDInCategory())
                .child("comments")
                .getRef();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Comment> comments = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Comment comment = ds.getValue(Comment.class);
                    comments.add(comment);
                }
                gotCommentsFromDataBase(comments, user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void gotCommentsFromDataBase(ArrayList<Comment> comments, final User user) {

        // Updating The Comments
        adapter = new commentAdapter(comments, getContext());
        commentsListView.setAdapter(adapter);

        sendCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendCommentProgressBar.setVisibility(View.VISIBLE);
                String msg = writeComment.getText().toString();

                if (msg.length() > 0) {
                    final ArrayList<Comment> comments = product.getComments();
                    Comment comment = new Comment(user.getName() + " " + user.getLastname(), msg);
                    comments.add(comment);

                    // Remove Last Comments
                    firebaseDatabase.getReference("Categories")
                            .child(product.getCategory())
                            .child(product.getProductIDInCategory())
                            .child("comments").removeValue();

                    //Update new Comment On Database
                    firebaseDatabase.getReference("Categories")
                            .child(product.getCategory())
                            .child(product.getProductIDInCategory())
                            .child("comments")
                            .setValue(comments);


                    writeComment.setText("");
                    Toast.makeText(getContext(), "העלאת התגובה בוצעה בהצלחה", Toast.LENGTH_SHORT).show();
                    SendCommentProgressBar.setVisibility(View.GONE);

                    adapter = new commentAdapter(comments, getContext());
                    commentsListView.setAdapter(adapter);


                } else {
                    Toast.makeText(getContext(), "תגובה לא חוקית", Toast.LENGTH_LONG).show();
                    SendCommentProgressBar.setVisibility(View.GONE);
                }

            }

        });

        makeReservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeReservationBtn.setVisibility(View.GONE);
                renterDetails.setVisibility(View.VISIBLE);
                renterDetails.setText(user.getName() + " " + user.getLastname()
                        + " " + user.getEmail() + " " + user.getNumber());
            }
        });

        addToFavoritesBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable img = getContext().getResources().getDrawable(R.drawable.ic_star_yellow_24dp);
                addToFavoritesBtm.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                String productUID = product.getProductIDInCategory();
                RelationCategoryProduct newFavorite = new RelationCategoryProduct(product.getCategory(), productUID);
                updateFavorites(newFavorite);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (insideCategoryFragment == null)
                    insideCategoryFragment = new InsideCategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Categories", product.getCategory());
                insideCategoryFragment.setArguments(bundle);
                outerTransaction(insideCategoryFragment);
            }
        });
    }

    private void updateImageFromUrl(Product currProduct, final ImageView image) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(currProduct.getImage());
        httpsReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Glide.with(getContext())
                            .load(task.getResult())
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .into(image);


                } else {
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

}
