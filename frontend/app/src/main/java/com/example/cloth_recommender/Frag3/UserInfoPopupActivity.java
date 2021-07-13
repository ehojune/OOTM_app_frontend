package com.example.cloth_recommender.Frag3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.cloth_recommender.R;

public class UserInfoPopupActivity extends Activity{

    TextView name;
    TextView email;
    TextView age;
    TextView gender;
    TextView birthday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.frag3_activity_userinfopopupactivity);

        //UI 객체생성
        name = (TextView)findViewById(R.id.name);
        email = (TextView)findViewById(R.id.email);
        age = (TextView)findViewById(R.id.age);
        gender = (TextView)findViewById(R.id.gender);
        birthday = (TextView)findViewById(R.id.birthday);


        //데이터 가져오기
        Intent intent = getIntent();
        String names = intent.getStringExtra("name");
        String emails = intent.getStringExtra("email");
        String ages = intent.getStringExtra("age");
        String genders = intent.getStringExtra("gender");
        String birthdays = intent.getStringExtra("birthday");
        name.setText("Name: "+names);
        //email.setText("Email: "+emails);
        age.setText("Age: "+ages);
        gender.setText("Gender: "+genders);
        birthday.setText("Birthday: "+birthdays);
    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

}
