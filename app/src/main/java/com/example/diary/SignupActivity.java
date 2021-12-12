package com.example.diary;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diary.DB.DBHelper;
import com.example.diary.DB.DataBases;

public class SignupActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private EditText signup_name, signup_id, signup_pw, signup_re_pw;
    private Button btn_signup;
    private String Name, ID, PW, RE_PW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        signup_name = findViewById(R.id.signup_name);
        signup_id = findViewById(R.id.signup_id);
        signup_pw = findViewById(R.id.signup_pw);
        signup_re_pw = findViewById(R.id.signup_re_pw);
        btn_signup = findViewById(R.id.btn_signup);

        dbHelper = new DBHelper(SignupActivity.this, DataBases.CreateDB._DATABASE, null, 1);
        dbHelper.createTable(); //첫 사용자가 등록됨과 동시에 테이블이 생성되게 하기 위함

        //회원가입 버튼 클릭 리스너
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name = String.valueOf(signup_name.getText());
                ID = String.valueOf(signup_id.getText());
                PW = String.valueOf(signup_pw.getText());
                RE_PW = String.valueOf(signup_re_pw.getText());

                if(Name.equals("") || ID.equals("") || PW.equals("")) {
                    Toast.makeText(SignupActivity.this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if(!RE_PW.equals(PW)) {
                    Toast.makeText(SignupActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    String result = dbHelper.getResult_userInfo(0, ID);
                    result = result != "" ? result : "데이터 없음";
                    Log.d("진입SignupResult", result);

                    if(result.contains("데이터 없음")) {
                        dbHelper.insert_userSignup(Name, ID, PW);
                        Toast.makeText(SignupActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        finish(); //다시 로그인 Activity로 이동
                    } else {
                        Toast.makeText(SignupActivity.this, "이미 존재하는 ID입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}