package tayyab.khan.fyp.smartparking.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tayyab.khan.fyp.smartparking.Activities.BookingDetails;
import tayyab.khan.fyp.smartparking.Model.ParkingPlace;
import tayyab.khan.fyp.smartparking.R;

public class PlaceRowAdapter extends RecyclerView.Adapter<PlaceRowAdapter.PlaceRowViewHolder> {
    private List<ParkingPlace> places;
    private Context ctx;

    public PlaceRowAdapter(List<ParkingPlace> places, Context ctx) {
        this.places = places;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public PlaceRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.place_row, parent, false);
        return new PlaceRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceRowViewHolder holder, int position) {
        final ParkingPlace place= places.get(position);

        if (place != null) {

            holder.areaTV.setText("Area: " + place.getParkingArea());
            holder.slotTV.setText("Slot: " + place.getParkingSlot());
            holder.placeTV.setText("Place: " + place.getParkingPlace());

            if (!place.getIsRoofed().equals("true"))
                holder.roofedTV.setText("Roofed: No");
            else
                holder.roofedTV.setText("Roofed: YES");

            if (place.getIsBooked().equals("false"))
                holder.ll.setBackgroundColor(0xFF48A23F);
            else
                holder.ll.setBackgroundColor(0xFFF28080);

            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, BookingDetails.class);
                    intent.putExtra("PLACE", place);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);
                    Toast.makeText(ctx, place.getParkingArea() + "->" + place.getParkingSlot() + "->" + place.getParkingPlace(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    class PlaceRowViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll;
        TextView areaTV, slotTV, placeTV, roofedTV, holderTV;

        public PlaceRowViewHolder(@NonNull View itemView) {
            super(itemView);

            ll = itemView.findViewById(R.id.ll_Place_row);
            areaTV = itemView.findViewById(R.id.areaTVPR);
            slotTV = itemView.findViewById(R.id.slotTVPR);
            placeTV = itemView.findViewById(R.id.placeTVPR);
            holderTV = itemView.findViewById(R.id.holderTVPR);
            roofedTV = itemView.findViewById(R.id.isRoofedTVPR);
        }
    }
}