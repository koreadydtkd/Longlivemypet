package com.mhj.longlivemypet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.io.InputStream;


public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    private final static String TAG = "MainActivity";
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    PetFragment petFragment;
    MapFragment mapFragment;
    HomeFragment homeFragment;
    CommunityFragment communityFragment;
    SoundFragment soundFragment;
    Bitmap bitmap;
    ImageView setImageBitmap;

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
                    case R.id.navigation_sound:
                        menuItem.setChecked(true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, soundFragment).commit();
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

    public void accessGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 92);
    }

    public void setImg(ImageView imageView){
        setImageBitmap = imageView;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 92) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(in);
                    in.close();
                    setImageBitmap.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "사진 선택을 취소하였습니다", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
