package za.ac.tut.travel_guide;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import za.ac.tut.travel_guide.Customer.MapsActivity;
import za.ac.tut.travel_guide.Customer.custProfile;
import za.ac.tut.travel_guide.Customer.review;
import za.ac.tut.travel_guide.review.reviews_main;
import za.ac.tut.travel_guide.util.email;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static za.ac.tut.travel_guide.IP.getIp;

public class viewDetails extends AppCompatActivity {
    TextView name,disatnce,suburb,code,street,price;
    Button mapView;
    ImageView imageView;
    ImageButton imageButton;
    LikeButton likeButton;
    //String place_id;
    private String TAG = viewDetails.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        name=(TextView)findViewById(R.id.pNmae);
        disatnce=(TextView)findViewById(R.id.distanceP);
        suburb=(TextView)findViewById(R.id.sSub);
        street=(TextView)findViewById(R.id.sAdr);
        code=(TextView)findViewById(R.id.pPostal);
        price=(TextView) findViewById(R.id.Pri);
        imageButton=(ImageButton) findViewById(R.id.imageButton);
        likeButton=(LikeButton) findViewById(R.id.star_button);
        likeButton.setLiked(true);
        imageView=(ImageView)findViewById(R.id.imageDetails);
        mapView=(Button)findViewById(R.id.mapView);
       email.setPlace_id(getIntent().getExtras().getString("place_id"));
        viewDe();
        likeButton.setOnLikeListener(new OnLikeListener() {

            @Override
            public void liked(LikeButton likeButton) {
                Toast.makeText(getApplicationContext(), "liked", Toast.LENGTH_SHORT).show();

                li("1");
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Toast.makeText(getApplicationContext(), "unliked", Toast.LENGTH_SHORT).show();
            li("0");
            }

        });
        new likeBack().execute();
        new counter().execute();
    }
    public void li(String lik){
        String type="update";
        new backWorker(this).execute(type,email.getPlace_id(),email.getEmail(),lik);
        new counter().execute();

    }
    public void viewDe(){
        name.setText(getIntent().getExtras().getString("name"));
        String[] split=(getIntent().getExtras().getString("address")).split("#");
        street.setText(split[0]);
        suburb.setText(split[1]);
        code.setText(split[2]);
        price.setText(getIntent().getExtras().getString("price"));
        disatnce.setText(getIntent().getExtras().getString("distance")+"km");
        Picasso.get().load(getIntent().getExtras().getString("imageurl")).into(imageView);

    }
    public void OnMapView(View v){
        String longitude=getIntent().getExtras().getString("longitude");
        String latitude=getIntent().getExtras().getString("latitude");
        Intent intent =new Intent(viewDetails.this,MapsActivity.class);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        startActivity(intent);
    }
    public void OnReview(View view){
        Intent intent =new Intent(viewDetails.this,review.class);
        //intent.putExtra("place_id",email.getPlace_id());
        //intent.putExtra("email",email.getEmail());
        startActivity(intent);
    }
    public void OnAddList(View view){
        Intent intent =new Intent(viewDetails.this,to_do_list_main.class);
        //intent.putExtra("place_id",email.getPlace_id());
        startActivity(intent);
    }
    public void OnComments(View view){
        Intent intent =new Intent(viewDetails.this,reviews_main.class);
        //intent.putExtra("place_id",email.getPlace_id());
        startActivity(intent);
    }

    public void profile(View view){
        Intent intent =new Intent(viewDetails.this,custProfile.class);
       // intent.putExtra("email",email.getEmail());
                startActivity(intent);

    }
    public void list(View view){
        Intent intent =new Intent(viewDetails.this,to_list_view.class);
        // intent.putExtra("email",email.getEmail());
        startActivity(intent);

    }

    getPro ppro= new getPro();
    private class likeBack extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            httpGet sh = new httpGet();
            // Making a request to url and getting response
            String url = getIp()+"check_like.php";
            String jsonStr = sh.makeServiceCall(url, email.getEmail(),email.getPlace_id());

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray custome = jsonObj.getJSONArray("server_response");

                    // looping through All Contacts
                    for (int i = 0; i < custome.length(); i++) {
                        JSONObject c = custome.getJSONObject(i);
                        ppro.setNumber(c.getString("like"));

                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;



        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            like(ppro.getNumber());
        }

    }
    private class counter extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            httpGet sh = new httpGet();
            // Making a request to url and getting response
            String url = getIp()+"count_place.php";
            String jsonStr = sh.makeServiceCall(url,email.getPlace_id());

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray custome = jsonObj.getJSONArray("server_response");

                    // looping through All Contacts
                    for (int i = 0; i < custome.length(); i++) {
                        JSONObject c = custome.getJSONObject(i);
                        ppro.setLikes(c.getString("num"));

                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;



        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            TextView like=(TextView)findViewById(R.id.count);
            like.setText(ppro.getLikes()+" Like(s)");


        }

    }
    public void like(String like){
       // Toast.makeText(getApplicationContext(), ppro.getNumber(), Toast.LENGTH_SHORT).show();
                  if(like.contains("1"))
                          likeButton.setLiked(true);
           else
                   likeButton.setLiked(false); }
}
