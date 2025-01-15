package my.capstone2;

import static my.capstone2.InitialActivity.mContext;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class Cocktail3 extends AppCompatActivity {
    EditText amount;
    EditText rate1;
    EditText rate2;
    EditText rate3;
    Button sendbt;

    String amount_s;
    String rate1_s;
    String rate2_s;
    String rate3_s;

    NotificationManager manager;
    NotificationCompat.Builder builder;

    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";

    BluetoothAdapter mBluetoothAdapter;
    ConnectedBluetoothThread mThreadConnectedBluetooth;
    Handler mBluetoothHandler;
    BluetoothSocket mBluetoothSocket;

    final static int BT_MESSAGE_READ = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail1);

        amount = (EditText) findViewById(R.id.amount);
        rate1 = (EditText) findViewById(R.id.rate1);
        rate2 = (EditText) findViewById(R.id.rate2);
        rate3 = (EditText) findViewById(R.id.rate3);
        sendbt = (Button)findViewById(R.id.sendbt);

        Intent intent = getIntent();

        rate1.setHint(intent.getStringExtra("drink1"));
        rate2.setHint(intent.getStringExtra("drink2"));
        rate3.setHint(intent.getStringExtra("drink3"));

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mBluetoothSocket = ((InitialActivity)mContext).mBluetoothSocket2;

        sendbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount_s = amount.getText().toString();
                rate1_s = rate1.getText().toString();
                rate2_s = rate2.getText().toString();
                rate3_s = rate3.getText().toString();

                if (amount_s.length() <= 0)
                    Toast.makeText(getApplicationContext(), "용량을 입력 해주세요.", Toast.LENGTH_LONG).show();
                else if((Integer.parseInt(amount_s) > 300) || (Integer.parseInt(amount_s) < 50))
                    Toast.makeText(getApplicationContext(), "50ml이상 300ml이하의 용량을 입력해주세요", Toast.LENGTH_LONG).show();
                else if (rate1_s.length() <= 0)
                    Toast.makeText(getApplicationContext(), "첫번째 음료의 비율을 입력 해주세요", Toast.LENGTH_LONG).show();
                else if ((Integer.parseInt(rate1_s) > 10) || (Integer.parseInt(rate1_s) < 0))
                    Toast.makeText(getApplicationContext(), "0이상 10이하의 비울을 입력해주세요", Toast.LENGTH_LONG).show();
                else if (rate2_s.length() <= 0)
                    Toast.makeText(getApplicationContext(), "두번째 음료의 비율을 입력 해주세요", Toast.LENGTH_LONG).show();
                else if ((Integer.parseInt(rate2_s) > 10) || (Integer.parseInt(rate2_s) < 0))
                    Toast.makeText(getApplicationContext(), "0이상 10이하의 비울을 입력해주세요", Toast.LENGTH_LONG).show();
                else if (rate3_s.length() <= 0)
                    Toast.makeText(getApplicationContext(), "세번째 음료의 비율을 입력 해주세요", Toast.LENGTH_LONG).show();
                else if ((Integer.parseInt(rate3_s) > 10) || (Integer.parseInt(rate3_s) < 0))
                    Toast.makeText(getApplicationContext(), "0이상 10이하의 비울을 입력해주세요", Toast.LENGTH_LONG).show();
                else if ((Integer.parseInt(rate1_s) + Integer.parseInt(rate2_s) + Integer.parseInt(rate3_s) > 10) || (Integer.parseInt(rate1_s) + Integer.parseInt(rate2_s) + Integer.parseInt(rate3_s) <= 0))
                    Toast.makeText(getApplicationContext(), "합이 1이상 10 이하가 되게 해주세요", Toast.LENGTH_LONG).show();
                else {
                    mThreadConnectedBluetooth = new Cocktail3.ConnectedBluetoothThread(mBluetoothSocket);
                    mThreadConnectedBluetooth.start();
                    String amount_out = amount_s + '\0';
                    String rate1_out = rate1_s + '\0';
                    String rate2_out = rate2_s + '\0';
                    String rate3_out = rate3_s + '\0';
                    mThreadConnectedBluetooth.write(amount_out);
                    mThreadConnectedBluetooth.write(rate1_out);
                    mThreadConnectedBluetooth.write(rate2_out);
                    mThreadConnectedBluetooth.write(rate3_out);
                }
            }
        });
        mBluetoothHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == BT_MESSAGE_READ) {
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (readMessage.contains("X")) {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(Cocktail3.this);
                        dlg.setTitle("오류"); //제목
                        dlg.setMessage("컵을 올바른 장소에 놔주세요"); // 메시지
//                버튼 클릭시 동작
                        dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                }
                        });
                        dlg.show();
                    }
                    if (readMessage.contains("O"))
                        Toast.makeText(getApplicationContext(), "음료 제조를 시작합니다", Toast.LENGTH_LONG).show();
                    if (readMessage.contains("Q")) {
                        showNoti();
                        AlertDialog.Builder dlg1 = new AlertDialog.Builder(Cocktail3.this);
                        dlg1.setTitle("음료 제조 완료"); //제목
                        dlg1.setMessage("음료가 완성되었습니다."); // 메시지
//                버튼 클릭시 동작
                        dlg1.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dlg1.show();
                    }
                }
            }
        };

    }

    private class ConnectedBluetoothThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedBluetoothThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100);
                        bytes = mmInStream.available();
                        bytes = mmInStream.read(buffer, 0, bytes);
                        mBluetoothHandler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }
        public void write(String str) {
            byte[] bytes = str.getBytes();
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "데이터 전송 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 해제 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
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
        if(amount_s != null)
            amount.setText(amount_s);
        if(rate1_s != null)
            rate1.setText(rate1_s);
        if(rate2_s != null)
            rate2.setText(rate2_s);
        if(rate3_s != null)
            rate3.setText(rate3_s);

    }

    protected void saveState(){ // 데이터를 저장한다.
        SharedPreferences pref1 = getSharedPreferences("pref1", Activity.MODE_PRIVATE);
        SharedPreferences pref2 = getSharedPreferences("pref2", Activity.MODE_PRIVATE);
        SharedPreferences pref3 = getSharedPreferences("pref3", Activity.MODE_PRIVATE);
        SharedPreferences pref4 = getSharedPreferences("pref4", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = pref1.edit();
        SharedPreferences.Editor editor2 = pref2.edit();
        SharedPreferences.Editor editor3 = pref3.edit();
        SharedPreferences.Editor editor4 = pref4.edit();
        editor1.putString("amount3", amount_s);
        editor2.putString("rate3_1", rate1_s);
        editor3.putString("rate3_2", rate2_s);
        editor4.putString("rate3_3", rate3_s);

        editor1.commit();
        editor2.commit();
        editor3.commit();
        editor4.commit();
    }
    protected void restoreState(){  // 데이터를 복구한다.
        SharedPreferences pref1 = getSharedPreferences("pref1", Activity.MODE_PRIVATE);
        SharedPreferences pref2 = getSharedPreferences("pref2", Activity.MODE_PRIVATE);
        SharedPreferences pref3 = getSharedPreferences("pref3", Activity.MODE_PRIVATE);
        SharedPreferences pref4 = getSharedPreferences("pref4", Activity.MODE_PRIVATE);

        if ((pref1 != null) && (pref1.contains("amount3")))
            amount_s = pref1.getString("amount3", "");
        if ((pref2 != null) && (pref2.contains("rate3_1")))
            rate1_s = pref2.getString("rate3_1", "");
        if ((pref3 != null) && (pref3.contains("rate3_2")))
            rate2_s = pref3.getString("rate3_2", "");
        if ((pref4 != null) && (pref4.contains("rate3_3")))
            rate3_s = pref4.getString("rate3_3", "");

    }
    protected void clearPref(){  // sharedpreference에 쓰여진 데이터 지우기
        SharedPreferences pref1 = getSharedPreferences("pref1", Activity.MODE_PRIVATE);
        SharedPreferences pref2 = getSharedPreferences("pref2", Activity.MODE_PRIVATE);
        SharedPreferences pref3 = getSharedPreferences("pref3", Activity.MODE_PRIVATE);
        SharedPreferences pref4 = getSharedPreferences("pref3", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = pref1.edit();
        SharedPreferences.Editor editor2 = pref2.edit();
        SharedPreferences.Editor editor3 = pref3.edit();
        SharedPreferences.Editor editor4 = pref4.edit();

        editor1.clear();
        editor2.clear();
        editor3.clear();
        editor4.clear();
        amount_s = null;
        rate1_s = null;
        rate2_s = null;
        rate3_s = null;
        editor1.commit();
        editor2.commit();
        editor3.commit();
        editor4.commit();
    }

    public void showNoti(){
        builder = null;
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //버전 오레오 이상일 경우
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            );

            builder = new NotificationCompat.Builder(this,CHANNEL_ID);

            //하위 버전일 경우
        }else{
            builder = new NotificationCompat.Builder(this);
        }

        //알림창 제목
        builder.setContentTitle("슴우디");

        //알림창 메시지
        builder.setContentText("음료가 제작되었습니다.");

        //알림창 아이콘
        builder.setSmallIcon(R.drawable.applogo);

        Bitmap bm= BitmapFactory.decodeResource(getResources(),R.drawable.applogo);
        builder.setLargeIcon(bm);//매개변수가 Bitmap을 줘야한다.

        //알림창 터치시 상단 알림상태창에서 알림이 자동으로 삭제되게 합니다.
        builder.setAutoCancel(true);

        Notification notification = builder.build();

        //알림창 실행
        manager.notify(1,notification);
    }

}