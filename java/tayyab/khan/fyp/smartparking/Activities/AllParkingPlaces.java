package tayyab.khan.fyp.smartparking.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import tayyab.khan.fyp.smartparking.Adapters.ParkingPlaceAdapter;
import tayyab.khan.fyp.smartparking.Helpers.AuthHelper;
import tayyab.khan.fyp.smartparking.Model.ParkingPlace;
import tayyab.khan.fyp.smartparking.R;

public class AllParkingPlaces extends AppCompatActivity {
    private RecyclerView placesRV;
    private ProgressBar pb;

    List<ParkingPlace> placeList;
    private String area="", slot="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_parking_places);
        getSupportActionBar().setTitle("Parking Places");

        placesRV = findViewById(R.id.rvAPPl);
        pb = findViewById(R.id.progressBarAPPl);

        placeList = new ArrayList<>();

        SharedPreferences prefs = getSharedPreferences("PARKING_PREFS", Context.MODE_PRIVATE);
        area = prefs.getString("AREA", "");
        slot = prefs.getString("SLOT", "");
        area = AuthHelper.area;
        slot = AuthHelper.slot;

        if (area.equals("") || slot.equals("")) {
            Toast.makeText(this, "Error! Load again.", Toast.LENGTH_SHORT).show();
        } else {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference cr = db.collection("parkingPlaces");
            cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(AllParkingPlaces.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {

                        placeList.clear();
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            ParkingPlace place = snapshot.toObject(ParkingPlace.class);
                            if (area.equals(place.getParkingArea()) && slot.equals(place.getParkingSlot())) {
                                placeList.add(place);
                            }
                        }

                        pb.setVisibility(View.GONE);
                        placesRV.setHasFixedSize(true);
                        placesRV.setLayoutManager(new LinearLayoutManager(AllParkingPlaces.this));
                        ParkingPlaceAdapter adapter = new ParkingPlaceAdapter(placeList, AllParkingPlaces.this);
                        placesRV.setAdapter(adapter);
                    }
                }
            });

        }
    }
}
