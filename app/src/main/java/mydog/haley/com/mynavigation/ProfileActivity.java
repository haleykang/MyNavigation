package mydog.haley.com.mynavigation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;

// 프로필 화면... 나중에 다시... 넘 힘들어..

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "**ProfileActivity**";
    static final int REQUEST_IMAGE_PICK = 2001;
    /*static final int REQUEST_IMAGE_CAPTURE = 2002;*/
    static final int REQUEST_IMAGE_CROP = 2003;
    private CircleImageView imageView;
   /* private Uri mUri;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imageView = (CircleImageView)findViewById(R.id.profile_image);


        ImageView bt = (ImageView)findViewById(R.id.add_photo_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPictureIntent();
            }
        });


    }


    // 사진 가져오기
    private void pickPictureIntent() {
        Intent pickPictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickPictureIntent.setType("image/*");
        if(pickPictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pickPictureIntent, REQUEST_IMAGE_PICK);
        }
    }
/*

    // 사진 촬영 -> 사진 촬영이 안드로이드 7.0에서 오류 나는듯.. 일단 삭제
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "edp_image.jpg"));
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
*/


    // 사진 크롭
    private void cropPictureIntent(Uri uri) {
        Intent cropPictureIntent = new Intent("com.android.camera.action.CROP");
        cropPictureIntent.setDataAndType(uri, "image/*");
        cropPictureIntent.putExtra("outputX", 500); // crop한 이미지의 x축 크기 (integer)
        cropPictureIntent.putExtra("outputY", 500); // crop한 이미지의 y축 크기 (integer)
        cropPictureIntent.putExtra("aspectX", 5); // crop 박스의 x축 비율 (integer)
        cropPictureIntent.putExtra("aspectY", 5); // crop 박스의 y축 비율 (integer)
        cropPictureIntent.putExtra("scale", true);
        cropPictureIntent.putExtra("return-data", true);
        if(cropPictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cropPictureIntent, REQUEST_IMAGE_CROP);
        }
    }

    // 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            switch(requestCode) {
                case REQUEST_IMAGE_PICK: // dispatchPickPictureIntent() 결과
                    cropPictureIntent(data.getData());
                    Log.v(TAG, "Uri : " + data.getData());
                    Uri uri = data.getData();
                    String path = uri.getPath();
                    Log.v(TAG, "Path : " + path);
                    break;
            /*    case REQUEST_IMAGE_CAPTURE: // dispatchTakePictureIntent() 결과
                    cropPictureIntent(mUri);
                    break;*/
                case REQUEST_IMAGE_CROP: // dispatchCropPictureIntent() 결과
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap)extras.get("data");
                    imageView.setImageBitmap(imageBitmap);
                    break;
            }
        }
    }




    // 아래 코드는 서버에 올리는 것 까지
    // 나는 갤러리에서 가져와서 데이터베이스에 저장만 할거임

/*
    // 사진 촬영
    public void captureCamera() {

        if(permission.isCheck("Camera")) {

            String state = Environment.getExternalStorageState();

            if(Environment.MEDIA_MOUNTED.equals(state)) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if(takePictureIntent.resolveActivity(getPackageManager()) != null) {

                    File photoFile = null;
                    try {
                        // createImageFile
                        photoFile = createImageFile();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                    if(photoFile != null) {
                        photoURI = FileProvider.getUriForFile(this, "mydog.haley.com.mynavigation", photoFile);
                        Log.v(TAG, "photoFile : " + photoFile.toString());
                        Log.v(TAG, "photoURI : " + photoURI.toString());

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQ_TAKE_PHOTO);

                    }
                }

            } else {
                Toast.makeText(ProfileActivity.this, "외장 메모리 미 지원", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(ProfileActivity.this, "저장소 권한 설정에 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    } // end of captureCamera()

    // 파일 생성
    private File createImageFile() throws IOException {
        // 이미지 파일 이름 생성
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/pathvalue/" + imageFileName);

        // 파일 저장
        mCurrentPhotoPath = storageDir.getAbsolutePath();
        Log.v(TAG, "mCurrentPhotoPath : " + mCurrentPhotoPath);
        return storageDir;

    }

    // 앨범 열기
    public void getAlbum() {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQ_TAKE_ALBUM);
    }

    // 이미지 크롭
    public void cropImage() {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        cropIntent.setDataAndType(photoURI, "image*//*");
        cropIntent.putExtra("scale", true);

       *//* if(isAlbum == false) {
            cropIntent.putExtra("output", photoURI); // 크롭된 이미지를 해당 경로에 저장
        } else if(isAlbum == true) {
            cropIntent.putExtra("output", albumURI); // 크롭된 이미지를 해당 경로에 저장

        }*//*
        cropIntent.putExtra("output", albumURI); // 크롭된 이미지를 해당 경로에 저장

        startActivityForResult(cropIntent, REQ_IMAGE_CROP);

    }

    // 동기화
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    // onActivityResult


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQ_TAKE_PHOTO:
                isAlbum = false;
                galleryAddPic();
                uploadFile(mCurrentPhotoPath);
                break;
            case REQ_TAKE_ALBUM:
                isAlbum = true;
                File albumFile = null;
                try {
                    albumFile = createImageFile();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                if(albumFile != null) {
                    albumURI = Uri.fromFile(albumFile);
                }
                photoURI = data.getData();
                cropImage();
                break;
            case REQ_IMAGE_CROP:
                galleryAddPic();
                // 업로드
                uploadFile(mCurrentPhotoPath);
                break;
        }

    }*/
}


