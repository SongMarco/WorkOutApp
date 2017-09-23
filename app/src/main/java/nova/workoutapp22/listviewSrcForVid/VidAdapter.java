package nova.workoutapp22.listviewSrcForVid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import nova.workoutapp22.R;

import static nova.workoutapp22.subSources.BasicInfo.BOX_GONE;

/**
 * Created by Administrator on 2017-09-23.
 */


public class VidAdapter extends BaseAdapter{

    private static Animation fadeIn2;
    private static Animation fadeOut2;

    private boolean mCheckBoxState = BOX_GONE;

    public void setCheckBoxState(boolean pState) {
        mCheckBoxState = pState;
        notifyDataSetChanged();
    }


    public ArrayList<VidItem> itemArrayList = new ArrayList<>();

    public void addItem(VidItem item) {

        itemArrayList.add(item);
        item.mID = getCount() - 1;

    }

    public void removeItem(VidItem item) { ///////리무브 코드 에러날 가능성 크다.

        /////// 1, 2, 3, 4, 5에서 3을 지우면 1, 2, 4, 5가 된다. -> 추후 어레이리스트 관리에 문제 발생
        /// 끝자락이 아니라면 무조건 mID를 재정렬해주어야 한다...
        itemArrayList.remove(item);
        if (item.mID != getCount()) { // 3은 끝자락이 아니다. (4가 아님)
            //처음부터 끝(0부터 3까지 재정렬 해주면되겠네 .
            for (int i = 0; i < getCount(); i++) {
                ((VidItem) getItem(i)).mID = i;
            }
        }

    }


    public void setItem(int mID, VidItem item) {

        itemArrayList.set(mID, item);

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

        ImageView imageView;
        TextView textView;
        CheckBox checkBox;

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            // item1.xml 을 view object 에 넣어준다.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.vid_item, parent, false);
        }

// 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득



        imageView = (ImageView) convertView.findViewById(R.id.imageViewVid);
        checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxVid);
        textView = (TextView)convertView.findViewById(R.id.textViewVid);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        VidItem item = itemArrayList.get(position);

        // 아이템 내 각 위젯에 데이터 반영

//        imageView.setImageURI( item.getUri() );

        textView.setText( item.getVidTitle() );

        if(item.getThumbUrl() != null) {
            Glide.with(context).load(item.getThumbUrl() ).into(imageView);
        }
        else{
           Glide.with(context).load(item.getUri()).into(imageView);

        }




        fadeIn2= AnimationUtils.loadAnimation(context, R.anim.fadein);
        fadeOut2 = AnimationUtils.loadAnimation(context, R.anim.fadeout);

        if (mCheckBoxState == BOX_GONE) {


            checkBox.setVisibility(View.GONE);
            checkBox.startAnimation(fadeOut2);
        } else {

            checkBox.setVisibility(View.VISIBLE);
            checkBox.startAnimation(fadeIn2);
        }

        return convertView;

    }
}
