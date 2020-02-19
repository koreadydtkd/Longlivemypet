package com.mhj.longlivemypet;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FindActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    EditText editText_findId, editText_findPw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        editText_findId = findViewById(R.id.editText_Find_Id);
        editText_findPw = findViewById(R.id.editText_Find_Pw);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        findViewById(R.id.button_Id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = editText_findId.getText().toString();
                if(phone.length() < 5){
                    Toast.makeText(FindActivity.this, "자릿수를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                firestore.collection("Users").whereEqualTo("phone", phone).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                                    UserDTO users = snapshot.toObject(UserDTO.class);
                                    if(users.getPhone().equals(phone)){
                                        StringBuilder sb = new StringBuilder();
                                        sb.append(users.getEmail());
                                        sb.replace(sb.indexOf("@") - 2 , sb.indexOf("@"), "**");
                                        Toast.makeText(FindActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                                Toast.makeText(FindActivity.this, "입력하신 내용을 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        findViewById(R.id.button_Pw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = editText_findPw.getText().toString();
                if(email == null || email.length() < 9){
                    Toast.makeText(getApplicationContext(), "다시 확인해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.sendPasswordResetEmail(email);
                Toast.makeText(getApplicationContext(), "메일을 확인해주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
