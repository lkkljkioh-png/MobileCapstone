package com.example.login; // 너 패키지명 맞게 수정

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etId, etPw;
    Button btnLogin;
    TextView tvSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // XML 이름 맞춰

        etId = findViewById(R.id.et_id);
        etPw = findViewById(R.id.et_pw);
        btnLogin = findViewById(R.id.btn_login);
        tvSignup = findViewById(R.id.tv_signup);

        // 로그인 버튼 클릭
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = etId.getText().toString();
                String pw = etPw.getText().toString();

                if (id.isEmpty() || pw.isEmpty()) {
                    Toast.makeText(MainActivity.this, "아이디와 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: 실제 로그인 로직 넣기
                    Toast.makeText(MainActivity.this, "로그인 시도", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 회원가입 이동
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}