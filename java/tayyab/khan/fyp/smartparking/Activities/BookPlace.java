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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import tayyab.khan.fyp.smartparking.Helpers.AuthHelper;
import tayyab.khan.fyp.smartparking.Model.ParkingPlace;
import tayyab.khan.fyp.smartparking.Model.User;
import tayyab.khan.fyp.smartparking.R;

public class BookPlace extends AppCompatActivity {
    private TextView areaTV, slotTV, placeTV, idTV, roofedTV;

    ParkingPlace place;
    boolean isError = false;

    private String longitude="", latitude="";

    private FusedLocationProviderClient client;
    private static final int LOCATION_PERMISSION_CODE = 102;


    private FusedLocationProviderClient fusedLocationClient;
    private String mLat;
    private String mLong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_place);
        getSupportActionBar().setTitle("Booking Details");

        areaTV = findViewById(R.id.pAreaTVBP);
        slotTV = findViewById(R.id.pSlotTVBP);
        placeTV = findViewById(R.id.pPlaceTVBP);
        idTV = findViewById(R.id.customerIdTVBP);
        roofedTV = findViewById(R.id.isRoofedTVBP);

        mLat = "0.0";
        mLong = "0.0";

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mLong = String.valueOf(location.getLongitude());
                            mLat = String.valueOf(location.getLatitude());
                        }
                    }
                });


        place = (ParkingPlace) getIntent().getSerializableExtra("PLACE");

        if (place != null) {
            areaTV.setText(place.getParkingArea());
            slotTV.setText(place.getParkingSlot());
            placeTV.setText(place.getParkingPlace());

            if (place.getCustomerId().equals("")) {
                idTV.setText("Not booked yet!");
                findViewById(R.id.cancelBtnBP).setVisibility(View.GONE);
            } else {
                idTV.setText(place.getCustomerId());
                if (!place.getCustomerId().equals(AuthHelper.id)) {
                    findViewById(R.id.cancelBtnBP).setVisibility(View.GONE);
                }
                findViewById(R.id.bookPlaceBtnBP).setVisibility(View.GONE);
            }

            if (!place.getIsRoofed().equals("true"))
                roofedTV.setText("NO");
            else
                roofedTV.setText("YES");
        }
    }




    public void onCancelBookingClicked(View view) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Canceling...");
        dialog.show();

//        FirebaseFirestore d = FirebaseFirestore.getInstance();
//        DocumentReference c = d.collection("customers").document(AuthHelper.id);
//        c.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//
//                    isError = false;
//                    DocumentSnapshot snapshot = task.getResult();
//                    User user = snapshot.toObject(User.class);

        HashMap<String, Object> map1 = new HashMap<>();
        if (!AuthHelper.currentBookings.equals("0"))
            map1.put("currentBookings", String.valueOf(Integer.parseInt(AuthHelper.currentBookings) - 1));
        else
            map1.put("currentBookings", "0");
        FirebaseFirestore.getInstance().collection("customers").document(AuthHelper.id).set(map1, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        isError = false;
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("customerId", "");
                        map.put("isBooked", "false");

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference cr = db.collection("parkingPlaces");
                        cr.document(place.getId()).set(map, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        isError = false;
//                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        isError = true;
                                    }
                                });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isError = true;
            }
        });

//                } else {
//                    isError = true;
//                }
//            }
//        });
        dialog.dismiss();
        if (isError) {
            Toast.makeText(this, "Error occurred!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Cancelled successfully!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void onSeeLocationBookingClicked(View view) {

        if (ContextCompat.checkSelfPermission(BookPlace.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                mLong = String.valueOf(location.getLongitude());
                                mLat = String.valueOf(location.getLatitude());
                            }
                        }
                    });

            Intent intent = new Intent(BookPlace.this, ShowLocation.class);
            intent.putExtra("LAT", String.valueOf(mLat));
            intent.putExtra("LONG", String.valueOf(mLong));
            intent.putExtra("PLAT", place.getPpLat());
            intent.putExtra("PLONG", place.getPpLong());

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else {
            requestLocationPermission();
        }
    }

    public void onBookPlaceClicked(View view) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Booking...");
        dialog.show();

        HashMap<String, Object> map = new HashMap<>();
        map.put("customerId", AuthHelper.id);
        map.put("isBooked", "true");

        // Get a new write batch
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();

        DocumentReference nycRef = db.collection("parkingPlaces").document(place.getId());
        batch.update(nycRef, map);

        String id = AuthHelper.currentBookings;
        if (id.equals("0"))
            id = "1";
        else
            id = String.valueOf(Integer.parseInt(id) + 1);

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("currentBookings", id);

        DocumentReference bookRef = db.collection("customers").document(AuthHelper.id);
        batch.update(bookRef, map1);

        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(BookPlace.this, "Booked successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(BookPlace.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Requesting Location permission
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to get more precise location!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(BookPlace.this,
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BookPlace.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
                }

                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    mLong = String.valueOf(location.getLongitude());
                                    mLat = String.valueOf(location.getLatitude());
                                }
                            }
                        });
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "****", Toast.LENGTH_SHORT).show();
        }
    }

}