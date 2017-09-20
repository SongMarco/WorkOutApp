package nova.workoutapp22;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import nova.workoutapp22.listviewSrcForGallery.GalAdapter;
import nova.workoutapp22.listviewSrcForGallery.GalItem;
import nova.workoutapp22.listviewSrcForWorkOut.WorkoutItem;
import nova.workoutapp22.subSources.BasicInfo;

import static nova.workoutapp22.R.id.gridViewGal;
import static nova.workoutapp22.subSources.BasicInfo.CROP_FROM_IMAGE;
import static nova.workoutapp22.subSources.BasicInfo.PICK_FROM_ALBUM;
import static nova.workoutapp22.subSources.BasicInfo.PICK_FROM_CAMERA;
import static nova.workoutapp22.subSources.KeySet.PREF_GAL;

public class GalleryActivity extends AppCompatActivity {

    Toolbar toolbarGallery;

    String woMenuState = BasicInfo.MENU_WO_NORMAL;

    GalAdapter galAdapter;

    GridView gridViewForGal;

    private Uri mImageCaptureUri, cropImageUri;
    private String absolutePath;

    boolean isItemAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);


        ///////////////////////툴바를 만듭니다
        toolbarGallery = (Toolbar) findViewById(R.id.toolbarGallery);
        setSupportActionBar(toolbarGallery);
        /////////////////////////////////////////

        ///리스트뷰의 어댑터 세팅하기.
        galAdapter = new GalAdapter();
        gridViewForGal = (GridView) findViewById(gridViewGal);
        gridViewForGal.setAdapter(galAdapter);


        String resDrawableUri = "android.resource://" + getApplicationContext().getPackageName() + "/drawable/basicimage";

        for (int i = 0; i < 5; i++) {
            galAdapter.addItem(new GalItem(Uri.parse(resDrawableUri)));


        }


        /////////////////////


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_workout, menu);


        //멀미모드임
        if (woMenuState.equals(BasicInfo.MENU_WO_MULT)) {


//            toolbarWorkoutActivity.getChildAt(1).startAnimation(fadeIn);
            menu.findItem(R.id.action_addItem).setVisible(false);
            menu.findItem(R.id.action_delete).setVisible(true);
            menu.findItem(R.id.action_selectAll).setVisible(true);
            menu.findItem(R.id.action_clearSelection).setVisible(true);


        }
        //싱글모드임임
        else {


//            toolbarWorkoutActivity.getChildAt(1).startAnimation(fadeIn);
            menu.findItem(R.id.action_addItem).setVisible(true);
            menu.findItem(R.id.action_delete).setVisible(false);
            menu.findItem(R.id.action_selectAll).setVisible(false);
            menu.findItem(R.id.action_clearSelection).setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_addItem:

                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakePhotoAction();
                    }
                };
                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakeAlbumAction();
                    }
                };

                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };

                new AlertDialog.Builder(this)
                        .setTitle("업로드할 이미지 선택")
                        .setPositiveButton("사진촬영", cameraListener)
                        .setNeutralButton("앨범선택", albumListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();

//
//                editFlag = true;
//                Toast.makeText(getApplicationContext(), "edit Flag = "+editFlag, Toast.LENGTH_SHORT).show();



                return true;

            case R.id.action_selectMult:


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


    public void doTakePhotoAction() // 카메라 촬영 후 이미지 가져오기
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시로 사용할 파일의 경로를 생성
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    public void doTakeAlbumAction() // 앨범에서 이미지 가져오기
    {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case PICK_FROM_ALBUM: {
                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.
                mImageCaptureUri = data.getData();

                galAdapter.addItem(new GalItem(mImageCaptureUri));
                galAdapter.notifyDataSetChanged();
                isItemAdded = true;
                Log.d("SmartWheel", mImageCaptureUri.getPath().toString());

                break;
            }

            case PICK_FROM_CAMERA: {


                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                // CROP할 이미지를 200*200 크기로 저장
                intent.putExtra("outputX", 200); // CROP한 이미지의 x축 크기
                intent.putExtra("outputY", 200); // CROP한 이미지의 y축 크기
                intent.putExtra("aspectX", 1); // CROP 박스의 X축 비율
                intent.putExtra("aspectY", 1); // CROP 박스의 Y축 비율
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_IMAGE); // CROP_FROM_CAMERA case문 이동
                break;
            }

            case CROP_FROM_IMAGE: {
                // 크롭이 된 이후의 이미지를 넘겨 받습니다.
                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                // 임시 파일을 삭제합니다.
                if (resultCode != RESULT_OK) {
                    return;
                }

                final Bundle extras = data.getExtras();

                // CROP된 이미지를 저장하기 위한 FILE 경로
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/TempCrop/" + System.currentTimeMillis() + ".jpg";

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data"); // CROP된 BITMAP

                    storeCropImage(photo, filePath); // CROP된 이미지를 외부저장소, 앨범에 저장한다.
                    absolutePath = filePath;
                    break;
                }


            }
        }
    }


    private void storeCropImage(Bitmap bitmap, String filePath) {
        // tempCrop 폴더를 생성하여 이미지를 저장하는 방식이다.
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tempCrop";
        File temp_crop = new File(dirPath);

        if (!temp_crop.exists()) // tempCrop 디렉터리에 폴더가 없다면 (새로 이미지를 저장할 경우에 속한다.)
            temp_crop.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {

            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));


            //////////////////////uri from file을 이용, 자른 이미지의 uri를 얻어옴
            cropImageUri = Uri.fromFile(copyFile);


            //////////////////// uri를 얻어 자른 이미지를 곧바로 추가한다.

            galAdapter.addItem(new GalItem(cropImageUri));
            isItemAdded = true;
            galAdapter.notifyDataSetChanged();

            //todo 생명주기 적용하기
//            saveState();


            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);


            // sendBroadcast를 통해 Crop된 사진을 앨범에 보이도록 갱신한다.
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(copyFile)));




            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        saveState();

    }

    @Override
    protected void onResume() {


        super.onResume();
        //아이템이 추가되지 않았을 때에만 리스토어 해라.
        if(!isItemAdded){
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
        SharedPreferences prefForGal = getSharedPreferences(PREF_GAL, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefForGal.edit();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriSerializer())
                .create();

        ArrayList<WorkoutItem> saveArray;
        saveArray = (ArrayList<WorkoutItem>) galAdapter.items.clone();


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


        SharedPreferences prefForGal = getSharedPreferences(PREF_GAL, Activity.MODE_PRIVATE);


        if ((prefForGal != null) && (prefForGal.contains("arrayList"))) {


            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Uri.class, new UriDeserializer())
                    .create();


            String json = prefForGal.getString("arrayList", null);

            //Type type = TypeToken.getParameterized( (ArrayList<MemoItem>) , ).getType();

            ArrayList<GalItem> loadArray = null;

            loadArray = gson.fromJson(json, new TypeToken<ArrayList<GalItem>>() {
            }.getType());


            galAdapter.items = (ArrayList<GalItem>) loadArray.clone();


            // mID를 세팅해줘야 아이템클릭(수정에 사용)이 제대로된다.
            for (int i = 0; i < galAdapter.getCount(); i++) {
                ((GalItem) galAdapter.getItem(i)).mID = i;
            }
        }



    }

    protected void clearMyPrefs() {
        //    Toast.makeText(getApplicationContext(), "pref cleared", Toast.LENGTH_SHORT).show();

        SharedPreferences prefForGal = getSharedPreferences(PREF_GAL, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefForGal.edit();
        editor.clear();
        editor.commit();
    }

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

}
