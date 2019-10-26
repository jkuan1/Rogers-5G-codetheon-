package com.example.rogers5g;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.filter.Filter;
import com.otaliastudios.cameraview.filter.Filters;
import com.otaliastudios.cameraview.filter.MultiFilter;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;
import com.otaliastudios.cameraview.size.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class CameraActivity extends AppCompatActivity implements Observer {
    List<FirebaseVisionFace> localFaces;
    DrawView drawView;

    // For activities
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.viewParent);
        drawView = new DrawView(getApplicationContext());
        viewGroup.addView(drawView);

        GetAndOverlay child = new GetAndOverlay();
        child.runCamera(this, this);
        localFaces = new ArrayList<>();
        List<FirebaseVisionFace> lastFaces = new ArrayList<>();

    }

    public void update(Observable o, Object listObj) {
        localFaces = ((GetAndOverlay) o).getFaces();
        Log.println(Log.INFO,
                "tracedFaces",
                localFaces.toString());
        drawView.invalidate();
    }

    class DrawView extends View
    {

        public DrawView(Context context) {
            super(context);
            setWillNotDraw(false);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);

            int radius=40;
            Paint paint=new Paint();
            for (FirebaseVisionFace face : localFaces) {
                paint.setColor(Color.parseColor("#CD5C5C"));
                Rect boundingRect = face.getBoundingBox();
                canvas.drawCircle((boundingRect.right + boundingRect.left)/2,
                        boundingRect.top,
                        radius,
                        paint);
            }
        }

    }
}
