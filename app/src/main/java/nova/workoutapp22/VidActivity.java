package nova.workoutapp22;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ListView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.net.URL;

import nova.workoutapp22.listviewSrcForVid.VidAdapter;
import nova.workoutapp22.listviewSrcForVid.VidItem;
import nova.workoutapp22.subSources.BasicInfo;
import nova.workoutapp22.subSources.DeveloperKey;

import static nova.workoutapp22.subSources.BasicInfo.BOX_GONE;
import static nova.workoutapp22.subSources.BasicInfo.MENU_WO_NORMAL;

public class VidActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {

    private static Animation fadeIn2;
    private static Animation fadeOut2;

    private static VidActivity instanceVid;

    public static VidActivity getInstanceVid() {
        return instanceVid;
    }

    ProgressDialog dialogLoading;
    Toolbar toolbarVid;

    VidAdapter vidAdapter;
    ListView listViewVid;

    boolean isMultMode = false;
    String menuState = MENU_WO_NORMAL;

    String gotUrl;

    Intent youtubeIntent;
    private YouTubePlayer youTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instanceVid = this;
        setContentView(R.layout.activity_vid);












        /// 유튜브 공유시 타이틀, 영상링크 가져오기
        Bundle extras = getIntent().getExtras();



        if(extras!=null) {
            gotUrl = extras.getString(Intent.EXTRA_TEXT);
            Log.v("ttbaby",gotUrl);
            Log.v("ttbaby", ""+getTitle(gotUrl));


        }
        fadeIn2= AnimationUtils.loadAnimation(this, R.anim.fadein);
        fadeOut2 = AnimationUtils.loadAnimation(this, R.anim.fadeout);







        ///////////////////////툴바를 만듭니다
        toolbarVid = (Toolbar) findViewById(R.id.toolbarVid);
        setSupportActionBar(toolbarVid);
        /////////////////////////////////////////

///리스트뷰의 어댑터 세팅하기.
        vidAdapter = new VidAdapter();
        listViewVid = (ListView) findViewById(R.id.listViewVid);
        listViewVid.setAdapter(vidAdapter);

        //////////////////유투브 프래그먼트 세팅하기

        YouTubePlayerSupportFragment frag =
                (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
        frag.initialize(DeveloperKey.DEVELOPER_KEY, this);






        String resDrawableUri = "android.resource://" + getApplicationContext().getPackageName() + "/drawable/basicimage";

        for (int i = 0; i < 5; i++) {
            vidAdapter.addItem(new VidItem("영상 예시"+(i+1), Uri.parse(resDrawableUri) ) );
        }



        youtubeIntent = new Intent(Intent.ACTION_SEARCH);
        youtubeIntent.setPackage("com.google.android.youtube");
        youtubeIntent.putExtra("query", "미식축구선수");
        youtubeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }








    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        if (!wasRestored) {
            //I assume the below String value is your video id
            player.cueVideo("nCgQDjiotG0");
        }
        youTubePlayer = player;
    }


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {

    }


    //region menu 관련 파트
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_workout, menu);



        //멀미모드임
        if (menuState.equals(BasicInfo.MENU_WO_MULT)) {

            toolbarVid.getChildAt(1).startAnimation(fadeIn2);
            menu.findItem(R.id.action_addItem).setVisible(false);
            menu.findItem(R.id.action_delete).setVisible(true);
            menu.findItem(R.id.action_selectAll).setVisible(true);
            menu.findItem(R.id.action_clearSelection).setVisible(true);


        }
        //싱글모드임임
        else {


            toolbarVid.getChildAt(1).startAnimation(fadeIn2);
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

                startActivity(youtubeIntent);





                return true;

            case R.id.action_selectMult:


//이미 멀티모드였다면 멀티모드를 비활성화하도록 할 것.
                if (isMultMode == true) {

                    setSingleChoice(listViewVid);

                }
                //멀티모드가 아니므로 멀티모드 활성화
                else {


                    setMultipleChoice(listViewVid);
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

        vidAdapter.setCheckBoxState(BOX_GONE);


        menuState = MENU_WO_NORMAL;
        isMultMode = false;
        invalidateOptionsMenu();

    }

    public void setMultipleChoice(ListView lv) {
//        Toast.makeText(getApplicationContext(), "다중 선택 모드로 변경되었습니다.", Toast.LENGTH_SHORT).show();


        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        vidAdapter.setCheckBoxState(true);


        //아이템클릭리스너를 무효화한다.
        lv.setOnItemClickListener(null);

        menuState = BasicInfo.MENU_WO_MULT;
        isMultMode = true;
        invalidateOptionsMenu();
    }




    //region 생명주기 관련 파트 - 저장 등
    @Override
    protected void onPause() {
        super.onPause();



    }


    @Override
    protected void onResume() {
        super.onResume();
//        if (youTubePlayer != null) {
//            Toast.makeText(instanceVid, "loaded", Toast.LENGTH_SHORT).show();
//            youTubePlayer.cueVideo("0fHaQdacCmM");
//        }
    }
}
