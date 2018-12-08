package com.example.jmugyenyi.mychat.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jmugyenyi.mychat.Fragments.PostAHouseFragment;
import com.example.jmugyenyi.mychat.R;
import com.example.jmugyenyi.mychat.utils.RoomCRUD;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class AddRoomActivity extends AppCompatActivity {

    private Button postPicRoom, saveRoom;
    private EditText roomDescriptionTextField, roomPriceTextField;
    private boolean allFieldsFilled = false;
    private  static  final int galleryPicture = 1;
    private StorageReference houseImageRef;
    private ProgressDialog loadingBar;
    private String houseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        saveRoom = findViewById(R.id.post_room);
        postPicRoom = findViewById(R.id.post_room_pic);
        houseImageRef = FirebaseStorage.getInstance().getReference().child("Room Images");
        houseId = getIntent().getStringExtra("addedHouseId");

        saveRoom.setOnClickListener(
                new View.OnClickListener(){

                    public void onClick(View v){

                        allFieldsFilled =saveRoomDetails();

                        if(allFieldsFilled){

                            Intent PostAHouseIntentResume  = new Intent(AddRoomActivity.this, MainActivity.class);
                            startActivity(PostAHouseIntentResume);
                        }
                        else{
                            Log.d("Room not filled", "Send an error msg");
                        }
                    }
                }
        );


        postPicRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,galleryPicture);
            }
        });


    }


    private boolean saveRoomDetails(){
        roomDescriptionTextField = findViewById(R.id.post_desc);
        roomPriceTextField = findViewById(R.id.post_room_rent);

        String roomDescription = roomDescriptionTextField.getText().toString();
        String roomPrice = roomPriceTextField.getText().toString();

        if(!roomDescription.isEmpty() && !roomPrice.isEmpty()){
            RoomCRUD roomObject = new RoomCRUD();
            roomObject.createRoomCollection(houseId, roomDescription, Double.parseDouble(roomPrice));
            return true;
        }

        return false;
    }


//    // Method to crop a picture from gallery
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//        if (requestCode==galleryPicture && resultCode== Activity.RESULT_OK)
//        {//Log.d(TAG, "onActivityResult: Got Here 2");
//            //Log.d(TAG, "onActivityResult: Code   "+requestCode);
//            Uri picUri = data.getData();
//
//            Intent intent = CropImage.activity()
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1,1)
//                    .getIntent(getContext());
//
//            startActivityForResult(intent,CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
//            // .start(getActivity());
//        }
//
//
//        //Log.d(TAG, "onActivityResult: Code2   "+CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            //Log.d(TAG, "onActivityResult: Got Here 3");
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//
//            if (resultCode == Activity.RESULT_OK) {
//
//                //Log.d(TAG, "onActivityResult: Got Here 4");
//                loadingBar.setTitle("Set House Image");
//                loadingBar.setMessage("Please wait!");
//                loadingBar.show();
//
//                final StorageReference filePath = houseImageRef.child(currentUserID + " .jpg");
//
//                Uri resultUri = result.getUri();
//                UploadTask uploadTask = filePath.putFile(resultUri);
//
//                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                    @Override
//                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                        if (!task.isSuccessful()) {
//                            throw task.getException();
//                        }
//
//                        // Continue with the task to get the download URL
//                        return filePath.getDownloadUrl();
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                        if (task.isSuccessful()) {
//                            final Uri downloadUri = task.getResult();
//
//                            loadingBar.dismiss();
//                            Log.d(TAG, "onComplete: " + userID.getHouseid());
//                            //loadingBar.dismiss();
//                            databaseReference.child("House").child(userID.getHouseid()).child("image").setValue(downloadUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(getActivity(), "Image successfuly uploaded!", Toast.LENGTH_SHORT).show();
//                                        loadingBar.dismiss();
//                                    } else {
//                                        String message = task.getException().toString();
//                                        Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
//                                        loadingBar.dismiss();
//                                    }
//                                }
//                            });
//                        } else {
//                            String message = task.getException().toString();
//                            Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
//                            loadingBar.dismiss();
//                        }
//                    }
//                });
//
//            }
//        }
//        }


        }
