package nova.workoutapp22.listviewSrcForGallery;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import nova.workoutapp22.subSources.MyApplication;

/**
 * Created by Administrator on 2017-09-09.
 */

public class GalAdapter extends BaseAdapter{





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


        return view;
    }
}
