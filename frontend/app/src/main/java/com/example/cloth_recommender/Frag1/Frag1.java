package com.example.cloth_recommender.Frag1;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.content.Intent;
import android.net.Uri;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import com.example.cloth_recommender.Frag2.NewPostActivity;
import com.example.cloth_recommender.R;
import com.example.cloth_recommender.server.ApiClient;
import com.example.cloth_recommender.server.RetrofitAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//외부에서 new Frag1 호출 시
public class Frag1 extends Fragment {
    public static ArrayList<String> postIDList = new ArrayList<>();

    RecyclerView recyclerView;  // 이미지를 보여줄 리사이클러뷰
    public static com.example.cloth_recommender.Frag1.MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
    String strID;
    int imgindex;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag1,container,false);

        SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.frag1_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(){
                //retrofit api creation

                RetrofitAPI retrofitAPI2 = ApiClient.getClient().create(RetrofitAPI.class);
                Call<List<String>> callpostIDs = retrofitAPI2.getPostID();
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

        recyclerView = v.findViewById(R.id.recyclerView_frag1);
        adapter = new com.example.cloth_recommender.Frag1.MultiImageAdapter(postIDList, getActivity().getApplicationContext(), imgindex, imgarr);
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

        int imgindex = resultCode;

        RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);
        Call<List<String>> callpostIDs = retrofitAPI.getPostID();
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
        recyclerView = getActivity().findViewById(R.id.recyclerView_frag1);

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

        adapter = new com.example.cloth_recommender.Frag1.MultiImageAdapter(postIDList, getActivity().getApplicationContext(), imgindex, imgarr);
        recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
    }
}