package com.example.cloth_recommender.Frag2;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.example.cloth_recommender.App;
import com.example.cloth_recommender.Frag3.LoginViewActivity;
import com.example.cloth_recommender.R;
import com.example.cloth_recommender.server.ApiClient;
import com.example.cloth_recommender.server.RetrofitAPI;
import com.example.cloth_recommender.server.UserData;
import com.example.cloth_recommender.server.postInfo;
import com.kakao.sdk.user.UserApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPostActivity extends Activity {

    private ImageView img;
    private ImageView userimg;
    private EditText cmt;
    private ImageButton btn;
    private static final String TAG = "ViewPostActivity";
    private String mCurrentPhotoPath;

    PopupWindow popUp;
    TextView postuser;
    TextView userbody;
    TextView genre;
    TextView date;
    postInfo postinfo;
    ImageButton deleteBtn;
    CheckBox wishbtn;
    CheckBox markbtn;
    ArrayList<String> likeIDs;
    ArrayList<String> MarkIDs;
    UserData userinfo;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.frag2_activity_viewpostactivity);
        popUp = new PopupWindow(this);
        img = findViewById(R.id.postimage);
        btn = findViewById(R.id.saveImage);
        postuser = findViewById(R.id.postuser);
        userbody = findViewById(R.id.userbody);
        genre = findViewById(R.id.genre);
        deleteBtn = findViewById(R.id.deleteBtn);
        wishbtn = findViewById(R.id.wishBtn);
        markbtn = findViewById(R.id.markbtn);
        date = findViewById(R.id.datetext);
        userimg = findViewById(R.id.userimage);
        RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);





        Intent intent = getIntent();
        String postID = intent.getStringExtra("postID");
        String userid;




        //retrofit api creation

        Call<postInfo> callpost = retrofitAPI.getPost(postID);
        callpost.enqueue(new Callback<postInfo>() {
            @Override
            public void onResponse(Call<postInfo> call, Response<postInfo> response) {
                postinfo = response.body();
                img.setImageResource(R.drawable.app_logo);
                postuser.setText(postinfo.userName);
                userbody.setText( postinfo.userBody.height +"cm - " + postinfo.userBody.weight+ "kg");
                genre.setText(postinfo.postgenre);
                date.setText(postinfo.date);

                App appState = ((App)getApplicationContext());

                //post할 때 user 정보
                Call<UserData> calluser = retrofitAPI.getUser(postinfo.userID);
                calluser.enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {
                        userinfo = response.body();
                        //Log.d("userinfo", String.valueOf(userinfo.userName));
                        Glide.with(userimg).load(userinfo.userProfile).circleCrop().into(userimg);
                    }
                    @Override
                    public void onFailure(Call<UserData> call, Throwable t) {
                        Log.d("userinfo", "fail");
                    }
                });


                Intent popintent = new Intent(getApplicationContext(), PopupActivity.class);
                ImageButton itempopup = findViewById(R.id.itempopup1);
                itempopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popintent.putExtra("top", postinfo.clothInfo.top);
                        popintent.putExtra("bot", postinfo.clothInfo.bot);
                        popintent.putExtra("out", postinfo.clothInfo.out);
                        popintent.putExtra("acc", postinfo.clothInfo.acc);
                        popintent.putExtra("sho", postinfo.clothInfo.sho);
                        startActivity(popintent);
                    }
                });

                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String,String> postidmap = new HashMap<>();
                        postidmap.put("_id", postID);
                        Call<Void> deleteCall = retrofitAPI.deletePost(postidmap);
                        deleteCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);
                                Call<List<String>> callpostIDs = retrofitAPI.getPostID();
                                callpostIDs.enqueue(new Callback<List<String>>() {
                                    @Override
                                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                                        Frag2.postIDList.clear();
                                        ArrayList<String> newList = (ArrayList<String>) response.body();;
                                        Frag2.postIDList.addAll(newList);
                                        Frag2.adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(Call<List<String>> call, Throwable t) {
                                    }
                                });
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                            }
                        });
                        finish();
                    }
                });


                RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);
                Call<List<String>> callwishlist = retrofitAPI.getLike(postID);
                callwishlist.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        likeIDs = (ArrayList<String>) response.body();
                        Log.d("likeIDs", likeIDs.toString());
                        if(likeIDs.contains(appState.getState())){
                            wishbtn.setChecked(true);
                        }
                        else{
                            wishbtn.setChecked(false);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                    }
                });

                Call<List<String>> callmarklist = retrofitAPI.getMark(postID);
                callmarklist.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        MarkIDs = (ArrayList<String>) response.body();
                        if(MarkIDs.contains(appState.getState())){
                            markbtn.setChecked(true);
                        }
                        else{
                            markbtn.setChecked(false);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                    }
                });


                wishbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Log.d("click", "click");
                        HashMap<String,String> idmap = new HashMap<>();
                        idmap.put("postid", postID);

                        idmap.put("userid", appState.getState());

                        Call<Void> setLikecall = retrofitAPI.setLike(idmap);
                        setLikecall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Log.d("click", "succ");
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.d("click", "fail");
                            }
                        });
                    }
                });
                markbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        HashMap<String,String> idmap = new HashMap<>();
                        idmap.put("postid", postID);


                        idmap.put("userid", appState.getState());

                        Call<Void> setMarkcall = retrofitAPI.setMark(idmap);
                        setMarkcall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                            }
                        });
                    }
                });

            }
            @Override
            public void onFailure(Call<postInfo> call, Throwable t) {
                Log.d("postinfo1", "fail");
            }
        });







        //저장기능
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