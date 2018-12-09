package com.example.jmugyenyi.mychat.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaActionSound;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jmugyenyi.mychat.Fragments.PostAHouseFragment;
import com.example.jmugyenyi.mychat.R;
import com.example.jmugyenyi.mychat.utils.RoomCRUD;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

// This is a class used to add a room to the application
public class AddRoomActivity extends AppCompatActivity {

    // Instance variables
    private Button postPicRoom, saveRoom;
    private EditText roomDescriptionTextField, roomPriceTextField;
    private boolean allFieldsFilled = false;
    private static final int galleryPicture = 1;
    private StorageReference houseImageRef;
    private ProgressDialog loadingBar;
    private String houseId;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String photoPath;
    private Uri photoUri;
    private String roomId;
    private DatabaseReference imageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);


        saveRoom = findViewById(R.id.post_room);
        postPicRoom = findViewById(R.id.post_room_pic);
        houseImageRef = FirebaseStorage.getInstance().getReference().child("Room Images");
        imageReference = FirebaseDatabase.getInstance().getReference().child("Room");
        houseId = getIntent().getStringExtra("addedHouseId");

        loadingBar = new ProgressDialog(getApplicationContext());

        saveRoom.setOnClickListener(
                new View.OnClickListener() {

                    public void onClick(View v) {

                        allFieldsFilled = saveRoomDetails();

                        if (allFieldsFilled) {

                            //Work in progress
                            //uploadRoomImage();

                            Intent PostAHouseIntentResume = new Intent(AddRoomActivity.this, MainActivity.class);
                            startActivity(PostAHouseIntentResume);
                        } else {
                            Log.d("Room not filled", "Send an error msg");
                        }
                    }
                }
        );

        postPicRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                photoUri = dispatchTakePictureIntent();
            }
        });

    }
    private void uploadRoomImage(){

        final boolean isTaskSuccessful = false;


        StorageReference houseImageRef;
        houseImageRef = FirebaseStorage.getInstance().getReference().child("Room Images");

        final StorageReference filePath = houseImageRef.child(roomId);

        UploadTask uploadTask = filePath.putFile(photoUri);
        loadingBar.setTitle("Set House Image");
        loadingBar.setMessage("Please wait!");
        loadingBar.show();


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

                    imageReference.child(roomId).child("picFileLocation").setValue(downloadUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Image successfully uploaded", Toast.LENGTH_LONG);
                            }else{
                                String message = task.getException().toString();
                                Toast.makeText(getApplicationContext(), "Image not uploaded", Toast.LENGTH_LONG);
                            }
                        }
                    });
                } else {
                    String message = task.getException().toString();

                }
            }
        });
    }


    // Method to save room details to database
    private boolean saveRoomDetails() {
        roomDescriptionTextField = findViewById(R.id.post_desc);
        roomPriceTextField = findViewById(R.id.post_room_rent);

        String roomDescription = roomDescriptionTextField.getText().toString();
        String roomPrice = roomPriceTextField.getText().toString();

        if (!roomDescription.isEmpty() && !roomPrice.isEmpty()) {
            RoomCRUD roomObject = new RoomCRUD();
            roomId = roomObject.createRoomCollection(houseId, roomDescription, Double.parseDouble(roomPrice), photoPath);
            return true;
        }

        return false;
    }

    private Uri dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createImageFile();

            }
            catch (IOException e){
                System.out.println(e.getCause());
            }

            if(photoFile != null){
                photoUri =FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                Log.d("loc", photoUri.toString());

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

        return photoUri;
    }

    private File createImageFile() throws IOException {
        String fileName = new SimpleDateFormat("MMyyyydd_HHmmss").format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                fileName,
                ".jpg",
                storageDir
        );

        photoPath = image.getAbsolutePath();
        return image;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        ImageView mImageView = findViewById(R.id.mImageView);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            if(data != null){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mImageView.setImageBitmap(imageBitmap);
            }

        }
    }
}
