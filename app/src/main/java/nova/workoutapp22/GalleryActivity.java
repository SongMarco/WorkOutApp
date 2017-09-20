package nova.workoutapp22;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import nova.workoutapp22.listviewSrcForGallery.GalAdapter;
import nova.workoutapp22.listviewSrcForGallery.GalItem;
import nova.workoutapp22.subSources.BasicInfo;

import static nova.workoutapp22.R.id.gridViewGal;
import static nova.workoutapp22.subSources.BasicInfo.CROP_FROM_IMAGE;
import static nova.workoutapp22.subSources.BasicInfo.PICK_FROM_ALBUM;
import static nova.workoutapp22.subSources.BasicInfo.PICK_FROM_CAMERA;

public class GalleryActivity extends AppCompatActivity {

    Toolbar toolbarGallery;

    String woMenuState = BasicInfo.MENU_WO_NORMAL;

    GalAdapter galAdapter;

    GridView gridViewForGal;

    private Uri mImageCaptureUri, cropImageUri;
    private String absolutePath;

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

        for(int i = 0; i<5; i++){
            galAdapter.addItem(new GalItem( Uri.parse(resDrawableUri)) );


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
                Log.d("SmartWheel", mImageCaptureUri.getPath().toString());
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
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/tempCrop";
        File temp_crop = new File(dirPath);

        if(!temp_crop.exists()) // tempCrop 디렉터리에 폴더가 없다면 (새로 이미지를 저장할 경우에 속한다.)
            temp_crop.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {

            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));


            //////////////////////uri from file을 이용, 자른 이미지의 uri를 얻어옴
            cropImageUri = Uri.fromFile(copyFile);
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



}
