package nova.workoutapp22.listviewSrcForMemo;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;

import nova.workoutapp22.R;
import nova.workoutapp22.subSources.MyApplication;

import static nova.workoutapp22.MainActivity.fadeIn;
import static nova.workoutapp22.MainActivity.fadeOut;


/**
 * Created by Administrator on 2017-09-02.
 */


public class MemoAdapter extends BaseAdapter {


    public void MemoAdapter(){   }

    boolean visibleBox = true;
    boolean goneBox = false;

    private boolean mCheckBoxState = goneBox;

    public void setCheckBoxState(boolean pState){
        mCheckBoxState = pState;
        notifyDataSetChanged();
    }


    public ArrayList<MemoItem> items = new ArrayList<>();


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
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //getView()는 Adapter 가 가지고 있는 data 를 어떻게 보여줄 것인가를 정의하는데 쓰인다.
    // ListView 를 예를 들면 하나의 list item 의 모양을 결정하는 역할을 하는 것이다.

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MemoItemViewLinearLayout view = new MemoItemViewLinearLayout(MyApplication.getAppContext());

        MemoItem item = items.get(position);
        view.setMemoInLayout(item.getMemo());
        view.SetDateInLayout(item.getDate());
        view.setImageFromUri(item.getUri());

        CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkBox);

        // 시작 상태, 삭제한 상태, 다중->단일로 갈때는 체크박스를 gone으로. 아니면 보이게!
        if (mCheckBoxState == goneBox) {


            checkBox.setVisibility(View.GONE);
            checkBox.startAnimation(fadeOut);
        }
        else {

            checkBox.setVisibility(View.VISIBLE);
            checkBox.startAnimation(fadeIn);
        }

        return view;
    }

}