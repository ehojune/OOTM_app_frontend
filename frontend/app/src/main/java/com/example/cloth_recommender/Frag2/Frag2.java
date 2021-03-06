package com.example.cloth_recommender.Frag2;
import android.graphics.drawable.Drawable;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.cloth_recommender.R;
import com.example.cloth_recommender.server.ApiClient;
import com.example.cloth_recommender.server.RetrofitAPI;
import com.example.cloth_recommender.server.UserData;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Frag2 extends Fragment {

    private static final String TAG = "MultiImageActivity";
    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    public static ArrayList<String> postIDList = new ArrayList<>();
    public static ArrayList<String> hotList = new ArrayList<>();
    public static ArrayList<String> newList = new ArrayList<>();

    RecyclerView recyclerView;  // 이미지를 보여줄 리사이클러뷰
    public static MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
    String strID;
    int imgindex;

    public Frag2() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag2,container,false);
        RetrofitAPI retrofitAPI2 = ApiClient.getClient().create(RetrofitAPI.class);
        Chip newchip = v.findViewById(R.id.newchip);
        Chip hotchip = v.findViewById(R.id.hotchip);
        newchip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                hotchip.setChecked(!(newchip.isChecked()));

                Call<List<String>> callpostIDs = retrofitAPI2.getPostID();
                callpostIDs.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        postIDList.clear();
                        hotList = (ArrayList<String>) response.body();;
                        postIDList.addAll(hotList);
                        adapter.notifyDataSetChanged();
                        Log.d("getpostid", "succ2");
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Log.d("getpostid", "fail2");
                    }
                });
            }
        });
        hotchip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                newchip.setChecked(!(hotchip.isChecked()));
                Call<List<String>> callpostIDs = retrofitAPI2.getPostID_hot();
                callpostIDs.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        postIDList.clear();
                        newList = (ArrayList<String>) response.body();;
                        Log.d("getpostid", response.body().get(0));
                        postIDList.addAll(newList);
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Log.d("getpostid", "fail");
                    }
                });

            }
        });

        SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.frag2_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(){
                //retrofit api creation
                RetrofitAPI retrofitAPI2 = ApiClient.getClient().create(RetrofitAPI.class);
                Call<List<String>> callpostIDs;
                if(hotchip.isChecked()){
                    callpostIDs = retrofitAPI2.getPostID_hot();
                }
                else{
                    callpostIDs = retrofitAPI2.getPostID();
                }
                callpostIDs.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        postIDList.clear();
                        ArrayList<String> newList = (ArrayList<String>) response.body();;
                        postIDList.addAll(newList);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                    }
                });
                mSwipeRefreshLayout.setRefreshing(false);
            };
        });

        FloatingActionButton btn_Newpostactivity = v.findViewById(R.id.NewPostActivity2);
        btn_Newpostactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentnewpost = new Intent(v.getContext(), NewPostActivity.class);
                Intent intent = getActivity().getIntent();
                strID = intent.getStringExtra("userid");
                intentnewpost.putExtra("userid", strID);
                startActivityForResult(intentnewpost, 1);

            }
        });


        //retrofit api creation
        RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);
        Call<List<String>> callpostIDs;
        if(hotchip.isChecked()){
            callpostIDs = retrofitAPI.getPostID_hot();
        }
        else{
            callpostIDs = retrofitAPI.getPostID();
        }
        callpostIDs.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                postIDList.clear();
                ArrayList<String> newList = (ArrayList<String>) response.body();;
                postIDList.addAll(newList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
            }
        });

        ArrayList<Drawable> imgarr = new ArrayList<Drawable>();
        imgarr.add(getResources().getDrawable(R.drawable.outfit1));
        imgarr.add(getResources().getDrawable(R.drawable.outfit2));
        imgarr.add(getResources().getDrawable(R.drawable.outfit3));
        imgarr.add(getResources().getDrawable(R.drawable.outfit4));
        imgarr.add(getResources().getDrawable(R.drawable.outfit5));
        imgarr.add(getResources().getDrawable(R.drawable.outfit6));
        imgarr.add(getResources().getDrawable(R.drawable.outfit7));
        imgarr.add(getResources().getDrawable(R.drawable.outfit8));
        imgarr.add(getResources().getDrawable(R.drawable.outfit9));
        imgarr.add(getResources().getDrawable(R.drawable.clothicon));

        recyclerView = v.findViewById(R.id.recyclerView);
        adapter = new MultiImageAdapter(postIDList, getActivity().getApplicationContext(), imgindex, imgarr);
        recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int imgindex = resultCode;

        Chip newchip = getActivity().findViewById(R.id.newchip);
        Chip hotchip = getActivity().findViewById(R.id.hotchip);
        RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);
        Call<List<String>> callpostIDs;
        if(hotchip.isChecked()){
            callpostIDs = retrofitAPI.getPostID_hot();
        }
        else{
            callpostIDs = retrofitAPI.getPostID();
        }
        callpostIDs.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                postIDList.clear();
                ArrayList<String> newList = (ArrayList<String>) response.body();;
                postIDList.addAll(newList);
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.d("aaaaaa","fail");
            }
        });
        recyclerView = getActivity().findViewById(R.id.recyclerView);

        ArrayList<Drawable> imgarr = new ArrayList<Drawable>();
        imgarr.add(getResources().getDrawable(R.drawable.outfit1));
        imgarr.add(getResources().getDrawable(R.drawable.outfit2));
        imgarr.add(getResources().getDrawable(R.drawable.outfit3));
        imgarr.add(getResources().getDrawable(R.drawable.outfit4));
        imgarr.add(getResources().getDrawable(R.drawable.outfit5));
        imgarr.add(getResources().getDrawable(R.drawable.outfit6));
        imgarr.add(getResources().getDrawable(R.drawable.outfit7));
        imgarr.add(getResources().getDrawable(R.drawable.outfit8));
        imgarr.add(getResources().getDrawable(R.drawable.outfit9));
        imgarr.add(getResources().getDrawable(R.drawable.clothicon));

        adapter = new MultiImageAdapter(postIDList, getActivity().getApplicationContext(), imgindex, imgarr);
        recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

}
