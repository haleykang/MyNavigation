package mydog.haley.com.mynavigation;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView walkTimeTextView;
    private static final String TAG = "**MainActivity**";
    private DBOpenHelper mDB;
    private long mTime;
    private String mToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, "onCreate()");
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 산책 하기 버튼
        Button go = (Button)findViewById(R.id.go_bt);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 산책 버튼 클릭

                // 애니메이션...

                // 산책 기록 화면으로 이동
                // 일단 스톱워치 화면으로 먼저 이동

                Intent intent = new Intent(MainActivity.this, TimeRecordActivity.class);
                // 이건 다시 공부하고 설정하자... 휴...
                // 산책 시간을 기록하고 메인으로 이동했을 때 추가된 값이 자동으로 적용 안됨
                // -> 새로고침 버튼을 만들거나 플래그를 다시 설정해서 하든가 아님 onNewIntent() 함수 재사용 해보거나..
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        this.walkTimeTextView = (TextView)findViewById(R.id.walk_time);


        // 데이터 베이스 오픈
        mDB = new DBOpenHelper(this);
        try {

            mDB.open();

        } catch(SQLException e) {
            e.printStackTrace();
        }

        // 기준 날짜 생성
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        mToday = dateFormat.format(date);

        // 값 가져오기 -> 갱신 했을 때 대비하기...흠...
        mTime = mDB.getSumDayTime(mToday);
        this.walkTimeTextView.setText(getStrTime(mTime));


        // mDB.close();


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.v(TAG, "onNewIntent()");

        mTime = mDB.getSumDayTime(mToday);
        this.walkTimeTextView.setText(getStrTime(mTime));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_report) {
            // Handle the camera action
        } else if(id == R.id.nav_diary) {

            startActivity(new Intent(this, DiaryActivity.class));

        } else if(id == R.id.nav_awards) {

        } else if(id == R.id.nav_manage) {


        }

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // 출력을 00분 형태로
    private String getStrTime(long time) {

        int m = (int)time / 60;

        return m + "분";


    }
}
