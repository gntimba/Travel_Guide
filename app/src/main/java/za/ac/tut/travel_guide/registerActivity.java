package za.ac.tut.travel_guide;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thulani on 2017/08/29.
 */

public class registerActivity extends AppCompatActivity {
    EditText name,pass,email,answer;
    Spinner question;
    private ImageView picture;
    private static final String baseUrlForImage = "http://192.168.1.100/php/images/";
    private static final int SELECT_PICTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=(EditText)findViewById(R.id.name);
        pass=(EditText)findViewById(R.id.password);
        email=(EditText)findViewById(R.id.email);
        question=(Spinner) findViewById(R.id.sQuestion);
        answer=(EditText)findViewById(R.id.sAnswer);
        picture=(ImageView)findViewById(R.id.imageView);
        //generate spinner items for security questions
        Spinner mySp = (Spinner) findViewById(R.id.sQuestion);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(registerActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySp.setAdapter(myAdapter);
    }
    public void OnRegister(View view){

        if(!Validate.emailIsValid(email.getText().toString()))
        {
            email.setError("Invalid Email Address");
        }


        if(pass.getText().length() < 1)
        {
            pass.setError("Passwords Invalid");
        }


        if(!Validate.fNameIsValid(name.getText().toString()))
        {
            name.setError("Invalid name");
        }

        if(answer.getText().toString().length() < 2)
        {
            answer.setError("Invalid security answer");
        }

        if(Validate.emailIsValid(email.getText().toString())
                && (pass.getText().length() >= 1)
                && Validate.fNameIsValid(name.getText().toString())
                && answer.getText().toString().length() >= 2)
        {
            String user = email.getText().toString();
            String password = pass.getText().toString();
            String names = name.getText().toString();
            String sQu = question.getSelectedItem().toString();
            String sAn = answer.getText().toString();
            Bitmap bitmap = ((BitmapDrawable) picture.getDrawable()).getBitmap();
            String imageData = encodeTobase64(bitmap);
            String type = "register";
            backWorker validate = new backWorker(this);
            validate.execute(type, user, password, names, sQu, sAn, imageData);
            finish();
        }

    }
    public void ViewPicture(View view){
        Intent imageDownload = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imageDownload.putExtra("crop", "true");
        imageDownload.putExtra("aspectX", 1);
        imageDownload.putExtra("aspectY", 1);
        imageDownload.putExtra("outputX", 200);
        imageDownload.putExtra("outputY", 200);
        imageDownload.putExtra("return-data", true);
        startActivityForResult(imageDownload, 2);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode == RESULT_OK && data != null)
        {
            Bundle extras = data.getExtras();
            Bitmap image = extras.getParcelable("data");
            picture.setImageBitmap(image);
        }
    }
    public String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }
    private void SetImage(Bitmap image) {
        picture.setImageBitmap(image);
        // upload


    }
    public void pageChanhe(View v){
        Intent intent =new Intent(registerActivity.this,MainActivity.class);
        startActivity(intent);
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
    public static String encodeTobase64(Bitmap image) {
        System.gc();

        if (image == null)return null;

        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] b = baos.toByteArray();

        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT); // min minSdkVersion 8

        return imageEncoded;
    }
}
