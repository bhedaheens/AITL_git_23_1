package com.allintheloop.Activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.allintheloop.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FullScreenMapActivity extends AppCompatActivity implements SurfaceHolder.Callback {


    private Camera camera = null;
    private SurfaceView cameraSurfaceView = null;
    private SurfaceHolder cameraSurfaceHolder = null;
    private boolean previewing = false;
    RelativeLayout relativeLayout;

    private Button btnCapture = null;
    int intheight, intwidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_screen_map);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        intheight = displayMetrics.heightPixels;
        intwidth = displayMetrics.widthPixels;


        relativeLayout = (RelativeLayout) findViewById(R.id.containerImg);
        relativeLayout.setDrawingCacheEnabled(true);
        cameraSurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
        //  cameraSurfaceView.setLayoutParams(new FrameLayout.LayoutParams(640, 480));
        cameraSurfaceHolder = cameraSurfaceView.getHolder();
        cameraSurfaceHolder.addCallback(this);
        cameraSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        btnCapture = (Button) findViewById(R.id.button1);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                camera.takePicture(cameraShutterCallback,
                        cameraPictureCallbackRaw,
                        cameraPictureCallbackJpeg);
            }
        });

    }

    Camera.ShutterCallback cameraShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            // TODO Auto-generated method stub
        }
    };

    Camera.PictureCallback cameraPictureCallbackRaw = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
        }
    };

    Camera.PictureCallback cameraPictureCallbackJpeg = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            if (data != null) {
                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                int screenHeight = getResources().getDisplayMetrics().heightPixels;
                Bitmap bm = BitmapFactory.decodeByteArray(data, 0, (data != null) ? data.length : 0);
                Bitmap scaled = Bitmap.createScaledBitmap(bm, screenWidth, screenHeight, true);
                int w = scaled.getWidth();
                int h = scaled.getHeight();
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // Notice that width and height are reversed

                    // Setting post rotate to 90
                    Matrix mtx = new Matrix();
                    mtx.postRotate(90);
                    // Rotating Bitmap
                    bm = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
                    bm = Bitmap.createScaledBitmap(bm, screenWidth, screenHeight, true);
                } else {// LANDSCAPE MODE
                    //No need to reverse width and height
                    scaled = Bitmap.createScaledBitmap(bm, screenWidth, screenHeight, true);
                    bm = scaled;
                }

                Bitmap newImage = Bitmap.createBitmap
                        (w, h, Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(newImage);

                canvas.drawBitmap(bm, 0f, 0f, null);

                Drawable drawable = getResources().getDrawable
                        (R.drawable.test_frame);
                drawable = new ScaleDrawable(drawable, 0, w, h).getDrawable();
                drawable.setBounds(0, 0, w, h);
                drawable.draw(canvas);

                File storagePath = new File(Environment.
                        getExternalStorageDirectory() + "/PhotoAR/");
                storagePath.mkdirs();

                File myImage = new File(storagePath,
                        Long.toString(System.currentTimeMillis()) + ".jpg");

                try {
                    FileOutputStream out = new FileOutputStream(myImage);
                    newImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    Log.d("In Saving File", e + "");
                } catch (IOException e) {
                    Log.d("In Saving File", e + "");
                }

                camera.startPreview();
//                photoPreview.setImageBitmap(bm);
            }
//            isImageCaptured = true;
//            photoPreview.setVisibility(View.VISIBLE);
//            surfaceView.setVisibility(View.GONE);
        }
    };


    @Override
    public void surfaceChanged(SurfaceHolder holder,
                               int format, int width, int height) {
        // TODO Auto-generated method stub

        if (previewing) {
            camera.stopPreview();
            previewing = false;
        }
        try {

            Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewSize(intwidth, intheight);
            parameters.setPictureSize(intwidth, intheight);
            parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
            camera.setDisplayOrientation(90);
            camera.setParameters(parameters);

            camera.setPreviewDisplay(cameraSurfaceHolder);
            camera.startPreview();
            previewing = true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        try {
            camera = Camera.open();
            setCameraDisplayOrientation(FullScreenMapActivity.this, Camera.CameraInfo.CAMERA_FACING_BACK, camera);
        } catch (RuntimeException e) {
            Toast.makeText(getApplicationContext(), "Device camera  is not working properly, please try after sometime.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;
    }

}







