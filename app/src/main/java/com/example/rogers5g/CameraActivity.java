package com.example.rogers5g;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.ml.vision.face.FirebaseVisionFace;
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
        viewGroup.addView(drawView, 0);
        drawView.invalidate();

        GetAndOverlay child = new GetAndOverlay();
        child.runCamera(this, this);
        localFaces = new ArrayList<>();
        List<FirebaseVisionFace> lastFaces = new ArrayList<>();

    }

    public void update(Observable o, Object listObj) {
        localFaces = ((GetAndOverlay) o).getFaces();
        Log.println(Log.INFO,
                "observerFacesFound",
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
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int desiredWidth = 1080;
            int desiredHeight = 2244;

            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);

            int width;
            int height;

            //Measure Width
            if (widthMode == MeasureSpec.EXACTLY) {
                //Must be this size
                width = widthSize;
            } else if (widthMode == MeasureSpec.AT_MOST) {
                //Can't be bigger than...
                width = Math.min(desiredWidth, widthSize);
            } else {
                //Be whatever you want
                width = desiredWidth;
            }

            //Measure Height
            if (heightMode == MeasureSpec.EXACTLY) {
                //Must be this size
                height = heightSize;
            } else if (heightMode == MeasureSpec.AT_MOST) {
                //Can't be bigger than...
                height = Math.min(desiredHeight, heightSize);
            } else {
                //Be whatever you want
                height = desiredHeight;
            }

            //MUST CALL THIS
            setMeasuredDimension(width, height);
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
            canvas.drawCircle(0, 0, 500, paint);
        }

    }
}
