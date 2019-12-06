package tayyab.khan.fyp.smartparking.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import tayyab.khan.fyp.smartparking.Adapters.UserRowAdapter;
import tayyab.khan.fyp.smartparking.Model.User;
import tayyab.khan.fyp.smartparking.R;

public class ManageUsers extends AppCompatActivity {
    private ProgressBar pb;
    private RecyclerView allUsersRV;
    private TextView notFoundTV;

    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        pb = findViewById(R.id.progressBarAMU);
        allUsersRV = findViewById(R.id.usersRVAMU);
        notFoundTV = findViewById(R.id.notFoundTV);

        users = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cr = db.collection("customers");
        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(ManageUsers.this, "Error! "+e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {

                    users.clear();
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        User user = snapshot.toObject(User.class);

                        users.add(user);
                    }

                    if (users.size() == 0)
                        notFoundTV.setVisibility(View.VISIBLE);
                    else
                        notFoundTV.setVisibility(View.GONE);

                    allUsersRV.setHasFixedSize(true);
                    allUsersRV.setLayoutManager(new LinearLayoutManager(ManageUsers.this));
                    UserRowAdapter adapter = new UserRowAdapter(users, ManageUsers.this);
                    allUsersRV.setAdapter(adapter);
                }
            }
        });
    }
}
