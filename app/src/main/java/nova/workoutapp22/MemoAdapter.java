package nova.workoutapp22;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import nova.workoutapp22.listviewSrcForMemo.MemoItem;
import nova.workoutapp22.listviewSrcForMemo.MemoItemView;
import nova.workoutapp22.subSources.MyApplication;

/**
 * Created by Administrator on 2017-09-02.
 */


public class MemoAdapter extends BaseAdapter {

    public void MemoAdapter(){

    }

    ArrayList<MemoItem> items = new ArrayList<>();

    @Override
    public int getCount() {
        return items.size();
    }
    ////////////////////////////////////////////////////////////
    public void addItem(MemoItem item) {

        items.add(item);
        item.mID = getCount()-1;

    }
////////////////////////////////

    public void setItem(int mID, MemoItem item) {



        items.set(mID, item);

    }

    public void removeItem(MemoItem item){ ///////리무브 코드 에러날 가능성 크다.

        /////// 1, 2, 3, 4, 5에서 3을 지우면 1, 2, 4, 5가 된다. -> 추후 어레이리스트 관리에 문제 발생
        /// 끝자락이 아니라면 무조건 mID를 재정렬해주어야 한다...
        items.remove(item);
        if(item.mID != getCount() ){ // 3은 끝자락이 아니다. (4가 아님)
            //처음부터 끝(0부터 3까지 재정렬 해주면되겠네 .
            for(int i = 0; i < getCount(); i++){
                ((MemoItem)getItem(i)).mID = i;
            }
        }

    }


    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MemoItemView view = new MemoItemView(MyApplication.getAppContext());

        MemoItem item = items.get(position);
        view.setMemo(item.getMemo());
        view.setDate(item.getDate());
        view.setImageWithUri(item.getUri());

        return view;
    }

}