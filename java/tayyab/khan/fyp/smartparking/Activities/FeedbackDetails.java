package tayyab.khan.fyp.smartparking.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

import tayyab.khan.fyp.smartparking.Model.Feedback;
import tayyab.khan.fyp.smartparking.R;

public class FeedbackDetails extends AppCompatActivity {
    private TextView titleTV, descTV;

    private Feedback feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_details);

        titleTV = findViewById(R.id.titleFDs);
        descTV = findViewById(R.id.descFDs);

        feedback = (Feedback) getIntent().getSerializableExtra("FEEDBACK");

        titleTV.setText(feedback.getTitle());
        descTV.setText(feedback.getDesc());
    }

    public void deleteFeedback(View view) {
        final ProgressDialog dialog = new ProgressDialog(FeedbackDetails.this);
        dialog.setMessage("Deleting...");
        dialog.setCancelable(false);
        dialog.show();

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        final CollectionReference itemsRef = rootRef.collection("feedbacks");
        Query query = itemsRef.whereEqualTo("sender", feedback.getSender()).whereEqualTo("time", feedback.getTime());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        itemsRef.document(document.getId()).delete();
                    }

                    dialog.dismiss();
                    finish();
                } else {
                    dialog.dismiss();
                    Toast.makeText(FeedbackDetails.this, "Error! try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });



//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference cr= db.collection("feedbacks");
//        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                if (e != null) {
//                    Toast.makeText(FeedbackDetails.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                } else {
//
//                    for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
//                        Feedback feed = snap.toObject(Feedback.class);
//
//                        if (feedback.getTitle().equals(feed.getTitle()) && feedback.getSender().equals(feed.getSender())) {
//
//                        }
//                    }
//                }
//            }
//        });
    }
}
