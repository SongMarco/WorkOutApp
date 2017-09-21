package nova.workoutapp22.listviewSrcForGallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.util.ArrayList;

import nova.workoutapp22.R;

import static nova.workoutapp22.MainActivity.fadeIn;
import static nova.workoutapp22.MainActivity.fadeOut;
import static nova.workoutapp22.subSources.BasicInfo.BOX_GONE;

/**
 * Created by Administrator on 2017-09-09.
 */

public class GalAdapter extends BaseAdapter {

    private boolean mCheckBoxState = BOX_GONE;


    public void setCheckBoxState(boolean pState) {
        mCheckBoxState = pState;
        notifyDataSetChanged();
    }


    public ArrayList<nova.workoutapp22.listviewSrcForGallery.GalItem> items = new ArrayList<>();

    public void addItem(nova.workoutapp22.listviewSrcForGallery.GalItem item) {

        items.add(item);
        item.mID = getCount() - 1;

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

        final Context context = parent.getContext();

        ImageView imageViewGal;
        CheckBox checkBox;

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            // item1.xml 을 view object 에 넣어준다.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.gal_item, parent, false);
        }

// 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득


        checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxGal);
        imageViewGal = (ImageView) convertView.findViewById(R.id.imageViewGal);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        nova.workoutapp22.listviewSrcForGallery.GalItem galItem = items.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        imageViewGal.setImageURI(items.get(position).getUri());

        if (mCheckBoxState == BOX_GONE) {


            checkBox.setVisibility(View.GONE);
            checkBox.startAnimation(fadeOut);
        } else {

            checkBox.setVisibility(View.VISIBLE);
            checkBox.startAnimation(fadeIn);
        }

        return convertView;


    }


}
