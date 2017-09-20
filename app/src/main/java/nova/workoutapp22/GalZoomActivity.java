package nova.workoutapp22;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import static nova.workoutapp22.subSources.KeySet.key_uri;

public class GalZoomActivity extends AppCompatActivity {

    Uri imgUri;
    ImageView imageViewZoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gal_zoom);



        imageViewZoom = (ImageView)findViewById(R.id.imageViewZoom);

        Intent LoadedIntent = getIntent();

        imgUri = Uri.parse ( LoadedIntent.getStringExtra(key_uri) );
        imageViewZoom.setImageURI(imgUri);
    }
}
