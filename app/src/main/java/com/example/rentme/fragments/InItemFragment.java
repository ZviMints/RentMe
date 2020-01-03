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
import com.example.rentme.R;
import com.example.rentme.adapters.CommentsAdapter;
import com.example.rentme.model.Author;
import com.example.rentme.model.Comment;
import com.example.rentme.model.Product;
import com.example.rentme.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

public class InItemFragment extends Fragment {
    InsideCategoryFragment insideCategoryFragment;
    Button backBtn;
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
    Button makeOrder;
    EditText writeComment;
    ProgressBar SendCommentProgressBar;

    ProgressBar progressBar;
    LinearLayout MainLinear;
    LinearLayout comment_section;

    View view;
    private Product product;
    private User author;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override

    // Step 1.
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_in_item, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        MainLinear = view.findViewById(R.id.MainLinear);
        comment_section = view.findViewById(R.id.comment_section);

        //Initialize product and user
        Product product = (Product) getArguments().getSerializable("Product");
        gotProductFromBundle(product);


        return view;
    }

    // Step 2.
    private void gotProductFromBundle(Product product) {

        this.product = product;

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
        makeOrder = view.findViewById(R.id.make_reservation);
        renterDetails = view.findViewById(R.id.renter_details);

        ArrayList<Comment> comments = (ArrayList<Comment>) product.getCommentsList();
        CommentsAdapter adapter;
        adapter = new CommentsAdapter(comments, getContext());
        commentsListView.setAdapter(adapter);

        InitializeText();
    }


    // Step 3.
    private void InitializeText() {

        this.productTitle.setText(product.getProductDetails().getTitle());
        this.categoryName.setText(product.getProductDetails().getCategory());
        this.moreDetails.setText(product.getProductDetails().getDetails());
        this.condition.setText(product.getProductDetails().getCondition());
        this.price.setText(product.getProductDetails().getPrice());

        NowNeedProductOwnerDetails();

    }

    // Step 4.
    public void NowNeedProductOwnerDetails() {

        String userUID = product.getAuthor().getUserUid();
        /**
         *         if (true) throw new NoSuchElementException(product.toString());
         *   Outputs:
                productDetails: [Zvi Mints TV,מוצרי חשמל,TEXT TEXT TEXT TEXT TEXT TEXT TEXT TEXT TEXT ,https://firebasestorage.googleapis.com/v0/b/rentme-cdf84.appspot.com/o/product%2F1575636785602.jpg?alt=media&token=cafa5c71-c43c-4083-bb99-60d7aca6de5c,חדש מהמפעל,20,לשעה,2019/12/06 14:53:29]
                author: (5AvwrkLTrSaj6bK9mAYU3YYxZFx1,Israel,Israeli)
                Product_UID: מוצרי חשמל: Zvi Mints TV
                comments: []
         */
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference("Users").child(userUID).getRef();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null)
                    throw new NoSuchElementException("Cant Retrieve user from database");
                gotUserNowInitializeAll(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //Step 5.
    public void gotUserNowInitializeAll(User author) {
        this.author = author;

        this.nameOfTheSeller.setText(author.getFullName());
        this.cityOfTheSeller.setText(author.getArea());
        this.uploadTime.setText(product.getProductDetails().getUploadTime());
        updateImageFromUrl(product, this.productPicture);

        progressBar.setVisibility(View.GONE);
        MainLinear.setVisibility(View.VISIBLE);


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Check if Connected or not!
        if (firebaseUser == null)
            comment_section.setVisibility(View.GONE);

        else
            comment_section.setVisibility(View.VISIBLE);

        InitializeOnClickListener();
    }

    // Step 6.
    private void InitializeOnClickListener() {

        // On Click Send Comment Button
        sendCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendCommentProgressBar.setVisibility(View.VISIBLE);
                GetCurrentUserToCommentFromFireBase();
            }

            private void GetCurrentUserToCommentFromFireBase() {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference ref = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).getRef();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user == null)
                            throw new NoSuchElementException("Cant Retrieve user from database");
                        GotCurrentUser(user);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            private void GotCurrentUser(User user) {

                String msg = writeComment.getText().toString();
                String USER_UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (USER_UID == null) throw new NoSuchElementException("USED_ID is Null");
                Author author = new Author(USER_UID, user.getName(), user.getLastname(),user.getArea());
                Comment comment = new Comment(author, msg);
                if (msg.length() > 0) {
                    List<Comment> comments = product.getCommentsList();
                    comments.add(comment);

                    //Upload the new comment at Product
                    String ProductUID = product.getPRODUCT_UID();
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                    // Remove Last Comments In Product FireBase
                    firebaseDatabase.getReference("Categories")
                            .child(product.getProductDetails().getCategory())
                            .child(ProductUID)
                            .child("comments_list")
                            .removeValue();
                    // Update New Comment In Product FireBase
                    firebaseDatabase.getReference("Categories")
                            .child(product.getProductDetails().getCategory())
                            .child(ProductUID)
                            .child("comments_list")
                            .setValue(comments);

                    writeComment.setText("");
                    CommentsAdapter adapter;
                    adapter = new CommentsAdapter((ArrayList<Comment>) comments, getContext());
                    commentsListView.setAdapter(adapter);
                    SendCommentProgressBar.setVisibility(View.GONE);

                } else {
                    Toast.makeText(getContext(), "תגובה לא חוקית", Toast.LENGTH_LONG).show();
                    SendCommentProgressBar.setVisibility(View.GONE);
                }
            }
        });


        makeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeOrder.setVisibility(View.GONE);
                renterDetails.setVisibility(View.VISIBLE);
                renterDetails.setText(
                        author.getName()
                                + " "
                                + author.getLastname() + "\n"
                                + author.getEmail() + "\n"
                                + author.getNumber());
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insideCategoryFragment = new InsideCategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Categories", product.getProductDetails().getCategory());
                insideCategoryFragment.setArguments(bundle);
                outerTransaction(insideCategoryFragment);
            }
        });
    }


    private void updateImageFromUrl(Product currProduct, final ImageView image) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(currProduct.getProductDetails().getImage());
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