package tayyab.khan.fyp.smartparking.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
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

import tayyab.khan.fyp.smartparking.Activities.AllParkingPlaces;
import tayyab.khan.fyp.smartparking.Activities.AllParkingSlots;
import tayyab.khan.fyp.smartparking.Helpers.AuthHelper;
import tayyab.khan.fyp.smartparking.Model.ParkingPlace;
import tayyab.khan.fyp.smartparking.R;

public class ParkingSlotAdapter extends RecyclerView.Adapter<ParkingSlotAdapter.SlotViewHolder> {
    private List<String> slots;
    private Context ctx;

    public ParkingSlotAdapter(List<String> slots, Context ctx) {
        this.slots = slots;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public SlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.parking_slot_row, parent, false);
        return new SlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotViewHolder holder, int position) {
        final String slot = slots.get(position);

        if (slot.length() > 15) {
            holder.nameTV.setText(slot.substring(0, 18)+"...");
        } else {
            holder.nameTV.setText(slot);
        }


        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = ctx.getSharedPreferences("PARKING_PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("SLOT", slot);
                editor.commit();
                AuthHelper.slot = slot;

                Intent intent = new Intent(ctx, AllParkingPlaces.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ctx.startActivity(intent);
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(ctx)
                        .setTitle("Delete Parking Area")
                        .setMessage("It will delete all the places in this slot?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences prefs = ctx.getSharedPreferences("PARKING_PREFS", Context.MODE_PRIVATE);
                                String area = prefs.getString("AREA", "");
                                area = AuthHelper.area;

                                if (area.equals("")) {
                                    dialog.dismiss();
                                    Toast.makeText(ctx, "Error! Try again", Toast.LENGTH_SHORT).show();
                                } else {
                                    dialog.dismiss();
                                    Log.d("HMMM", slot+", "+area);
                                    deleteArea(slot, area);
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.ic_delete_b)
                        .show();

            }
        });
    }

    private void deleteArea(final String slot, final String a) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cr = db.collection("parkingPlaces");
        cr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot snapshot: task.getResult()) {
                        ParkingPlace place = snapshot.toObject(ParkingPlace.class);
                        if (place.getParkingArea().equals(a) && place.getParkingSlot().equals(slot)){
                            String id = place.getId();

                            db.collection("parkingPlaces").document(id).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(ctx, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ctx, "Error! Try again.", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    }

                } else {
                    Toast.makeText(ctx, "Error! Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return slots.size();
    }

    class SlotViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView nameTV;
        ImageView deleteBtn;

        public SlotViewHolder(@NonNull View itemView) {
            super(itemView);

            cv = itemView.findViewById(R.id.cvPSR);
            nameTV = itemView.findViewById(R.id.slotNameTVPSR);
            deleteBtn = itemView.findViewById(R.id.deleteSlotBtn);
        }
    }
}