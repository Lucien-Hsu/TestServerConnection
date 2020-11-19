package com.example.httpcontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.regex.MatchResult;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "mTest";
    Context context;

    //讀取網頁所需路徑
    private String webAddress = "http://192.168.59.126:8080/API/";
    private String readData = "API_Show1";
    private String addGet = "API_Insert4?";
    private String addPost = "API_Insert4";
    private String updateGet = "API_Update2?";
    private String deleteGet = "API_Delete2?";

    //查詢資料所需欄位
    private String nameString = "cName=";
    private String sexString = "cSex=";
    private String birthString = "cBirthday=";
    private String emailString = "cEmail=";
    private String phoneString = "cPhone=";
    private String addrString = "cAddr=";
    private String idString = "cID=";

    //宣告元件
    private EditText etName, etBirth, etEmail, etPhone, etAddr, etID;
    private Button btnAddGet, btnCancel, btnAddPost, btnUpdate, btnDate, btnDelete;
    private Switch switchSex;
    private TextView tvDisplay;

    private String sexData;
    private String idData;
    private StringBuilder myAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        findViews();

        doSwitchSex();
        doButtonCancel();
        //建立監聽器
        BtnListener btnListener = new BtnListener();
        btnAddGet.setOnClickListener(btnListener);
        btnAddPost.setOnClickListener(btnListener);
        btnUpdate.setOnClickListener(btnListener);
        btnDate.setOnClickListener(btnListener);
        btnDelete.setOnClickListener(btnListener);
    }

    /**
     * 按鈕監聽
     */
    private class BtnListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_add_get:

                    break;

                case R.id.btn_add_post:

                    break;

                case R.id.btn_update:

                    break;

                case R.id.btn_data:
                    new PHPReadDate().start();
                    break;

                case R.id.btn_delete:
                    if(etID.length() == 0){
                        Toast.makeText(context, "Please input ID", Toast.LENGTH_SHORT).show();
                    }else{
                        //取得URL
                        idData = etID.getText().toString();
                        myAddress = new StringBuilder();
                        //取得Authority
                        myAddress.append(webAddress);
                        //取得讀取資料庫之網頁
                        myAddress.append(deleteGet);
                        //取得條件
                        myAddress.append(idString);
                        //取得引數
                        myAddress.append(idData);
                        Log.d(TAG, "my address = " + myAddress);
                        new PHPDeleteData().start();
                    }
                    break;
            }
            Toast.makeText(context, view.getId() +"", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 另開執行緒刪除資料
     */
    private class PHPDeleteData extends Thread{
        private URL url;
        private HttpURLConnection conn;
        private int code;
        private InputStream inputStream;
        private String dataString;

        @Override
        public void run() {
            super.run();
            Log.d(TAG, "myAddress= " + myAddress);

            try {
                //取得URL
                url = new URL(myAddress.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                //取得網路連線
                conn = (HttpURLConnection)url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                //將Request設為GET
                conn.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }

            try {
                //取得回應碼
                code = conn.getResponseCode();
                Log.d(TAG, "code = " + code);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //依照回應碼做相應處理
            if(code == HttpURLConnection.HTTP_OK){
                try {
                    inputStream = conn.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //取得回傳資料之串流
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader stringReader = new BufferedReader(reader);
                try {
                    dataString = stringReader.readLine();
                    Log.d(TAG, "dataString = " + dataString);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //關閉串流
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //在主執行緒執行
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(dataString.length() == 0){
                            //若無取內容則結束
                            return;
                        }

                        //將內容顯示到螢幕
                        tvDisplay.setText(dataString);
                        Log.d(TAG, "dataString = " + dataString);
                    }
                });

            }//end OK
        }//end run
    }

    /**
     * 另開執行緒讀取資料
     */
    private class PHPReadDate extends Thread {
        private StringBuilder myAddress;
        private URL url;
        private HttpURLConnection conn;
        private int code;
        private InputStream inputStream;
        private String dataString;

        @Override
        public void run() {
            super.run();
            myAddress = new StringBuilder();
            //取得Authority
            myAddress.append(webAddress);
            //取得讀取資料庫之網頁
            myAddress.append(readData);
            Log.d(TAG, "myAddress= " + myAddress);

            try {
                //取得URL
                url = new URL(myAddress.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                //取得網路連線
                conn = (HttpURLConnection)url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                //將Request設為GET
                conn.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }

            try {
                //取得回應碼
                code = conn.getResponseCode();
                Log.d(TAG, "code = " + code);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //依照回應碼做相應處理
            if(code == HttpURLConnection.HTTP_OK){
                try {
                    inputStream = conn.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //取得回傳資料之串流
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader stringReader = new BufferedReader(reader);
                try {
                    dataString = stringReader.readLine();
                    Log.d(TAG, "dataString = " + dataString);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //關閉串流
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //在主執行緒執行
                runOnUiThread(new Runnable() {


                    private int length;
                    private JSONArray jsonArray;

                    @Override
                    public void run() {
                        if(dataString.length() == 0){
                            //若無取內容則結束
                            return;
                        }

                        //解析JSON
                        try {
                            //轉為JSON陣列
                            jsonArray = new JSONArray(dataString);
                            //取得陣列長度
                            length = jsonArray.length();
                            Log.d(TAG, "length = " + length);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        StringBuffer userData = new StringBuffer();

                        //取出陣列中所有元素
                        for(int i = 0 ; i <length ; i++){

                            JSONObject jsonObj = null;
                            try {
                                //取得第i個元素的JSON物件
                                jsonObj = jsonArray.getJSONObject(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //取出該元素的名字
                            String myName = null;
                            try {
                                myName = jsonObj.getString("cName");
                                userData.append("name = " + myName + "\n");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //取出該元素的性別
                            String mySex = null;
                            try {
                                mySex = jsonObj.getString("cSex");
                                userData.append("sex = " + mySex + "\n");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //取出該元素的生日
                            String myBirth = null;
                            try {
                                myBirth = jsonObj.getString("cBirthday");
                                userData.append("Birthday = " + myBirth + "\n");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //取出該元素的信箱
                            String myEmail = null;
                            try {
                                myEmail = jsonObj.getString("cEmail");
                                userData.append("E-mail = " + myEmail + "\n");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //取出該元素的電話
                            String myPhone = null;
                            try {
                                myPhone = jsonObj.getString("cPhone");
                                userData.append("Phone = " + myPhone + "\n");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //取出該元素的地址
                            String myAddr = null;
                            try {
                                myAddr = jsonObj.getString("cAddr");
                                userData.append("Address = " + myAddr + "\n\n");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }//end for

                        //將內容顯示到螢幕
                        tvDisplay.setText(userData);
                        Log.d(TAG, "userData = " + userData);
                    }
                });

            }//end OK
        }//end run
    }//end Thread

    /**
     * "取消按鈕"監聽
     */
    private void doButtonCancel() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etName.setText("");
                etBirth.setText("");
                etEmail.setText("");
                etPhone.setText("");
                etAddr.setText("");
                etID.setText("");
                sexData = "M";
            }
        });
    }

    /**
     * Switch監聽
     */
    private void doSwitchSex() {
        //設定switch監聽
        switchSex.setChecked(false);
        switchSex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked == true) {
                    sexData = "F";
                }else {
                    sexData = "M";
                }
                Toast.makeText(context, sexData, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 取得元件
     */
    private void findViews() {
        //EditText
        etName = findViewById(R.id.et_name);
        etBirth = findViewById(R.id.et_birth);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etAddr = findViewById(R.id.et_addr);
        etID = findViewById(R.id.et_id);
        //按鈕
        btnAddGet = findViewById(R.id.btn_add_get);
        btnCancel = findViewById(R.id.btn_cancel);
        btnAddPost = findViewById(R.id.btn_add_post);
        btnUpdate = findViewById(R.id.btn_update);
        btnDate = findViewById(R.id.btn_data);
        btnDelete = findViewById(R.id.btn_delete);
        //Switch
        switchSex = findViewById(R.id.switch_sex);
        //TextView
        tvDisplay = findViewById(R.id.tv_display);
    }


}