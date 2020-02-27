package com.mhj.longlivemypet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class JoinActivity extends AppCompatActivity {
    EditText editText_Email, editText_Pw, editText_Pw_Check;
    CheckBox checkBox, checkBox2;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        editText_Email = findViewById(R.id.editText_Email);
        editText_Pw = findViewById(R.id.editText_Pw);
        editText_Pw_Check = findViewById(R.id.editText_Pw_Check);
        checkBox = findViewById(R.id.checkbox);
        checkBox2 = findViewById(R.id.checkbox2);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        findViewById(R.id.button_Join_Ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userjoin();
            }
        });

    }//onCreate

    public void userjoin() { //회원가입
        final String email = editText_Email.getText().toString();
        final String pw = editText_Pw.getText().toString();
        final String pw_check = editText_Pw_Check.getText().toString();

        if(email.length() < 10 || pw_check.length() < 6){
            Toast.makeText(JoinActivity.this, "아이디 또는 비밀번호를 다시 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!pw.equals(pw_check)){
            Toast.makeText(JoinActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!checkBox.isChecked() || !checkBox2.isChecked()){
            Toast.makeText(JoinActivity.this, "동의후에 가입이 가능합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, pw_check).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String nick = null;
                    String name = null;
                    String birth = null;
                    String phone = null;
                    boolean sex = false; // true=여자, false=남자
                    UserDTO users = new UserDTO(email, nick, name, birth, phone, sex);
                    firestore.collection("Users").document(email).set(users);
                    startActivity(new Intent(getApplicationContext(), JoinSecondActivity.class));
                }else{
                    Log.e("JoinActivity_Error", task.getException().toString());
                    Toast.makeText(JoinActivity.this, "아이디 또는 비밀번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

}