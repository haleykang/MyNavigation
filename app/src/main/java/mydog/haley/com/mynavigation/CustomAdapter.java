package mydog.haley.com.mynavigation;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 2017-07-24.
 */

public class CustomAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<WalkTimeVO> mVO;
    private ViewHolder holder;


    public CustomAdapter(Context context, ArrayList<WalkTimeVO> mVO) {
        this.mInflater = LayoutInflater.from(context);
        this.mVO = mVO;
    }

    @Override
    public int getCount() {
        return mVO.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if(v == null) {
            holder = new ViewHolder();
            v = mInflater.inflate(R.layout.list_item, null);
            holder.code = (TextView)v.findViewById(R.id.list_code);
            holder.title = (TextView)v.findViewById(R.id.list_title);
            holder.content = (TextView)v.findViewById(R.id.list_content);
            holder.time = (TextView)v.findViewById(R.id.list_time);
            holder.date = (TextView)v.findViewById(R.id.list_date);

            v.setTag(holder);
        } else {
            holder = (ViewHolder)v.getTag();
        }

        // WalkTimeVO 객체를 생성해서 각 뷰 포지션에 맞는 데이터 가져오기
        WalkTimeVO vo = mVO.get(position);

        // 리스트뷰의 아이템에 맞는 String 값을 입력
        holder.title.setText(vo.getTitle());
        holder.content.setText(vo.getContent());
        holder.time.setText(String.valueOf(vo.getTime()));
        holder.date.setText(vo.getDateWithTime());


        return v;
    }

    // ArrayList getter, setter


    public ArrayList<WalkTimeVO> getArrayList() {
        return mVO;
    }

    public void setArrayList(ArrayList<WalkTimeVO> mVO) {
        this.mVO = mVO;
    }

    // ViewHolder Class
    private class ViewHolder {

        TextView code;
        TextView title;
        TextView content;
        TextView time;
        TextView date;

    }

}
