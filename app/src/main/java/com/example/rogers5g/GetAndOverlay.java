package com.example.rogers5g;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LifecycleOwner;

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
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;
import com.otaliastudios.cameraview.size.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class GetAndOverlay extends Observable {
    private FirebaseVisionFaceDetectorOptions options;
    private FirebaseVisionFaceDetector detector;
    private List<FirebaseVisionFace> lastFaces;
    private Frame lastFrame;

    // Access last seen face w/ getFaces
    protected void runCamera(Activity parentActivity, LifecycleOwner lifeCycleOwner) {
        addObserver((Observer) parentActivity);

        lastFaces = new ArrayList<>();
        options = new FirebaseVisionFaceDetectorOptions.Builder()
                .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .setContourMode(FirebaseVisionFaceDetectorOptions.NO_CONTOURS)
                .enableTracking()
                .build();

        detector = FirebaseVision.getInstance().getVisionFaceDetector(options);

        CameraView camera = parentActivity.findViewById(R.id.camera);
        camera.setLifecycleOwner(lifeCycleOwner);
        // https://natario1.github.io/CameraView/docs/frame-processing.html
        camera.addFrameProcessor(new FrameProcessor() {
            @Override
            @WorkerThread
            public void process(final Frame frame) {
                byte[] data = frame.getData();
                int rotation = frame.getRotation();
                long time = frame.getTime();
                Size size = frame.getSize();
                int format = frame.getFormat();

                FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
                        .setWidth(size.getWidth())
                        .setHeight(size.getHeight())
                        .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                        .setRotation(FirebaseVisionImageMetadata.ROTATION_270)
                        .build();

                FirebaseVisionImage image = FirebaseVisionImage.fromByteArray(data, metadata);

                Task<List<FirebaseVisionFace>> result =
                        detector.detectInImage(image)
                                .addOnSuccessListener(
                                        new OnSuccessListener<List<FirebaseVisionFace>>() {
                                            @Override
                                            public void onSuccess(List<FirebaseVisionFace> faces) {
                                                if (faces.size() != 0) {
                                                    lastFaces = faces;
                                                    lastFrame = frame;
                                                    Log.println(Log.INFO,
                                                            "tracedFaces",
                                                            faces.toString());
                                                    setChanged();
                                                    notifyObservers(faces);
                                                }
                                            }
                                        })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.println(Log.ERROR,
                                                        "faceERR",
                                                        "Failed to read faces");
                                            }
                                        });
            }
        });
    }

    public List<FirebaseVisionFace> getFaces() {
        return lastFaces;
    }
    public Frame getFrame() {
        return lastFrame;
    }
}
