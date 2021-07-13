package com.example.cloth_recommender.Frag3;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.cloth_recommender.LoginActivity;
import com.example.cloth_recommender.R;
import com.example.cloth_recommender.server.ApiClient;
import com.example.cloth_recommender.server.RetrofitAPI;
import com.example.cloth_recommender.server.UserData;
import com.kakao.network.ApiErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewActivity extends Fragment {

    String strNickname;
    String strProfile;
    String strID;
    String strEmail, strAgeRange, strGender, strBirthday;

    PopupWindow popUp;


    List<UserData> forTest;
    public static ArrayList<String> postIDList = new ArrayList<>();
    RecyclerView recyclerView;  // 이미지를 보여줄 리사이클러뷰
    public static com.example.cloth_recommender.Frag3.MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
    int imgindex;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag3_loginview,container, false);

        SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.frag3_refresh_layout);
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

        RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);








        TextView tvNickname = v.findViewById(R.id.tvNickname);
        ImageView ivProfile = v.findViewById(R.id.ivProfile);
        Button btnLogout = v.findViewById(R.id.btnLogout);
        Button btnSignout = v.findViewById(R.id.btnSignout);

        //TextView tvEmail = v.findViewById(R.id.tvEmail);
        //TextView tvAgeRange = v.findViewById(R.id.tvAgeRange);
        //TextView tvGender = v.findViewById(R.id.tvGender);
        //TextView tvBirthday = v.findViewById(R.id.tvBirthday);

        Intent intent = getActivity().getIntent();
        strNickname = intent.getStringExtra("name");
        strProfile = intent.getStringExtra("profile");
        strID = intent.getStringExtra("userid");

        HashMap<String,String> map = new HashMap<>();
        map.put("username", strNickname);
        map.put("userID", strID);
        map.put("userProfile", strProfile);

        Call<Void> call2 = retrofitAPI.putNewUser(map);
        call2.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });

        //strEmail = intent.getStringExtra("email");
        strAgeRange = intent.getStringExtra("ageRange");
        strGender = intent.getStringExtra("gender");
        strBirthday = intent.getStringExtra("birthday");


        Intent popintent = new Intent(getContext(), UserInfoPopupActivity.class);
        Button btn_DetailInfo_popup = v.findViewById(R.id.btnDetailInfo);
        btn_DetailInfo_popup.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                popintent.putExtra("name", strNickname);
                //popintent.putExtra("email", strEmail);
                popintent.putExtra("age", strAgeRange);
                popintent.putExtra("gender", strGender);
                popintent.putExtra("birthday", strBirthday);
                startActivity(popintent);
            }
        });



        tvNickname.setText(strNickname);
        Glide.with(getActivity()).load(strProfile).circleCrop().into(ivProfile);

        //tvEmail.setText(strEmail);
        //tvAgeRange.setText(strAgeRange);
        //tvGender.setText(strGender);
        //tvBirthday.setText(strBirthday);






        btnLogout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });

        btnSignout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("탈퇴하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {
                                        int result = errorResult.getErrorCode();

                                        if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                                            Toast.makeText(getActivity().getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity().getApplicationContext(), "회원탈퇴에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) {
                                        Toast.makeText(getActivity().getApplicationContext(), "로그인 세션이 닫혔습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }

                                    @Override
                                    public void onNotSignedUp() {
                                        Toast.makeText(getActivity().getApplicationContext(), "가입되지 않은 계정입니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }

                                    @Override
                                    public void onSuccess(Long result) {
                                        HashMap<String,String> useridmap = new HashMap<>();
                                        useridmap.put("userID", strID);
                                        Call<Void> deleteCall = retrofitAPI.deleteUser(useridmap);
                                        deleteCall.enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                            }
                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                            }
                                        });

                                        Toast.makeText(getActivity().getApplicationContext(), "회원탈퇴에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                });

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });


        // Frag2에서 그대로 가져옴
//retrofit api creation
        //RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);
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

        recyclerView = v.findViewById(R.id.recyclerView3);
        adapter = new com.example.cloth_recommender.Frag3.MultiImageAdapter(postIDList, getActivity().getApplicationContext(), imgindex, imgarr);
        recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        return v;

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
        recyclerView = getActivity().findViewById(R.id.recyclerView3);

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

        adapter = new com.example.cloth_recommender.Frag3.MultiImageAdapter(postIDList, getActivity().getApplicationContext(), imgindex, imgarr);
        recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
    }
}