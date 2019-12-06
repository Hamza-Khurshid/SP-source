package tayyab.khan.fyp.smartparking.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tayyab.khan.fyp.smartparking.Activities.ManageParking;
import tayyab.khan.fyp.smartparking.Activities.SeeUserDetails;
import tayyab.khan.fyp.smartparking.Model.User;
import tayyab.khan.fyp.smartparking.R;

public class UserRowAdapter extends RecyclerView.Adapter<UserRowAdapter.UserViewHolder> {
    private List<User> users;
    private Context ctx;

    public UserRowAdapter(List<User> users, Context ctx) {
        this.users = users;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.user_row, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final User user = users.get(position);

        holder.nameTV.setText(user.getName());
        if (user.getCurrentBookings().equals("0"))
            holder.bookingsTV.setText("0");
        else
            holder.bookingsTV.setText(user.getCurrentBookings());


        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, SeeUserDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("CUSTOMER", user);
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView nameTV, bookingsTV;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            cv = itemView.findViewById(R.id.cvUR);
            nameTV = itemView.findViewById(R.id.nameTVUR);
            bookingsTV = itemView.findViewById(R.id.bookingsTVUR);
        }
    }
}