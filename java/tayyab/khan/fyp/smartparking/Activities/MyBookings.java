package tayyab.khan.fyp.smartparking.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import tayyab.khan.fyp.smartparking.Adapters.CustomerPlaceRowAdapter;
import tayyab.khan.fyp.smartparking.Helpers.AuthHelper;
import tayyab.khan.fyp.smartparking.Model.ParkingPlace;
import tayyab.khan.fyp.smartparking.R;

public class MyBookings extends AppCompatActivity {
    private RecyclerView rv;
    private ProgressBar pb;
    private TextView notFoundTV;

    private List<ParkingPlace> placeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);
        getSupportActionBar().setTitle("My Bookings");

        rv = findViewById(R.id.rvMBs);
        pb = findViewById(R.id.pbMBs);
        notFoundTV = findViewById(R.id.noPlaceFoundTV);

        placeList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cr = db.collection("parkingPlaces");
        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(MyBookings.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    placeList.clear();
                    for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                        ParkingPlace place = snap.toObject(ParkingPlace.class);

                        if (place.getCustomerId().equals(AuthHelper.id))
                            placeList.add(place);
                    }

                    pb.setVisibility(View.GONE);
                    if (placeList.size() == 0)
                        notFoundTV.setVisibility(View.VISIBLE);
                    else
                        notFoundTV.setVisibility(View.GONE);
                    rv.setHasFixedSize(true);
                    GridLayoutManager layout_manager = new GridLayoutManager(MyBookings.this, 2);
                    rv.setLayoutManager(layout_manager);
                    CustomerPlaceRowAdapter adapter = new CustomerPlaceRowAdapter(placeList, MyBookings.this);
                    rv.setAdapter(adapter);
                }
            }
        });
    }
}
