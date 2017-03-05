package com.outfit.jure.demoapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int SELECT_PHOTO = 100;
    ImageView imageView;
    Button sendEmailBt;
    Uri selectedImage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imgView);
        sendEmailBt = (Button) findViewById(R.id.sendEmail);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Uri selectedImage = savedInstanceState.getParcelable("image");
        if(selectedImage!=null){
            setUri(selectedImage);
            imageView.setImageURI(selectedImage);// To display selected image in image view
            sendEmailBt.setEnabled(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("image", getUri());
        super.onSaveInstanceState(outState);
    }

    public void selectPhoto(View v){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    public void sendEmail(View v){
        Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "QA testers");
        String body = getBody();
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent .putExtra(Intent.EXTRA_STREAM, getUri());
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String email = sharedPref.getString("email", getResources().getString(R.string.def_email));
        intent.setData(Uri.parse("mailto:"+email)); // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(intent);
    }

    private String getBody(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
        double screenInches = (double)Math.round(Math.sqrt(x+y)* 10) / 10;
        float dpHeight = dm.heightPixels / dm.density;
        float dpWidth = dm.widthPixels / dm.density;
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        return getString(R.string.body, Build.MANUFACTURER.toUpperCase(),  Build.MODEL, screenInches, dpHeight, dpWidth, height, width, dm.density);
        //return "Screen size(inches):"+screenInches+"\nScreen size(dpHeight, dpWidth): "+dpHeight+", "+dpWidth+"\nScreen size (pxHeight,pxWidth): "+height+", "+width+"\nDensity: "+dm.density;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    setUri(imageReturnedIntent.getData());
                    imageView.setImageURI(getUri());// To display selected image in image view
                    sendEmailBt.setEnabled(true);
                }
        }
    }

    public void setUri(Uri selectedImage) {
        this.selectedImage = selectedImage;
    }

    public Uri getUri() {
        return selectedImage;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
        }

        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
