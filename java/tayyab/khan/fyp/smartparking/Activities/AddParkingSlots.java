package tayyab.khan.fyp.smartparking.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import tayyab.khan.fyp.smartparking.Helpers.AuthHelper;
import tayyab.khan.fyp.smartparking.R;

public class AddParkingSlots extends AppCompatActivity {
    private TextView areaTV;
    private EditText namePSlotET, idPPlaceET;
    private CheckBox isRoofedCB;
    private Button selectLocationBtnAPS;

    private String parkingArea = "", namePSlot = "", namePPlace = "", placeLat = "", placeLong = "";
    private String isRoofed = "false";

    private static final int LOCATION_PERMISSION_CODE = 109;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking_slots);

        areaTV = findViewById(R.id.areaTVAPS);
        namePSlotET = findViewById(R.id.namePSlotETAPS);
        idPPlaceET = findViewById(R.id.idPPlaceETAPS);
        isRoofedCB = findViewById(R.id.isRoofedCBAPS);
        selectLocationBtnAPS = findViewById(R.id.selectLocationBtnAPS);

//        parkingArea = getIntent().getStringExtra("AREA");
        parkingArea = AuthHelper.pArea;
        areaTV.setText("Add Parking Place to Area " +parkingArea);

//        isRoofedCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    isRoofed = "true";
//                }
//            }
//        });
//
        if (!AuthHelper.slotLat.equals("") && !AuthHelper.slotLong.equals("")) {
            selectLocationBtnAPS.setText(AuthHelper.slotLat + ", " + AuthHelper.slotLong);
            placeLat = AuthHelper.slotLat;
            placeLong = AuthHelper.slotLong;
        }
//
//        if (!AuthHelper.isRoofed.equals("true"))
//            isRoofedCB.setChecked(true);
//
//        if (!AuthHelper.placeName.equals(""))
//            namePPlace = AuthHelper.placeName;
//
//        if (!AuthHelper.slotName.equals(""))
//            namePSlot = AuthHelper.slotName;
    }

    // Add parking button clicked
    public void onAddParkingSlotClicked(View view) {
        if (isRoofedCB.isChecked())
            isRoofed = "true";

        if (parkingArea.equals("")) {
            Toast.makeText(this, "You haven't selected Parking Area!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(AddParkingSlots.this, ManageParking.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        } else {
            namePSlot = namePSlotET.getText().toString().trim();
            namePPlace = idPPlaceET.getText().toString().trim();

            if (namePSlot.equals("") || namePPlace.equals("")) {
                Toast.makeText(this, "Enter complete information!", Toast.LENGTH_SHORT).show();
            } else {

                if (placeLat.equals("") && placeLong.equals("")) {
                    Toast.makeText(this, "Select location first", Toast.LENGTH_SHORT).show();
                } else {
                    final ProgressDialog dialog = new ProgressDialog(AddParkingSlots.this);
                    dialog.setMessage("Adding Parking Place...");
                    dialog.setCancelable(false);
                    dialog.show();

                    String id = parkingArea+namePSlot+namePPlace;
                    id = id.replace(" ", "");

                    Map<String, Object> place = new HashMap<>();
                    place.put("id", id);
                    place.put("parkingArea", parkingArea);
                    place.put("parkingSlot", namePSlot);
                    place.put("parkingPlace", namePPlace);
                    place.put("ppLat", placeLat);
                    place.put("ppLong", placeLong);
                    place.put("isBooked", "false");
                    place.put("isRoofed", isRoofed);
                    place.put("customerId", "");

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference cr = db.collection("parkingPlaces");
                    cr.document(id).set(place, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    dialog.dismiss();
                                    Toast.makeText(AddParkingSlots.this, "Parking Place Added Successfully!", Toast.LENGTH_LONG).show();
//                                    AuthHelper.slotLong = "";
//                                    AuthHelper.slotLat = "";
                                    AuthHelper.pArea = "";
                                    startActivity(new Intent(AddParkingSlots.this, ManageParking.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    Toast.makeText(AddParkingSlots.this, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        }
    }

    // Select location button clicked
    public void onSelectLocationClicked(View view) {

        if (ContextCompat.checkSelfPermission(AddParkingSlots.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            if (isRoofedCB.isChecked())
//                AuthHelper.isRoofed = "true";
//            else
//                AuthHelper.isRoofed = "false";
//
//            AuthHelper.slotName = namePSlot;
//            AuthHelper.placeName = namePPlace;
            startActivity(new Intent(AddParkingSlots.this, SelecParkingPlace.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        } else {
            requestLocationPermission();
        }
    }

    // Requesting Location permission
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to get the location!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AddParkingSlots.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }

    // In result of permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                if (isRoofedCB.isChecked())
//                    AuthHelper.isRoofed = "true";
//                else
//                    AuthHelper.isRoofed = "false";
//
//                AuthHelper.slotName = namePSlot;
//                AuthHelper.placeName = namePPlace;
                AuthHelper.pArea = "";
                startActivity(new Intent(AddParkingSlots.this, SelecParkingPlace.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "****", Toast.LENGTH_SHORT).show();
        }
    }

}