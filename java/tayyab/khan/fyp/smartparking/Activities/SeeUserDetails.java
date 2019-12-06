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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import tayyab.khan.fyp.smartparking.Model.User;
import tayyab.khan.fyp.smartparking.R;

public class SeeUserDetails extends AppCompatActivity {
    private TextView nameTV, phoneTV, booksTV;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_user_details);
        getSupportActionBar().setTitle("User Details");

        nameTV = findViewById(R.id.usernameTVSUD);
        phoneTV = findViewById(R.id.phoneNoTVSUD);
        booksTV = findViewById(R.id.bookingsTVSUD);

        user = (User) getIntent().getSerializableExtra("CUSTOMER");

        nameTV.setText(user.getName());
        phoneTV.setText(user.getPhone());
        if (user.getCurrentBookings().equals(""))
            booksTV.setText("0");
        else
            booksTV.setText(user.getCurrentBookings());
    }

    public void deleteUser(View view) {

        if (user != null) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Deleting...");
            dialog.setCancelable(false);
            dialog.show();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference cr = db.collection("customers");
            cr.document(user.getId()).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            dialog.dismiss();
                            Toast.makeText(SeeUserDetails.this, "User Deleted Successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SeeUserDetails.this, ManageUsers.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(SeeUserDetails.this, "Error!" + e.getMessage() , Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
