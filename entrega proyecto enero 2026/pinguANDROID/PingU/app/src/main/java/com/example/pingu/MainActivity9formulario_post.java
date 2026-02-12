package com.example.pingu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity9formulario_post extends AppCompatActivity {

    ImageView imageView8;
    ImageView iv7;
    ImageView iv6;
    EditText ett4;
    Toolbar tb9;
    boolean isImagen = false;
    private ActivityResultLauncher<String> seleccionarImagen = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    imageView8.setImageURI(uri);
                    isImagen = true;
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity9formulario_post);
        tb9 = findViewById(R.id.tb9);
        setSupportActionBar(tb9);
        imageView8 = findViewById(R.id.imageView8);
        iv7 = findViewById(R.id.iv7);
        iv6 = findViewById(R.id.iv6);
        ett4 = findViewById(R.id.ett4);

        iv6.setOnClickListener(v -> {
            if (!ett4.getText().toString().equals("")) {
                ett4.setText("");
                ett4.setHint("Tranquilo, aquí no almacenamos secretos ni hacemos colección de mensajes " +
                        "Lo que se borra, se va directo al iceberg del olvido ❄\uD83D\uDC27");  // ese codigo es el emoji pingüino
            }
        });

        iv7.setOnClickListener(v -> {
            imageView8.setImageResource(R.drawable.pulsa_imagen);
            isImagen=false;

        });

        imageView8.setOnClickListener(v -> {
            seleccionarImagen.launch("image/*");
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_extend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btnPerfil) {
            Intent intent = new Intent(MainActivity9formulario_post.this, MainActivity2perfil.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.btnFollow) {
            Intent intent = new Intent(MainActivity9formulario_post.this, MainActivity8follows.class);
            startActivity(intent);
        } else if (id == R.id.btnFollowers) {
            Intent intent = new Intent(MainActivity9formulario_post.this, MainActivity7followers.class);
            startActivity(intent);
        } else if (id == R.id.btnPosts) {
            Intent intent = new Intent(MainActivity9formulario_post.this, MainActivity5muro.class);
            startActivity(intent);
        } else if (id == R.id.btnHome) {
            Intent intent = new Intent(MainActivity9formulario_post.this, MainActivity1Home.class);
            startActivity(intent);
        } else if (id == R.id.cancelar) {
            finish();
            return true;
        } else if (id == R.id.publicar) {
            String texto = ett4.getText().toString().trim();
            if (texto.isEmpty()&&!isImagen){
                Toast.makeText(MainActivity9formulario_post.this, "Nada que publicar aún \uD83D\uDC27❄\n" +
                        "Añade un mensaje y/o imagen. ", Toast.LENGTH_SHORT).show();
                return  true;
            }
            Toast.makeText(MainActivity9formulario_post.this, "\uD83D\uDCE2 ¡Listo! Tu publicación llegó al iceberg de PingU \uD83D\uDC27", Toast.LENGTH_SHORT).show();
            finish();
            return true;

        }
        ;
        return super.onOptionsItemSelected(item);
    }
}