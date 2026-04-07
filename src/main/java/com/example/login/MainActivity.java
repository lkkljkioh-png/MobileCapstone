package com.example.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText et_id, et_pw;
    Button btn_login;
    TextView tv_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pw);
        btn_login = findViewById(R.id.btn_login);
        tv_signup=findViewById(R.id.tv_signup);

        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);

        btn_login.setOnClickListener(v -> {

            String inputId = et_id.getText().toString();
            String inputPw = et_pw.getText().toString();

            String savedId = sp.getString("id", "");
            String savedPw = sp.getString("pw", "");

            if (inputId.equals(savedId) && inputPw.equals(savedPw)) {
                Intent intent = new Intent(MainActivity.this, LoadingActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "아이디나 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        tv_signup.setOnClickListener(v -> {
            Intent intent=new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}