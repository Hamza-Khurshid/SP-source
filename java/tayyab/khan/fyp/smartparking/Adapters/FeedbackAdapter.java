package tayyab.khan.fyp.smartparking.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tayyab.khan.fyp.smartparking.Activities.FeedbackDetails;
import tayyab.khan.fyp.smartparking.Activities.SeeUserDetails;
import tayyab.khan.fyp.smartparking.Model.Feedback;
import tayyab.khan.fyp.smartparking.R;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {
    private List<Feedback> feeds;
    private Context ctx;

    public FeedbackAdapter(List<Feedback> feeds, Context ctx) {
        this.feeds = feeds;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.feedback_row, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        final Feedback feed = feeds.get(position);

        holder.titleTV.setText(feed.getTitle());
        if (feed.getDesc().length() > 20)
            holder.descTV.setText(feed.getDesc().substring(0, 20)+"...");
        else
            holder.descTV.setText(feed.getDesc());


        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, FeedbackDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("FEEDBACK", feed);
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    class FeedbackViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView titleTV, descTV, timeTV;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);

            cv = itemView.findViewById(R.id.cvFR);
            titleTV = itemView.findViewById(R.id.titleTV);
            descTV = itemView.findViewById(R.id.descTV);
            timeTV = itemView.findViewById(R.id.timeTV);
        }
    }
}