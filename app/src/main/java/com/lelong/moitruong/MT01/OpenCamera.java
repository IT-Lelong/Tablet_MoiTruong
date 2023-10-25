package com.lelong.moitruong.MT01;

import androidx.annotation.NonNull;
import android.util.Size;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.ZoomState;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.lelong.moitruong.Constant_Class;
import com.lelong.moitruong.Create_Table;
import com.lelong.moitruong.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class OpenCamera extends AppCompatActivity {
    private Create_Table Cre_db = null;
    String selectedDate, selectedDepartment, selectedDetail, g_User;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
    private ImageButton captureButton;
    private Preview preview;
    private PreviewView viewFind;
    private SeekBar zoomSeekBar;
    private CameraSelector cameraSelector;
    private ProcessCameraProvider cameraProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt01_open_camera_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        selectedDate = intent.getStringExtra("ngay");
        selectedDepartment = intent.getStringExtra("bophan");
        selectedDetail = intent.getStringExtra("hangmuc");
        g_User = Constant_Class.UserID;

        Cre_db = new Create_Table(getApplicationContext());
        Cre_db.open();

        viewFind = findViewById(R.id.viewFind);
        captureButton = findViewById(R.id.capture_button);
        captureButton.setOnClickListener(view -> takePhoto());
        zoomSeekBar = findViewById(R.id.zoom_seekbar);
        zoomSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float zoomLevel = (float) progress / 100.0f;
                setZoom(zoomLevel);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBar.setMax(100);
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setMax(100);
            }
        });

        startCamera();
    }

    private void setZoom(float zoomLevel) {
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);
        if (camera != null) {
            CameraInfo cameraInfo = camera.getCameraInfo();
            CameraControl cameraControl = camera.getCameraControl();

            ZoomState currentZoomState = cameraInfo.getZoomState().getValue();

            if (currentZoomState != null) {
                float maxZoomRatio = currentZoomState.getMaxZoomRatio();
                float minZoomRatio = currentZoomState.getMinZoomRatio();

                float targetZoomRatio = minZoomRatio + (maxZoomRatio - minZoomRatio) * zoomLevel;

                cameraControl.setZoomRatio(targetZoomRatio);
            }
        }
    }

    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                //ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                cameraProvider = cameraProviderFuture.get();
                bindCameraUseCases(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindCameraUseCases(@NonNull ProcessCameraProvider cameraProvider) {
        preview = new Preview.Builder().build();
        imageCapture = new ImageCapture.Builder()
                .setTargetResolution(new Size(1920, 1080))
                .build();

        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);

        // Liên kết Preview với viewFinder
        preview.setSurfaceProvider(viewFind.getSurfaceProvider());
    }

    private void takePhoto() {
        if (captureButton.isEnabled()) {
            captureButton.setEnabled(false);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    captureButton.setEnabled(true);
                }
            }, 3000);
        }
        String timestamp = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
        String fileName = selectedDepartment + "_" + selectedDetail + "_" + g_User + "_" + selectedDate + "_" + timestamp + ".png";

        File newDirectory = new File(getExternalMediaDirs()[0], selectedDate.replace("-", ""));

        if (!newDirectory.exists()) {
            newDirectory.mkdirs(); //Tạo thư mục
        }

        File photoFile = new File(newDirectory, fileName);
        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Cre_db.call_insertPhotoData(selectedDetail, selectedDate, selectedDepartment, g_User, fileName);
                        runOnUiThread(() -> {
                            Toast.makeText(OpenCamera.this, "Photo saved: " + fileName, Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {

                    }
                });
    }

}