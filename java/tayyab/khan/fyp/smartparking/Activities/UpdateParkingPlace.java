package tayyab.khan.fyp.smartparking.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

import tayyab.khan.fyp.smartparking.Model.ParkingPlace;
import tayyab.khan.fyp.smartparking.R;

public class UpdateParkingPlace extends AppCompatActivity {
    private EditText areaET, slotET, placeET;
    private CheckBox isRoofedCB;

    ParkingPlace place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_parking_place);

        areaET = findViewById(R.id.namePAreaETUPP);
        slotET = findViewById(R.id.namePSlotETUPP);
        placeET = findViewById(R.id.namePPlaceETUPP);
        isRoofedCB = findViewById(R.id.isRoofedCBUPP);

        place = (ParkingPlace) getIntent().getSerializableExtra("PLACE");
        if (place != null) {

            areaET.setText(place.getParkingArea());
            slotET.setText(place.getParkingSlot());
            placeET.setText(place.getParkingPlace());

            if (place.getIsRoofed().equals("true")) {
                isRoofedCB.setChecked(true);
            }

        } else {
            Toast.makeText(this, "Error! Try Reloading.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onUpdateParkingPlaceClicked(View view) {
        String area = areaET.getText().toString().trim();
        String slot = slotET.getText().toString().trim();
        String place1 = placeET.getText().toString().trim();
        String is = "false";

        if (isRoofedCB.isChecked()) {
            is = "true";
        }

        if (area.equals("") || slot.equals("") || place1.equals("")) {
            Toast.makeText(this, "Empty fields are not allowed!", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> map = new HashMap<>();
            map.put("parkingArea", area);
            map.put("parkingSlot", slot);
            map.put("parkingPlace", place1);
            map.put("isRoofed", is);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference cr = db.collection("parkingPlaces");
            cr.document(place.getId()).update(map)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(UpdateParkingPlace.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UpdateParkingPlace.this, AllParkingPlaces.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateParkingPlace.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
