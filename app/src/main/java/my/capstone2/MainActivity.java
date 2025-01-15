package my.capstone2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText metnameInupt1;
    EditText metnameInupt2;
    EditText metnameInupt3;
    EditText medrink1;
    EditText medrink2;
    EditText medrink3;
    Button mbtbutton1;
    Button mbtbutton2;
    Button mbtbutton3;
    String text1;
    String text2;
    String text3;
    String drink1;
    String drink2;
    String drink3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        metnameInupt1 = findViewById(R.id.nameInput1);
        metnameInupt2 = findViewById(R.id.nameInput2);
        metnameInupt3 = findViewById(R.id.nameInput3);
        medrink1 = findViewById(R.id.drink1);
        medrink2 = findViewById(R.id.drink2);
        medrink3 = findViewById(R.id.drink3);
        mbtbutton1 = findViewById(R.id.button1);
        mbtbutton2 = findViewById(R.id.button2);
        mbtbutton3 = findViewById(R.id.button3);


        mbtbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text1 = metnameInupt1.getText().toString();
                drink1 = medrink1.getText().toString();
                drink2 = medrink2.getText().toString();
                drink3 = medrink3.getText().toString();

                if(text1.length() != 0)
                    mbtbutton1.setText(text1);
                else
                    mbtbutton1.setText("칵테일 이름1");
                Intent intent = new Intent(MainActivity.this, Cocktail1.class);
                intent.putExtra("drink1", drink1);
                intent.putExtra("drink2", drink2);
                intent.putExtra("drink3", drink3);
                startActivity(intent);
            }
        });

        mbtbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text2 = metnameInupt2.getText().toString();
                drink1 = medrink1.getText().toString();
                drink2 = medrink2.getText().toString();
                drink3 = medrink3.getText().toString();

                if(text2.length() != 0)
                    mbtbutton2.setText(text2);
                else
                    mbtbutton2.setText("칵테일 이름2");
                Intent intent = new Intent(MainActivity.this, Cocktail2.class);
                intent.putExtra("drink1", drink1);
                intent.putExtra("drink2", drink2);
                intent.putExtra("drink3", drink3);
                startActivity(intent);
            }
        });

        mbtbutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text3 = metnameInupt3.getText().toString();
                drink1 = medrink1.getText().toString();
                drink2 = medrink2.getText().toString();
                drink3 = medrink3.getText().toString();

                if(text3.length() != 0)
                    mbtbutton3.setText(text3);
                else
                    mbtbutton3.setText("칵테일 이름3");
                Intent intent = new Intent(MainActivity.this, Cocktail3.class);
                intent.putExtra("drink1", drink1);
                intent.putExtra("drink2", drink2);
                intent.putExtra("drink3", drink3);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() { // Activity가 보이지 않을때 값을 저장한다.
        super.onPause();
        saveState();
    }

    @Override
    protected void onStart() {  // Activity가 보이기 시작할때 값을 저장한다.
        super.onStart();
        restoreState();
        if(text1 != null) {
            mbtbutton1.setText(text1);
            metnameInupt1.setText(text1);
        }
        if(text2 != null) {
            mbtbutton2.setText(text2);
            metnameInupt2.setText(text2);
        }
        if(text3 != null) {
            mbtbutton3.setText(text3);
            metnameInupt3.setText(text3);
        }
        if(drink1 != null)
            medrink1.setText(drink1);
        if(drink2 != null)
            medrink2.setText(drink2);
        if(drink3 != null)
            medrink3.setText(drink3);
    }

    protected void saveState(){ // 데이터를 저장한다.
        SharedPreferences pref1 = getSharedPreferences("pref1", Activity.MODE_PRIVATE);
        SharedPreferences pref2 = getSharedPreferences("pref2", Activity.MODE_PRIVATE);
        SharedPreferences pref3 = getSharedPreferences("pref3", Activity.MODE_PRIVATE);
        SharedPreferences pref4 = getSharedPreferences("pref4", Activity.MODE_PRIVATE);
        SharedPreferences pref5 = getSharedPreferences("pref5", Activity.MODE_PRIVATE);
        SharedPreferences pref6 = getSharedPreferences("pref6", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = pref1.edit();
        SharedPreferences.Editor editor2 = pref2.edit();
        SharedPreferences.Editor editor3 = pref3.edit();
        SharedPreferences.Editor editor4 = pref4.edit();
        SharedPreferences.Editor editor5 = pref5.edit();
        SharedPreferences.Editor editor6 = pref6.edit();
        editor1.putString("text1", text1);
        editor2.putString("text2", text2);
        editor3.putString("text3", text3);
        editor4.putString("drink1", drink1);
        editor5.putString("drink2", drink2);
        editor6.putString("drink3", drink3);

        editor1.commit();
        editor2.commit();
        editor3.commit();
        editor4.commit();
        editor5.commit();
        editor6.commit();
    }
    protected void restoreState(){  // 데이터를 복구한다.
        SharedPreferences pref1 = getSharedPreferences("pref1", Activity.MODE_PRIVATE);
        SharedPreferences pref2 = getSharedPreferences("pref2", Activity.MODE_PRIVATE);
        SharedPreferences pref3 = getSharedPreferences("pref3", Activity.MODE_PRIVATE);
        SharedPreferences pref4 = getSharedPreferences("pref4", Activity.MODE_PRIVATE);
        SharedPreferences pref5 = getSharedPreferences("pref5", Activity.MODE_PRIVATE);
        SharedPreferences pref6 = getSharedPreferences("pref6", Activity.MODE_PRIVATE);

        if ((pref1 != null) && (pref1.contains("text1")))
            text1 = pref1.getString("text1", "");
        if ((pref2 != null) && (pref2.contains("text2")))
            text2 = pref2.getString("text2", "");
        if ((pref3 != null) && (pref3.contains("text3")))
            text3 = pref3.getString("text3", "");
        if ((pref4 != null) && (pref4.contains("drink1")))
            drink1 = pref4.getString("drink1", "");
        if ((pref5 != null) && (pref5.contains("drink2")))
            drink2 = pref5.getString("drink2", "");
        if ((pref6 != null) && (pref6.contains("drink3")))
            drink3 = pref6.getString("drink3", "");
    }
    protected void clearPref(){  // sharedpreference에 쓰여진 데이터 지우기
        SharedPreferences pref1 = getSharedPreferences("pref1", Activity.MODE_PRIVATE);
        SharedPreferences pref2 = getSharedPreferences("pref2", Activity.MODE_PRIVATE);
        SharedPreferences pref3 = getSharedPreferences("pref3", Activity.MODE_PRIVATE);
        SharedPreferences pref4 = getSharedPreferences("pref4", Activity.MODE_PRIVATE);
        SharedPreferences pref5 = getSharedPreferences("pref5", Activity.MODE_PRIVATE);
        SharedPreferences pref6 = getSharedPreferences("pref6", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = pref1.edit();
        SharedPreferences.Editor editor2 = pref2.edit();
        SharedPreferences.Editor editor3 = pref3.edit();
        SharedPreferences.Editor editor4 = pref4.edit();
        SharedPreferences.Editor editor5 = pref5.edit();
        SharedPreferences.Editor editor6 = pref6.edit();
        editor1.clear();
        editor2.clear();
        editor3.clear();
        editor4.clear();
        editor5.clear();
        editor6.clear();
        text1 = null;
        text2 = null;
        text3 = null;
        drink1 = null;
        drink2 = null;
        drink3 = null;
        editor1.commit();
        editor2.commit();
        editor3.commit();
        editor4.commit();
        editor5.commit();
        editor6.commit();
    }
}