package jp.techacademy.toshiaki.asakura.autoslideshowapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button mGoButton;
    Button mAutoButton;
    Button mBackButton;
    ImageView mImageView;
    Cursor mCursor;
    Timer mTimer;

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo();
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.buttonGo) {
            getImageNext();
        } else if (v.getId() == R.id.buttonAuto) {
            getImageAuto();
        } else if (v.getId() == R.id.buttonBack) {
            getImagePreview();
        }
    }

    public void getContentsInfo() {

        Button mGoButton = (Button) findViewById(R.id.buttonGo);
        mGoButton.setOnClickListener(this);
        Button mAutoButton = (Button) findViewById(R.id.buttonAuto);
        mAutoButton.setOnClickListener(this);
        Button mBackButton = (Button) findViewById(R.id.buttonBack);
        mBackButton.setOnClickListener(this);

        ContentResolver resolver = getContentResolver();
        mCursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

        if (mCursor.moveToFirst()) {

            int fieldIndex = mCursor.getColumnIndex(MediaStore.Images.Media._ID);
            Long id = mCursor.getLong(fieldIndex);
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            Log.d("asat", imageUri.toString());
            ImageView mImageView = (ImageView) findViewById(R.id.imageView);
            mImageView.setImageURI(imageUri);
        }
    }

    public void getImageNext() {
        Log.d("asat", "【進む】ボタンは正常に動いています！");

        if (mCursor.moveToNext()) {
            int fieldIndex = mCursor.getColumnIndex(MediaStore.Images.Media._ID);
            Long id = mCursor.getLong(fieldIndex);
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            Log.d("asat", imageUri.toString());
            ImageView mImageView = (ImageView) findViewById(R.id.imageView);
            mImageView.setImageURI(imageUri);

        }else {

            mCursor.moveToFirst();
            int fieldIndex = mCursor.getColumnIndex(MediaStore.Images.Media._ID);
            Long id = mCursor.getLong(fieldIndex);
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            Log.d("asat", imageUri.toString());
            ImageView mImageView = (ImageView) findViewById(R.id.imageView);
            mImageView.setImageURI(imageUri);
        }
    }

    public void getImageAuto() {
        Log.d("asat", "【再生/停止】ボタンは正常に動いています！");
        Button mBackButton = (Button) findViewById(R.id.buttonBack);
        Button mGoButton = (Button) findViewById(R.id.buttonGo);

        if (mTimer == null){

            mGoButton.setEnabled(false);
            mGoButton.setTextColor(0xff888888);
            mBackButton.setEnabled(false);
            mBackButton.setTextColor(0xff888888);

            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {

                @Override
                public void run() {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mCursor.moveToNext()) {
                                int fieldIndex = mCursor.getColumnIndex(MediaStore.Images.Media._ID);
                                Long id = mCursor.getLong(fieldIndex);
                                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                                Log.d("asat", imageUri.toString());
                                ImageView mImageView = (ImageView) findViewById(R.id.imageView);
                                mImageView.setImageURI(imageUri);
                            }else {
                                mCursor.moveToFirst();
                                int fieldIndex = mCursor.getColumnIndex(MediaStore.Images.Media._ID);
                                Long id = mCursor.getLong(fieldIndex);
                                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                                Log.d("asat", imageUri.toString());
                                ImageView mImageView = (ImageView) findViewById(R.id.imageView);
                                mImageView.setImageURI(imageUri);
                            }
                        }
                    });
                }
            }, 300, 500);

        } else{

            mTimer.cancel();

            mGoButton.setEnabled(true);
            mGoButton.setTextColor(0xff000000);
            mBackButton.setEnabled(true);
            mBackButton.setTextColor(0xff000000);

            mTimer = null;
        }

    }
    public void getImagePreview() {
        Log.d("asat", "【戻る】ボタンは正常に動いています！");
        if (mCursor.moveToPrevious()) {
            int fieldIndex = mCursor.getColumnIndex(MediaStore.Images.Media._ID);
            Long id = mCursor.getLong(fieldIndex);
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            Log.d("asat", imageUri.toString());
            ImageView mImageView = (ImageView) findViewById(R.id.imageView);
            mImageView.setImageURI(imageUri);

        }else {

            mCursor.moveToLast();
            int fieldIndex = mCursor.getColumnIndex(MediaStore.Images.Media._ID);
            Long id = mCursor.getLong(fieldIndex);
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            Log.d("asat", imageUri.toString());
            ImageView mImageView = (ImageView) findViewById(R.id.imageView);
            mImageView.setImageURI(imageUri);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCursor != null) {
            mCursor.close();
        }
    }
}