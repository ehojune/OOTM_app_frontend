package com.example.cloth_recommender.Frag2;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cloth_recommender.R;
import com.example.cloth_recommender.server.ApiClient;
import com.example.cloth_recommender.server.RetrofitAPI;
import com.example.cloth_recommender.server.UserData;
import com.example.cloth_recommender.server.postInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MultiImageAdapter extends RecyclerView.Adapter<MultiImageAdapter.ViewHolder>{
    private ArrayList<String> mData = null ;
    private Context mContext = null ;
    int mImgindex;
    ArrayList<Drawable> mImgarr;
    boolean isImageFitToScreen;
    // 생성자에서 데이터 리스트 객체, Context를 전달받음.
    public MultiImageAdapter(ArrayList<String> list, Context context, int imgindex, ArrayList<Drawable> imgarr) {

        mData = list ;
        mContext = context;
        mImgindex = imgindex;
        mImgarr = imgarr;
    }


    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView marktext;
        ViewHolder(View itemView) {
            super(itemView) ;
            // 뷰 객체에 대한 참조.
            image = itemView.findViewById(R.id.galleryimage2);
            marktext = itemView.findViewById(R.id.totalmarknum);

        }
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    // LayoutInflater - XML에 정의된 Resource(자원) 들을 View의 형태로 반환.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;    // context에서 LayoutInflater 객체를 얻는다.
        View view = inflater.inflate(R.layout.frag2_multi_image_item, parent, false) ;	// 리사이클러뷰에 들어갈 아이템뷰의 레이아웃을 inflate.
        ViewHolder vh = new ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String postID = mData.get(position) ;

        /**
        Glide.with(mContext)
                .load("https://cdn.pixabay.com/photo/2019/12/26/10/44/horse-4720178_1280.jpg")
                .into(holder.image);*/


        //retrofit api creation
        RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);
        //post할 때 user 정보
        Call<postInfo> calluser = retrofitAPI.getPost(postID);
        calluser.enqueue(new Callback<postInfo>() {
            @Override
            public void onResponse(Call<postInfo> call, Response<postInfo> response) {
                mImgindex= Integer.parseInt(response.body().postImage);
                holder.image.setImageDrawable(mImgarr.get(mImgindex));
                holder.marktext.setText(Integer.toString(response.body().markUsers.size()));

            }
            @Override
            public void onFailure(Call<postInfo> call, Throwable t) {
                Log.d("userinfo", "fail");
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewPostActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //intent.putExtra("image", image_uri.toString());
                intent.putExtra("postID", postID);
                //postID를 전달해주는게필요함
                v.getContext().startActivity(intent);

            }
        });
    }
    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }

}