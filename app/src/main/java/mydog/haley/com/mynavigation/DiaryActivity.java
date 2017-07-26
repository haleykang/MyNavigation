package mydog.haley.com.mynavigation;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        addListItem(); // 이름 나중에 바꾸자

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

        // 리스트 뷰 버튼 클릭 이벤트 처리
        setOnListButtonClick();


    } // end of onCreate()


    // do~while문을 이용해 Cursor의 내용을 WalkTimeVO 클래스에 입력 후 ArrayList에 ADD...?
    private void addListItem() {

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


    private void setOnListButtonClick() {

        // 리스트 뷰 내의 수정 & 삭제 버튼 클릭 시 처리할 이벤트
        mCustomAdapter.setOnListButtonClick(new CustomAdapter.onListButtonClick() {
            // 삭제 버튼 클릭 처리
            @Override
            public void onDeleteClick(WalkTimeVO vo) {

                final int code = vo.getCode();
                final int position = mCustomAdapter.getPosition();

                Log.v(TAG, "Code : " + code + ", position : " + position);
                // 삭제

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("산책 일기를 삭제하시겠습니까?")
                        .setCancelable(true)
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.v(TAG, "산책 기록 삭제");
                                // 코드 값 기준으로 삭제
                                Log.v(TAG, "code = " + code + ", position : " + position);
                                boolean result = mDBDbOpenHelper.deleteDiary(code);
                                // boolean result = mDBDbOpenHelper.deleteAll();

                                Log.v(TAG, "Delete result : " + result);

                                if(result) {

                                    refresh(position);
                                  /*  // 데이터베이스 값을 정상적으로 삭제했다면 position 도 삭제
                                    mArrayList.remove(position);
                                    // 어댑터에 ArrayList를 다시 셋팅
                                    mCustomAdapter.setArrayList(mArrayList);
                                    // 어댑터에 값이 변경된 것을 알려줌
                                    mCustomAdapter.notifyDataSetChanged();*/
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
                                Log.v(TAG, "code = " + code + ", position : " + position);
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }

            // 수정 버튼 클릭 처리
            @Override
            public void onEditClick(WalkTimeVO vo) {

                // 커스텀 다이얼로그 삽입
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.diary_dialog, null);

                // 수정을 위한 다이얼로그 창 출력
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogView);

                // 필요한 정보 가져오기
                final int code = vo.getCode(); // 데이터베이스 수정을 위해
                final long time = vo.getTime();
                final String date = vo.getDateWithTime();
                final int position = mCustomAdapter.getPosition();

                final EditText titleEt = (EditText)dialogView.findViewById(R.id.title_et);
                final EditText contentEt = (EditText)dialogView.findViewById(R.id.content_et);


                // editText의 초기 값을 데이터 베이스에 저장된 값으로 지정
                titleEt.setText(vo.getTitle());
                contentEt.setText(vo.getContent());


                final AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);

                dialog.show();

                // 저장 버튼 처리
                Button saveBt = (Button)dialogView.findViewById(R.id.save_bt);
                saveBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String title = titleEt.getText().toString();
                        String content = contentEt.getText().toString();


                        // 데이터베이스에 업데이트
                        mWalkTimeVO = new WalkTimeVO(code, title, content, time, date);
                        mDBDbOpenHelper.updateDiary(mWalkTimeVO);

                        refresh(position, mWalkTimeVO);

                        // 흠... 이걸 함수화
                       /* mArrayList.set(position, mWalkTimeVO);
                        mCustomAdapter.setArrayList(mArrayList);
                        mCustomAdapter.notifyDataSetChanged();*/


                        dialog.cancel();

                    }
                });

                // 취소 버튼 처리
                Button cancelBt = (Button)dialogView.findViewById(R.id.cancel_bt);
                cancelBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.cancel();


                    }
                });


            }
        });

    }

    private void refresh(int position, WalkTimeVO vo) {

        mArrayList.set(position, vo);
        mCustomAdapter.setArrayList(mArrayList);
        mCustomAdapter.notifyDataSetChanged();

    }

    private void refresh(int position) {
        mArrayList.remove(position);
        mCustomAdapter.setArrayList(mArrayList);
        mCustomAdapter.notifyDataSetChanged();

    }

    // 액티비티 종료 될 때 DB 클로즈
    @Override
    protected void onDestroy() {

        Log.v(TAG, "onDestroy()");

        mDBDbOpenHelper.close();
        super.onDestroy();
    }
}
