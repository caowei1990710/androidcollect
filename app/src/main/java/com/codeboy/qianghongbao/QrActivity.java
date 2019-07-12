package com.codeboy.qianghongbao;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.codeboy.qianghongbao.util.BitmapUtils;

/**
 * Created by snsoft on 3/5/2017.
 */
//我 600，1200 + 700,100 收付款 500,500 收款 1100，500 設置金額 200，870
public class QrActivity extends Activity {
    private Handler handler;
    private ImageView qr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_activtiy);
        handler = new Payhandler();
        qr = (ImageView)findViewById(R.id.qr);
        handler.sendEmptyMessageDelayed(0, 1000);
//        BitmapUtils.execShellCmd("getevent -p");
//        BitmapUtils.execShellCmd("sendevent /dev/input/event0 1 158 1");
//        BitmapUtils.execShellCmd("sendevent /dev/input/event0 1 158 0");
//        BitmapUtils.execShellCmd("input keyevent 3");//home
//        BitmapUtils.execShellCmd("input text  'helloworld!' ");


//        BitmapUtils.execShellCmd("input swipe 100 250 200 280");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("resultCode","requestCode:"+requestCode +" resultCode:"+resultCode + " data:"+data);
        if (resultCode == Activity.RESULT_OK) {
            /**
             * 当选择的图片不为空的话，在获取到图片的途径
             */
            Uri uri = data.getData();
            Log.e("Qr", "uri = " + uri);
            try {
                String[] pojo = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(uri, pojo, null, null, null);
                if (cursor != null) {
                    ContentResolver cr = this.getContentResolver();
                    int colunm_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    //获取图片的路径
                    String path = cursor.getString(colunm_index);
                    /***
                     * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话
                     * ，你选择的文件就不一定是图片了，这样的话，我们判断文件的后缀名
                     * 如果是图片格式的话，那么才可以
                     */
                    if (path.endsWith("jpg") || path.endsWith("png")) {
//                        picPath = path;
                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        qr.setImageBitmap(bitmap);
                    } else {
//                        alert();
                    }
                } else {
//                    alert();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class Payhandler extends Handler {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
//                    Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
//                    startActivity(LaunchIntent);
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            BitmapUtils.execShellCmd("input tap 600 1200");
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    BitmapUtils.execShellCmd("input tap 700 100");
//                                    handler.postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            BitmapUtils.execShellCmd("input tap 500 500");
//                                            handler.postDelayed(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    BitmapUtils.execShellCmd("input tap 500 1100");
//                                                    handler.postDelayed(new Runnable() {
//                                                        @Override
//                                                        public void run() {
//                                                            BitmapUtils.execShellCmd("input tap 200 870");
//                                                            handler.postDelayed(new Runnable() {
//                                                                @Override
//                                                                public void run() {
//                                                                    BitmapUtils.execShellCmd("input text  '100' ");
//                                                                    handler.postDelayed(new Runnable() {
//                                                                        @Override
//                                                                        public void run() {
//                                                                            BitmapUtils.execShellCmd("input tap 100 480");
//                                                                            handler.postDelayed(new Runnable() {
//                                                                                @Override
//                                                                                public void run() {
//                                                                                    BitmapUtils.execShellCmd("input text  'wo' ");
//                                                                                    handler.postDelayed(new Runnable() {
//                                                                                        @Override
//                                                                                        public void run() {
//                                                                                            BitmapUtils.execShellCmd("input tap 500 800");
//                                                                                            handler.postDelayed(new Runnable() {
//                                                                                                @Override
//                                                                                                public void run() {
//                                                                                                    BitmapUtils.execShellCmd("input tap 100 550");
//                                                                                                    handler.postDelayed(new Runnable() {
//                                                                                                        @Override
//                                                                                                        public void run() {
//                                                                                                            BitmapUtils.execShellCmd("input tap 500 1000");
//                                                                                                            handler.postDelayed(new Runnable() {
//                                                                                                                @Override
//                                                                                                                public void run() {
//                                                                                                                    BitmapUtils.getImageFromCamera(QrActivity.this);
//                                                                                                                }
//                                                                                                            }, 10000);
//                                                                                                        }
//                                                                                                    }, 1000);
//                                                                                                }
//                                                                                            }, 1000);
//                                                                                        }
//                                                                                    }, 1000);
//                                                                                }
//                                                                            }, 1000);
//                                                                        }
//                                                                    }, 1000);
////                                                                    handler.postDelayed(new Runnable() {
////                                                                        @Override
////                                                                        public void run() {
////                                                                            BitmapUtils.execShellCmd("input tap 50 50");
////                                                                            handler.postDelayed(new Runnable() {
////                                                                                @Override
////                                                                                public void run() {
////                                                                                    BitmapUtils.execShellCmd("input tap 500 860");
////                                                                                }
////                                                                            },1000);
////                                                                        }
////                                                                    },1000);
//                                                                }
//                                                            }, 1000);
//                                                        }
//                                                    }, 1000);
//                                                }
//                                            }, 1000);
//                                        }
//                                    }, 1000);
//                                }
//                            }, 1000);
//                        }
//                    }, 1000);
//
//                    break;
//                case 1:
//                    break;
//            }
        }
//    }
}
