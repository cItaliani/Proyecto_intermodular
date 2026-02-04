package com.example.pingu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity9formulario_post extends AppCompatActivity {
    Toolbar tb9;
    ImageView iv9, iconGallery;
    TextView tv9, textAddImage;
    FrameLayout imageContainer;
    ImageButton btnRemoveImage;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_activity9formulario_post);

        tb9 = findViewById(R.id.tb9);
        setSupportActionBar(tb9);

        iv9 = findViewById(R.id.iv9);
        tv9 = findViewById(R.id.tv9);
        imageContainer = findViewById(R.id.imageContainer);
        btnRemoveImage = findViewById(R.id.btnRemoveImage);
        iconGallery = findViewById(R.id.iconGallery);
        textAddImage = findViewById(R.id.textAddImage);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            selectedImageUri = imageUri;
                            mostrarImagenSeleccionada();
                        }
                    }
                }
        );

        // Configuración mejorada para manejar el teclado con EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());

            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    Math.max(systemBars.bottom, ime.bottom)
            );
            return insets;
        });

        // Click en el contenedor para añadir imagen
        imageContainer.setOnClickListener(view -> {
            if (selectedImageUri == null) {
                abrirSelectorImagen();
            }
        });

        // Click para eliminar imagen
        btnRemoveImage.setOnClickListener(view -> eliminarImagen());
    }

    private void abrirSelectorImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            imagePickerLauncher.launch(intent);
        }
    }

    private void mostrarImagenSeleccionada() {
        // Redimensionar el contenedor a 200dp
        ViewGroup.LayoutParams params = imageContainer.getLayoutParams();
        params.height = (int) (200 * getResources().getDisplayMetrics().density);
        imageContainer.setLayoutParams(params);

        // Mostrar imagen y ocultar placeholder
        iv9.setImageURI(selectedImageUri);
        iconGallery.setVisibility(View.GONE);
        textAddImage.setVisibility(View.GONE);
        btnRemoveImage.setVisibility(View.VISIBLE);

        Toast.makeText(this, "Imagen seleccionada", Toast.LENGTH_SHORT).show();
    }

    private void eliminarImagen() {
        // Redimensionar el contenedor a 100dp
        ViewGroup.LayoutParams params = imageContainer.getLayoutParams();
        params.height = (int) (100 * getResources().getDisplayMetrics().density);
        imageContainer.setLayoutParams(params);

        // Limpiar imagen y mostrar placeholder
        selectedImageUri = null;
        iv9.setImageDrawable(null);
        iconGallery.setVisibility(View.VISIBLE);
        textAddImage.setVisibility(View.VISIBLE);
        btnRemoveImage.setVisibility(View.GONE);

        Toast.makeText(this, "Imagen eliminada", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Actualizando seguidos", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.btnFollowers) {
            Intent intent = new Intent(MainActivity9formulario_post.this, MainActivity7followers.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.btnPosts) {
            Intent intent = new Intent(MainActivity9formulario_post.this, MainActivity5muro.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.btnHome) {
            Intent intent = new Intent(MainActivity9formulario_post.this, MainActivity1Home.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.cancelar) {
            finish();
            return true;
        } else if (id == R.id.publicar) {
            publicarPost();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void publicarPost() {
        String textoPost = tv9.getText().toString().trim();

        if (textoPost.isEmpty()) {
            Toast.makeText(this, "Escribe algo antes de publicar", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Se ha publicado tu post", Toast.LENGTH_SHORT).show();
        finish();
    }
}