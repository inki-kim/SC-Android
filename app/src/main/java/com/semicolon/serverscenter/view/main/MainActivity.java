package com.semicolon.serverscenter.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.semicolon.serverscenter.R;
import com.semicolon.serverscenter.util.ActivityUtils;
import com.semicolon.serverscenter.util.BottomNavigationViewHelper;
import com.semicolon.serverscenter.view.chatbot.ChatbotActivity;

public class MainActivity extends AppCompatActivity {

    private MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_act);

        ManagerFragment managerFragment = ManagerFragment.newInstance();

        ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(),
                managerFragment, R.id.contentFrame);

        // Create the presenter
        mMainPresenter = new MainPresenter(managerFragment);

        HomeFragment homeFragment = HomeFragment.newInstance();

        ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(),
                homeFragment, R.id.contentFrame);

        // Create the presenter
        mMainPresenter = new MainPresenter(homeFragment);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bnvMain);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

//                HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager()
//                        .findFragmentById(R.id.contentFrame);
//                ManagerFragment managerFragment = (ManagerFragment) getSupportFragmentManager()
//                        .findFragmentById(R.id.contentFrame);

                HomeFragment homeFragment = null;
                ManagerFragment managerFragment = null;

                switch (item.getItemId()) {
                    case R.id.action_home:
                        if (homeFragment == null) {
                            homeFragment = HomeFragment.newInstance();

                            ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(),
                                    homeFragment, R.id.contentFrame);
                        }

                        // Create the presenter
                        mMainPresenter = new MainPresenter(homeFragment);

                        return true;
                    case R.id.action_manager:
                        if (managerFragment == null) {
                            managerFragment = ManagerFragment.newInstance();

                            ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(),
                                    managerFragment, R.id.contentFrame);
                        }

                        // Create the presenter
                        mMainPresenter = new MainPresenter(managerFragment);

                        return true;
                    case R.id.action_chatbot:
                        startActivity(new Intent(MainActivity.this, ChatbotActivity.class));

                        return true;
                }

                return false;
            }
        });
    }

}
