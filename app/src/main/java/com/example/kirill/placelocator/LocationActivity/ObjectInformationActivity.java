package com.example.kirill.placelocator.LocationActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kirill.placelocator.R;

public class ObjectInformationActivity extends AppCompatActivity {
    private ImageView imageViewIcon;
    private TextView textViewAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_information);
        initComponents();
    }

    private void initComponents() {
        this.imageViewIcon = (ImageView) this.findViewById(R.id.activity_object_information_icon);
        this.textViewAbout = (TextView) this.findViewById(R.id.activity_object_information_about);
        Intent intent = getIntent();
        if(intent != null){
            this.imageViewIcon.setImageResource(intent.getIntExtra(LocationActivity.PLACE_DRAWABLE_ID, 0));
            this.textViewAbout.setText(intent.getIntExtra(LocationActivity.PLACE_ABOUT, 0));
            this.setTitle(intent.getIntExtra(LocationActivity.PLACE_TITLE, 0));
        }
    }
}
