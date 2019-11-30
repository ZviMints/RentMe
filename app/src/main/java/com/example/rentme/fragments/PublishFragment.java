package com.example.rentme.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rentme.R;
import com.example.rentme.activities.MainActivity;
import com.example.rentme.model.Product;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class PublishFragment extends Fragment implements AdapterView.OnItemSelectedListener  {

    private Button publishBtn;
    private Button backMenu;
    private Button addCameraPicBtm;
    private Button addGaleryPicBtm;
    private ProgressBar progressBar;
    private Spinner categorySelectedSpin;
    private Spinner productConditionSpin;
    private Spinner rentPeriodSpin;
    private EditText titleLayer;
    private EditText detailsLayer;
    private EditText priceLayer;
    public ImageView imageview;



    private String[] categoryNames={"בחר קטגורייה...","אביזרים","מוצרי חשמל","מטבח","גינה","ספורט"};
    private String[] statusNames={"בחר מצב...","חדש","כמו חדש","משומש","שבור"};
    private String[] RentPeriodOptions = {"לשעה","ליום","לשבוע","לחודש","לשנה"};
    public String selectedCategory = "קטגורייה...";
    public String selectedCondition = "בחר מצב...";
    public String productTitle = "";
    public String Price ="";
    public String rentPeriod ="";
    public String image = "";
    public String details = "";
    private long currentTime;
    private String downloadUri = "";


    private MainFragment mainFragment;
    private final int RESULT_LOAD_IMG = 1;
    private final int RESULT_CAPTURE_IMG = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publish, container, false);
        categorySelectedSpin = (Spinner)view.findViewById(R.id.select_category);
        productConditionSpin = (Spinner)view.findViewById(R.id.product_condition);
        rentPeriodSpin = (Spinner)view.findViewById(R.id.rent_period);
        publishBtn = view.findViewById(R.id.addBtn);
        addCameraPicBtm = view.findViewById(R.id.add_camera_pic_Btm);
        addGaleryPicBtm = view.findViewById(R.id.add_galery_pic_Btm);
        titleLayer = (EditText)view.findViewById(R.id.product_title);
        detailsLayer = (EditText)view.findViewById(R.id.details);
        priceLayer = (EditText)view.findViewById(R.id.price);
        backMenu = view.findViewById(R.id.backMain);
        progressBar = view.findViewById(R.id.progressbar);
        imageview = view.findViewById(R.id.product_pic);


        //start category spinner
        categorySelectedSpin.setOnItemSelectedListener(this);
        ArrayAdapter aaCategory = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,categoryNames);
        aaCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySelectedSpin.setAdapter(aaCategory);
        //end category spinner

        //start condition spinner
        productConditionSpin.setOnItemSelectedListener(this);
        ArrayAdapter aaArea = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,statusNames);
        aaArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productConditionSpin.setAdapter(aaArea);
        //end condition spinner

        //start rent period spinner
        rentPeriodSpin.setOnItemSelectedListener(this);
        ArrayAdapter aaRentPeriod = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,RentPeriodOptions);
        aaRentPeriod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rentPeriodSpin.setAdapter(aaRentPeriod);
        //end rent period spinner

        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                productTitle =  titleLayer.getText().toString();
                details = detailsLayer.getText().toString();
                Price = priceLayer.getText().toString();

                if ((productTitle.length() > 0)&& (selectedCategory != "בחר קטגורייה...")&& (details.length() > 0) &&
                        (selectedCondition !=  "בחר מצב...") &&  (Price.length() > 0)) {

                    Product addedProduct = new Product(productTitle, selectedCategory, details, selectedCondition, Price, rentPeriod, downloadUri.toString());
                    FirebaseDatabase.getInstance().getReference("category")
                            .child(selectedCategory).child(new Date().getTime()+": "+productTitle).setValue(addedProduct)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getContext(), "פרסום " + productTitle + " בוצע בהצלחה", Toast.LENGTH_SHORT).show();
                                        if (mainFragment == null)
                                            mainFragment = new MainFragment();
                                        outerTransaction(mainFragment);
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "קלט לא חוקי", Toast.LENGTH_SHORT).show();
                }
            }
         });

        addCameraPicBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, RESULT_CAPTURE_IMG);//zero can be replaced with any action code (called requestCode)
//                Toast.makeText(getContext(), ((MainActivity)(getActivity())).getImageview().toString(), Toast.LENGTH_LONG).show();

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
        Toast.makeText(getContext(),
                "\ncategory : "+ String.valueOf(categorySelectedSpin.getSelectedItem()) +
                        "\ncndition : "+ String.valueOf(productConditionSpin.getSelectedItem())+
                        "\nrentperiod : "+ String.valueOf(rentPeriodSpin.getSelectedItem()),
                Toast.LENGTH_SHORT).show();

        selectedCategory = categorySelectedSpin.getItemAtPosition(categorySelectedSpin.getSelectedItemPosition()).toString();
        selectedCondition = productConditionSpin.getItemAtPosition(productConditionSpin.getSelectedItemPosition()).toString();
        rentPeriod = rentPeriodSpin.getItemAtPosition(rentPeriodSpin.getSelectedItemPosition()).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void outerTransaction(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.OuterFragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch(reqCode) {
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

                if (resultCode == RESULT_OK ) {
                    Bundle extras = data.getExtras();//finish converting and copy the image
                    Bitmap bitmap = extras.getParcelable("data");//receive image to bitmap
                    imageview.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(getContext(), "You haven't take a picture", Toast.LENGTH_LONG).show();
                }
                break;
        }

        UploadPicAndUpdateDownloadUri();
    }

    private void UploadPicAndUpdateDownloadUri() {
        //start push choosen picture to fireBase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        currentTime = new Date().getTime();
        StorageReference choosenPicRef = storageRef.child(currentTime + ".jpg");
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

        //get the uploaded pic URL
        final StorageReference ref = storageRef.child(currentTime + ".jpg");


        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUri = task.getResult().toString();

                    Toast.makeText(getContext(), downloadUri, Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getContext(), "not work", Toast.LENGTH_LONG).show();
                }
            }
        });
        //get the uploaded pic URL

    }




}
