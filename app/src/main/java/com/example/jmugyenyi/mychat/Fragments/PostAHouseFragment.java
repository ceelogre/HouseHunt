package com.example.jmugyenyi.mychat.Fragments;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import com.example.jmugyenyi.mychat.Activities.LocationActivity;

import com.example.jmugyenyi.mychat.R;
import com.example.jmugyenyi.mychat.model.User;
import com.example.jmugyenyi.mychat.utils.HouseCRUD;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostAHouseFragment extends Fragment {

    protected static final String TAG = "PostAHouseFragment";

    private FirebaseAuth mfirebaseAuth;

    private View postHouse;
    private Button uploadPicButton,  getLocationButton, saveButton;
    private EditText housename, houseStreet, houseCity, houseCountry, houseNumberOrooms, houseNumberOmates, houseRent;
    private CircleImageView circleImageView;
    private String saveHousename, saveHouseStreet, saveHouseCity, saveHouseCountry,
            saveHouseNumberOrooms, saveHouseNumberOmates, saveHouseRent;



    private  static  final int galleryPicture = 1;

    private  StorageReference houseImageRef;
    private String currentUserID;
    private DatabaseReference databaseReference;

    private ProgressDialog loadingBar;

    private User userID = new User();

    public PostAHouseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         postHouse= inflater.inflate(R.layout.fragment_post_ahouse, container, false);
        mfirebaseAuth = FirebaseAuth.getInstance();
        currentUserID = mfirebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        houseImageRef = FirebaseStorage.getInstance().getReference().child("House Images");

        initializeFields();

//what I had
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // take you to the map activity
                Intent intent = new Intent(getContext(), LocationActivity.class);
                intent.putExtra("house_ID",userID.getHouseid());
                startActivity(intent);


            }
        });
//end of what I had
        //Joel's
         new FireBaseBackgroundTasks().execute();

        Log.d(TAG, "onCreateView: Oncreate Working");

//end

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHouseInfo();
                Toast.makeText(getActivity(), "Info saved!", Toast.LENGTH_SHORT).show();
            }
        });

        uploadPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,galleryPicture);
            }
        });



        return  postHouse;
    }

    // Method to crop a picture from gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode==galleryPicture && resultCode== Activity.RESULT_OK)
        {//Log.d(TAG, "onActivityResult: Got Here 2");
            //Log.d(TAG, "onActivityResult: Code   "+requestCode);
            Uri picUri = data.getData();

            Intent intent = CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .getIntent(getContext());

            startActivityForResult(intent,CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
                   // .start(getActivity());
        }


        //Log.d(TAG, "onActivityResult: Code2   "+CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            //Log.d(TAG, "onActivityResult: Got Here 3");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode== Activity.RESULT_OK){

                //Log.d(TAG, "onActivityResult: Got Here 4");
                loadingBar.setTitle("Set House Image");
                loadingBar.setMessage("Please wait!");
                loadingBar.show();

                final StorageReference filePath = houseImageRef.child(currentUserID+" .jpg");

                Uri resultUri = result.getUri();
                UploadTask uploadTask = filePath.putFile(resultUri);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            final Uri downloadUri = task.getResult();

                            loadingBar.dismiss();
                            Log.d(TAG, "onComplete: "+userID.getHouseid());
                            //loadingBar.dismiss();
                            databaseReference.child("House").child(userID.getHouseid()).child("image").setValue(downloadUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getActivity(), "Image saved in Database!", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }else{
                                        String message = task.getException().toString();
                                        Toast.makeText(getActivity(), "Error: "+message, Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(getActivity(), "Error: "+message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
            }
        }

    }

    private void initializeFields() {

        circleImageView = postHouse.findViewById(R.id.post_house_image);
        uploadPicButton = postHouse.findViewById(R.id.post_house_uploadButton);
        //cameraButton = postHouse.findViewById(R.id.post_house_cameraButton);
        getLocationButton = postHouse.findViewById(R.id.post_house_locationButton);
        saveButton =  postHouse.findViewById(R.id.post_house_saveButton);
        housename = postHouse.findViewById(R.id.post_house_name);
        houseStreet = postHouse.findViewById(R.id.post_house_street);
        houseCity = postHouse.findViewById(R.id.post_house_city);
        houseCountry = postHouse.findViewById(R.id.post_house_country);
        houseNumberOrooms = postHouse.findViewById(R.id.post_house_rooms);
        houseNumberOmates = postHouse.findViewById(R.id.post_house_housemates);
        houseRent = postHouse.findViewById(R.id.post_house_rent);
        loadingBar = new ProgressDialog(getActivity());
    }

    private void saveHouseInfo() {

        saveHousename = housename.getText().toString();
        saveHouseStreet = houseStreet.getText().toString();
        saveHouseCity = houseCity.getText().toString();
        saveHouseCountry = houseCountry.getText().toString();
        saveHouseNumberOrooms = houseNumberOrooms.getText().toString();
        saveHouseNumberOmates = houseNumberOmates.getText().toString();
        saveHouseRent = houseRent.getText().toString();

        if (TextUtils.isEmpty(saveHousename)  || TextUtils.isEmpty(saveHouseStreet) ||
                TextUtils.isEmpty(saveHouseCity)|| TextUtils.isEmpty(saveHouseCountry)|| TextUtils.isEmpty(saveHouseNumberOrooms)
                ||TextUtils.isEmpty(saveHouseNumberOmates) || TextUtils.isEmpty(saveHouseRent)){
            Toast.makeText(getActivity(), "Enter Missing Input!",Toast.LENGTH_SHORT).show();
        }else
        {
            HouseCRUD houseCRUD = new HouseCRUD(mfirebaseAuth);
            houseCRUD.createHouseCollection(saveHousename,saveHouseStreet,saveHouseCity,
                    saveHouseCountry,saveHouseNumberOrooms,saveHouseNumberOmates,saveHouseRent);
            houseCRUD.addRoomToHouse();
        }
    }
    private class FireBaseBackgroundTasks extends AsyncTask<Void, Void, String> {

        String houseID ="test";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... params) {
            databaseReference.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange first one: ");
                    if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))
                            && (dataSnapshot.hasChild("image")) && (dataSnapshot.hasChild("house")))
                    {
                         houseID = dataSnapshot.child("house").getValue()
                                .toString().replace("=true","")
                                .replaceAll("\\{","")
                                .replaceAll("\\}","");
                         userID.setHouseid(houseID);
                        Log.d(TAG, "onDataChange: "+houseID);

                    }else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))
                            && (dataSnapshot.hasChild("house")))
                    {
                         houseID = dataSnapshot.child("house").getValue()
                                .toString().replace("=true","")
                                .replaceAll("\\{","")
                                .replaceAll("\\}","");
                        userID.setHouseid(houseID);
                        Log.d(TAG, "onDataChange: "+houseID);
                    }else
                    {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return houseID;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }


}
