package tayyab.khan.fyp.smartparking.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
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
import tayyab.khan.fyp.smartparking.Adapters.PlaceRowAdapter;
import tayyab.khan.fyp.smartparking.Helpers.AuthHelper;
import tayyab.khan.fyp.smartparking.Model.ParkingPlace;
import tayyab.khan.fyp.smartparking.R;

public class SearchPlace extends AppCompatActivity {
    private EditText searchET;
    private ImageButton searchBtn;
    private TextView notFoundTV;
    private RecyclerView rv;

    private List<ParkingPlace> placeList;

    String search = "";

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_place);
        getSupportActionBar().setTitle("Book A Place");

//        searchET = findViewById(R.id.searchETSP);
//        searchBtn = findViewById(R.id.searchBtnSP);
        notFoundTV = findViewById(R.id.noPlaceFoundTV);
        rv = findViewById(R.id.rvSP);

        placeList = new ArrayList<>();

//        searchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                search(searchET.getText().toString());
//            }
//        });

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cr = db.collection("parkingPlaces");
        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    dialog.dismiss();
                    Toast.makeText(SearchPlace.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    placeList.clear();
                    for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                        ParkingPlace place = snap.toObject(ParkingPlace.class);

                        if (place.getIsBooked().equals("false"))
                            placeList.add(place);
                    }

                    dialog.dismiss();
                    if (placeList.size() == 0)
                        notFoundTV.setVisibility(View.VISIBLE);
                    else
                        notFoundTV.setVisibility(View.GONE);
                    rv.setHasFixedSize(true);
                    GridLayoutManager layout_manager = new GridLayoutManager(SearchPlace.this, 2);
                    rv.setLayoutManager(layout_manager);
                    CustomerPlaceRowAdapter adapter = new CustomerPlaceRowAdapter(placeList, SearchPlace.this);
                    rv.setAdapter(adapter);
                }
            }
        });
    }

//    private void search(final String query) {
//        Toast.makeText(this, query+"", Toast.LENGTH_SHORT).show();
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference cr = db.collection("parkingPlaces");
//        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                if (e != null) {
//                    dialog.dismiss();
//                    Toast.makeText(SearchPlace.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                } else {
//
//                    placeList.clear();
//                    for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
//                        ParkingPlace place = snap.toObject(ParkingPlace.class);
//                        if (place.getParkingArea().toLowerCase().contains(query.toLowerCase()) || place.getParkingPlace().toLowerCase().contains(query.toLowerCase()) || place.getParkingSlot().toLowerCase().contains(query.toLowerCase())) {
//                            placeList.add(place);
//                        }
//                    }
//
//                    dialog.dismiss();
//                    if (placeList.size() == 0)
//                        notFoundTV.setVisibility(View.VISIBLE);
//                    else
//                        notFoundTV.setVisibility(View.GONE);
//                    rv.setHasFixedSize(true);
//                    GridLayoutManager layout_manager = new GridLayoutManager(SearchPlace.this, 2);
//                    rv.setLayoutManager(layout_manager);
//                    CustomerPlaceRowAdapter adapter = new CustomerPlaceRowAdapter(placeList, SearchPlace.this);
//                    rv.setAdapter(adapter);
//                }
//            }
//        });
//    }
}
