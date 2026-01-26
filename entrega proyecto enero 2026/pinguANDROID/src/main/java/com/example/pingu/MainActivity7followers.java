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

public class MainActivity7followers extends AppCompatActivity {
Toolbar tb7;
ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_activity7followers);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tb7=findViewById(R.id.tb7);
        setSupportActionBar(tb7);
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
            Intent intent=new Intent(MainActivity7followers.this, MainActivity2perfil.class);
            startActivity(intent);
            return true;
        }else if (id==R.id.btnFollow){
            Intent intent = new Intent(MainActivity7followers.this, MainActivity8follows.class);
            startActivity(intent);
        }else if (id==R.id.btnFollowers){
           Intent intent = new Intent(MainActivity7followers.this, MainActivity7followers.class);
           startActivity(intent);
            Toast.makeText(this, "actualizando seguidores", Toast.LENGTH_SHORT).show();
        }else if (id==R.id.btnPosts){
            Intent intent = new Intent(MainActivity7followers.this, MainActivity5muro.class);
            startActivity(intent);
        }else if (id==R.id.btnHome){
            Intent intent = new Intent(MainActivity7followers.this, MainActivity1Home.class);
            startActivity(intent);
            };
        return super.onOptionsItemSelected(item);
    }
}