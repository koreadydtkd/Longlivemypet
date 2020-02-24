package com.mhj.longlivemypet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class JoinSecondActivity extends AppCompatActivity {
    EditText editText_Nick, editText_Name, editText_Birth, editText_Phone;
    RadioButton radioButton; // true = 여자, false = 남자
    boolean nick_ck = true;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_second);

        editText_Nick = findViewById(R.id.editText_Nick);
        editText_Name = findViewById(R.id.editText_Name);
        radioButton = findViewById(R.id.radiobutton_W);
        editText_Birth = findViewById(R.id.editText_Birth);
        editText_Phone = findViewById(R.id.editText_Phone);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        findViewById(R.id.button_Nick_Check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nick_check();
            }
        });

        findViewById(R.id.button_Join_Ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join_ok();
            }
        });

    }

    private void nick_check() { //별명 중복체크
        if(editText_Nick.length() < 2){
            Toast.makeText(JoinSecondActivity.this, "2자 이상입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        final String nick = editText_Nick.getText().toString();
        firestore.collection("Users").whereEqualTo("nick", nick).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                            UserDTO users = snapshot.toObject(UserDTO.class);
                            if(users.getNick().equals(nick)){
                                Toast.makeText(JoinSecondActivity.this, "중복된 별명입니다", Toast.LENGTH_SHORT).show();
                                nick_ck = true;
                                return;
                            }
                        }
                        Toast.makeText(JoinSecondActivity.this, "사용가능한 별명입니다", Toast.LENGTH_SHORT).show();
                        nick_ck = false;
               }
        });

    }

    public void join_ok() {
        if(nick_ck){
            Toast.makeText(JoinSecondActivity.this, "별명을 중복확인해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        final String email = auth.getCurrentUser().getEmail();
        final String nick = editText_Nick.getText().toString();
        if(nick.length() < 2){
            Toast.makeText(JoinSecondActivity.this, "2자이상 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        final String name = editText_Name.getText().toString();
        if(name.length() < 2){
            Toast.makeText(JoinSecondActivity.this, "잘못된 입력입니다.\n다시 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        final String birth = editText_Birth.getText().toString();
        if(birth.length() < 6){
            Toast.makeText(JoinSecondActivity.this, "생년월일 앞 6자리를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        final String phone = editText_Phone.getText().toString();
        if(phone.length() < 10){
            Toast.makeText(JoinSecondActivity.this, "잘못된 입력입니다.\n다시 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        final boolean sex = radioButton.isChecked();

        UserDTO userDTO = new UserDTO(email, nick, name, birth, phone, sex);
        firestore.collection("Users").document(email).set(userDTO);
        Toast.makeText(JoinSecondActivity.this, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

}
