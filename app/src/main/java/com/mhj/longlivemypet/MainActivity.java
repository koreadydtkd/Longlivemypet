package com.mhj.longlivemypet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    PetFragment petFragment;
    MapFragment mapFragment;
    HomeFragment homeFragment;
    CommunityFragment communityFragment;
    SoundFragment soundFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String email = auth.getCurrentUser().getEmail();
        firestore.collection("Users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String name = (String) task.getResult().get("name");
                    if (name == null) {
                        Log.d(TAG, "접속한 아이디 필드이름: " + name);
                        startActivity(new Intent(getApplicationContext(), JoinSecondActivity.class));
                    }
                }
            }
        });

        petFragment = new PetFragment();
        mapFragment = new MapFragment();
        homeFragment = new HomeFragment();
        communityFragment = new CommunityFragment();
        soundFragment = new SoundFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, homeFragment).commit();
        final BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_pet:
                        menuItem.setChecked(true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, petFragment).commit();
                        break;
                    case R.id.navigation_location:
                        menuItem.setChecked(true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mapFragment).commit();
                        break;
                    case R.id.navigation_home:
                        menuItem.setChecked(true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                        break;
                    case R.id.navigation_community:
                        menuItem.setChecked(true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, communityFragment).commit();
                        break;
                    case R.id.navigation_sound:
                        menuItem.setChecked(true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, soundFragment).commit();
                        break;
                }
                return false;
            }
        });

    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

}
