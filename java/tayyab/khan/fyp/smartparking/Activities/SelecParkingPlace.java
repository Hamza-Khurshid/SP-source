package tayyab.khan.fyp.smartparking.Activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.internal.maps.zzk;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;

import tayyab.khan.fyp.smartparking.Helpers.AuthHelper;
import tayyab.khan.fyp.smartparking.R;

public class SelecParkingPlace extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LatLng mDefaultLocation = new LatLng(31.416086, 73.070088);
    private static final int DEFAULT_ZOOM = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selec_parking_place);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.addMarker(new MarkerOptions().position(mDefaultLocation).title("Marker in Faisalabad"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mDefaultLocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));

        // Setting a click event handler for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
                mDefaultLocation = latLng;
            }
        });
    }

    public void selectPlace(View view) {
        Intent intent = new Intent(SelecParkingPlace.this, AddParkingSlots.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        AuthHelper.slotLat = String.valueOf( mDefaultLocation.latitude );
        AuthHelper.slotLong = String.valueOf( mDefaultLocation.longitude );

        startActivity(intent);
    }
}
























//    @Override
//    public boolean onTouchEvent(MotionEvent e) {
//        int fingers = e.getPointerCount();
//        if( e.getAction()==MotionEvent.ACTION_DOWN ){
//            isPinch=false;  // Touch DOWN, don't know if it's a pinch yet
//        }
//        if( e.getAction()==MotionEvent.ACTION_MOVE && fingers==2 ){
//            isPinch=true;   // Two fingers, def a pinch
//        }
//        return super.onTouchEvent(e);
//    }
//
//    private void getDeviceLocation() {
//        try {
//            if (mLocationPermissionGranted) {
//                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
//                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        if (task.isSuccessful()) {
//                            Location mLastKnownLocation = task.getResult();
//                            Log.d("TAG", "Latitude: " + mLastKnownLocation.getLatitude());
//                            Log.d("TAG", "Longitude: " + mLastKnownLocation.getLongitude());
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                    new LatLng(mLastKnownLocation.getLatitude(),
//                                            mLastKnownLocation.getLongitude()), 16.0f));
//                        } else {
//                            Log.d("TAG", "Current location is null. Using defaults.");
//                            Log.e("TAG", "Exception: %s", task.getException());
//                            mMap.moveCamera(CameraUpdateFactory
//                                    .newLatLngZoom(mDefaultLocation, 16.0f));
//                        }
//                    }
//                });
//            }
//        } catch (SecurityException e)  {
//            Log.e("Exception: %s", e.getMessage());
//        }
//    }
//
//    private void pickCurrentPlace() {
//        if (mMap == null) {
//            return;
//        }
//
//        if (mLocationPermissionGranted) {
//            getDeviceLocation();
//        } else {
//            Log.i(TAG, "The user did not grant location permission.");
//            mMap.addMarker(new MarkerOptions()
//                    .title("Location permission denied!")).setPosition(mDefaultLocation);
//
//            // Prompt the user for permission.
//            getLocationPermission();
//        }
//    }
