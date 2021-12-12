package com.example.diary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diary.DB.DBHelper;
import com.example.diary.DB.DataBases;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private EditText login_id, login_pw;
    private Button btn_signup, btn_login;
    private String ID, PW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_id = findViewById(R.id.login_id);
        login_pw = findViewById(R.id.login_pw);
        btn_signup = findViewById(R.id.btn_signup);
        btn_login = findViewById(R.id.btn_login);

        dbHelper = new DBHelper(LoginActivity.this, DataBases.CreateDB._DATABASE, null, 1);

        Date from = new Date();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = transFormat.format(from);

        //회원가입 버튼 클릭 리스너
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_id.setText("");
                login_pw.setText("");
                //회원가입 Activity로 이동
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        //로그인 버튼 클릭 리스너
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = String.valueOf(login_id.getText());
                PW = String.valueOf(login_pw.getText());

                if(ID.equals("") || PW.equals("")) {
                    Toast.makeText(LoginActivity.this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    String result = dbHelper.getResult_userInfo(1, ID);
                    result = result != "" ? result : "데이터 없음";
                    Log.d("진입LoginResult", result);

                    if(result.contains("데이터 없음")) {
                        Toast.makeText(LoginActivity.this, "사용자 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        String split_data[] = result.split("/");
                        Log.d("진입LoginSplitData", split_data[0] + "," + PW + "," + split_data[1]);
                        if(PW.equals(split_data[1].trim())) {
                            Toast.makeText(LoginActivity.this, split_data[0] + "님 환영합니다.", Toast.LENGTH_SHORT).show();
                            //MainActivity로 이동
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("Name", split_data[0]);
                            intent.putExtra("Date", date);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}