package mydog.haley.com.mynavigation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 데이터 베이스 관리 클래스 - WALKTIME 테이블
 */

public class DBOpenHelper {

    private static final String TAG = "**DBOpenHelper**";

    private static final String DATABASE_NAME = "MyDogDiary";

    private static final int DATABASE_VERSION = 10;
    // DATABASE_VERSION 변경 1->2->3->4->5->6->7->8->9

    public static SQLiteDatabase mDB;

    private DataBaseHelper mDBHelper;

    private Context mContext;

    private class DataBaseHelper extends SQLiteOpenHelper {


        // 데이터베이스 헬퍼 생성자

        public DataBaseHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        // 최초 DB 생성시 한번 호출
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.v(TAG, "테이블 생성");

            // 테이블 생성
            db.execSQL(DataBase.CreatDB._CREATE);

        }

        // 버전 업데이트 됐을 때 DB를 다시 생성
        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            // DROP TABLE IF EXISTS -> 예전 버전 삭제 후 새 버전 재 시작
            Log.v(TAG, "데이터베이스 업그레이드");
            db.execSQL("DROP TABLE IF EXISTS " + DataBase.CreatDB._TABLENAME);
            // onCreate() 재시작
            onCreate(db);

        }


    } // end of DateBaseHelper


    // 1. DBOpenHelper 생성자
    public DBOpenHelper(Context context) {

        this.mContext = context;
    }

    // 2. DB를 open 하는 메소드
    public DBOpenHelper open() throws SQLException {
        Log.v(TAG, "DB open");
        mDBHelper = new DataBaseHelper(mContext);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    // 3. DB를 모두 사용 후 닫는 메소드
    public void close() {
        Log.v(TAG, "DB Close");
        mDB.close();
    }

    // 4. CRUD 함수 생성

    // 4-1) 새로운 산책 기록 입력 함수 -> 최초에는 title & content는 디폴트 값
    public long insertDiary(WalkTimeVO vo) {
        Log.v(TAG, "insertDiary");
        // ContentValues 에 값 저장
        ContentValues values = new ContentValues();
        values.put(DataBase.CreatDB.TIME, vo.getTime());

        // 새로운 값 추가
        return mDB.insert(DataBase.CreatDB._TABLENAME, null, values);

    }

    // 4-2) 산책 기록 수정
    public boolean updateDiary(WalkTimeVO vo) {

        // ContentValues로 변경 할 값  저장하기
        ContentValues values = new ContentValues();
        values.put(DataBase.CreatDB.TITLE, vo.getTitle());
        values.put(DataBase.CreatDB.CONTENT, vo.getContent());

        // row 업데이트
        /*
        int update (String table, ContentValues values,
                String whereClause, String[] whereArgs)
         */

        // 이렇게 업데이트 하면 안됨..


        boolean result = mDB.update(DataBase.CreatDB._TABLENAME, values,
                "code=" + vo.getCode(), null) > 0;

        return result;
    }

    // 4-3) 데이터 삭제 함수 // 입력한 code값을 삭제
    public boolean deleteDiary(int code) {

        return mDB.delete(DataBase.CreatDB._TABLENAME, "code=" + code, null) > 0;

    }

    // 4-3-1) 테스트를 위해 모든 데이터 삭제 함수
    public boolean deleteAll() {
        return mDB.delete(DataBase.CreatDB._TABLENAME, null, null) > 0;
    }

    // 4-4) 커서 전체를 선택하는 메소드
    public Cursor getAllDiary() {

        // code desc 정렬 설정 추가 하기
        String orderBy = DataBase.CreatDB.CODE + " DESC";
        return mDB.query(DataBase.CreatDB._TABLENAME, null, null, null, null, null, orderBy);

    }

    // 4-5) 상세 보기
    public Cursor getDiary(int code) {
        Cursor cursor = mDB.query(DataBase.CreatDB._TABLENAME, null,
                "code=" + code, null, null, null, null);

        // 받아온 컬럼이 null이 아니고 0번째가 아닐 경우 제일 처음으로 커서 보내기
        if(cursor != null & cursor.getCount() != 0) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    // 4-6) 날짜 기준으로 검색 - > time 값 가져오는 함수
    public Long getSumDayTime(String date) {
        // 이 값을 바꾸면 될듯 일단 테스트
        // date_with_time (strftime('%Y-%m-%d','now','localtime'))
        // strftime('%Y-%m-%d', date_with_time) =

        long sumTime = 0;

        // where date(date_with_time) =
        String sql = "select sum(time) from walktime "
                + "where date_with_time >= '" + date + " 00:00:00' and date_with_time <= '" + date + " 23:59:59'";

    /*    // SQL 쉽게 수정 -> 왜 안될까??????
        String sql = "select sum(time) from walktime "
                + "where strftime('%Y-%m-%d',date_with_time)=" + date;*/

        Cursor cursor = mDB.rawQuery(sql, null);
        if(cursor.moveToFirst()) {

            sumTime = cursor.getLong(0);
        }

        cursor.close();

        return sumTime;

    }
}






/*

   */
/* // table name
    private static final String TABLE_WALKTIME = "walktime";

    // walktime Table Columns names
    private static final String KEY_CODE = "code";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_TIME = "time";
    private static final String KEY_DATE = "date";*//*


*/
/*
    // 1. 생성자
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }*//*


    // 2. DB가 존재하지 않을 때 최초에 실행되는 함수
    // DB 생성 & 테이블 생성
    */
/*@Override
    public void onCreate(SQLiteDatabase db) {

        // 테이블 생성
        String sql = "CREATE TABLE " + TABLE_WALKTIME + "("
                + KEY_CODE + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT NOT NULL,"
                + KEY_CONTENT + " TEXT NOT NULL,"
                + KEY_TIME + " INTEGER NOT NULL,"
                + KEY_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";


        db.execSQL(sql);

    }*//*


    // 3. Application 버전 올라가 Table 구조가 변경 될 때 사용
    // drop table, alter table 등등
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // DROP TABLE IF EXISTS -> 예전 버전 삭제 후 새 버전 재 시작
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WALKTIME);

        // onCreate() 재시작
        onCreate(db);

    }
*/


// 4. 사용자 정의 함수 - CRUD

// 추가 필요한 것 - 날짜 별로 시간 값 가져오는 함수


    /*// 4-1) 새로운 산책 기록 입력 함수
    public void addDiary(WalkTimeVO vo) {

        // 쓰기가 가능한 데이터베이스 열기
        SQLiteDatabase db = this.getWritableDatabase();

        // ContentValues, Cursors - 데이터베이스 값을 가지는 객체
        // Cursors - 우리가 커서를 두는 것과 같이 데이터베이스에서 값을 가져올 때 Cursors 인터페이스 사용
        // ContentValues - 반대로 자료를 데이터베이스에 입력하기 위해 사용하는 객체
        // ContentValues 객체에 데이터베이스 테이블과 일치하는 자료를 입력 한 수 insert() 메소드 사용해 추가

        // ContentValues 에 값 저장
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, vo.getTitle());
        values.put(KEY_CONTENT, vo.getContent());
        values.put(KEY_TIME, vo.getTime());

        // 새로운 값 추가
        db.insert(TABLE_WALKTIME, null, values);
        db.close(); // 연결 종료

    }*/

    /*// 4-2) 산책 기록 세부 보기
    public WalkTimeVO getDiary(int code) {
        // 읽기가 가능한 데이터베이스 가져오기
        SQLiteDatabase db = this.getReadableDatabase();

        // Cursors 객체 -> 데이터베이스에서 값을 가져올 때 사용
        *//*
        moveToFirst  커서가 쿼리(질의) 결과 레코드들 중에서 가장 처음에 위치한 레코드를 가리키도록 합니다.
        moveToNext  다음 레코드로 커서를 이동합니다.
        moveToPrevious  이전 레코드로 커서를 이동합니다.
        getCount  질의 결과값(레코드)의 갯수를 반환합니다.
        getColumnIndexOrThrow  특정 필드의 인덱스값을 반환하며, 필드가 존재하지 않을경우 예외를 발생시킵니다.
        getColumnName  특정 인덱스값에 해당하는 필드 이름을 반환합니다.
        getColumnNames  필드 이름들을 String 배열 형태로 반환합니다.
        moveToPosition  커서를 특정 레코드로 이동시킵니다.
        getPosition  커서가 현재 가리키고 있는 위치를 반환합니다.

        출처: http://androidhuman.com/210 [커니의 안드로이드 이야기]
        *//*
        *//*
           query(테이블 이름,
           가져올 컬럼 이름,
           where 조건 절(없으면 null),
           where 조건 절의 값을 ?로 지정했을 경우 해당 값을 가져올 조건,
           groupBy 구문,
           having 구문,
           orderBy 구문,
           limit 결과 값의 갯수 제한
         *//*
        Cursor cursor = db.query(TABLE_WALKTIME,
                new String[]{ KEY_TITLE, KEY_CONTENT, KEY_TIME, KEY_DATE },
                KEY_CODE + "=?",
                new String[]{ String.valueOf(code) },
                null, null, null, null);

        // String.valueOf(code) // code 값을 String 객체로 가져옴

        // 커서가 쿼리 결과 레코드 중 가장 처음에 위치한 레코드를 가리키도록
        if(cursor != null) {
            cursor.moveToFirst();
        }

        // DATE 값으로 가져올 수 있는 방법을 한 번 생각해보자
        WalkTimeVO vo = new WalkTimeVO(code, cursor.getString(0),
                cursor.getString(1), Long.parseLong(cursor.getString(2)),
                cursor.getString(3));

        return vo;

    }*/

   /* // 4-3) 모든 산책 기록 가져오기
    public List<WalkTimeVO> getAllDiarys() {

        List<WalkTimeVO> diaryList = new ArrayList<WalkTimeVO>();

        // Select All Query;
        String sql = "SELECT * FROM " + TABLE_WALKTIME
                + " ORDER BY CODE DESC";

        // Open Datebase
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        // cursor의 결과 값을 list에 저장하기
        if(cursor.moveToFirst()) {
            do {
                WalkTimeVO vo = new WalkTimeVO();

                // code 값은 자동 값인데 조절해도 됨??
                vo.setCode(Integer.parseInt(cursor.getString(0)));
                vo.setTitle(cursor.getString(1));
                vo.setContent(cursor.getString(2));
                vo.setTime(Long.parseLong(cursor.getString(3)));
                vo.setDateWithTime(cursor.getString(4));

                // list에 저장
                diaryList.add(vo);

            } while(cursor.moveToNext());
        }

        return diaryList;

    }*/


/*    // 4-4) 테이블에 포함된 산책 기록 갯수 세기
    public int getDiaryCount() {
        String sql = "SELECT * FROM " + TABLE_WALKTIME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.close();

        return cursor.getCount();
    }*/


    /*// 4-5) 산택 기록 수정 -> 산책 기록 제목 & 내용만 변경 가능
    public int updateDiary(WalkTimeVO vo) {

        // 읽고 쓰기가 가능하게 데이터베이스 열기
        SQLiteDatabase db = this.getWritableDatabase();

        // ContentValues로 데이터베이스에 값 저장하기
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, vo.getTitle());
        values.put(KEY_CONTENT, vo.getContent());

        // row 업데이트
        *//*
        int update (String table, ContentValues values,
                String whereClause, String[] whereArgs)
         *//*

        int result = db.update(TABLE_WALKTIME,
                values,
                KEY_CODE + " = ?",
                new String[]{ String.valueOf(vo.getCode()) });

        return result;

    }*/

  /*  // 4-6) 데이터 삭제 함수 -> 파라미터로 넘겨준 WalkTimeVO 객체에 해당하는 값 삭제
    public void delete(WalkTimeVO vo) {

        // 읽고 쓰기가 가능하게 데이터베이스 열기
        SQLiteDatabase db = getWritableDatabase();

        *//*
        int delete (String table,
                String whereClause,String[] whereArgs)
         *//*

        db.delete(TABLE_WALKTIME,
                KEY_CODE + "=?",
                new String[]{ String.valueOf(vo.getCode()) });

        db.close();

    }*/



