package nova.workoutapp22;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MotivationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motivation);

        Toolbar toolbarMotivation = (Toolbar) findViewById(R.id.toolBarMotivation);
        setSupportActionBar(toolbarMotivation);


        ImageButton buttonGal = (ImageButton) findViewById(R.id.buttonGallery);
        ImageButton buttonVid = (ImageButton) findViewById(R.id.buttonVid);

        buttonGal.setOnClickListener(mClickListener);
        buttonVid.setOnClickListener(mClickListener);


    }


    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.buttonGallery:
                    intent = new Intent(getApplicationContext(), GalActivity.class);
                    startActivity(intent);
                    break;
                case R.id.buttonVid:

                    break;

            }
        }
    };


}
