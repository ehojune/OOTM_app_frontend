package com.example.cloth_recommender.Frag2;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.cloth_recommender.R;
import com.example.cloth_recommender.server.ApiClient;
import com.example.cloth_recommender.server.RetrofitAPI;
import com.example.cloth_recommender.server.UserData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPostActivity extends AppCompatActivity {

    private static final String TAG = "MultiImageActivity";
    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체

    //RecyclerView recyclerView;  // 이미지를 보여줄 리사이클러뷰
    //MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
    Uri imageUri;
    ImageView getImage;
    ImageView getImage2;
    ImageView getImage3;
    private ImageButton btn_back;
    UserData userinfo;
    Button saveButton;

    private EditText HeightInput;
    private EditText WeightInput;
    private EditText TopInput;
    private EditText BottomInput;
    private EditText ShoeInput;
    private EditText OuterInput;
    private EditText AccInput;

    private CheckBox Minimal;
    private CheckBox Casual;
    private CheckBox Street;
    private CheckBox Amekaji;
    private CheckBox Cityboy;



    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag2_activity_newpostactivity);

        //retrofit api creation
        RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);

        //post할 때 user 정보
        Intent intentfrag2 = getIntent();
        String strID = intentfrag2.getStringExtra("userid");
        Log.d("strID", strID);

        Call<UserData> calluser = retrofitAPI.getUser(strID);

        calluser.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                userinfo = response.body();
                Log.d("userinfo", String.valueOf(userinfo.userName));
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Log.d("userinfo", "fail");
            }
        });





        getImage = this.findViewById(R.id.Uploadimg1);
        getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 201);
            }
        });


        getImage2 = this.findViewById(R.id.Uploadimg2);
        getImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(Intent.ACTION_PICK);
                intent2.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent2.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent2.setAction(Intent.ACTION_GET_CONTENT);
                intent2.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent2, 202);
            }
        });

        getImage3 = this.findViewById(R.id.Uploadimg3);
        getImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(Intent.ACTION_PICK);
                intent3.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent3.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent3.setAction(Intent.ACTION_GET_CONTENT);
                intent3.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent3, 203);
            }
        });


        btn_back = this.findViewById(R.id.Back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 앨범으로 이동하는 버튼

        saveButton = this.findViewById(R.id.savepost_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeightInput = findViewById(R.id.HeightInfo);
                String HeightInfo = HeightInput.getText().toString();
                Log.d("Input", HeightInfo);
                WeightInput = findViewById(R.id.WeightInfo);
                String WeightInfo = WeightInput.getText().toString();
                OuterInput = findViewById(R.id.OuterInfo);
                String OuterInfo = OuterInput.getText().toString();
                Log.d("Input", OuterInfo);
                TopInput = findViewById(R.id.TopInfo);
                String TopInfo = TopInput.getText().toString();
                BottomInput = findViewById(R.id.BottomInfo);
                String BottomInfo = BottomInput.getText().toString();
                AccInput = findViewById(R.id.AccInfo);
                String AccInfo = AccInput.getText().toString();
                ShoeInput = findViewById(R.id.ShoeInfo);
                String ShoeInfo = ShoeInput.getText().toString();

                ArrayList<String> GenreArray = new ArrayList<>();
                Minimal = findViewById(R.id.Minimal);
                if (((CheckBox) Minimal).isChecked()){
                    GenreArray.add("Minimal");
                }
                Casual = findViewById(R.id.Casual);
                if (((CheckBox) Casual).isChecked()){
                    GenreArray.add("Casual");
                }
                Street = findViewById(R.id.Street);
                if (((CheckBox) Casual).isChecked()){
                    GenreArray.add("Street");
                }
                Amekaji = findViewById(R.id.Amekaji);
                if (((CheckBox) Amekaji).isChecked()){
                    GenreArray.add("Amekaji");
                }
                Cityboy = findViewById(R.id.Cityboy);
                if (((CheckBox) Cityboy).isChecked()){
                    GenreArray.add("Cityboy");
                }
                String Genre = "";
                for (String s : GenreArray)
                {
                    Genre += s + "_";
                }


                //postDB에 정보추가
                HashMap<String,String> postmap = new HashMap<>();
                postmap.put("userName", userinfo.userName);
                postmap.put("userID", strID);
                postmap.put("height", HeightInfo);
                postmap.put("weight", WeightInfo);
                postmap.put("top", TopInfo);
                postmap.put("bot", BottomInfo);
                postmap.put("sho", ShoeInfo);
                postmap.put("out", OuterInfo);
                postmap.put("acc", AccInfo);
                postmap.put("genrearray", Genre.substring(0, Genre.length()-1));
                Log.d("GenreArray", Genre.substring(0, Genre.length()-1));
                Call<Void> calladdpost = retrofitAPI.addPost(postmap);
                calladdpost.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                    }
                });
                finish();

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(201, resultCode, data);

        if(data == null){   // 어떤 이미지도 선택하지 않은 경우
            Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
        }
        else{   // 이미지를 하나라도 선택한 경우
            if(data.getClipData() == null){     // 이미지를 하나만 선택한 경우
                Log.e("single choice: ", String.valueOf(data.getData()));
                imageUri = data.getData();
                getImage.setImageURI(imageUri);
/*
                adapter = new MultiImageAdapter(uriList, getApplicationContext());
                recyclerView.setAdapter(adapter);
                //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
                recyclerView.setLayoutManager(gridLayoutManager);

 */
            }
 /*
            else{      // 이미지를 여러장 선택한 경우
                ClipData clipData = data.getClipData();
                Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if(clipData.getItemCount() > 20){   // 선택한 이미지가 21장 이상인 경우
                    Toast.makeText(getApplicationContext(), "사진은 20장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                }
                else{   // 선택한 이미지가 1장 이상 20장 이하인 경우
                    Log.e(TAG, "multiple choice");

                    for (int i = 0; i < clipData.getItemCount(); i++){
                        Uri imageUri = clipData.getItemAt(i).getUri();  // 선택한 이미지들의 uri를 가져온다.
                        try {
                            uriList.add(imageUri);  //uri를 list에 담는다.

                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }

                    adapter = new MultiImageAdapter(uriList, getApplicationContext());
                    recyclerView    .setAdapter(adapter);   // 리사이클러뷰에 어댑터 세팅
                    //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));     // 리사이클러뷰 수평 스크롤 적용
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
                    recyclerView.setLayoutManager(gridLayoutManager);
*/



        }
    }
/*
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){   // 어떤 이미지도 선택하지 않은 경우
            Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
        }
        else{   // 이미지를 하나라도 선택한 경우
            if(data.getClipData() == null){     // 이미지를 하나만 선택한 경우
                Log.e("single choice: ", String.valueOf(data.getData()));
                imageUri = data.getData();
                getImage.setImageURI(imageUri);
            }
        }
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(202, resultCode, data);

        if(data == null){   // 어떤 이미지도 선택하지 않은 경우
            Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
        }
        else{
            if(data.getClipData() == null){     // 이미지를 하나만 선택한 경우
                Log.e("single choice: ", String.valueOf(data.getData()));
                imageUri = data.getData();
                getImage.setImageURI(imageUri);
            }
        }
    }
 */
}


