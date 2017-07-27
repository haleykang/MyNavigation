package mydog.haley.com.mynavigation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by user on 2017-07-27.
 */

public class PermissionCheck {

    public static final int MY_PERMISSION_CAMERA = 1;
    public static final int MY_PERMISSION_DEVICE = 2;
    public static final int MY_PERMISSION_LOCATION = 3;
    public static final int MY_PERMISSION_SMS = 4;
    public static final int MY_PERMISSION_IMAGE = 5;

    Context mContext;

    public PermissionCheck(Context context) {
        mContext = context;
    }


    // 이 코드는 계속 사용되니 복사해서 정리
    public boolean isCheck(String permission) {

        switch(permission) {
            case "Camera": {

                Log.v("Camera Permission", "CALL");
                // 만약 카메라에 대한 권한 & EXTERNAL_STORAGE 사용에 대한 권한이 주어지지 않았다면
                if(ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // 다시 보기 않기 버튼을 만드려면, else 말고 이 부분에 바로 요청을 하도록
                    /*
                    ActivityCompat.requestPermissions((Activity)mContext,
                                new String[]{ Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE },
                                MY_PERMISSION_CAMERA);
                     */
                    if(ActivityCompat.shouldShowRequestPermissionRationale((Activity)mContext,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        new AlertDialog.Builder(mContext)
                                .setTitle("알림")
                                .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 허용해주세요.")
                                .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.parse("package:mydog.haley.com.mynavigation"));
                                        mContext.startActivity(intent);
                                    }
                                })
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ((Activity)mContext).finish();
                                    }
                                })
                                .setCancelable(false)
                                .create()
                                .show();

                    } else {
                        ActivityCompat.requestPermissions((Activity)mContext,
                                new String[]{ Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE },
                                MY_PERMISSION_CAMERA);
                    }

                } else {

                    return true;
                }

            } // end of case Camera
            break;
        } // end of switch

        return false;

    } // end of isCheck


}
