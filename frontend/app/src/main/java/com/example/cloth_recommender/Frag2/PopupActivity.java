package com.example.cloth_recommender.Frag2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.cloth_recommender.R;

public class PopupActivity extends Activity {

    TextView top;
    TextView bot;
    TextView sho;
    TextView acc;
    TextView out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.frag2_popup);

        //UI 객체생성
        out = (TextView)findViewById(R.id.name);
        top = (TextView)findViewById(R.id.email);
        bot = (TextView)findViewById(R.id.age);
        acc = (TextView)findViewById(R.id.gender);
        sho = (TextView)findViewById(R.id.birthday);


        //데이터 가져오기
        Intent intent = getIntent();
        String tops = intent.getStringExtra("top");
        String bots = intent.getStringExtra("bot");
        String outs = intent.getStringExtra("out");
        String accs = intent.getStringExtra("acc");
        String shos = intent.getStringExtra("sho");
        top.setText("Top: "+tops);
        bot.setText("Bottom: "+bots);
        out.setText("Outer: "+outs);
        acc.setText("Accessory: "+accs);
        sho.setText("Shoes: "+shos);
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
