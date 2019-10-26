package com.example.rogers5g;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.filter.Filter;
import com.otaliastudios.cameraview.filter.Filters;
import com.otaliastudios.cameraview.filter.MultiFilter;

public class CameraActivity extends AppCompatActivity {
    // For activities
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CameraView camera = findViewById(R.id.camera1);
        camera.setLifecycleOwner(this);
        camera.setFilter(new MultiFilter(Filters.TINT.newInstance()));
    }
}
