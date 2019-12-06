package tayyab.khan.fyp.smartparking.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import javax.annotation.Nullable;

import tayyab.khan.fyp.smartparking.Helpers.AuthHelper;
import tayyab.khan.fyp.smartparking.Helpers.ConstantsHelper;
import tayyab.khan.fyp.smartparking.Model.User;
import tayyab.khan.fyp.smartparking.R;

public class SignupActivity extends AppCompatActivity {
    private EditText nameET, phoneET, passwordET;
    private String name = "", phone = "", password = "";
    private ProgressBar progressBar;

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("USER_AUTH", Context.MODE_PRIVATE);

        if (prefs != null) {
            String id = prefs.getString("id", "");
            if (!id.equals("")) {
                updateUI(id);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameET = findViewById(R.id.nameETAS);
        phoneET = findViewById(R.id.emailETAS);
        passwordET = findViewById(R.id.passwordETAS);
        progressBar = findViewById(R.id.progressBarAS);


        phoneET.addTextChangedListener(new TextWatcher() {
            int first = 0;
            int second;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                second = first;
                first = s.length();

                if (s.length() == 4 && first > second) {
                    str += "-";
                    phoneET.setText(str);
                    phoneET.setSelection(str.length());
                }
            }
        });
    }

    public void NavigateToLogin(View view) {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void onSingnupClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        name = nameET.getText().toString().trim();
        phone = phoneET.getText().toString().trim();
        password = passwordET.getText().toString().trim();

        if (name.equals("") || phone.equals("") || password.equals("")) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "No empty field allowed!", Toast.LENGTH_SHORT).show();
        } else {

            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = String.valueOf(tsLong);
            String a = String.valueOf(Math.random() * 10000000).substring(0, 5);
            final String id = a + ts;

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference cr = db.collection("customers");

//            cr.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                @Override
//                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
//                        User user = snapshot.toObject(User.class);
//
//                        if (user.getPhone().equals(phone) || user.getPhone().equals(ConstantsHelper.ADMIN_PHONE)) {
//                            progressBar.setVisibility(View.GONE);
//                            Toast.makeText(SignupActivity.this, "Phone already registered!", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                    }
                    saveUserData(id);
//                }
//            })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(SignupActivity.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
        }
    }

    private void saveUserData(final String id) {

        HashMap<String, Object> map = new HashMap<>();

        map.put("id", id);
        map.put("name", name);
        map.put("phone", phone);
        map.put("password", password);
        String books = "0";
        map.put("currentBookings", books);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customers")
                .document(id)
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignupActivity.this, "User created successfully!", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = getSharedPreferences("USER_AUTH", Context.MODE_PRIVATE).edit();
                        editor.putString("id", id);

                        AuthHelper.id = id;
                        AuthHelper.name = name;

                        editor.commit();

                        Intent intent = new Intent(SignupActivity.this, UserDashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignupActivity.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(String id) {
        if (id.equals(ConstantsHelper.ADMIN_ID)) {
            Intent intent = new Intent(SignupActivity.this, AdminDashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SignupActivity.this, UserDashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}