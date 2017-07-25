package mydog.haley.com.mynavigation;

/**
 * 산책 시간 정보 보관하는 클래스
 */

public class WalkTimeVO {

    // 1. 테이블 컬럼 이름 인스턴스 변수 선언
    private int code;
    private String title;
    private String content;
    private long time;
    private String dateWithTime; // SQLite의 Date 또는 Time값은 String으로 가져오면 됨


    // 2. 생성자
    // 2-1) 다 입력하는 생성자
    public WalkTimeVO(int code, String title, String content, long time, String dateTime) {
        this.code = code;
        this.title = title;
        this.content = content;
        this.time = time;
        this.dateWithTime = dateTime;

    }


    // 2-2) 기본 생성자
    public WalkTimeVO() {

    }

    // 2-3) title, content 입력 받는 생성자 -> update에 사용
    public WalkTimeVO(String title, String content) {
        this.title = title;
        this.content = content;

    }

    // 2-4 ) time만 입력받는 생성자
    public WalkTimeVO(long time) {
        this.time = time;
    }


    // 3. get, set 함수
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDateWithTime() {
        return dateWithTime;
    }

    public void setDateWithTime(String date) {
        this.dateWithTime = date;
    }


    @Override
    public String toString() {
        return "WalkTimeVO{" +
                "code=" + code +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", dateWithTime='" + dateWithTime + '\'' +
                '}';
    }



}
