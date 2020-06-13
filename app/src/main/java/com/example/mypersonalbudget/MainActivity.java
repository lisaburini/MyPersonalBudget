package com.example.mypersonalbudget;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Intent;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.example.mypersonalbudget.fragments.FragmentHome;
import com.example.mypersonalbudget.fragments.FragmentStatistics;
import com.example.mypersonalbudget.fragments.FragmentTransactions;
import com.example.mypersonalbudget.ui.login.IntroActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button btnLogout;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private BottomNavigationView bottomNav;
    private FragmentHome fragmentHome;
    private FragmentTransactions fragmentTransactions;
    private FragmentStatistics fragmentStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Check current user
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(authStateListener);


        getSupportActionBar().setTitle(getString(R.string.app_name));

        fragmentHome = new FragmentHome();
        fragmentTransactions = new FragmentTransactions();
        fragmentStatistics = new FragmentStatistics();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentHome).commit();

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        selectedFragment = fragmentHome;
                        break;
                    case R.id.menu_transactions:
                        selectedFragment = fragmentTransactions;
                        break;
                    case R.id.menu_statistics:
                        selectedFragment = fragmentStatistics;
                        break;
                }
                if(selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }
                return true;
            }
        });

    }

    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            if (firebaseUser == null) {
                Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                startActivity(intent);
                finish();
            } else {

                return;
            }
            /*if (firebaseUser != null) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } */
        }
    };

    //Per collegare il file xml del menù alla main activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    //Per gestire il click dei vari item del menù
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                profile();
                return true;
            case R.id.edit_profile:
                editProfile();
                return true;
            case R.id.change_password:
                changePassword();
                return true;
            case R.id.logout:
                logout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logout(){
        mAuth.getInstance().signOut();
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void profile(){
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }

    public void editProfile(){
        Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    public void changePassword(){
        Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
        startActivity(intent);
    }
}
