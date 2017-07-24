package mydog.haley.com.mynavigation;

import android.provider.BaseColumns;

/**
 * 데이터 베이스 테이블 생성
 */

public class DataBase {

    // 데이터베이스 호출 시 사용될 생성자
    public static final class CreatDB implements BaseColumns {

        public static final String CODE = "code";

        public static final String TITLE = "title";

        public static final String CONTENT = "content";

        public static final String TIME = "time";

        public static final String DATE_WITH_TIME = "date_with_time";

        public static final String _TABLENAME = "walktime";


        public static final String _CREATE = "create table " + _TABLENAME + "("
                + CODE + " integer primary key autoincrement , "
                + TITLE + " text not null,"
                + CONTENT + " text not null,"
                + TIME + " integer not null,"
                + DATE_WITH_TIME + " datetime default (datetime('now','localtime'))" + ")";

    }
}
