package nova.workoutapp22;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import nova.workoutapp22.listviewSrcForVid.VidAdapter;
import nova.workoutapp22.listviewSrcForVid.VidItem;
import nova.workoutapp22.subSources.BasicInfo;
import nova.workoutapp22.subSources.DeveloperKey;

import static nova.workoutapp22.subSources.BasicInfo.BOX_GONE;
import static nova.workoutapp22.subSources.BasicInfo.MENU_WO_NORMAL;
import static nova.workoutapp22.subSources.KeySet.PREF_VID;

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
    String youtubeID;

    Intent youtubeIntent;
    private YouTubePlayer youTubePlayer;

    boolean isItemAdded = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instanceVid = this;
        setContentView(R.layout.activity_vid);


        fadeIn2 = AnimationUtils.loadAnimation(this, R.anim.fadein);
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
            vidAdapter.addItem(new VidItem("영상 예시" + (i + 1), Uri.parse(resDrawableUri)));
        }


        /// 유튜브 공유시 타이틀, 영상링크 가져오기
        Bundle extras = getIntent().getExtras();


//url :: https://youtu.be/9sbRbVxGcsA

        if (extras != null) {
            gotUrl = extras.getString(Intent.EXTRA_TEXT);
            Log.v("ttbaby", gotUrl);


            if (gotUrl.contains("https://youtu.be/")) {
                String[] str = gotUrl.split("/");

                youtubeID = str[3];
            }
            String url = "https://img.youtube.com/vi/" + youtubeID + "/0.jpg";
            vidAdapter.addItem(new VidItem("예시예시", url));

        }


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
        Log.v("init failed", "init");

        if (error.isUserRecoverableError()) {
            error.getErrorDialog(this, 0).show();
        } else {
            String errorMessage = String.format(
                    "There was an error initializing the YouTubePlayer",
                    error.toString());
        }

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
                isItemAdded = true;
                Toast.makeText(instanceVid, "add clicked", Toast.LENGTH_SHORT).show();
                youtubeIntent = new Intent(Intent.ACTION_SEARCH);
                youtubeIntent.setPackage("com.google.android.youtube");
                youtubeIntent.putExtra("query", "미식축구선수");


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
        saveState();

    }

    @Override
    protected void onResume() {


        super.onResume();
        //아이템이 추가되지 않았을 때에만 리스토어 해라.
        if (!isItemAdded) {
            restoreState();

        }
        isItemAdded = false;
    }


    public void saveState() {
        saveStateWithGson();
//        saveStateWithJson();
    }

    public void restoreState() {

        restoreStateWithGson();


        // 주의 !! restore에서 오류가 많이 나는데,
        // 아이템을 추가할 경우 toJson도 손보아야 한다.

//        restoreStateWithJson();
    }


    public void saveStateWithGson() {

        Toast.makeText(this, "saveCalled", Toast.LENGTH_SHORT).show();
        SharedPreferences prefVid = getSharedPreferences(PREF_VID, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefVid.edit();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriSerializer())
                .create();

        ArrayList<VidItem> saveArray;
        saveArray = (ArrayList<VidItem>) vidAdapter.itemArrayList.clone();


        String json = gson.toJson(saveArray);

        editor.putString("arrayList", json);
        //apply vs commit

        //void apply () : API 9(2.3) 에서 추가. 호출만 하고 다음코드를 실행하므로 스레드가 block 되지 않는다. 함수가 곧바로 실행되지 않고 비동기 처리된다.
        // boolean commit () : 호출시 스레드는 block 되고 함수 종료시 처리결과를 true/false 로 반환한다.
        // 굳이 결과값이 필요 없다면 비동기로 처리하는 apply 를 사용하는게 반응성면에서 좋다.

        editor.apply();

    }


    public void restoreStateWithGson() {
        Toast.makeText(getApplicationContext(), "restore state Called", Toast.LENGTH_SHORT).show();


        SharedPreferences prefVid = getSharedPreferences(PREF_VID, Activity.MODE_PRIVATE);


        if ((prefVid != null) && (prefVid.contains("arrayList"))) {


            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Uri.class, new UriDeserializer())
                    .create();


            String json = prefVid.getString("arrayList", null);

            //Type type = TypeToken.getParameterized( (ArrayList<MemoItem>) , ).getType();

            ArrayList<VidItem> loadArray = null;

            loadArray = gson.fromJson(json, new TypeToken<ArrayList<VidItem>>() {
            }.getType());


            vidAdapter.itemArrayList = (ArrayList<VidItem>) loadArray.clone();


            // mID를 세팅해줘야 아이템클릭(수정에 사용)이 제대로된다.
            for (int i = 0; i < vidAdapter.getCount(); i++) {
                ((VidItem) vidAdapter.getItem(i)).mID = i;
            }
        }


    }

//    protected void clearMyPrefs() {
//        //    Toast.makeText(getApplicationContext(), "pref cleared", Toast.LENGTH_SHORT).show();
//
//        SharedPreferences prefForGal = getSharedPreferences(PREF_GAL, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefForGal.edit();
//        editor.clear();
//        editor.commit();
//    }

    public class UriSerializer implements JsonSerializer<Uri> {
        public JsonElement serialize(Uri src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

    public class UriDeserializer implements JsonDeserializer<Uri> {
        @Override
        public Uri deserialize(final JsonElement src, final Type srcType,
                               final JsonDeserializationContext context) throws JsonParseException {
            return Uri.parse(src.getAsString());
        }
    }
    //endregion

}
