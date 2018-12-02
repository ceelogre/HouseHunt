package com.example.jmugyenyi.mychat.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jmugyenyi.mychat.R;
import com.example.jmugyenyi.mychat.utils.HouseCRUD;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostAHouseFragment extends Fragment {

    private FirebaseAuth mfirebaseAuth;

    private View postHouse;
    private Button uploadPicButton, cameraButton, getLocationButton, saveButton;
    private EditText housename, houseStreet, houseCity, houseCountry, houseNumberOrooms, houseNumberOmates, houseRent;
    private String saveHousename, saveHouseStreet, saveHouseCity, saveHouseCountry,
            saveHouseNumberOrooms, saveHouseNumberOmates, saveHouseRent;

    public PostAHouseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         postHouse= inflater.inflate(R.layout.fragment_post_ahouse, container, false);
        mfirebaseAuth = FirebaseAuth.getInstance();

        initializeFields();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHouseInfo();
                Toast.makeText(getActivity(), "Info saved!", Toast.LENGTH_SHORT).show();
            }
        });

        return  postHouse;
    }

    private void initializeFields() {

        uploadPicButton = postHouse.findViewById(R.id.post_house_uploadButton);
        cameraButton = postHouse.findViewById(R.id.post_house_cameraButton);
        getLocationButton = postHouse.findViewById(R.id.post_house_locationButton);
        saveButton =  postHouse.findViewById(R.id.post_house_saveButton);
        housename = postHouse.findViewById(R.id.post_house_name);
        houseStreet = postHouse.findViewById(R.id.post_house_street);
        houseCity = postHouse.findViewById(R.id.post_house_city);
        houseCountry = postHouse.findViewById(R.id.post_house_country);
        houseNumberOrooms = postHouse.findViewById(R.id.post_house_rooms);
        houseNumberOmates = postHouse.findViewById(R.id.post_house_housemates);
        houseRent = postHouse.findViewById(R.id.post_house_rent);
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
//            HouseCRUD houseCRUD = new HouseCRUD(mfirebaseAuth);
//            houseCRUD.createHouseCollection(saveHousename,saveHouseStreet,saveHouseCity,
//                    saveHouseCountry,saveHouseNumberOrooms,saveHouseNumberOmates,saveHouseRent);
//            houseCRUD.addRoomToHouse();
        }
    }


}
