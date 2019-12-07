package com.example.rentme.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentme.activities.MainActivity;
import com.example.rentme.R;
import com.example.rentme.adapters.ProductListAdapter;
import com.example.rentme.model.Author;
import com.example.rentme.model.Comment;
import com.example.rentme.model.Product;
import com.example.rentme.model.ProductDetails;
import com.example.rentme.model.Relation;
import com.example.rentme.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ProfileFragment extends Fragment {

    TextView titleName;
    TextView titleLastName;
    TextView titleNumber;
    TextView titleArea;
    TextView titleEmail;

    ListView myPublishedProduct;
    Button logoutBtn;
    Button back;
    Button change_profile;
    ProgressBar progressBar;


    LinearLayout mainLinear;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;

    MainFragment mainFragment;
    EditProfileFragment editProfileFragment;
    ProductListAdapter adapter;

    ArrayList<Relation> newMyProductsId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newMyProductsId = new ArrayList<Relation>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        titleName = view.findViewById(R.id.tvFirstName);
        titleLastName = view.findViewById(R.id.tvLastName);
        progressBar = view.findViewById(R.id.progressbar);

        titleNumber = view.findViewById(R.id.tvNumber);
        titleArea = view.findViewById(R.id.tvArea);
        titleEmail = view.findViewById(R.id.tvEmail);

        logoutBtn = view.findViewById(R.id.signOut);
        back = view.findViewById(R.id.backToMain);
        mainLinear = view.findViewById(R.id.mainLinear);
        change_profile = view.findViewById(R.id.change_profile);
        myPublishedProduct = view.findViewById(R.id.my_published_product);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // Initialize User
        DatabaseReference ref = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).getRef();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user == null) throw new NoSuchElementException("Cant Retrieve user from database");
                gotUserFromFireBase(user);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


        buildMyPublishedProductList();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Toast.makeText(getContext(),"התנתקת בהצלחה",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });


        // Back To Main Activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainFragment == null)
                    mainFragment = new MainFragment();
                outerTransaction(mainFragment);
            }
        });

        //Edit Profile
        change_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editProfileFragment == null)
                    editProfileFragment = new EditProfileFragment();
                outerTransaction(editProfileFragment);
            }
        });

        return view;
    }

    private void buildMyPublishedProductList() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseUser.getUid()).child("posts_list").getRef();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<Relation> myProductsId = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Relation productId = ds.getValue(Relation.class);
                        myProductsId.add(productId);
                    }
                    showMyPublishedProducts(myProductsId);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void showMyPublishedProducts(final ArrayList<Relation> myProductsId) {
        newMyProductsId = new ArrayList<Relation>();
        //start the adapter of the listView
        adapter = new ProductListAdapter(new ArrayList<Product>(),getActivity());//suppose to get from the data base
        myPublishedProduct.setAdapter(adapter);

        for (final Relation myProductId : myProductsId){
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("Categories")
                    .child(myProductId.getCategoryName()).getRef();
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot category) {
                    if (category.exists()) {
                        DataSnapshot currProductUid = category.child(myProductId.getProductUid());
                        if (currProductUid.exists()) {
                            newMyProductsId.add(myProductId);
                            Author author = currProductUid.child("author").getValue(Author.class);
                            ProductDetails productDetails = currProductUid.child("productDetails").getValue(ProductDetails.class);

                            List<Comment> comments = new ArrayList<>();
                            for (DataSnapshot dsComments : currProductUid.child("comments_list").getChildren()) {
                                String msg = dsComments.child("msg").getValue().toString();
                                Author commentAuthor = dsComments.child("author").getValue(Author.class);
                                comments.add(new Comment(commentAuthor, msg));
                            }

                            Product product = new Product(productDetails, author, comments);

                            adapter.addProductFromProfile(product);
                            adapter.notifyDataSetChanged();


                        }
                    }//update user post list if some product was allready removed
                    if (newMyProductsId.size() != myProductsId.size()) {
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(firebaseUser.getUid()).child("posts_list").removeValue();
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(firebaseUser.getUid()).child("posts_list")
                                .setValue(newMyProductsId);

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }



    private void gotUserFromFireBase(User user) {

        progressBar.setVisibility(View.GONE);
        mainLinear.setVisibility(LinearLayout.VISIBLE);
        logoutBtn.setVisibility(LinearLayout.VISIBLE);

        String name = user.getName();
        String lastname = user.getLastname();
        String area = user.getArea();
        String number = user.getNumber();
        String email = user.getEmail();

        this.titleName.setText(name);
        this.titleLastName.setText(lastname);
        this.titleNumber.setText(number);
        this.titleArea.setText(area);
        this.titleEmail.setText(email);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void outerTransaction(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.OuterFragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

