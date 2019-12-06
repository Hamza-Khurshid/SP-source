package tayyab.khan.fyp.smartparking.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import tayyab.khan.fyp.smartparking.Adapters.CustomerPlaceRowAdapter;
import tayyab.khan.fyp.smartparking.Helpers.AuthHelper;
import tayyab.khan.fyp.smartparking.Model.ParkingPlace;
import tayyab.khan.fyp.smartparking.R;

public class UserDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private EditText searchET;
    private ImageButton searchBtn;
    private TextView notFoundTV;
    private RecyclerView rv;

    private List<ParkingPlace> placeList;

    String search = "";

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        searchET = findViewById(R.id.searchETSP);
        searchBtn = findViewById(R.id.searchBtnSP);
        notFoundTV = findViewById(R.id.noPlaceFoundTV);
        rv = findViewById(R.id.rvSP);

        placeList = new ArrayList<>();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(searchET.getText().toString());
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cr = db.collection("parkingPlaces");
        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    dialog.dismiss();
                    Toast.makeText(UserDashboard.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    placeList.clear();
                    for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                        ParkingPlace place = snap.toObject(ParkingPlace.class);
                        placeList.add(place);
                    }

                    dialog.dismiss();
                    if (placeList.size() == 0)
                        notFoundTV.setVisibility(View.VISIBLE);
                    else
                        notFoundTV.setVisibility(View.GONE);
                    rv.setHasFixedSize(true);
                    GridLayoutManager layout_manager = new GridLayoutManager(UserDashboard.this, 2);
                    rv.setLayoutManager(layout_manager);
                    CustomerPlaceRowAdapter adapter = new CustomerPlaceRowAdapter(placeList, UserDashboard.this);
                    rv.setAdapter(adapter);
                }
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout_udb) {

            AuthHelper.id = "";
            SharedPreferences prefs = getSharedPreferences("USER_AUTH", Context.MODE_PRIVATE);
            if (prefs != null) {

                String i = prefs.getString("id", "");
                getApplicationContext().getSharedPreferences("USER_AUTH", Context.MODE_PRIVATE).edit().clear().commit();
                prefs.edit().clear();
                Toast.makeText(this, "Logout Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserDashboard.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            } else {
                Toast.makeText(this, "No user logged in!", Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_book_place) {

            Intent intent = new Intent(UserDashboard.this, SearchPlace.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_my_bookings) {

            Intent intent = new Intent(UserDashboard.this, MyBookings.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);


        } else if (id == R.id.nav_feedback) {

            Intent intent = new Intent(UserDashboard.this, CustomerFeedback.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_general_info) {

            Intent intent = new Intent(UserDashboard.this, GeneralInformation.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void search(final String query) {
        Toast.makeText(this, query+"", Toast.LENGTH_SHORT).show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cr = db.collection("parkingPlaces");
        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    dialog.dismiss();
                    Toast.makeText(UserDashboard.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {

                    placeList.clear();
                    for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                        ParkingPlace place = snap.toObject(ParkingPlace.class);
                        if (place.getParkingArea().toLowerCase().contains(query.toLowerCase()) || place.getParkingPlace().toLowerCase().contains(query.toLowerCase()) || place.getParkingSlot().toLowerCase().contains(query.toLowerCase())) {
                            placeList.add(place);
                        }
                    }

                    dialog.dismiss();
                    if (placeList.size() == 0)
                        notFoundTV.setVisibility(View.VISIBLE);
                    else
                        notFoundTV.setVisibility(View.GONE);
                    rv.setHasFixedSize(true);
                    GridLayoutManager layout_manager = new GridLayoutManager(UserDashboard.this, 2);
                    rv.setLayoutManager(layout_manager);
                    CustomerPlaceRowAdapter adapter = new CustomerPlaceRowAdapter(placeList, UserDashboard.this);
                    rv.setAdapter(adapter);
                }
            }
        });
    }
}
