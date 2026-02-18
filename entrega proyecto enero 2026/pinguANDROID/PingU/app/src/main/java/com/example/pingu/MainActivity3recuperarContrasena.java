package com.example.pingu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity3recuperarContrasena extends AppCompatActivity {
    Toolbar tb3;
    ImageView ivLogo3;
    TextView tv3;
    EditText ettUsuario3;
    EditText ettCorreo3;
    Button btn3;
    Random random = new Random();

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

        tb3 = findViewById(R.id.tb3);
        ivLogo3 = findViewById(R.id.ivLogo3);
        tv3 = findViewById(R.id.tv3);
        ettUsuario3 = findViewById(R.id.ettUsuario3);
        ettCorreo3 = findViewById(R.id.ettCorreo3);
        btn3 = findViewById(R.id.btn3);
        setSupportActionBar(tb3);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        tb3.setTitleTextColor(getResources().getColor(R.color.white));

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validar usuario vacío
                if (ettUsuario3.getText().toString().trim().isEmpty()) {
                    String[] frasesUsuario = {
                            "⚠️ Sin usuario no hay recuperación 🚫",
                            "Ehhh, ¿el usuario? 🤨 Lo necesito",
                            "¿Usuario invisible? No funciona así 👻",
                            "Pon tu usuario, porfa 😅",
                            "⚠️ Campo obligatorio, campeón",
                            "Tío, el usuario... ¿dónde está? 🤷‍♂️",
                            "No seas tímido, pon tu usuario 😏",
                            "Necesito tu usuario para ayudarte 🎯",
                            "El usuario no es opcional, crack 🎪",
                            "¿Olvidaste algo? Sí, el usuario 🧠"
                    };
                    String fraseAleatoria = frasesUsuario[random.nextInt(frasesUsuario.length)];
                    Toast.makeText(MainActivity3recuperarContrasena.this, fraseAleatoria, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar email vacío
                if (ettCorreo3.getText().toString().trim().isEmpty()) {
                    String[] frasesEmailVacio = {
                            "Ehhh, ¿y el email? 🤔",
                            "El correo no se pone solo 🙃",
                            "¿Email? ¿Hola? 📧",
                            "Sin email no puedo enviarte nada 🚷",
                            "Falta algo importante... el email 📬",
                            "¿Te olvidaste del correo? 😬",
                            "Email obligatorio, amigo 🎯",
                            "Pon el email, no seas vago 😅",
                            "¿Dónde te envío la contraseña? 🤨",
                            "Necesito tu email, campeón 💌"
                    };
                    String fraseAleatoria = frasesEmailVacio[random.nextInt(frasesEmailVacio.length)];
                    Toast.makeText(MainActivity3recuperarContrasena.this, fraseAleatoria, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar formato de email
                if (validarEmail(ettCorreo3.getText().toString()) != true) {
                    String[] frasesEmailInvalido = {
                            "⚠️ Ese email no pinta bien 🤔",
                            "Email inválido, revísalo porfa 📧",
                            "¿Seguro que ese es tu email? 🧐",
                            "Formato de email incorrecto 🚫",
                            "Eso no es un email válido, crack 😅",
                            "Email mal escrito, inténtalo 📝",
                            "Revisa el formato del email 🔍",
                            "Ese email tiene pinta rara 🤨"
                    };
                    String fraseAleatoria = frasesEmailInvalido[random.nextInt(frasesEmailInvalido.length)];
                    Toast.makeText(MainActivity3recuperarContrasena.this, fraseAleatoria, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Si todo está bien
                Toast.makeText(MainActivity3recuperarContrasena.this, "✅ Revisa tu correo 📧", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity3recuperarContrasena.this, MainActivity.class);
                startActivity(intent);
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

    public static boolean validarEmail(String email) {
        String patron = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(patron);
    }
}