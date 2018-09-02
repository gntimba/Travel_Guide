package za.ac.tut.travel_guide;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class addPlace extends AppCompatActivity {


    private static final int SELECT_PICTURE = 1;
    ImageView picture;
    EditText street, suburb, code, name, price;
    Spinner spin;
    JSONObject jsonobject;
    JSONArray jsonarray;
    ArrayList<String> worldlist;
    private Button b, button;
    private TextView longi, lati, admin;
    private LocationManager locationManager;
    private LocationListener listener;
    private String longitude, latitude;
    private String TAG = addPlace.class.getSimpleName();

    public static String encodeTobase64(Bitmap image) {
        System.gc();

        if (image == null) return null;

        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] b = baos.toByteArray();

        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT); // min minSdkVersion 8

        return imageEncoded;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_place);
        street = (EditText) findViewById(R.id.Street);
        suburb = (EditText) findViewById(R.id.suburb);
        code = (EditText) findViewById(R.id.code);
        name = (EditText) findViewById(R.id.PlaceName);
        price = (EditText) findViewById(R.id.price);
        admin = (TextView) findViewById(R.id.admi);
        button = (Button) findViewById(R.id.regin);
        //admin.setText(getIntent().getExtras().getString("email"));
        admin.setText("test");
        longi = (TextView) findViewById(R.id.lonID);
        lati = (TextView) findViewById(R.id.latID);
        b = (Button) findViewById(R.id.button);
        button.setEnabled(false);
        picture = (ImageView) findViewById(R.id.pic);
        spin = (Spinner) findViewById(R.id.my_spinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(addPlace.this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(myAdapter);
        new DownloadJSON().execute();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude() + "";
                latitude = location.getLatitude() + "";
                button.setEnabled(true);
                longi.setText(longitude);
                lati.setText(latitude);
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 5000, 10, listener);
            }
        });
    }

    public void ViewPicture(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (Build.VERSION.SDK_INT < 19) {
                    String selectedImagePath = getPath(selectedImageUri);
                    Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                    SetImage(bitmap);
                } else {
                    ParcelFileDescriptor parcelFileDescriptor;
                    try {
                        parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImageUri, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        parcelFileDescriptor.close();
                        SetImage(image);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void SetImage(Bitmap image) {
        this.picture.setImageBitmap(image);

    }

    public String getPath(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    public void add(View v) {


        if (name.getText().toString().isEmpty())
            name.setError("name of the place cant be empty");
        if (price.getText().toString().isEmpty())
            price.setError("the field must not be empty");
        if (street.getText().toString().isEmpty())
            street.setError("the field must not be empty");
        if (suburb.getText().toString().isEmpty())
            suburb.setError("the field must not be empty");
        if (code.getText().toString().isEmpty())
            code.setError("the field must not be empty");

        if (!name.getText().toString().isEmpty() && !price.getText().toString().isEmpty() && !street.getText().toString().isEmpty()
                && !suburb.getText().toString().isEmpty() && !code.getText().toString().isEmpty()) {
            String client = spin.getSelectedItem().toString();
            String names = name.getText().toString();
            String admi = admin.getText().toString();
            String prices = price.getText().toString();
            String address = street.getText() + "#" + suburb.getText() + "#" + code.getText();
            Bitmap bitmap = ((BitmapDrawable) picture.getDrawable()).getBitmap();
            String imageData = encodeTobase64(bitmap);
            String type = "addPlace";
            backWorker backWorker = new backWorker(this);
            backWorker.execute(type, names, address, imageData, longitude, latitude, client, admi, prices);
            button.setEnabled(false);
            price.setText("");
            suburb.setText("");
            street.setText("");
            name.setText("");
            code.setText("");
        }


    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {


        httpGet get = new httpGet();

        @Override
        protected Void doInBackground(Void... params) {
            // Locate the WorldPopulation Class

            // Create an array to populate the spinner
            worldlist = new ArrayList<String>();
            // JSON file URL address
            jsonobject = get.getJSONfromURL(IP.getIp() + "spinner.php");

            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("server_response");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);


                    // Populate spinner with country names
                    worldlist.add(jsonobject.optString("client_e_address"));

                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml


            Spinner mySpinner = (Spinner) findViewById(R.id.my_spinner);

            // Spinner adapter
            mySpinner.setAdapter(new ArrayAdapter<String>(addPlace.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    worldlist));

            // Spinner on item click listener


        }
    }

}
