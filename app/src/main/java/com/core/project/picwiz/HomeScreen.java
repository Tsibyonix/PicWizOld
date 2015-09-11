package com.core.project.picwiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        FrameLayout homeScreen = (FrameLayout) findViewById(R.id.home_Screen_Layout);

        //Floating Menu; API: https://github.com/Clans/FloatingActionButton
        final FloatingActionMenu floatingActionMenu = (FloatingActionMenu) findViewById(R.id.floatingActionMenu);
        floatingActionMenu.setClosedOnTouchOutside(true);

        floatingActionMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (floatingActionMenu.isOpened()) {
                    Toast.makeText(HomeScreen.this, floatingActionMenu.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
                }
                floatingActionMenu.toggle(true);
            }
        });

        //FloatingActionButton
        FloatingActionButton floatingActionButton_Camera = (FloatingActionButton) findViewById(R.id.floatingActionButton_Camera);
        FloatingActionButton floatingActionButton_Gallery = (FloatingActionButton) findViewById(R.id.floatingActionButton_Gallery);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedId = item.getItemId();
        switch (selectedId)
        {
            case R.id.action_Settings:
                startActivity(new Intent(this, Settings.class));
                break;
            case R.id.action_MyProfile:
                break;
            case R.id.action_About:
                startActivity(new Intent(this, About.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
