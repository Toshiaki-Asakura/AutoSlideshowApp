package jp.techacademy.toshiaki.asakura.autoslideshowapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.util.Log;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button mGoButton;
    Button mAutoButton;
    Button mBackButton;
    ImageView mImageView;
    Cursor mCursor;

    private static final int PERMISSIONS_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mGoButton = (Button) findViewById(R.id.buttonGo);
        mGoButton.setOnClickListener(this);
        Button mAutoButton = (Button) findViewById(R.id.buttonAuto);
        mAutoButton.setOnClickListener(this);
        Button mBackButton = (Button) findViewById(R.id.buttonBack);
        mBackButton.setOnClickListener(this);

        ContentResolver resolver = getContentResolver();
        Cursor mCursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo1();
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo1();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo1();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {

         if (v.getId() == R.id.buttonGo) {
            getContentsInfo2();
        } else if (v.getId() == R.id.buttonAuto) {
            getContentsInfo3();
        } else if (v.getId() == R.id.buttonBack) {
            getContentsInfo4();
        }
    }

    public void getContentsInfo1() {

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
        mCursor.close();
    }

    public void getContentsInfo2() {
        Log.d("asat", "【進む】ボタンは正常に動いています！");

        ContentResolver resolver = getContentResolver();
        mCursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

        if (mCursor!=null) {

            int fieldIndex = mCursor.getColumnIndex(MediaStore.Images.Media._ID);
            mCursor.moveToNext();
            Long id = mCursor.getLong(fieldIndex);
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            Log.d("asat", imageUri.toString());
            ImageView mImageView = (ImageView) findViewById(R.id.imageView);
            mImageView.setImageURI(imageUri);
        }   mCursor.close();
    }

    public void getContentsInfo3() {
        Log.d("asat", "【再生/停止】ボタンは正常に動いています！");
    }
    public void getContentsInfo4() {
        Log.d("asat", "【戻る】ボタンは正常に動いています！");
    }


}




