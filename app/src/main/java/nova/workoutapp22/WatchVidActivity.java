package nova.workoutapp22;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import nova.workoutapp22.subSources.DeveloperKey;
import nova.workoutapp22.subSources.KeySet;

import static nova.workoutapp22.subSources.KeySet.key_mID;
import static nova.workoutapp22.subSources.KeySet.key_vidTitle;
import static nova.workoutapp22.subSources.KeySet.key_youtubeID;

public class WatchVidActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    //YouTube Data API v3 서비스를 사용하기 API 키 필용
    //새 키를 생성하려면   Google APIs Console 에서 발급
    //private static final String YoutubeDeveloperKey = "AIzaSyDmaXanAKeyf4Q91e6lr0wucRRGdiPDKj8";



    String vidKey;

    EditText etTitle;

    Button buttonSaveWatch;
    YouTubePlayer.Provider provider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_vid);




        Log.v("keykey","key="+vidKey);
        Log.d("youtube Test",
                "사용가능여부:" + YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this)); //SUCCSESS


        //YouTubePlayer를 초기화
        //public void initialize (String developerKey,
        //                        YouTubePlayer.OnInitializedListener onInitializedListener)

        //YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        //youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);

        etTitle = (EditText)findViewById(R.id.editTextWatch);

        buttonSaveWatch = (Button)findViewById(R.id.buttonSaveWatch);



        buttonSaveWatch.setOnClickListener(mClickListener);




        vidKey = getIntent().getStringExtra(KeySet.key_youtubeID);

        Log.v("et","et = "+getIntent().getStringExtra(key_vidTitle));
        etTitle.setText( getIntent().getStringExtra(key_vidTitle) );

        provider = getYouTubePlayerProvider();
        provider.initialize(DeveloperKey.DEVELOPER_KEY, this);

    }


    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.buttonSaveWatch:

                    saveAndSetResult();
                    finish();

                    break;

            }
        }
    };


    public void saveAndSetResult() {


        //  Toast.makeText(getApplicationContext(), "입력 내용이 저장됩니다.",Toast.LENGTH_SHORT).show();

        Intent intentForSave = new Intent();

        intentForSave.putExtra(key_vidTitle, etTitle.getText().toString() );
        intentForSave.putExtra(key_mID, getIntent().getIntExtra(key_mID, 0) );
        intentForSave.putExtra(key_youtubeID, vidKey);

        setResult(RESULT_OK, intentForSave);
    }
    /**
     * 플레이어가 초기화될 때 호출됩니다.
     * 매개변수
     * <p>
     * provider YouTubePlayer를 초기화화는 데 사용된 제공자입니다.
     * player 제공자에서 동영상 재생을 제어하는 데 사용할 수 있는 YouTubePlayer입니다
     * wasRestored
     * YouTubePlayerView 또는 YouTubePlayerFragment가 상태를 복원하는 과정의 일부로서
     * 플레이어가 이전에 저장된 상태에서 복원되었는지 여부입니다.
     * true는 일반적으로 사용자가 재생이 다시 시작될 거라고 예상하는 지점에서 재생을 다시 시작하고
     * 새 동영상이 로드되지 않아야 함을 나타냅니다.
     */


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            //player.cueVideo("wKJ9KzGQq0w"); //video id.

            youTubePlayer.loadVideo(vidKey);  //http://www.youtube.com/watch?v=IA1hox-v0jQ

            //cueVideo(String videoId)
            //지정한 동영상의 미리보기 이미지를 로드하고 플레이어가 동영상을 재생하도록 준비하지만
            //play()를 호출하기 전에는 동영상 스트림을 다운로드하지 않습니다.
            //https://developers.google.com/youtube/android/player/reference/com/google/android/youtube/player/YouTubePlayer
        }
    }

    //플레이어가 초기화되지 못할 때 호출됩니다.
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(DeveloperKey.DEVELOPER_KEY, this);
        }
    }

}