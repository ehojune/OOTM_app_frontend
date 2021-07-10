package com.example.cloth_recommender.Frag2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.cloth_recommender.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ViewPostActivity extends Activity {

    private ImageView img;
    private EditText cmt;
    private Button btn;
    private static final String TAG = "ViewPostActivity";
    private String mCurrentPhotoPath;

    PopupWindow popUp;
    boolean click = true;
    Bitmap bitmap;

    //AppDataBase_gallery db;
    String uri_string;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.frag2_activity_viewpostactivity);

        popUp = new PopupWindow(this);

        img = findViewById(R.id.image_big);
        //cmt = findViewById(R.id.comment);
        btn = findViewById(R.id.saveImage);



        try {
            uri_string = getIntent().getStringExtra("image");
            Log.d("look", "string"+uri_string);
            Uri uri = Uri.parse(uri_string);
            bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), uri));
            img.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        db = AppDataBase_gallery.getInstance(this);
        List<User_gallery> user_galleryList = db.userDao().getAll();


        Log.d("look", "bitmap came");

        for(User_gallery user_gallery : user_galleryList){
            if(user_gallery.getUri().equals(uri_string)){
                cmt.setText(user_gallery.getMemo());

                break;
            }
        }
*/
        btn.setOnClickListener(v -> {

            try {

                BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                //찍은 사진이 없으면
                if (bitmap == null) {
                    Toast.makeText(this, "저장할 사진이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    //저장
                    saveImg();
                    mCurrentPhotoPath = ""; //initialize
                }

            } catch (Exception e) {
              //  Log.w(TAG, "SAVE ERROR!", e);
            }
        });









        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;

        layoutParams.dimAmount = 0.7f;

        getWindow().setAttributes(layoutParams);



    }

    private void saveImg() {

        try {
            //저장할 파일 경로
            File storageDir = new File(getFilesDir() + "/capture");
            if (!storageDir.exists()) //폴더가 없으면 생성.
                storageDir.mkdirs();

            String filename = "캡쳐파일" + ".jpg";

            // 기존에 있다면 삭제
            File file = new File(storageDir, filename);
            boolean deleted = file.delete();
            Log.w(TAG, "Delete Dup Check : " + deleted);
            FileOutputStream output = null;

            try {
                output = new FileOutputStream(file);
                BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, output); //해상도에 맞추어 Compress
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);

                Log.d("look", ""+bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    assert output != null;
                    output.close();
                    Log.e(TAG, "Captured Saved");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Log.e(TAG, "Captured Saved");
            Toast.makeText(this, "Capture Saved ", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.w(TAG, "Capture Saving Error!", e);
            Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show();

        }
    }

}