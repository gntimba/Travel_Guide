package za.ac.tut.travel_guide.Customer;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import za.ac.tut.travel_guide.IP;
import za.ac.tut.travel_guide.JSONfunctions;
import za.ac.tut.travel_guide.MainActivity;
import za.ac.tut.travel_guide.R;
import za.ac.tut.travel_guide.util.email;


public class user_main extends AppCompatActivity {
    // Declare Variables
    JSONObject jsonobject;
    JSONArray jsonarray;
    SeekBar seekBar;
    ListView listview;
    public int change;
    ListViewAdapter adapter;
    ProgressDialog mProgressDialog;
    ProgressDialog loc;
    ArrayList<HashMap<String, String>> arraylist;
    private LocationManager locationManager;
    private LocationListener listener;
    double longitude=0,latitude=0;
TextView textView;
    SharedPreferences sp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from listview_main.xml
        setContentView(R.layout.activity_user_main);
        email.setEmail(getIntent().getExtras().getString("email"));
        sp = getSharedPreferences("login",MODE_PRIVATE);

        if(!sp.getBoolean("logged",false)) {
            sp.edit().putBoolean("logged",true).apply();
            sp.edit().putString("email",getIntent().getExtras().getString("email")).apply();

        }



        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                loc.dismiss();
                longitude=location.getLongitude();
                latitude=location.getLatitude();
                new DownloadJSON().execute();


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }

        };

        configure_button();
        seek();

        // Execute DownloadJSON AsyncTask

    }


    public void seek(){
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        textView=(TextView) findViewById(R.id.seekView);
        textView.setText(seekBar.getProgress()+" KM");
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progres_v;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        change=i*4;
                        textView.setText(change+" KM");
                        new DownloadJSON().execute();
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);

    }

    void configure_button(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        loc =new ProgressDialog(user_main.this);
        loc.setTitle("Getting Your Location");
        loc.setMessage("Acquiring GPS");
        loc.setIndeterminate(false);
        loc.show();
        locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 5000 , 10, listener);
       // locationManager.removeUpdates(listener);
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
    }

    // DownloadJSON AsyncTask
    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(user_main.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Getting your places");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create an array
            arraylist = new ArrayList<HashMap<String, String>>();
            // Retrieve JSON Objects from the given URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL(IP.getIp()+"getAllPlaces.php");

            try {
                // Locate the array name in JSON
                jsonarray = jsonobject.getJSONArray("server_response");

                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    jsonobject = jsonarray.getJSONObject(i);
                    // Retrive JSON Objects
                    map.put("name", jsonobject.getString("name"));
                    map.put("address", jsonobject.getString("address"));
                    map.put("latitude", jsonobject.getString("latitude"));
                     map.put("longitude", jsonobject.getString("longitude"));
                    map.put("imagename", jsonobject.getString("imagename"));
                    DecimalFormat df = new DecimalFormat("0.00");
                    String price= df.format(Double.parseDouble(jsonobject.getString("price")));
                    map.put("price","R"+price);
                   // map.put("email",email.getEmail());
                    map.put("place_id",jsonobject.getString("place_id"));
                    double latitude1=Double.parseDouble(jsonobject.getString("latitude"));
                    double longitude1=Double.parseDouble(jsonobject.getString("longitude"));
                    Location endPoint=new Location("locationA");
                    endPoint.setLatitude(latitude1);
                    endPoint.setLongitude(longitude1);

                    Location startPoint=new Location("locationA");
                    startPoint.setLatitude(latitude);
                    startPoint.setLongitude(longitude);
                    int distance= (int) startPoint.distanceTo(endPoint);
                    distance=distance/1000;
                    map.put("distance",distance+"");


                    // Set the JSON Objects into the array

                    if (change==0){
                    arraylist.add(map);}
                    else if (distance<change) {
                        arraylist.add(map);

                    }
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the listview in listview_main.xml
            listview = (ListView) findViewById(R.id.listmain);
            // Pass the results into ListViewAdapter.java
          //  Collections.
            Collections.sort(arraylist, new MyCustomComparator());
            adapter = new ListViewAdapter(user_main.this, arraylist);
            adapter.notifyDataSetChanged();
            // Set the adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }
    public static class MyCustomComparator implements Comparator<HashMap<String, String>> {

        @Override
        public int compare(HashMap<String, String> lhs,
                           HashMap<String, String> rhs) {
            return extractInt(lhs.get("distance")) - extractInt(rhs.get("distance"));
            //return lhs.get("distance").compareToIgnoreCase(rhs.get("distance"));


        }

        private int extractInt(String distance) {
            String num = distance.replaceAll("\\D", "");
            // return 0 if no digits found
            return num.isEmpty() ? 0 : Integer.parseInt(num);
        }
    }
}
