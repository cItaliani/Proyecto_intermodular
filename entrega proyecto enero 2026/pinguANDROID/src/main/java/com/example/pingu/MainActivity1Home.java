package com.example.pingu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity1Home extends AppCompatActivity {
Toolbar tb1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_activity1_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tb1=findViewById(R.id.tb1);
        setSupportActionBar(tb1);

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
            Intent intent=new Intent(MainActivity1Home.this, MainActivity2perfil.class);
            startActivity(intent);
            return true;
        }else if (id==R.id.btnFollow){
            Intent intent = new Intent(MainActivity1Home.this, MainActivity8follows.class);
            startActivity(intent);
        }else if (id==R.id.btnFollowers){
            Intent intent = new Intent(MainActivity1Home.this, MainActivity7followers.class);
            startActivity(intent);

        }else if (id==R.id.btnPosts){
            Intent intent = new Intent(MainActivity1Home.this, MainActivity5muro.class);
            startActivity(intent);

        }else if (id==R.id.btnHome){
            Intent intent = new Intent(MainActivity1Home.this, MainActivity1Home.class);
            startActivity(intent);
            Toast.makeText(this, "actualizando el home", Toast.LENGTH_SHORT).show();};
        return super.onOptionsItemSelected(item);
    }
}