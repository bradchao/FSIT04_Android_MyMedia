package tw.org.iii.mymediatest;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private ImageView img;
    private File sdroot, picFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.img);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    0);
        }else{
            init();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init();
        }
    }

    private void init(){
        sdroot = Environment.getExternalStorageDirectory();
        picFile = new File(sdroot, "brad.jpg");
    }


    public void test1(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);
    }


    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case 0: afterTakePic(data); break;
                case 1: afterTakePic2(); break;
                case 2: afterRecord(data); break;
            }
        }

    }

    private void afterTakePic(Intent data){
        Bundle bundle = data.getExtras();
        Bitmap bmp = (Bitmap) bundle.get("data");

        img.setImageBitmap(bmp);
    }


    public void test2(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(picFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent,1);

    }

    private void afterTakePic2(){
        Bitmap bmp = BitmapFactory.decodeFile(
                picFile.getAbsolutePath() );
        img.setImageBitmap(bmp);
    }


    public void test3(View view) {
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent, 2);
    }

    private void afterRecord(Intent data){
        Uri uri = data.getData();

        Log.v("brad", uri.getPath());
        Log.v("brad", getRealPathFromUri(uri));
    }

    private String getRealPathFromUri(Uri contentUri){
        String[] fields = {MediaStore.Audio.Media.DATA};
        ContentResolver resolver = getContentResolver();
        Cursor cursor =resolver.query(
                contentUri, fields,
                null,null,null);
        cursor.moveToFirst();
        String filePath =
                cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

        return filePath;
    }


}
