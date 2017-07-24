package mydog.haley.com.mynavigation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;

// 산책 시간 기록하는 액티비티 -> 이 클래스는 정리가 필요함 !!!!
public class TimeRecordActivity extends AppCompatActivity {

    private static final String TAG = "**TimeRecordActivity**";
    private final Context context = this;

    // 산책을 시작합니다. 3,2,1 -> 이 페이지로 넘어오자 마자 기록 시작


    // 스톱워치 텍스트 뷰 -> Chronometer로 대체
    /*private static TextView mStopwatch;*/
    private Chronometer mChronometer;

    // 산책 시작 시간
    // private long mStartTime; // 따로 선언 안해도 됨!!!!

    // 산책 종료 시간
    private long mStopTime;

    // 최종 산책 시간
    private long mResultTime;

    private DBOpenHelper mDBOpenHelper;

    /*private static long mResultTime;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_record);

        Log.v(TAG, "onCreate()");

        this.mChronometer = (Chronometer) findViewById(R.id.chronometer);

        // choronometer 포멧을 HH:MM:SS 형태로 변경하기
        this.mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long time = SystemClock.elapsedRealtime() - chronometer.getBase();

                chronometer.setText(getStrTime(time));

            }
        });

        Toast.makeText(context, "산책 기록을 시작합니다.", Toast.LENGTH_SHORT).show();

        this.mChronometer.setBase(SystemClock.elapsedRealtime());
        // 산책 기록 시작
        this.mChronometer.start();
        // 산책 시작 시간 저장
        Log.v(TAG, "산책 시간 기록 시작");

        // 데이터베이스 생성
        mDBOpenHelper = new DBOpenHelper(this);

        // 데이터베이스 오픈
        try {
            mDBOpenHelper.open();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    } // end of onCreate();

    public void onStopClick(View v) {

        Log.v(TAG, "산책 중지 버튼 클릭");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("산책을 종료할까요?")
                .setCancelable(true)
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.v(TAG, "산책 종료");
                        // Chronometer 정지 -- ok
                        mChronometer.stop(); // setBase 타임에 영향 안줌
                        mStopTime = SystemClock.elapsedRealtime();
                        // 최종 산책 시간 저장
                        mResultTime = (mStopTime - mChronometer.getBase()) / 1000; // 초 단위로 출력 -> 좋다
                        Log.v(TAG, "mResultTime : " + mResultTime);

                        Toast.makeText(context, "산책 시간 : " + mResultTime + "초", Toast.LENGTH_SHORT).show();
                        WalkTimeVO vo = new WalkTimeVO("title", "content", mResultTime);
                        // 데이터베이스에 입력
                        mDBOpenHelper.insertDiary(vo);
                        // 데이터베이스 close();
                        mDBOpenHelper.close();

                        startActivity(new Intent(context, MainActivity.class));


                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.v(TAG, "다이얼로그 취소");
                        dialogInterface.cancel();

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }


    private String getStrTime(long time) {

        int h = (int) (time / 3600000);
        int m = (int) (time - h * 3600000) / 60000;
        int s = (int) (time - h * 3600000 - m * 60000) / 1000;
        String hh = h < 10 ? "0" + h : h + "";
        String mm = m < 10 ? "0" + m : m + "";
        String ss = s < 10 ? "0" + s : s + "";

        return hh + ":" + mm + ":" + ss;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*// 핸들러 생성
    static Handler mTimer = new Handler() {

        // 핸들러 처리
        @Override
        public void handleMessage(Message msg) {

            Log.v(TAG, "handleMessage(Message msg)");
            // 핸들러 작동시 텍스트 뷰 수정
            mStopwatch.setText(getTime());

            // 메세지 보내기
            mTimer.sendEmptyMessage(0);

        }
    }; // end of Handler mTimer*/


   /* @Override
    protected void onDestroy() {
        // 해당 액티비티 종료시 메세지를 초기화 시켜서 메모리릭 방지
        // 아닌가?? 고민되네...
        mTimer.removeMessages(0);
        super.onDestroy();
    } // end of onDestroy();*/
/*
    // 시간 가져오기
    private static String getTime() {
        long now = SystemClock.elapsedRealtime(); // 현재 시간 구하기
        long time = now - mBaseTime; // 현재 시간과 지난 시간을 빼서 시간 구함

        // 포맷을 바꿔서 리턴
        long hour = time / 1000 / 60;
        long minute = (time / 1000) % 60;
        long sec = (time % 1000) / 10;


        String strTime = String.format("%02d:%02d:%02d", hour, minute, sec);

        return strTime;


    }*/

  /*  private static String getTime(long time) {

        // 포맷을 바꿔서 리턴
        long hour = time / 1000 / 60;
        long minute = (time / 1000) % 60;
        long sec = (time % 1000) / 10;


        String strTime = String.format("%02d:%02d:%02d", hour, minute, sec);

        return strTime;

    }*/

   /* private void startTimer() {

        // 팝업으로 산책 기록을 시작합니다.

        // 3 2 1 쉬고
        Toast.makeText(this, "산책 기록을 시작합니다.", Toast.LENGTH_SHORT).show();

        try {
            Thread.sleep(500);
        } catch(Exception e) {
            e.printStackTrace();

        }

        mBaseTime = SystemClock.elapsedRealtime(); // 산책 시작 시간 기록
        mTimer.sendEmptyMessage(0); // 핸들러로 메세지 보내서 핸들러 시작


    }*/

    /*public void onStopClick(View v) {

        Log.v(TAG, "종료 버튼 클릭");

        // 산책 정말 종료 할 건지 물어보기
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        Log.v(TAG, "다이얼로그 창 생성");

        dialog.setMessage("산책을 종료할까요?")
                .setCancelable(true)
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.v(TAG, "산책 종료 버튼 클릭");

                        mTimer.removeMessages(0); // 핸들러 메세지 삭제해서 동작 중지
                        mPauseTime = SystemClock.elapsedRealtime(); // 중지한 시간 기록

                        // mResultTime 저장
                        mResultTime = mPauseTime - mBaseTime; // 시간 기록 정보만 보관하는 클래스를 따로 만들자  -> DB 생성

                        WalkTime walkTime = new WalkTime();
                        walkTime.setTimeForDay(mResultTime);

                        Log.v(TAG, "TimeForDay : " + walkTime.getStrTime());
                        Log.v(TAG, "mResultTime : " + mResultTime);
                        Log.v(TAG, "mResultTime : " + getTime(mResultTime));

                        // 이건 그냥 텍스트로 반환 - 총 누적 산책 시간 기록을 위해서 mPauseTime - mBaseTime 시간 알아야함
                        mStrTime = mStopwatch.getText().toString(); // 총 산책 시간 확인
                        Log.v(TAG, "mStrTime : " + mStrTime);

                        // -> 자동으로 산책 기록 기록


                        // main 페이지로 이동
                        startActivity(new Intent(TimeRecordActivity.this, MainActivity.class));

                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.v(TAG, "산책 종료 안함 버튼 클릭");
                        // 산책 시간 기록 계속
                        dialogInterface.cancel();


                    }
                });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();


    } // end of onStopClick*/

}



