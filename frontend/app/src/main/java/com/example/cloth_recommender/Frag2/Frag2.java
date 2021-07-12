package com.example.cloth_recommender.Frag2;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.example.cloth_recommender.R;
import com.example.cloth_recommender.server.ApiClient;
import com.example.cloth_recommender.server.RetrofitAPI;
import com.example.cloth_recommender.server.UserData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Frag2 extends Fragment {

    private static final String TAG = "MultiImageActivity";
    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    ArrayList<String> postIDList = new ArrayList<>();

    RecyclerView recyclerView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터



    public Frag2() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag2,container,false);


        Button btn_getImage = v.findViewById(R.id.getImage);
        btn_getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 200);
            }
        });

        Button btn_Newpostactivity = v.findViewById(R.id.NewPostActivity);
        btn_Newpostactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentnewpost = new Intent(v.getContext(), NewPostActivity.class);
                Intent intent = getActivity().getIntent();
                String strID = intent.getStringExtra("userid");
                intentnewpost.putExtra("userid", strID);
                startActivity(intentnewpost);
            }
        });

        //retrofit api creation
        RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);
        Call<List<String>> callpostIDs = retrofitAPI.getPostID();
        callpostIDs.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                postIDList.clear();
                ArrayList<String> newList = (ArrayList<String>) response.body();;
                postIDList.addAll(newList);
                adapter.notifyDataSetChanged();
                Log.d("postidlist", String.valueOf(postIDList.size()));
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
            }
        });

        recyclerView = v.findViewById(R.id.recyclerView);
        adapter = new MultiImageAdapter(postIDList, getActivity().getApplicationContext());
        recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 앨범으로 이동하는 버튼


    }

    @Override


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("single choice: ", String.valueOf(data.getData()));
        Uri imageUri = data.getData();
        uriList.add(imageUri);
    }
}