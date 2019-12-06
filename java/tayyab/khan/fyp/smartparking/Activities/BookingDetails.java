package tayyab.khan.fyp.smartparking.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import org.w3c.dom.Text;

import java.util.HashMap;

import tayyab.khan.fyp.smartparking.Model.ParkingPlace;
import tayyab.khan.fyp.smartparking.Model.User;
import tayyab.khan.fyp.smartparking.R;

public class BookingDetails extends AppCompatActivity {
    private TextView areaTV, slotTV, placeTV, idTV, roofedTV;

    ParkingPlace place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        getSupportActionBar().setTitle("Booking Details");

        areaTV = findViewById(R.id.pAreaTVBDs);
        slotTV = findViewById(R.id.pSlotTVBDs);
        placeTV = findViewById(R.id.pPlaceTVBDs);
        idTV = findViewById(R.id.customerIdTVBDs);
        roofedTV = findViewById(R.id.isRoofedTVBDs);

        place = (ParkingPlace) getIntent().getSerializableExtra("PLACE");

        if (place != null) {
            areaTV.setText(place.getParkingArea());
            slotTV.setText(place.getParkingSlot());
            placeTV.setText(place.getParkingPlace());

            if (place.getCustomerId().equals("")) {
                idTV.setText("Not booked yet!");
                findViewById(R.id.cancelBtnBDs).setBackgroundColor(0x00000000);
                findViewById(R.id.cancelBtnBDs).setEnabled(false);
            } else {
                idTV.setText(place.getCustomerId());
            }

            if (!place.getIsRoofed().equals("true"))
                roofedTV.setText("NO");
            else
                roofedTV.setText("YES");
        }
    }

    public void onCancelBookingClicked(View view) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Deleting...");
        dialog.show();

        final HashMap<String, Object> map = new HashMap<>();
        map.put("customerId", "");
        map.put("isBooked", "false");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cr = db.collection("customers");
        cr.document(place.getCustomerId()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);

                        // Get a new write batch
                        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                        WriteBatch batch = db1.batch();

                        DocumentReference pDoc = db1.collection("parkingPlaces").document(place.getId());
                        batch.update(pDoc, map);

                        String cBooks = "0";
                        if (user.getCurrentBookings().equals("1") || user.getCurrentBookings().equals("0")) {
                            cBooks = "0";
                        } else {
                            cBooks = String.valueOf(Integer.parseInt(user.getCurrentBookings()) - 1);
                        }

                        HashMap<String, Object> map1 = new HashMap<>();
                        map1.put("currentBookings", cBooks);

                        DocumentReference uDoc = db1.collection("customers").document(user.getId());
                        batch.update(uDoc, map1);

                        // Commit the batch
                        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    dialog.dismiss();
                                    Toast.makeText(BookingDetails.this, "Cancelled Successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(BookingDetails.this, ManageBookings.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(BookingDetails.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
    }
}
