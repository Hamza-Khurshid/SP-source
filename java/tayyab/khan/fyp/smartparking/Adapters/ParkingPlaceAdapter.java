package tayyab.khan.fyp.smartparking.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import tayyab.khan.fyp.smartparking.Activities.AllParkingSlots;
import tayyab.khan.fyp.smartparking.Activities.UpdateParkingPlace;
import tayyab.khan.fyp.smartparking.Model.ParkingPlace;
import tayyab.khan.fyp.smartparking.R;

public class ParkingPlaceAdapter extends RecyclerView.Adapter<ParkingPlaceAdapter.PlaceViewHolder> {
    private List<ParkingPlace> places;
    private Context ctx;

    public ParkingPlaceAdapter(List<ParkingPlace> places, Context ctx) {
        this.places = places;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.parking_place_row, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        final ParkingPlace place= places.get(position);


        if (place.getParkingPlace().length() > 15) {
            holder.nameTV.setText(place.getParkingPlace().substring(0, 18)+"...");
        } else {
            holder.nameTV.setText(place.getParkingPlace());
        }

        holder.bookedTV.setText("Booked: " + place.getIsBooked());
        holder.roofedTV.setText("Roofed: " + place.getIsRoofed());

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, UpdateParkingPlace.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("PLACE", place);
                ctx.startActivity(intent);
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ctx)
                        .setTitle("Delete Parking Place")
                        .setMessage("It will permanently delete this place!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(ctx, "Deleting...", Toast.LENGTH_LONG).show();
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                CollectionReference cr = db.collection("parkingPlaces");
                                cr.document(place.getId()).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(ctx, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ctx, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.ic_delete_b)
                        .show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView nameTV, roofedTV, bookedTV;
        ImageView deleteBtn;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);

            cv = itemView.findViewById(R.id.cvPPR);
            nameTV = itemView.findViewById(R.id.slotNameTVPPR);
            deleteBtn = itemView.findViewById(R.id.deleteSlotBtnPPR);
            roofedTV = itemView.findViewById(R.id.roofedPPR);
            bookedTV = itemView.findViewById(R.id.bookedTVPPR);
        }
    }
}