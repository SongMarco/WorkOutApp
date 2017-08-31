package nova.workoutapp22;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class sampleChooseImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_choose_image);
    }

    public void onButton11Clicked(View v){
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }
}
