package nova.workoutapp22;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;

import nova.workoutapp22.subSources.BasicInfo;

import static nova.workoutapp22.MainActivity.fadeIn;
import static nova.workoutapp22.subSources.BasicInfo.BOX_GONE;
import static nova.workoutapp22.subSources.BasicInfo.MENU_WO_NORMAL;

public class VidActivity extends AppCompatActivity {

    private static VidActivity instanceVid;

    public static VidActivity getInstanceVid(){return instanceVid;}


    Toolbar toolbarVid;


    String menuState = MENU_WO_NORMAL;


    ListView listViewVid;


    boolean isMultMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instanceVid = this;
        setContentView(R.layout.activity_vid);

        ///////////////////////툴바를 만듭니다
        toolbarVid = (Toolbar) findViewById(R.id.toolbarVid);
        setSupportActionBar(toolbarVid);
        /////////////////////////////////////////








    }


    //region menu 관련 파트
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_workout, menu);


        //멀미모드임
        if (menuState.equals(BasicInfo.MENU_WO_MULT)) {

            toolbarVid.getChildAt(1).startAnimation(fadeIn);
            menu.findItem(R.id.action_addItem).setVisible(false);
            menu.findItem(R.id.action_delete).setVisible(true);
            menu.findItem(R.id.action_selectAll).setVisible(true);
            menu.findItem(R.id.action_clearSelection).setVisible(true);


        }
        //싱글모드임임
        else {


            toolbarVid.getChildAt(1).startAnimation(fadeIn);
            menu.findItem(R.id.action_addItem).setVisible(true);
            menu.findItem(R.id.action_delete).setVisible(false);
            menu.findItem(R.id.action_selectAll).setVisible(false);
            menu.findItem(R.id.action_clearSelection).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
//        Toast.makeText(this, "onPrep called", Toast.LENGTH_SHORT).show();


        //        menu.findItem(R.id.start).setVisible(!isStarted);
        //        menu.findItem(R.id.stop).setVisible(isStarted);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_addItem:

                return true;

            case R.id.action_selectMult:


//이미 멀티모드였다면 멀티모드를 비활성화하도록 할 것.
                if (isMultMode == true) {

                    setSingleChoice(gridViewForGal);

                }
                //멀티모드가 아니므로 멀티모드 활성화
                else {


                    setMultipleChoice(gridViewForGal);
                }


                return true;

            case R.id.action_delete:

                return true;

            case R.id.action_selectAll:

                return true;

            case R.id.action_clearSelection:

                return true;

            default:
                return true;
        }

    }
    //endregion


    public void setSingleChoice(ListView lv) {

//        Toast.makeText(getApplicationContext(), "단일 선택 모드로 변경되었습니다.", Toast.LENGTH_SHORT).show();

        lv.clearChoices();
        lv.setChoiceMode(GridView.CHOICE_MODE_SINGLE);

        galAdapter.setCheckBoxState(BOX_GONE);

        setItemClick();

        galMenuState = MENU_WO_NORMAL;
        isMultMode = false;
        invalidateOptionsMenu();

    }

    public void setMultipleChoice(GridView lv) {
//        Toast.makeText(getApplicationContext(), "다중 선택 모드로 변경되었습니다.", Toast.LENGTH_SHORT).show();


        lv.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

        galAdapter.setCheckBoxState(true);


        //아이템클릭리스너를 무효화한다.
        lv.setOnItemClickListener(null);

        galMenuState = BasicInfo.MENU_WO_MULT;
        isMultMode = true;
        invalidateOptionsMenu();
    }


}
