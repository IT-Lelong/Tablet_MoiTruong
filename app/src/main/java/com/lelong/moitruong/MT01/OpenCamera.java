package com.lelong.moitruong.MT01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
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

        startCamera();
    }

    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindCameraUseCases(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindCameraUseCases(@NonNull ProcessCameraProvider cameraProvider) {
        preview = new Preview.Builder().build();
        imageCapture = new ImageCapture.Builder().build();

        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

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
