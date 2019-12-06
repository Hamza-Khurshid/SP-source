package tayyab.khan.fyp.smartparking.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import tayyab.khan.fyp.smartparking.Adapters.FeedbackAdapter;
import tayyab.khan.fyp.smartparking.Model.Feedback;
import tayyab.khan.fyp.smartparking.R;

public class SeeFeedback extends AppCompatActivity {
    private TextView notFoundTV;
    private RecyclerView rv;
    private ProgressBar pb;

    List<Feedback> feedbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_feedback);

        notFoundTV = findViewById(R.id.noFeedbackTV);
        rv = findViewById(R.id.rvFeedBack);
        pb = findViewById(R.id.pbFeedback);

        feedbacks = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cr = db.collection("feedbacks");
        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    notFoundTV.setVisibility(View.VISIBLE);
                    Toast.makeText(SeeFeedback.this, "Error!", Toast.LENGTH_SHORT).show();
                } else {

                    feedbacks.clear();
                    for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                        Feedback feed = snap.toObject(Feedback.class);
                        feedbacks.add(feed);
                    }

                    if (feedbacks.size() == 0)
                        notFoundTV.setVisibility(View.VISIBLE);

                    pb.setVisibility(View.GONE);
                    rv.setHasFixedSize(true);
                    rv.setLayoutManager(new LinearLayoutManager(SeeFeedback.this));
                    FeedbackAdapter adapter = new FeedbackAdapter(feedbacks, SeeFeedback.this);
                    rv.setAdapter(adapter);
                }

            }
        });
    }
}
