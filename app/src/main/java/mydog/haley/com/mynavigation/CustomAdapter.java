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
    // private ViewHolder holder;
    private Context context;


    public CustomAdapter(Context context, ArrayList<WalkTimeVO> mVO) {
        this.mInflater = LayoutInflater.from(context);
        this.mVO = mVO;
        this.context = context;
    }

    // 리스트에서 보여줄 데이터 갯수
    @Override
    public int getCount() {
        return mVO.size();
    }


    @Override
    public Object getItem(int i) {

        // 선택한 아이템의 code 값을 리턴
        return mVO.get(i).getCode();
    }

    // getItem() 메소드가 리턴한 객체의 고유 식별자
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        View v = convertView;

        // 리스트 아이템이 새로 추가 될 경우 v는 null 값이 됨
        if(v == null) {
            holder = new ViewHolder();
            v = mInflater.inflate(R.layout.list_item, null);

            holder.title = (TextView)v.findViewById(R.id.list_title);
            holder.content = (TextView)v.findViewById(R.id.list_content);
            holder.time = (TextView)v.findViewById(R.id.list_time);
            holder.date = (TextView)v.findViewById(R.id.list_date);
            // 수정 버튼
            holder.editBt = (TextView)v.findViewById(R.id.list_edit_bt);
            // 삭제 버튼
            holder.deleteBt = (TextView)v.findViewById(R.id.list_delete_bt);
            // 데이터와 view를 묶음
            v.setTag(holder);
        } else {
            holder = (ViewHolder)v.getTag();
        }

        // WalkTimeVO 객체를 생성해서 각 뷰 포지션에 맞는 데이터 가져오기
        final WalkTimeVO vo = mVO.get(position);

        // 리스트뷰의 아이템에 맞는 String 값을 입력
        holder.title.setText(vo.getTitle());
        holder.content.setText(vo.getContent());
        holder.time.setText(getStrTime(vo.getTime()));
        holder.date.setText(vo.getDateWithTime());

        holder.deleteBt.setTag(position);
        holder.editBt.setTag(position);

        // 수정 버튼 처리
        holder.editBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // view의 position 값을 전달
                setPosition((int)view.getTag());

                if(callback != null) {
                    callback.onEditClick(vo);


                }
            }
        });

        // 삭제 버튼 처리
        holder.deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // view의 position 값을 전달
                setPosition((int)view.getTag());

                if(callback != null) {
                    callback.onDeleteClick(vo);
                }
            }
        });

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

        TextView title = null;
        TextView content = null;
        TextView time = null;
        TextView date = null;

        TextView editBt = null;
        TextView deleteBt = null;


    }

    // 클릭 처리
    public interface onListButtonClick {

        void onDeleteClick(WalkTimeVO vo);

        void onEditClick(WalkTimeVO vo);

    }

    private onListButtonClick callback = null;

    public void setOnListButtonClick(onListButtonClick callback) {
        this.callback = callback;
    }


    private int mPosition;

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int positionV) {
        this.mPosition = positionV;
    }

    private String getStrTime(long time) {

        int m = (int)time / 60;

        return m + "분";


    }

}
