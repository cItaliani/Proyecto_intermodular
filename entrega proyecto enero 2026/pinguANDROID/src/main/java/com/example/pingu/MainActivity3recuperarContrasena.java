package com.example.pingu;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity3recuperarContrasena extends AppCompatActivity {
    Toolbar tb3;
    ImageView ivLogo3;
    TextView tv3;
    EditText ettUsuario3;
    EditText ettCorreo3;
    Button btn3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_activity3recuperar_contrasena);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tb3=findViewById(R.id.tb3);
        ivLogo3=findViewById(R.id.ivLogo3);
        tv3=findViewById(R.id.tv3);
        ettUsuario3=findViewById(R.id.ettUsuario3);
        ettCorreo3=findViewById(R.id.ettCorreo3);
        btn3=findViewById(R.id.btn3);
        setSupportActionBar(tb3);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        tb3.setTitleTextColor(getResources().getColor(R.color.white));



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}