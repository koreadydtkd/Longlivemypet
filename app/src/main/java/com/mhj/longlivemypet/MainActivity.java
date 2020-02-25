package com.mhj.longlivemypet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    PetFragment petFragment;
    MapFragment mapFragment;
    HomeFragment homeFragment;
    CommunityFragment communityFragment;
    MoreFragment moreFragment;
    PetCalendarFragment petCalendarFragment;

    private final static int ID_PET = 1;
    private final static int ID_MAP = 2;
    private final static int ID_HOME = 3;
    private final static int ID_FORUM = 4;
    private final static int ID_MORE = 5;

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
        petCalendarFragment = new PetCalendarFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, homeFragment).commit();

        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_PET, R.drawable.ic_person));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_MAP, R.drawable.ic_map));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_FORUM, R.drawable.ic_forum));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_MORE, R.drawable.more));

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {
                    case ID_PET:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, petFragment).commit();
                        break;
                    case ID_MAP:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mapFragment).commit();
                        break;
                    case ID_HOME:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                        break;
                    case ID_FORUM:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, communityFragment).commit();
                        break;
                    case ID_MORE:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, moreFragment).commit();
                        break;
                }
            }
        });

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
           @Override
           public void onShowItem(MeowBottomNavigation.Model item) {
               String name;
               switch (item.getId()) {
                   case ID_PET:
                       name = "PET";
                       break;
                   case ID_MAP:
                       name = "MAP";
                       break;
                   case ID_HOME:
                       name = "HOME";
                       break;
                   case ID_FORUM:
                       name = "FORUM";
                       break;
                   case ID_MORE:
                       name = "MORE";
                       break;
                   default:
                       name = "";
               }
           }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                return;
            }
        });

        bottomNavigation.show(ID_HOME, true);

    }

    public void replaceFragment(int resID) {
        if(resID == R.id.navigation_pet){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, petFragment).commit();
        }else if(resID == R.id.navigation_community){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, communityFragment).commit();
        }else if(resID == R.id.navigation_more){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, moreFragment).commit();
        }else if(resID == R.layout.fragment_pet_calendar){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, petCalendarFragment).commit();
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

    }

    @Override
    public void onGranted(int requestCode, String[] permissions){

    }

}
