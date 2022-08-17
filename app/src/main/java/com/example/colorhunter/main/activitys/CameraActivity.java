package com.example.colorhunter.main.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.colorhunter.R;
import com.example.colorhunter.main.YUVtoRGB;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity {

    private ImageButton addNewColorBtn, backBtn, flipCamera;
    private PreviewView previewView;
    private View colorPreview, centerSign;
    private TextView textViewR, textViewG, textViewB, textViewHex;

    YUVtoRGB translator = new YUVtoRGB();

    private int rC, gC, bC, surfaceWidth, surfaceHeight;
    private String colorHex;

    private CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
    ProcessCameraProvider cameraProvider;

    private static final int PERMISSION_REQUEST_CAMERA = 135;

    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        addNewColorBtn = findViewById(R.id.camera_capture_button);
        previewView = findViewById(R.id.view_finder);
        backBtn = findViewById(R.id.back_button);
        colorPreview = findViewById(R.id.color_preview);
        centerSign = findViewById(R.id.center_sign);
        textViewB = findViewById(R.id.textB);
        textViewG = findViewById(R.id.textG);
        textViewR = findViewById(R.id.textR);
        textViewHex = findViewById(R.id.textHex);
        flipCamera = findViewById(R.id.flip_camera);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CAMERA);
        }else{
            initializeCamera();
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CameraActivity.this, ListActivity.class);
                startActivity(i);
            }
        });

        flipCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraProvider.unbindAll();
                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA){
                    cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
                }else if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA)
                    cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                initializeCamera();
            }
        });

        addNewColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CameraActivity.this, AddNewColorActivity.class);
                intent.putExtra("hex", colorHex);
                intent.putExtra("r", rC);
                intent.putExtra("g", gC);
                intent.putExtra("b", bC);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA && grantResults.length > 0 &&
        grantResults[0] == PackageManager.PERMISSION_GRANTED){
            initializeCamera();
        }
    }

    private void initializeCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    cameraProvider = cameraProviderFuture.get();

                    Preview preview = new Preview.Builder().build();

                    preview.setSurfaceProvider(previewView.getSurfaceProvider());

                    ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                            .setTargetResolution(new Size(surfaceWidth, surfaceHeight))
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build();

                    imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(CameraActivity.this),
                            new ImageAnalysis.Analyzer() {
                                @Override
                                public void analyze(@NonNull ImageProxy image) {
                                    @SuppressLint("UnsafeOptInUsageError") Image img = image.getImage();
                                    Bitmap bitmap = translator.translateYUV(img, CameraActivity.this);

                                    int pixel = bitmap.getPixel(bitmap.getWidth() / 2, bitmap.getHeight() / 2);

                                    if (pixel != 0) {
                                        rC = Color.red(pixel);
                                        gC = Color.green(pixel);
                                        bC = Color.blue(pixel);
                                        colorHex = "#" + Integer.toHexString(pixel);

                                        colorPreview.setBackgroundColor(Color.parseColor(colorHex));

                                        textViewR.setText("R: " + rC);
                                        textViewG.setText("G: " + gC);
                                        textViewB.setText("B: " + bC);
                                        textViewHex.setText("Hex: " + colorHex);

                                    }

                                    image.close();
                                }
                            });

                    cameraProvider.bindToLifecycle(CameraActivity.this, cameraSelector, imageAnalysis, preview);

                    surfaceHeight = preview.getResolutionInfo().getResolution().getHeight();
                    surfaceWidth = preview.getResolutionInfo().getResolution().getWidth();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

}