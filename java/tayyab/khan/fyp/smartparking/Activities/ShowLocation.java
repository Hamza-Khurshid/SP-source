package tayyab.khan.fyp.smartparking.Activities;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import tayyab.khan.fyp.smartparking.R;
import tayyab.khan.fyp.smartparking.TaskLoadedCallback;

public class ShowLocation extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    private MarkerOptions place1, place2;
//    Button getDirection;
    private Polyline currentPolyline;
    private TextView goBack;

    String longitutde = "", latitude = "";
    private static final int DEFAULT_ZOOM = 16;
    private static final int NO_ZOOM = 18;

    ArrayList markerPoints = new ArrayList();
    Double plat = 0.0;
    Double plon = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_location);

        goBack = findViewById(R.id.backBtn);

        Double ylat = Double.parseDouble( getIntent().getStringExtra("LAT") );
        Double ylon = Double.parseDouble( getIntent().getStringExtra("LONG") );
        plat = Double.parseDouble( getIntent().getStringExtra("PLAT") );
        plon = Double.parseDouble( getIntent().getStringExtra("PLONG") );

//        getDirection = findViewById(R.id.getLocationBtnSL);
//        getDirection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new FetchURL(ShowLocation.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
//            }
//        });
        //27.658143,85.3199503
        //27.667491,85.3208583

        place1 = new MarkerOptions().position(new LatLng(ylon, ylat)).title("Your Location");
//        place1 = new MarkerOptions().position(new LatLng(31.4504, 73.1350)).title("Your Location");
//        place2 = new MarkerOptions().position(new LatLng(plat, plon)).title("Parking Place");
//
        longitutde = getIntent().getStringExtra("LONG");
        latitude = getIntent().getStringExtra("LAT");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(latitude.equals("0.0") || longitutde.equals("0.0"))
            Toast.makeText(this, "Your Location not available. Please enable it.", Toast.LENGTH_SHORT).show();

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.valueOf(latitude), Double.valueOf(longitutde)))
                .title("My Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(plat, plon))
                .title("Parking Place")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));

        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(plat, plon))
                .zoom(14)
                .bearing(0)
                .tilt(45)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5000, null);
    }

//    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
//        // Origin of route
//        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
//        // Destination of route
//        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//        // Mode
//        String mode = "mode=" + directionMode;
//        // Building the parameters to the web service
//        String parameters = str_origin + "&" + str_dest + "&" + mode;
//        // Output format
//        String output = "json";
//        // Building the url to the web service
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
//        return url;
//    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}

//         Add a marker in Sydney and move the camera
//        Toast.makeText(this, "LT:"+latitude+" LN:"+longitutde, Toast.LENGTH_SHORT).show();
//        if (!longitutde.equals("") && !latitude.equals("")) {
//            LatLng lt = new LatLng(Double.valueOf(latitude), Double.valueOf(longitutde));
//            mMap.addMarker(new MarkerOptions().position(lt).title("Parking Place"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom( lt, DEFAULT_ZOOM ));
//        } else {
//        LatLng lt = new LatLng(31.416086, 73.070088);
//        mMap.addMarker(new MarkerOptions().position(lt).title("Default Location"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lt, NO_ZOOM));
//        }
//
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//
//                if (markerPoints.size() > 1) {
//                    markerPoints.clear();
//                    mMap.clear();
//                }
//
//                // Adding new item to the ArrayList
//                markerPoints.add(latLng);
//
//                // Creating MarkerOptions
//                MarkerOptions options = new MarkerOptions();
//
//                // Setting the position of the marker
//                options.position(latLng);
//
//                if (markerPoints.size() == 1) {
//                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                } else if (markerPoints.size() == 2) {
//                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                }
//
//                // Add new marker to the Google Map Android API V2
//                mMap.addMarker(options);
//
//                // Checks, whether start and end locations are captured
//                if (markerPoints.size() >= 2) {
//                    LatLng origin = (LatLng) markerPoints.get(0);
//                    LatLng dest = (LatLng) markerPoints.get(1);
//
//                    // Getting URL to the Google Directions API
//                    String url = getDirectionsUrl(origin, dest);
//
//                    DownloadTask downloadTask = new DownloadTask();
//
//                    // Start downloading json data from Google Directions API
//                    downloadTask.execute(url);
//                }
//
//            }
//        });
//    }
//
//    private class DownloadTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... url) {
//
//            String data = "";
//
//            try {
//                data = downloadUrl(url[0]);
//            } catch (Exception e) {
//                Log.d("Background Task", e.toString());
//            }
//            return data;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            ParserTask parserTask = new ParserTask();
//
//
//            parserTask.execute(result);
//
//        }
//    }
//
//    // A class to parse the Google Places in JSON format
//    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
//
//        // Parsing the data in non-ui thread
//        @Override
//        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
//
//            JSONObject jObject;
//            List<List<HashMap<String, String>>> routes = null;
//
//            try {
//                jObject = new JSONObject(jsonData[0]);
//                DirectionsJSONParser parser = new DirectionsJSONParser();
//
//                routes = parser.parse(jObject);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return routes;
//        }
//
//        @Override
//        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
//            ArrayList points = new ArrayList();
//            PolylineOptions lineOptions = new PolylineOptions();
//            MarkerOptions markerOptions = new MarkerOptions();
//
//            if (result.size() > 0) {
//                for (int i = 0; i < result.size(); i++) {
//
//                    List<HashMap<String, String>> path = result.get(i);
//
//                    for (int j = 0; j < path.size(); j++) {
//                        HashMap<String, String> point = path.get(j);
//
//                        double lat = Double.parseDouble(point.get("lat"));
//                        double lng = Double.parseDouble(point.get("lng"));
//                        LatLng position = new LatLng(lat, lng);
//
//                        points.add(position);
//                    }
//
//                    lineOptions.addAll(points);
//                    lineOptions.width(12);
//                    lineOptions.color(Color.RED);
//                    lineOptions.geodesic(true);
//
//                }
//                Log.d("POSITION: ", "dfdf");
//
//                // Drawing polyline in the Google Map for the i-th route
////            if (lineOptions != null) {
//                mMap.addPolyline(lineOptions);
////                Toast.makeText(ShowLocation.this, "WTF", Toast.LENGTH_SHORT).show();
////            } else
////                Toast.makeText(ShowLocation.this, "Not available on device!", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(ShowLocation.this, "Result zero", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private String getDirectionsUrl(LatLng origin, LatLng dest) {
//
//        // Origin of route
//        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
//
//        // Destination of route
//        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//
//        // Sensor enabled
//        String sensor = "sensor=false";
//        String mode = "mode=driving";
//        // Building the parameters to the web service
//        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;
//
//        // Output format
//        String output = "json";
//
//        // Building the url to the web service
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
//
//        return url;
//    }
//
//
//    // A method to download json data from url
//    private String downloadUrl(String strUrl) throws IOException {
//        String data = "";
//        InputStream iStream = null;
//        HttpURLConnection urlConnection = null;
//        try {
//            URL url = new URL(strUrl);
//
//            urlConnection = (HttpURLConnection) url.openConnection();
//
//            urlConnection.connect();
//
//            iStream = urlConnection.getInputStream();
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
//
//            StringBuffer sb = new StringBuffer();
//
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//
//            data = sb.toString();
//
//            br.close();
//
//        } catch (Exception e) {
//            Log.d("Exception", e.toString());
//        } finally {
//            iStream.close();
//            urlConnection.disconnect();
//        }
//        return data;
//    }
//
//}