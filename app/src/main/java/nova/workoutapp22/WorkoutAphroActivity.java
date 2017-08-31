package nova.workoutapp22;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class WorkoutAphroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_aphro);

        Button button = (Button) findViewById(R.id.AphroBackButton);
        button.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent();
                intent.putExtra("name","mike");
                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }



}
