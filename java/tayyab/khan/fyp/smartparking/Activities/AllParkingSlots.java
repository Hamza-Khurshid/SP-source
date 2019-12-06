package tayyab.khan.fyp.smartparking.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;

import tayyab.khan.fyp.smartparking.Adapters.ParkingAreaAdapter;
import tayyab.khan.fyp.smartparking.Adapters.ParkingSlotAdapter;
import tayyab.khan.fyp.smartparking.Helpers.AuthHelper;
import tayyab.khan.fyp.smartparking.Model.ParkingPlace;
import tayyab.khan.fyp.smartparking.R;

public class AllParkingSlots extends AppCompatActivity {
    private ProgressBar pb;
    private RecyclerView slotsRV;

    List<String> slots;
    private String area = " haha ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_parking_slots);
        getSupportActionBar().setTitle("Parking Slots");

        pb = findViewById(R.id.progressBarAPSl);
        slotsRV = findViewById(R.id.rvAPSl);

        slots = new ArrayList<>();

        area = getSharedPreferences("PARKING_PREFS", Context.MODE_PRIVATE).getString("AREA", "");

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cr = db.collection("parkingPlaces");
        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e == null) {

                    pb.setVisibility(View.GONE);
                    slots.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        ParkingPlace place = doc.toObject(ParkingPlace.class);
                        if (place != null) {
                            if (AuthHelper.area.equals(place.getParkingArea()))
                                slots.add(place.getParkingSlot());
                        }
                    }
                    HashSet<String> listToSet = new HashSet<>(slots);
                    List<String> mSlots = new ArrayList<>(listToSet);

                    slotsRV.setHasFixedSize(true);
                    slotsRV.setLayoutManager(new LinearLayoutManager(AllParkingSlots.this));
                    ParkingSlotAdapter adapter = new ParkingSlotAdapter(mSlots, AllParkingSlots.this);
                    slotsRV.setAdapter(adapter);
                } else {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(AllParkingSlots.this, "Error!: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
