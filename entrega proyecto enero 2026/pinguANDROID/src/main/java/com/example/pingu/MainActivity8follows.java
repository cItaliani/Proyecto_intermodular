package com.example.pingu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity8follows extends AppCompatActivity {
    Toolbar tb8;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_activity8follows);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tb8=findViewById(R.id.tb8);
        setSupportActionBar(tb8);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.btnPerfil){
            Intent intent=new Intent(MainActivity8follows.this, MainActivity2perfil.class);
            startActivity(intent);
            return true;
        }else if (id==R.id.btnFollow){
           Intent intent = new Intent(MainActivity8follows.this, MainActivity8follows.class);
           startActivity(intent);
            Toast.makeText(this, "actualizando seguidos", Toast.LENGTH_SHORT).show();
        }else if (id==R.id.btnFollowers){
            Intent intent = new Intent(MainActivity8follows.this, MainActivity7followers.class);
            startActivity(intent);
        }else if (id==R.id.btnPosts){
            Intent intent = new Intent(MainActivity8follows.this, MainActivity5muro.class);
            startActivity(intent);
        }else if (id==R.id.btnHome){
            Intent intent = new Intent(MainActivity8follows.this, MainActivity1Home.class);
            startActivity(intent);
           };

        return super.onOptionsItemSelected(item);
    }
}