package com.mhj.longlivemypet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    private final static String TAG = "MainActivity";
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    PetFragment petFragment;
    MapFragment mapFragment;
    HomeFragment homeFragment;
    CommunityFragment communityFragment;
    MoreFragment moreFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AutoPermissions.Companion.loadAllPermissions(this, 100);

        String email = auth.getCurrentUser().getEmail();
        firestore.collection("Users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String name = (String) task.getResult().get("name");
                    if (name == null) {
                        startActivity(new Intent(getApplicationContext(), JoinSecondActivity.class));
                    }
                }
            }
        });

        petFragment = new PetFragment();
        mapFragment = new MapFragment();
        homeFragment = new HomeFragment();
        communityFragment = new CommunityFragment();
        moreFragment = new MoreFragment();
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
                    case R.id.navigation_map:
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
                    case R.id.navigation_more:
                        menuItem.setChecked(true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, moreFragment).commit();
                        break;
                }
                return false;
            }
        });

    }



    public void replaceFragment(int resID) {
        if(resID == R.id.navigation_pet){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, petFragment).commit();
        }else if(resID == R.id.navigation_community){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, communityFragment).commit();
        }else if(resID == R.id.navigation_more){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, moreFragment).commit();
        }
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }



    @Override
    public void onDenied(int requestCode, String[] permissions){
        StringBuilder sb = new StringBuilder();
        for(String permission : permissions){
            sb.append(permission);
            sb.append(",");
            //Toast.makeText(this,sb.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGranted(int requestCode, String[] permissions){
        StringBuilder sb = new StringBuilder();
        for(String permission : permissions){
            sb.append(permission);
            sb.append(",");
            //Toast.makeText(this,sb.toString(),Toast.LENGTH_SHORT).show();
        }
    }

}
