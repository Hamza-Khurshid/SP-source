package tayyab.khan.fyp.smartparking.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;

import tayyab.khan.fyp.smartparking.Adapters.ParkingAreaAdapter;
import tayyab.khan.fyp.smartparking.Helpers.AuthHelper;
import tayyab.khan.fyp.smartparking.Model.ParkingPlace;
import tayyab.khan.fyp.smartparking.R;

public class ManageParking extends AppCompatActivity {
    private RecyclerView parkingAreasRV;
    private ProgressBar pb;
    private TextView notFoundTV;

    List<String> areaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_parking);
        getSupportActionBar().setTitle("Parking Areas");

        parkingAreasRV = findViewById(R.id.parkingAreasRVAMP);
        pb = findViewById(R.id.progressBarAMP);
        notFoundTV = findViewById(R.id.notFoundTV);

        areaList = new ArrayList<>();


        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cr = db.collection("parkingPlaces");
        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e == null) {

                    pb.setVisibility(View.GONE);
                    areaList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        ParkingPlace place = doc.toObject(ParkingPlace.class);
                        areaList.add(place.getParkingArea());
                    }
                    HashSet<String> listToSet = new HashSet<>(areaList);
                    List<String> areas = new ArrayList<>(listToSet);

                    if (areas.size() == 0)
                        notFoundTV.setVisibility(View.VISIBLE);
                    else
                        notFoundTV.setVisibility(View.GONE);

                    parkingAreasRV.setHasFixedSize(true);
                    parkingAreasRV.setLayoutManager(new LinearLayoutManager(ManageParking.this));
                    ParkingAreaAdapter adapter = new ParkingAreaAdapter(areas, ManageParking.this);
                    parkingAreasRV.setAdapter(adapter);
                } else {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(ManageParking.this, "Error!: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void onAddParingAreaClicked(View view) {


        final View view1 = getLayoutInflater().inflate(R.layout.dialog_input, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//        alertDialog.setTitle("Add Paring Area");
//        alertDialog.setIcon(getApplicationContext().getResources().getDrawable(R.drawable.ic_slots));
        alertDialog.setCancelable(false);


        final EditText et = view1.findViewById(R.id.dialoge_inputET);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String parkingArea = et.getText().toString().trim();
                if (parkingArea.equals("")) {
                    dialog.dismiss();
                    Toast.makeText(ManageParking.this, "Parking Area's name can not be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    saveParkingArea(parkingArea, dialog);
                }
            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        alertDialog.setView(view1);
        alertDialog.show();

    }

    private void saveParkingArea(final String parkingArea, final DialogInterface d) {
        d.dismiss();
        Intent intent = new Intent(ManageParking.this, AddParkingSlots.class);
        intent.putExtra("AREA", parkingArea);
        AuthHelper.pArea = parkingArea;
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}