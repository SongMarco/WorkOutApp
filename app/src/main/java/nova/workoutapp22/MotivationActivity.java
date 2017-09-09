package nova.workoutapp22;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MotivationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motivation);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolBarMotivation);
        setSupportActionBar(myToolbar);
    }
}
