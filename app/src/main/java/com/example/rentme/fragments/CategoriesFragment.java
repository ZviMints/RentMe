package com.example.rentme.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.rentme.R;
import com.example.rentme.activities.admin.CategoriesManagement;
import com.example.rentme.activities.admin.ConfManagement;
import com.example.rentme.adapters.MainAdapter;
import com.example.rentme.model.Category;
import com.example.rentme.model.Configurations;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;


public class CategoriesFragment extends Fragment {

    private GridView gridView;
    InsideCategoryFragment insideCategoryFragment;
    Button publishBtn;
    PublishFragment publishFragment;
    LoginFragment loginFragment;

    FirebaseDatabase firebaseDatabase;
    Configurations conf;

    LinearLayout adminPanel;
    TextView admin_close;
    Button admin_ConfManagement;
    Button admin_CategoriesManagement;
    Button admin_open;

    TextView how_many_products_tv;

    // Get From Database
    private List<Category> categories = new ArrayList<>();

    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void gotCategoriesFromFireBase(List<Category> categories) {

        int size = (categories == null) ? 0 : categories.size();
        how_many_products_tv.setText(size + "");

        // Initialize titles
        this.categories = categories;


        IntializeGridView();
    }

    private void IntializeGridView() {
        progressBar.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
        MainAdapter adapter = new MainAdapter(getContext(),categories);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (insideCategoryFragment == null)
                    insideCategoryFragment = new InsideCategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Categories",categories.get(+position).getTitle());
                insideCategoryFragment.setArguments(bundle);
                outerTransaction(insideCategoryFragment);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        progressBar = view.findViewById(R.id.progressbar);
        gridView = view.findViewById(R.id.grid_view);

        how_many_products_tv = view.findViewById(R.id.how_many_products_tv);

        adminPanel = view.findViewById(R.id.adminPanel);
        admin_close = view.findViewById(R.id.admin_close);
        admin_ConfManagement = view.findViewById(R.id.admin_UsersManagement);
        admin_CategoriesManagement = view.findViewById(R.id.admin_CategoriesManagement);
        admin_open = view.findViewById(R.id.admin_open);

        firebaseDatabase = FirebaseDatabase.getInstance();

        // Initialize Categories
        if(this.categories.isEmpty()) {
            DatabaseReference ref = firebaseDatabase.getReference("Configurations").child("configurations").getRef();
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Configurations conf = dataSnapshot.getValue(Configurations.class);
                    if (conf == null)
                        throw new NoSuchElementException("Cant Retrieve Configurations from database");

                    Map<String,String> categoriesOptions = conf.getCategoriesOptions();
                    Set<String> names = categoriesOptions.keySet();
                    Collection<String> images = categoriesOptions.values();
                    List<Category> categories = new ArrayList<>();

                    Iterator<String> itrNames = names.iterator();
                    Iterator<String> itrImages = images.iterator();

                    while(itrNames.hasNext() && itrImages.hasNext()){
                        Category category = new Category(itrNames.next(),itrImages.next());
                        categories.add(category);
                    }

                    gotCategoriesFromFireBase(categories);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        } else IntializeGridView();

        //Check For Admin User
        DatabaseReference ref = firebaseDatabase.getReference("Configurations").child("configurations").getRef();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Configurations conf = dataSnapshot.getValue(Configurations.class);
                if (conf == null)
                    throw new NoSuchElementException("Cant Retrieve Configurations from database");
                checkAuth(conf);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        publishBtn = view.findViewById(R.id.publish);
        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                    if (publishFragment == null)
                        publishFragment = new PublishFragment();
                    outerTransaction(publishFragment);
                }
                else {
                    if (loginFragment == null)
                        loginFragment = new LoginFragment();
                    outerTransaction(loginFragment);
                }
            }
        });

        // Admin OnClick
        admin_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminPanel.setVisibility(View.VISIBLE);
                admin_open.setVisibility(View.GONE);
            }
        });
        admin_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminPanel.setVisibility(View.GONE);
                admin_open.setVisibility(View.VISIBLE);
            }
        });
        admin_CategoriesManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CategoriesManagement.class);
                startActivity(intent);
            }
        });

        admin_ConfManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ConfManagement.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void checkAuth(Configurations conf) {
        this.conf = conf;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth == null) return;
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null) return; // Can be removed
        boolean admin = conf.getAdminsList().contains(firebaseUser.getEmail());
        if(admin) {
            admin_open.setVisibility(View.VISIBLE);

        }
        else {
            adminPanel.setVisibility(View.GONE);
            admin_open.setVisibility(View.GONE);
        }
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
