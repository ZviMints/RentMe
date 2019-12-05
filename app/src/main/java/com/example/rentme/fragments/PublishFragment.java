package com.example.rentme.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.rentme.R;
import com.example.rentme.model.Configurations;
import com.example.rentme.model.Product;
import com.example.rentme.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static android.app.Activity.RESULT_OK;

public class PublishFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Button publishBtn;
    private Button backMenu;
    private Button addCameraPicBtm;
    private Button addGaleryPicBtm;
    private ProgressBar progressBar_afterPublish;
    private ProgressBar progressBarAfterAddPic;
    private Spinner categorySelectedSpin;
    private Spinner productConditionSpin;
    private Spinner rentPeriodSpin;
    private EditText titleLayer;
    private EditText detailsLayer;
    private EditText priceLayer;
    public ImageView imageview;

    private List<String> statusNames;
    private List<String> RentPeriodOptions;
    public String selectedCategory = "קטגורייה...";
    public String selectedCondition = "בחר מצב...";

    public String productTitle;
    public String Price;
    public String strDate;
    public String userUid;
    public String rentPeriod;
    public String image;
    public String details;
    private long currentTime;
    private String downloadUri = "";

    private MainFragment mainFragment;
    private final int RESULT_LOAD_IMG = 1;
    private final int RESULT_CAPTURE_IMG = 0;

    private ScrollView mainScrollView;
    private ProgressBar progressBarOnLoad;
    private List<String> categoryNames = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;

    private void gotConfigurationsFromFireBase(Configurations conf) {
        this.categoryNames = conf.getCategoriesOptions();
        this.statusNames = conf.getStateOptions();
        this.RentPeriodOptions = conf.getRentOptions();
        IntializeSpinners();

    }

    private void IntializeSpinners() {
        mainScrollView.setVisibility(View.VISIBLE);
        progressBarOnLoad.setVisibility(View.GONE);

        //start category spinner
        categorySelectedSpin.setOnItemSelectedListener(this);
        ArrayAdapter aaCategory = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, categoryNames);
        aaCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySelectedSpin.setAdapter(aaCategory);
        //end category spinner

        //start condition spinner
        productConditionSpin.setOnItemSelectedListener(this);
        ArrayAdapter aaArea = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, statusNames);
        aaArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productConditionSpin.setAdapter(aaArea);
        //end condition spinner

        //start rent period spinner
        rentPeriodSpin.setOnItemSelectedListener(this);
        ArrayAdapter aaRentPeriod = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, RentPeriodOptions);
        aaRentPeriod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rentPeriodSpin.setAdapter(aaRentPeriod);
        //end rent period spinner

        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar_afterPublish.setVisibility(View.VISIBLE);
                productTitle = titleLayer.getText().toString();
                details = detailsLayer.getText().toString();
                Price = priceLayer.getText().toString();
                Date date = new Date();
                strDate = getDateByFormat(date);
                userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if ((productTitle.length() > 0) &&
                        (details.length() > 0) &&
                        (Price.length() > 0)) {

                    Product addedProduct = new Product(productTitle, selectedCategory, details, selectedCondition, Price, rentPeriod, strDate, userUid,date.getTime()+"",downloadUri);
                    FirebaseDatabase.getInstance().getReference("Categories")
                            .child(selectedCategory).child(date.getTime() + ": " + productTitle).setValue(addedProduct)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar_afterPublish.setVisibility(View.GONE);
                                        Toast.makeText(getContext(), "פרסום " + productTitle + " בוצע בהצלחה", Toast.LENGTH_SHORT).show();
                                        if (mainFragment == null)
                                            mainFragment = new MainFragment();
                                        outerTransaction(mainFragment);
                                    } else {
                                        progressBar_afterPublish.setVisibility(View.GONE);
                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                } else {
                    progressBar_afterPublish.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "קלט לא חוקי", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publish, container, false);

        categorySelectedSpin = view.findViewById(R.id.select_category);
        productConditionSpin =  view.findViewById(R.id.product_condition);
        rentPeriodSpin =  view.findViewById(R.id.rent_period);
        publishBtn = view.findViewById(R.id.addBtn);
        addCameraPicBtm = view.findViewById(R.id.add_camera_pic_Btm);
        addGaleryPicBtm = view.findViewById(R.id.add_galery_pic_Btm);
        titleLayer =  view.findViewById(R.id.product_title);
        detailsLayer =  view.findViewById(R.id.details);
        priceLayer =  view.findViewById(R.id.price);
        backMenu = view.findViewById(R.id.backMain);
        progressBar_afterPublish = view.findViewById(R.id.progressBar_afterPublish);
        progressBarAfterAddPic = view.findViewById(R.id.progressBar_afterAddPic);
        progressBarOnLoad = view.findViewById(R.id.progressBarOnLoad);
        mainScrollView = view.findViewById(R.id.mainScrollView);
        progressBarOnLoad = view.findViewById(R.id.progressBarOnLoad);
        imageview = view.findViewById(R.id.product_pic);


        firebaseDatabase = FirebaseDatabase.getInstance();
        // Initialize Configurations
        if(this.categoryNames.isEmpty()) { // Example category names
            DatabaseReference ref = firebaseDatabase.getReference("Configurations").child("configurations").getRef();
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Configurations conf = dataSnapshot.getValue(Configurations.class);
                    if (conf == null)
                        throw new NoSuchElementException("Cant Retrieve Configurations from database");
                    gotConfigurationsFromFireBase(conf);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else IntializeSpinners();


        // On Click Events
        addCameraPicBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, RESULT_CAPTURE_IMG);//zero can be replaced with any action code (called requestCode)
            }
        });

        addGaleryPicBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        backMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainFragment == null)
                    mainFragment = new MainFragment();
                outerTransaction(mainFragment);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedCategory = categorySelectedSpin.getItemAtPosition(categorySelectedSpin.getSelectedItemPosition()).toString();
        selectedCondition = productConditionSpin.getItemAtPosition(productConditionSpin.getSelectedItemPosition()).toString();
        rentPeriod = rentPeriodSpin.getItemAtPosition(rentPeriodSpin.getSelectedItemPosition()).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void outerTransaction(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.OuterFragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case RESULT_LOAD_IMG:

                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageview.setImageBitmap(selectedImage);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
                }
                break;

            case RESULT_CAPTURE_IMG:

                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();//finish converting and copy the image
                    Bitmap bitmap = extras.getParcelable("data");//receive image to bitmap
                    imageview.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(getContext(), "You haven't take a picture", Toast.LENGTH_LONG).show();
                }
                break;
        }
        progressBarAfterAddPic.setVisibility(View.VISIBLE);
        UploadPicAndUpdateDownloadUri();
    }

    private void UploadPicAndUpdateDownloadUri() {
        //start push choosen picture to fireBase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        currentTime = new Date().getTime();
        final StorageReference choosenPicRef = storageRef.child("product/"+currentTime + ".jpg");

        // Get the data from an ImageView as bytes
        imageview.setDrawingCacheEnabled(true);
        imageview.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] theData = baos.toByteArray();

        UploadTask uploadTask = choosenPicRef.putBytes(theData);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
        //finnish push choosen picture to fireBase storage

        //start get the uploaded pic URL
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return choosenPicRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUri = task.getResult().toString();
                    progressBarAfterAddPic.setVisibility(View.GONE);

                } else {
                    Toast.makeText(getContext(), "not work", Toast.LENGTH_LONG).show();
                }
            }
        });
        //end get the uploaded pic URL
    }
    private String getDateByFormat(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(date);
    }
}
