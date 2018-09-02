package za.ac.tut.travel_guide.Customer;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static za.ac.tut.travel_guide.IP.getIp;

/**
 * Created by Tshepo on 8/30/2017.
 */

public class BackgroundWorker extends AsyncTask<String, Void, String>{
    Context context;
    AlertDialog alertDialog;
    BackgroundWorker (Context ctx) {context = ctx;}

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String register_url = getIp()+"reg_customer.php";

            try{
                String email_addrs = params[1];
                String password = params[2];
                String firstname  = params[3] ;
                String lastname  = params[4];
                String gendr = params[5];
                String dateOB = params[6];
                String sQ = params[7];
                String sA  = params[8];
                String image=params[9];
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream =httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email_address", "UTF-8")+ "=" + URLEncoder.encode(email_addrs, "UTF-8") + "&"
                        +  URLEncoder.encode("password", "UTF-8")+ "=" + URLEncoder.encode(password, "UTF-8") + "&"
                        +  URLEncoder.encode("fname", "UTF-8")+ "=" + URLEncoder.encode(firstname, "UTF-8") + "&"
                        +  URLEncoder.encode("lname", "UTF-8")+ "=" + URLEncoder.encode(lastname, "UTF-8") + "&"
                        +  URLEncoder.encode("sex", "UTF-8")+ "=" + URLEncoder.encode(gendr, "UTF-8") + "&"
                        +  URLEncoder.encode("dob", "UTF-8")+ "=" + URLEncoder.encode(dateOB, "UTF-8") + "&"
                        +  URLEncoder.encode("security_q", "UTF-8")+ "=" + URLEncoder.encode(sQ, "UTF-8") + "&"
                        +  URLEncoder.encode("image", "UTF-8")+ "=" + URLEncoder.encode(image, "UTF-8") + "&"
                        +  URLEncoder.encode("security_a", "UTF-8")+ "=" + URLEncoder.encode(sA, "UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    result = result + line;
                }
                bufferedReader.close();
                return result;
            }
            catch(MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }




        return null;
    }


    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Insert Status");
    }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.setMessage(result);
        alertDialog.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {

        super.onProgressUpdate(values);
    }


}