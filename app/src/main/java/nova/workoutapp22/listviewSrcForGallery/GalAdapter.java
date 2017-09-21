package nova.workoutapp22.listviewSrcForGallery;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;

import nova.workoutapp22.R;
import nova.workoutapp22.subSources.MyApplication;

import static nova.workoutapp22.MainActivity.fadeIn;
import static nova.workoutapp22.MainActivity.fadeOut;
import static nova.workoutapp22.subSources.BasicInfo.BOX_GONE;

/**
 * Created by Administrator on 2017-09-09.
 */

public class GalAdapter extends BaseAdapter{


    private boolean mCheckBoxState = BOX_GONE;


    public void setCheckBoxState(boolean pState){
        mCheckBoxState = pState;
        notifyDataSetChanged();
    }






    public ArrayList<GalItem> items = new ArrayList<>();

    public void addItem(GalItem item) {

        items.add(item);
        item.mID = getCount()-1;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GalViewGridLayout view = new GalViewGridLayout(MyApplication.getAppContext());

        GalItem item = items.get(position);

        view.setImageFromUri(item.getUri() );


        CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkBoxGal);

        // 시작 상태, 삭제한 상태, 다중->단일로 갈때는 체크박스를 gone으로. 아니면 보이게!
        if (mCheckBoxState == BOX_GONE) {


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
