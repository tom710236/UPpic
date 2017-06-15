package com.example.tom.uppic;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CONTACTS = 1;
    Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openPic();

    }

    //取得 權限
    private void openPic() {
        int permission = ActivityCompat.checkSelfPermission(this,
                WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(this,
                READ_EXTERNAL_STORAGE);
        int permission3 = ActivityCompat.checkSelfPermission(this,
                CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED || permission3 != PackageManager.PERMISSION_GRANTED) {
            //若尚未取得權限，則向使用者要求允許聯絡人讀取與寫入的權限，REQUEST_CONTACTS常數未宣告則請按下Alt+Enter自動定義常數值。
            ActivityCompat.requestPermissions(this,
                    new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA},
                    REQUEST_CONTACTS);
        }
    }
    //拍照
    public void onTake(View v) {
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  //建立動作為拍照的意圖
        startActivityForResult(it, 100);   //啟動意圖並要求傳回資料

    }
    //上傳
    public void onUp(View v) {
        Post post = new Post();
        post.run();
    }

    //拍照後的預覽畫面設定
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            Bundle extras = data.getExtras();         //將 Intent 的附加資料轉為 Bundle 物件
            Bitmap bmp = (Bitmap) extras.get("data"); //由 Bundle 取出名為 "data" 的 Bitmap 資料
            ImageView imv = (ImageView) findViewById(R.id.imageView);
            imv.setImageBitmap(bmp);                  //將 Bitmap 資料顯示在 ImageView 中
            imgUri = convertUri(data.getData());  //取得選取相片的 Uri 並做 Uri 格式轉換
            //要上傳照片的Uri
            Log.e("imgUri", valueOf(imgUri));
        }

    }


    Uri convertUri(Uri uri) {
        if (uri.toString().substring(0, 7).equals("content")) {  //如果是以 "content" 開頭
            String[] colName = {MediaStore.MediaColumns.DATA};    //宣告要查詢的欄位
            Cursor cursor = getContentResolver().query(uri, colName,  //以 imgUri 進行查詢
                    null, null, null);
            cursor.moveToFirst();      //移到查詢結果的第一筆記錄
            uri = Uri.parse("file://" + cursor.getString(0)); //將路徑轉為 Uri
            cursor.close();     //關閉查詢結果
        }
        return uri;   //傳回 Uri 物件
    }

    // 執行緒 - 執行PostUserInfo()方法
    class Post extends Thread {
        @Override
        public void run() {
            //上傳 照片的方法
            Postfile();
        }

        private void Postfile() {
        }

    }
}