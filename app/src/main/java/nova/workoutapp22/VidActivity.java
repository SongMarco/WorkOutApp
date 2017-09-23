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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubePlayer;
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

import static nova.workoutapp22.subSources.BasicInfo.BOX_GONE;
import static nova.workoutapp22.subSources.BasicInfo.MENU_WO_NORMAL;
import static nova.workoutapp22.subSources.BasicInfo.REQ_WATCH;
import static nova.workoutapp22.subSources.KeySet.PREF_VID;
import static nova.workoutapp22.subSources.KeySet.key_mID;
import static nova.workoutapp22.subSources.KeySet.key_vidTitle;
import static nova.workoutapp22.subSources.KeySet.key_youtubeID;

public class VidActivity extends AppCompatActivity {

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

    String resDrawableUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        instanceVid = this;
        setContentView(R.layout.activity_vid);

        Toast.makeText(instanceVid, "onCreateCalled", Toast.LENGTH_SHORT).show();
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

        setItemClick();


//url :: https://youtu.be/9sbRbVxGcsA


        restoreState();

        Log.v("ggwp", "intent = " + getIntent());
        if (getIntent() != null) {
            addItemFromIntent(getIntent());
        }


    }

    public void setItemClick() {
        listViewVid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


                VidItem item = (VidItem) vidAdapter.getItem(position);

                // 수정 -- 메모 보기 액티비티 띄우기
                Intent intent = new Intent(getApplicationContext(), WatchVidActivity.class);



                intent.putExtra(key_youtubeID, item.getYoutubeID());

                intent.putExtra(key_vidTitle, item.getVidTitle() );

                intent.putExtra(key_mID, item.getmID() );


                // 모든 선택 상태 초기화.
                listViewVid.clearChoices();
                vidAdapter.notifyDataSetChanged();


                startActivityForResult(intent, REQ_WATCH);
                //////////////////

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //새로운 메모 작성이 완료되었다.

        if (requestCode == REQ_WATCH) {
            // Toast.makeText(getApplicationContext(),"onActivResult 호출됨, 요청 코드 : "+requestCode+
            //        ", 결과 코드 : " +resultCode, Toast.LENGTH_SHORT).show();

            if (resultCode == RESULT_OK) {

                VidItem newmit = setItemFromIntent(data);

                Toast.makeText(instanceVid, "newurl ="+newmit.getThumbUrl(), Toast.LENGTH_SHORT).show();


                int mmID = data.getExtras().getInt(key_mID);

                ////////////////////////////
                ////////////////////////////
                ////////////////////////////

                //////////////////////주의 주의 3시간 이상 삽질한 문제 : new 아이템 만들고 ID를 초기화 안함
                // -> 계쏙해서 잘못된 mID를 전달하게 됨.
                // 인텐트의 전달이 계속 잘못되면 인텐트 관련 메소드를 살피자. 이것도 인텐트 관련 메소드다.

                newmit.setmID(mmID); //////////////ㄹㅇ 정신나간 코드임;


                vidAdapter.setItem(mmID, newmit);
                Toast.makeText(instanceVid, "mid="+ newmit.getmID()+", title="+newmit.getVidTitle(), Toast.LENGTH_SHORT).show();


                vidAdapter.notifyDataSetChanged();
                Log.v("순서추적", "데이타 변경 완료");

                saveState();


            }
        }
    }


    public VidItem setItemFromIntent(Intent data) {
        String youtubeID = data.getStringExtra(key_youtubeID);
        String vidTitle = data.getStringExtra(key_vidTitle);


        return new VidItem(vidTitle, (youtubeID));

    }
//
//    public String IDtoThumb(String youtubeID){
//        return "https://img.youtube.com/vi/" + youtubeID + "/0.jpg";
//
//
//    }

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
//
//                vidAdapter.addItem(new VidItem("영상 예시", Uri.parse(resDrawableUri)));
//                vidAdapter.notifyDataSetChanged();

                youtubeIntent = new Intent(Intent.ACTION_SEARCH);
                youtubeIntent.setPackage("com.google.android.youtube");
                youtubeIntent.putExtra("query", "미식축구선수");


                startActivityForResult(youtubeIntent, 2);


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


    public void addItemFromIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        //엑스트라 존재 -> 아이템을 공유한 것임.

        Toast.makeText(instanceVid, "addFromIntent", Toast.LENGTH_SHORT).show();
        if (extras != null) {
            isItemAdded = true;
            gotUrl = extras.getString(Intent.EXTRA_TEXT);



            if (gotUrl.contains("https://youtu.be/")) {
                String[] str = gotUrl.split("/");

                youtubeID = str[3];
            }
            String url = "https://img.youtube.com/vi/" + youtubeID + "/0.jpg";
            vidAdapter.addItem(new VidItem("제목을 입력하세요.", url, youtubeID));

            saveState();
            vidAdapter.notifyDataSetChanged();
        }

        isItemAdded = true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Toast.makeText(instanceVid, "onNew", Toast.LENGTH_SHORT).show();
        setIntent(intent);

        addItemFromIntent(intent);

    }


    @Override
    protected void onPause() {
        super.onPause();
        saveState();

    }

    @Override
    protected void onResume() {

//        Toast.makeText(instanceVid, "onresume", Toast.LENGTH_SHORT).show();
        super.onResume();

        // 아이템이 추가되지 않았을 때에만 리스토어 해라.

        restoreState();

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
//        Toast.makeText(getApplicationContext(), "restore state Called", Toast.LENGTH_SHORT).show();


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
