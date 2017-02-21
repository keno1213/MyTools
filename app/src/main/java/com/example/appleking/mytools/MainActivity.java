package com.example.appleking.mytools;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.appleking.mytools.tools.takePhoto.TakePhotoActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gotoPhoto(View view){
        Intent intent  = new Intent(this, TakePhotoActivity.class);
        startActivity(intent);
    }
}
