package tayyab.khan.fyp.smartparking.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.HashMap;

import tayyab.khan.fyp.smartparking.Helpers.AuthHelper;
import tayyab.khan.fyp.smartparking.Model.Feedback;
import tayyab.khan.fyp.smartparking.R;

public class CustomerFeedback extends AppCompatActivity {
    private EditText titleET, descET;

    String title = "", desc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_feedback);
        getSupportActionBar().setTitle("Feedback");

        titleET = findViewById(R.id.feedTitleET);
        descET = findViewById(R.id.feedDescET);
    }

    public void addFeedBack(View view) {
        final ProgressDialog dialog = new ProgressDialog(CustomerFeedback.this);
        dialog.setMessage("Uploading...");
        dialog.setCancelable(false);
        dialog.show();

        title = titleET.getText().toString().trim();
        desc = descET.getText().toString().trim();

        if (title.equals("") || desc.equals("")) {
            dialog.dismiss();
            Toast.makeText(this, "Both fields required!", Toast.LENGTH_SHORT).show();
        } else {
            Date dateObj = new Date();
            String date = dateObj.toString();
            String id = AuthHelper.id;

            if (id.equals("")) {
                dialog.dismiss();
                Toast.makeText(this, "Error! Try login again", Toast.LENGTH_SHORT).show();
            } else {

//                HashMap<String, Object> map = new HashMap<>();
//                map.put("title", title);
//                map.put("desc", desc);
//                map.put("time", date);
//                map.put("sender", id);
                Feedback feedback = new Feedback(title, desc, id, date);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference cr = db.collection("feedbacks");
                cr.add(feedback).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        dialog.dismiss();
                        Toast.makeText(CustomerFeedback.this, "Uploaded successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CustomerFeedback.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }
}
