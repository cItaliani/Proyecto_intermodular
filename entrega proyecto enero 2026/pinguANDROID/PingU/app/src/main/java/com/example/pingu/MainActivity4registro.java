package com.example.pingu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity4registro extends AppCompatActivity {
    Toolbar tb4;
    ImageView iv4;
    TextView tv4;
    EditText ettNombre4;
    EditText ett1ap4;
    EditText ett2ap4;
    EditText ettalias;
    EditText ettCorreo4;
    Button btn4;

    EditText ettcontrasena4;
    EditText ettcontrasenaOk4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_activity4registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tb4 = findViewById(R.id.tb4);
        iv4 = findViewById(R.id.iv4);
        tv4 = findViewById(R.id.tv4);
        ettNombre4 = findViewById(R.id.ettNombre4);
        ett1ap4 = findViewById(R.id.ett1ap4);
        ett2ap4 = findViewById(R.id.ett2ap4);
        ettalias = findViewById(R.id.ettalias);
        ettCorreo4 = findViewById(R.id.ettCorreo4);
        btn4 = findViewById(R.id.btn4);

        ettcontrasena4 = findViewById(R.id.ettcontrasena4);
        ettcontrasenaOk4 = findViewById(R.id.ettcontrasenaOk);

        setSupportActionBar(tb4);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.atras);

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ettNombre4.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity4registro.this, "Debes introducir tú nombre", Toast.LENGTH_SHORT).show();
                }

                else if (ett1ap4.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity4registro.this, "Debes introducir tu primer apellido", Toast.LENGTH_SHORT).show();
                }
                else if (ett2ap4.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity4registro.this, "Debes introducir tu segundo apellido", Toast.LENGTH_SHORT).show();
                }
                else if (ettalias.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity4registro.this, "Debes introducir tu nombre de usuario", Toast.LENGTH_SHORT).show();
                }
                else if (ettCorreo4.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity4registro.this, "Debes introducir tu correo electrónico", Toast.LENGTH_SHORT).show();
                }
                else if (ettcontrasena4.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity4registro.this, "Debes introducir una contraseña", Toast.LENGTH_SHORT).show();
                }else if (ettcontrasenaOk4.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity4registro.this, "Debes introducir la contraseña otra vez", Toast.LENGTH_SHORT).show();
                } else{
                    if (ettcontrasena4.getText().toString().equals(ettcontrasenaOk4.getText().toString())){
                        Toast.makeText(MainActivity4registro.this, "ok", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity4registro.this, MainActivity2perfil.class);
                        //falta el enviar datos para que pueda acceder
                        startActivity(intent);
                    }else{
                        Log.i("ERROR",ettcontrasenaOk4.getText().toString());
                        Log.i("ERROR",ettcontrasena4.getText().toString());
                        Toast.makeText(MainActivity4registro.this, "Las contraseñas tienen que ser iguales", Toast.LENGTH_SHORT).show();
                    }

                }
        }
    });

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