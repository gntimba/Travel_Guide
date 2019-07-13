package za.ac.tut.travel_guide;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

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

import za.ac.tut.travel_guide.Customer.user_main;

/**
 * Created by Thulani on 2017/08/28.
 */

public class backWorker extends AsyncTask<String, Void, String> {

    Context ctx;
    public String email = null;
    AlertDialog dialog;

    public backWorker(Context ctx) {
        this.ctx = ctx;
    }

    protected String doInBackground(String... params) {

        String ip = IP.getIp();
        String type = params[0];
        String add_place = ip + "addplace.php";
        String login_url = ip + "login.php";
        String register_url = ip + "register.php";
        String admin_url = ip + "adminlogin.php";
        String user_login = ip + "userlogin.php";
        String update_user = ip + "updateCust.php";
        String review_place = ip + "review.php";
        String update_like = ip + "update_like.php";
        String add_Activity = ip + "addActivity.php";
        if (type.equals("register")) {
            String email = params[1];
            String pass = params[2];
            String name = params[3];
            String question = params[4];
            String answer = params[5];
            String image = params[6];
            try {

                URL url = new URL(register_url);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setDoInput(true);
                OutputStream output = http.getOutputStream();
                BufferedWriter bWritter = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                        + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8") + "&"
                        + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&"
                        + URLEncoder.encode("question", "UTF-8") + "=" + URLEncoder.encode(question, "UTF-8") + "&"
                        + URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(image, "UTF-8") + "&"
                        + URLEncoder.encode("answer", "UTF-8") + "=" + URLEncoder.encode(answer, "UTF-8");
                bWritter.write(post_data);
                bWritter.flush();
                output.close();
                InputStream read = http.getInputStream();
                BufferedReader bRead = new BufferedReader(new InputStreamReader(read, "iso-8859-1"));
                String result = "";
                String line;
                while ((line = bRead.readLine()) != null) {
                    result += line;
                }
                bRead.close();
                read.close();
                http.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("login")) {
            try {
                String user = params[1];
                String pass = params[2];
                email = user;
                URL url = new URL(login_url);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setDoInput(true);
                OutputStream output = http.getOutputStream();
                BufferedWriter bWritter = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
                String post_data = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8") + "&"
                        + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");
                bWritter.write(post_data);
                bWritter.flush();
                output.close();
                InputStream read = http.getInputStream();
                BufferedReader bRead = new BufferedReader(new InputStreamReader(read, "iso-8859-1"));
                String result = "";
                String line = null;
                while ((line = bRead.readLine()) != null) {
                    result += line;
                }
                bRead.close();
                read.close();
                http.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("profile")) {

            String profile_url = ip + "profile.php", json_string;
            try {
                URL url = new URL(profile_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((json_string = bufferedReader.readLine()) != null) {
                    stringBuilder.append(json_string + "\n");

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                // setResponse(stringBuilder.toString().trim());
                //result=stringBuilder.toString().trim();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("addPlace")) {
            String name = params[1];
            String address = params[2];
            String imagePlace = params[3];
            String longitude = params[4];
            String latitude = params[5];
            String client = params[6];
            String price = params[8];
            String admin = params[7];
            try {

                URL url = new URL(add_place);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setDoInput(true);
                OutputStream output = http.getOutputStream();
                BufferedWriter bWritter = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
                String post_data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&"
                        + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8") + "&"
                        + URLEncoder.encode("imagePlace", "UTF-8") + "=" + URLEncoder.encode(imagePlace, "UTF-8") + "&"
                        + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(longitude, "UTF-8") + "&"
                        + URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(latitude, "UTF-8") + "&"
                        + URLEncoder.encode("price", "UTF-8") + "=" + URLEncoder.encode(price, "UTF-8") + "&"
                        + URLEncoder.encode("client", "UTF-8") + "=" + URLEncoder.encode(client, "UTF-8") + "&"
                        + URLEncoder.encode("admin", "UTF-8") + "=" + URLEncoder.encode(admin, "UTF-8");
                bWritter.write(post_data);
                bWritter.flush();
                output.close();
                InputStream read = http.getInputStream();
                BufferedReader bRead = new BufferedReader(new InputStreamReader(read, "iso-8859-1"));
                String result = "";
                String line;
                while ((line = bRead.readLine()) != null) {
                    result += line;
                }
                bRead.close();
                read.close();
                http.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("admin")) {
            try {
                String user = params[1];
                String pass = params[2];
                email = user;
                URL url = new URL(admin_url);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setDoInput(true);
                OutputStream output = http.getOutputStream();
                BufferedWriter bWritter = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
                String post_data = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8") + "&"
                        + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");
                bWritter.write(post_data);
                bWritter.flush();
                output.close();
                InputStream read = http.getInputStream();
                BufferedReader bRead = new BufferedReader(new InputStreamReader(read, "iso-8859-1"));
                String result = "";
                String line = null;
                while ((line = bRead.readLine()) != null) {
                    result += line;
                }
                bRead.close();
                read.close();
                http.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("user")) {
            try {
                String user = params[1];
                String pass = params[2];
                email = user;
                URL url = new URL(user_login);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setDoInput(true);
                OutputStream output = http.getOutputStream();
                BufferedWriter bWritter = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
                String post_data = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8") + "&"
                        + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");
                bWritter.write(post_data);
                bWritter.flush();
                output.close();
                InputStream read = http.getInputStream();
                BufferedReader bRead = new BufferedReader(new InputStreamReader(read, "iso-8859-1"));
                String result = "";
                String line = null;
                while ((line = bRead.readLine()) != null) {
                    result += line;
                }
                bRead.close();
                read.close();
                http.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("user_update")) {
            try {
                String email_addrs = params[1];
                String firstname = params[2];
                String lastname = params[3];
                String dateOB = params[4];
                String image = params[5];
                URL url = new URL(update_user);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setDoInput(true);
                OutputStream output = http.getOutputStream();
                BufferedWriter bWritter = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
                String post_data = URLEncoder.encode("email_address", "UTF-8") + "=" + URLEncoder.encode(email_addrs, "UTF-8") + "&"
                        + URLEncoder.encode("fname", "UTF-8") + "=" + URLEncoder.encode(firstname, "UTF-8") + "&"
                        + URLEncoder.encode("lname", "UTF-8") + "=" + URLEncoder.encode(lastname, "UTF-8") + "&"
                        + URLEncoder.encode("dob", "UTF-8") + "=" + URLEncoder.encode(dateOB, "UTF-8") + "&"
                        + URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(image, "UTF-8");
                bWritter.write(post_data);
                bWritter.flush();
                output.close();
                InputStream read = http.getInputStream();
                BufferedReader bRead = new BufferedReader(new InputStreamReader(read, "iso-8859-1"));
                String result = "";
                String line = null;
                while ((line = bRead.readLine()) != null) {
                    result += line;
                }
                bRead.close();
                read.close();
                http.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("review")) {
            try {
                String place_id = params[1];
                String comment = params[2];
                String rate = params[3];
                String cust_email = params[4];
                URL url = new URL(review_place);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setDoInput(true);
                OutputStream output = http.getOutputStream();
                BufferedWriter bWritter = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
                String post_data = URLEncoder.encode("place_id", "UTF-8") + "=" + URLEncoder.encode(place_id, "UTF-8") + "&"
                        + URLEncoder.encode("comment", "UTF-8") + "=" + URLEncoder.encode(comment, "UTF-8") + "&"
                        + URLEncoder.encode("rate", "UTF-8") + "=" + URLEncoder.encode(rate, "UTF-8") + "&"
                        + URLEncoder.encode("cust_email", "UTF-8") + "=" + URLEncoder.encode(cust_email, "UTF-8");
                bWritter.write(post_data);
                bWritter.flush();
                output.close();
                InputStream read = http.getInputStream();
                BufferedReader bRead = new BufferedReader(new InputStreamReader(read, "iso-8859-1"));
                String result = "";
                String line = null;
                while ((line = bRead.readLine()) != null) {
                    result += line;
                }
                bRead.close();
                read.close();
                http.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("update")) {
            try {
                String place_id = params[1];
                String user = params[2];
                String like = params[3];
                URL url = new URL(update_like);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setDoInput(true);
                OutputStream output = http.getOutputStream();
                BufferedWriter bWritter = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
                String post_data = URLEncoder.encode("place_id", "UTF-8") + "=" + URLEncoder.encode(place_id, "UTF-8") + "&"
                        + URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8") + "&"
                        + URLEncoder.encode("like", "UTF-8") + "=" + URLEncoder.encode(like, "UTF-8");
                bWritter.write(post_data);
                bWritter.flush();
                output.close();
                InputStream read = http.getInputStream();
                BufferedReader bRead = new BufferedReader(new InputStreamReader(read, "iso-8859-1"));
                String result = "";
                String line = null;
                while ((line = bRead.readLine()) != null) {
                    result += line;
                }
                bRead.close();
                read.close();
                http.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (type.equals("activity")) {
            try {
                String place_id = params[1];
                String user = params[3];
                String activity = params[2];
                URL url = new URL(add_Activity);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setDoInput(true);
                OutputStream output = http.getOutputStream();
                BufferedWriter bWritter = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
                String post_data = URLEncoder.encode("place_id", "UTF-8") + "=" + URLEncoder.encode(place_id, "UTF-8") + "&"
                        + URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8") + "&"
                        + URLEncoder.encode("activity", "UTF-8") + "=" + URLEncoder.encode(activity, "UTF-8");
                bWritter.write(post_data);
                bWritter.flush();
                output.close();
                InputStream read = http.getInputStream();
                BufferedReader bRead = new BufferedReader(new InputStreamReader(read, "iso-8859-1"));
                String result = "";
                String line = null;
                while ((line = bRead.readLine()) != null) {
                    result += line;
                }
                bRead.close();
                read.close();
                http.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    protected void onPreExecute() {


        dialog = new AlertDialog.Builder(ctx).create();
        dialog.setTitle("Status");

    }


    @Override
    protected void onPostExecute(String result) {
        // if(typ!="profile") {
        //dialog.setMessage(result);
        try {


            if (!result.contains("like")) {
                // dialog.show();

                Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
            }

            if (result.contains("success")) {
                //Toast.makeText(ctx, email, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ctx, profileView.class);
                intent.putExtra("email", email);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.getApplicationContext().startActivity(intent);
            } else if (result.contains("Admin")) {
                Intent intent = new Intent(ctx, AdminMenu.class);
                intent.putExtra("email", email);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.getApplicationContext().startActivity(intent);
            } else if (result.contains("User")) {
                Intent intent = new Intent(ctx, user_main.class);
                intent.putExtra("email", email);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.getApplicationContext().startActivity(intent);
            }
        }catch (Exception ex)
        {
            Toast.makeText(ctx, "please check your internet connection", Toast.LENGTH_SHORT).show();
            System.out.println(ex.toString());
        }


    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
