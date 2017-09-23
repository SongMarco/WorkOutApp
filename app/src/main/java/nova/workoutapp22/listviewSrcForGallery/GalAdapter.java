package nova.workoutapp22.listviewSrcForGallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

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
    Bitmap myBitmap = null;

    public void setCheckBoxState(boolean pState) {
        mCheckBoxState = pState;
        notifyDataSetChanged();
    }


    //todo GalItem 수정함. 버그 확인

    public ArrayList<GalItem> itemArrayList = new ArrayList<>();

    public void addItem(GalItem item) {

        itemArrayList.add(item);
        item.mID = getCount() - 1;

    }

    public void removeItem(GalItem item) { ///////리무브 코드 에러날 가능성 크다.

        /////// 1, 2, 3, 4, 5에서 3을 지우면 1, 2, 4, 5가 된다. -> 추후 어레이리스트 관리에 문제 발생
        /// 끝자락이 아니라면 무조건 mID를 재정렬해주어야 한다...
        itemArrayList.remove(item);
        if (item.mID != getCount()) { // 3은 끝자락이 아니다. (4가 아님)
            //처음부터 끝(0부터 3까지 재정렬 해주면되겠네 .
            for (int i = 0; i < getCount(); i++) {
                ((GalItem) getItem(i)).mID = i;
            }
        }

    }

    @Override
    public int getCount() {
        return itemArrayList.size();
    }

    @Override
    public Object getItem(int position) {

        return itemArrayList.get(position);
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
        GalItem galItem = itemArrayList.get(position);

        // 아이템 내 각 위젯에 데이터 반영

        Uri uri = galItem.getUri();
        Log.v("uritg","uri = "+uri);



            Glide.with(context).
                    load(uri).
                    into(imageViewGal);




        if (mCheckBoxState == BOX_GONE) {

            
            checkBox.setVisibility(View.GONE);
            checkBox.startAnimation(fadeOut);
        } else {

            checkBox.setVisibility(View.VISIBLE);
            checkBox.startAnimation(fadeIn);
        }

            checkBox.bringToFront();
        return convertView;


    }


}
