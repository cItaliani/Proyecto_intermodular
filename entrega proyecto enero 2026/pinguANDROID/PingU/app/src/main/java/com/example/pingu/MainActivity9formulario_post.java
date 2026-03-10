package com.example.pingu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity9formulario_post extends AppCompatActivity {

    ImageView imageView9;
    ImageView iv7;
    ImageView iv6;
    EditText ett4;
    Toolbar tb9;
    boolean isImagen = false;
    private ActivityResultLauncher<String> seleccionarImagen = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    imageView9.setImageURI(uri);
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
        imageView9 = findViewById(R.id.imageView9);
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
            imageView9.setImageResource(R.drawable.pulsa_imagen);
            isImagen=false;

        });

        imageView9.setOnClickListener(v -> {
            Toast.makeText(
                    MainActivity9formulario_post.this,
                    "📸 Subir imágenes estará disponible en próximas versiones de PingU 🐧❄",
                    Toast.LENGTH_SHORT
            ).show();
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

         }else if (id == R.id.publicar) {
            item.setEnabled(false);
                String texto = ett4.getText().toString().trim();

                if (texto.isEmpty() && !isImagen) {
                    Toast.makeText(MainActivity9formulario_post.this,
                            "Nada que publicar aún 🐧❄\nAñade un mensaje y/o imagen.",
                            Toast.LENGTH_SHORT).show();
                    item.setEnabled(true);
                    return true;
                }

                String idAutor = getSharedPreferences("PinguPrefs", MODE_PRIVATE)
                        .getString("id_usuario", null);

                if (idAutor == null || idAutor.isEmpty()) {
                    Toast.makeText(MainActivity9formulario_post.this,
                            "⚠️ No se ha encontrado el usuario logado",
                            Toast.LENGTH_LONG).show();
                    return true;
                }

            String idPostPadre = getIntent().getStringExtra("id_post_padre");

            CreatePostRequest request = new CreatePostRequest(
                    texto,
                    "",
                    idAutor,
                    idPostPadre
            );

                ApiService apiService = ApiClient.getClient().create(ApiService.class);

                apiService.createPost(request).enqueue(new Callback<CreatePostResponse>() {
                    @Override
                    public void onResponse(Call<CreatePostResponse> call, Response<CreatePostResponse> response) {
                        item.setEnabled(true);
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(MainActivity9formulario_post.this,
                                    "📢 ¡Publicación a la vista! Tu publicación llegó al iceberg de PingU 🐧",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(MainActivity9formulario_post.this,
                                    "⚠️ No se pudo publicar el post",
                                    Toast.LENGTH_LONG).show();

                            try {
                                if (response.errorBody() != null) {
                                    Log.e("CREATE_POST", response.errorBody().string());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CreatePostResponse> call, Throwable t) {
                        item.setEnabled(true);
                        Toast.makeText(MainActivity9formulario_post.this,
                                "❌ No se pudo conectar con el servidor",
                                Toast.LENGTH_LONG).show();
                        Log.e("CREATE_POST", "Error creando post", t);
                    }
                });

                return true;
            }
        ;
        return super.onOptionsItemSelected(item);
    }
}