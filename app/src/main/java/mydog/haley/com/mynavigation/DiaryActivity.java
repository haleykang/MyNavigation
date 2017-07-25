package mydog.haley.com.mynavigation;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DiaryActivity extends AppCompatActivity {

    private static final String TAG = "**DiaryActivity**";
    private DBOpenHelper mDBDbOpenHelper;
    private Cursor mCursor;
    private WalkTimeVO mWalkTimeVO;
    private ArrayList<WalkTimeVO> mArrayList;
    private CustomAdapter mCustomAdapter;
    private Context context = this;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        Log.v(TAG, "onCreate()");

        mListView = (ListView)findViewById(R.id.list_view);


        // 데이터베이스 생성
        mDBDbOpenHelper = new DBOpenHelper(this);
        try {
            mDBDbOpenHelper.open();
        } catch(SQLException e) {
            e.printStackTrace();
        }


        // ArrayList 초기화
        mArrayList = new ArrayList<WalkTimeVO>();

        doWhileCursorToArray(); // 이름 나중에 바꾸자

        // 값이 제대로 입력 됐는지 확인을 위해 로그 찍기
        for(WalkTimeVO i : mArrayList) {
            Log.v(TAG, "Code : " + i.getCode());
            Log.v(TAG, "Title : " + i.getTitle());
            Log.v(TAG, "Content : " + i.getContent());
            Log.v(TAG, "Time : " + i.getTime());
            Log.v(TAG, "DateWithTime : " + i.getDateWithTime());
        }

        // 리스트뷰에 사용할 어댑터 초기화
        mCustomAdapter = new CustomAdapter(this, mArrayList);
        mListView.setAdapter(mCustomAdapter);


        // 리스트뷰의 아이템을 길게 눌렀을 경우 삭제하기 위해 롱클릭 리스너 설정
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent,
                                           View view, int position, long id) {

                // 롱 클릭한 아이템의 코드 값 가져오기
                final int code = (int)parent.getAdapter().getItem(position);
                final int po = position;


                Log.v(TAG, "code = " + code + ", position : " + position);

                // 최종 삭제 여부 물어보는 다이얼로그 생성

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("산책 일기를 삭제하시겠습니까?")
                        .setCancelable(true)
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.v(TAG, "산책 기록 삭제");
                                // 코드 값 기준으로 삭제
                                Log.v(TAG, "code = " + code + ", position : " + po);
                                boolean result = mDBDbOpenHelper.deleteDiary(code);
                                // boolean result = mDBDbOpenHelper.deleteAll();

                                Log.v(TAG, "Delete result : " + result);

                                if(result) {
                                    // 데이터베이스 값을 정상적으로 삭제했다면 position 도 삭제
                                    mArrayList.remove(po);
                                    // 어댑터에 ArrayList를 다시 셋팅
                                    mCustomAdapter.setArrayList(mArrayList);
                                    // 어댑터에 값이 변경된 것을 알려줌
                                    mCustomAdapter.notifyDataSetChanged();
                                } else {
                                    // 잘못된 position을 가져왔을 경우 다시 확인 요청
                                    Toast.makeText(DiaryActivity.this, "삭제하려는 데이터를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();

                                }

                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


                return false;
            }


        }); // end of onItemLongClick()


    } // end of onCreate()

    // do~while문을 이용해 Cursor의 내용을 WalkTimeVO 클래스에 입력 후 ArrayList에 ADD...?
    private void doWhileCursorToArray() {

        mCursor = null;

        // DB에 있는 모든 컬럼을 가져옴
        mCursor = mDBDbOpenHelper.getAllDiary();
        // 컬럼 갯수 확인
        Log.v(TAG, "Count = " + mCursor.getCount());

        while(mCursor.moveToNext()) {
            // WalkTimeVO에 입력된 값을 입력
            mWalkTimeVO = new WalkTimeVO(
                    mCursor.getInt(mCursor.getColumnIndex("code")),
                    mCursor.getString(mCursor.getColumnIndex("title")),
                    mCursor.getString(mCursor.getColumnIndex("content")),
                    mCursor.getLong(mCursor.getColumnIndex("time")),
                    mCursor.getString(mCursor.getColumnIndex("date_with_time"))
            );

            // 입력된 값을 가지고 있는 WalkTimeVO을 ArrayList에 add
            mArrayList.add(mWalkTimeVO);
        }

        //Cursor 닫기
        mCursor.close();

    }


    // 액티비티 종료 될 때 DB 클로즈
    @Override
    protected void onDestroy() {

        Log.v(TAG, "onDestroy()");

        mDBDbOpenHelper.close();
        super.onDestroy();
    }
}
