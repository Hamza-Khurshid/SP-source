package tayyab.khan.fyp.smartparking.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

import tayyab.khan.fyp.smartparking.Helpers.AuthHelper;
import tayyab.khan.fyp.smartparking.Helpers.ConstantsHelper;
import tayyab.khan.fyp.smartparking.Model.User;
import tayyab.khan.fyp.smartparking.R;

public class LoginActivity extends AppCompatActivity {
    private EditText phoneET, passwordET;
    private String phone = "", password = "";
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
        setContentView(R.layout.activity_login);

        phoneET = findViewById(R.id.emailETAL);
        passwordET = findViewById(R.id.passwordETAL);
        progressBar = findViewById(R.id.progressBarAL);

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

                if (s.length() == 4 && first>second) {
                    str += "-";
                    phoneET.setText(str);
                    phoneET.setSelection(str.length());
                }
            }
        });
    }

    public void NavigateToSignUp(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    public void onLoginClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        phone = phoneET.getText().toString().trim();
        password = passwordET.getText().toString().trim();

        if (phone.equals("") || password.equals("")) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "All fields required!", Toast.LENGTH_SHORT).show();
        } else {

            if (phone.equals(ConstantsHelper.ADMIN_PHONE) && password.equals(ConstantsHelper.ADMIN_PASSWORD)) {
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(LoginActivity.this, AdminDashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference cr = db.collection("customers");
                cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(LoginActivity.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {

                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                User user = snapshot.toObject(User.class);

                                if (user != null) {
                                    if (user.getPhone().equals(phone) && user.getPassword().equals(password)) {

                                        SharedPreferences.Editor editor = getSharedPreferences("USER_AUTH", Context.MODE_PRIVATE).edit();
                                        editor.putString("id", user.getId());
                                        AuthHelper.id = user.getId();

                                        progressBar.setVisibility(View.GONE);

                                        AuthHelper.id = user.getId();
                                        AuthHelper.name = user.getName();
                                        AuthHelper.currentBookings = user.getCurrentBookings();

                                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, UserDashboard.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        return;
                                    }
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

        }
    }

    private void updateUI(final String id) {
        if (id.equals("spadminid007")) {
            Intent intent = new Intent(LoginActivity.this, AdminDashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Intent intent = new Intent(LoginActivity.this, UserDashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}