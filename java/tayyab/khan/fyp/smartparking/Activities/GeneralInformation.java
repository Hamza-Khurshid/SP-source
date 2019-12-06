package tayyab.khan.fyp.smartparking.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
import java.util.List;

import javax.annotation.Nullable;

import tayyab.khan.fyp.smartparking.Adapters.PlaceRowAdapter;
import tayyab.khan.fyp.smartparking.Model.ParkingPlace;
import tayyab.khan.fyp.smartparking.R;

public class GeneralInformation extends AppCompatActivity {
//    private RecyclerView rv;
//    private ProgressBar pb;
//
//    private List<ParkingPlace> placeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_information);
        getSupportActionBar().setTitle("General Information");

//        rv = findViewById(R.id.rvGI);
//        pb = findViewById(R.id.pbGI);
//
//        placeList = new ArrayList<>();
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference cr = db.collection("parkingPlaces");
//        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//
//                if (e != null) {
//                    Toast.makeText(GeneralInformation.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                } else {
//
//                    placeList.clear();
//                    for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
//                        ParkingPlace place = snap.toObject(ParkingPlace.class);
//                        placeList.add(place);
//                    }
//
//                    pb.setVisibility(View.GONE);
//                    rv.setHasFixedSize(true);
//                    GridLayoutManager layout_manager = new GridLayoutManager(GeneralInformation.this, 2);
//                    rv.setLayoutManager(layout_manager);
//                    PlaceRowAdapter adapter = new PlaceRowAdapter(placeList, GeneralInformation.this);
//                    rv.setAdapter(adapter);
//                }
//            }
//        });
    }
}
