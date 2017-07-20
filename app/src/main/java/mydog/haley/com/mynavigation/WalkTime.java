package mydog.haley.com.mynavigation;

/**
 * 산책 시간 정보 보관하는 클래스
 */

public class WalkTime {

    // 1. 인스턴스 변수선언
    private long waltime;

    public long getWaltime() {
        return waltime;
    }

    public void setWaltime(long waltime) {
        this.waltime = waltime;
    }

    @Override
    public String toString() {
        return "WalkTime{" +
                "waltime=" + waltime +
                '}';
    }


}
