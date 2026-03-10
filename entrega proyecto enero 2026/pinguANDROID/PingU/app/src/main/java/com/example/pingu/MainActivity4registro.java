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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

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
    Random random = new Random();

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
        ettcontrasenaOk4 = findViewById(R.id.ettcontrasenaOk4);

        setSupportActionBar(tb4);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.atras);

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validar nombre
                if (ettNombre4.getText().toString().trim().isEmpty()) {
                    String[] frasesNombre = {
                            "⚠️ El nombre es obligatorio, crack 🚫",
                            "Ehhh, ¿tu nombre? 🤨",
                            "¿Nombre invisible? No funciona 👻",
                            "Pon tu nombre, porfa 😅",
                            "Sin nombre no hay registro 🎯",
                            "¿Te olvidaste de tu nombre? 😂",
                            "El nombre no es opcional, campeón 🎪",
                            "Necesito saber cómo te llamas 🧠"
                    };
                    String fraseAleatoria = frasesNombre[random.nextInt(frasesNombre.length)];
                    Toast.makeText(MainActivity4registro.this, fraseAleatoria, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar primer apellido
                if (ett1ap4.getText().toString().trim().isEmpty()) {
                    String[] frasesApellido1 = {
                            "⚠️ Primer apellido obligatorio 🚫",
                            "¿Y el primer apellido? 🤨",
                            "Falta el primer apellido, tío 👻",
                            "Pon tu primer apellido porfa 😅",
                            "Sin apellido no hay registro 🎯",
                            "¿Te olvidaste del apellido? 😬",
                            "El primer apellido es necesario 🎪",
                            "Necesito tu primer apellido 📝"
                    };
                    String fraseAleatoria = frasesApellido1[random.nextInt(frasesApellido1.length)];
                    Toast.makeText(MainActivity4registro.this, fraseAleatoria, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar segundo apellido
                if (ett2ap4.getText().toString().trim().isEmpty()) {
                    String[] frasesApellido2 = {
                            "⚠️ Segundo apellido obligatorio 🚫",
                            "¿Y el segundo apellido? 🤨",
                            "Falta el segundo apellido 👻",
                            "Pon tu segundo apellido porfa 😅",
                            "Completa con el segundo apellido 🎯",
                            "¿Te olvidaste del segundo? 😬",
                            "El segundo apellido también va 🎪",
                            "Necesito el segundo apellido 📝"
                    };
                    String fraseAleatoria = frasesApellido2[random.nextInt(frasesApellido2.length)];
                    Toast.makeText(MainActivity4registro.this, fraseAleatoria, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar nombre de usuario (alias)
                if (ettalias.getText().toString().trim().isEmpty()) {
                    String[] frasesUsuario = {
                            "⚠️ Necesitas un nombre de usuario 🚫",
                            "¿Tu nombre de usuario? 🤨",
                            "Falta el nombre de usuario 👻",
                            "Elige un nombre de usuario 😅",
                            "Sin usuario no puedes entrar 🎯",
                            "¿Qué usuario quieres? 😬",
                            "El nombre de usuario es clave 🔑",
                            "Inventa un nombre de usuario 🎨"
                    };
                    String fraseAleatoria = frasesUsuario[random.nextInt(frasesUsuario.length)];
                    Toast.makeText(MainActivity4registro.this, fraseAleatoria, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar email vacío
                if (ettCorreo4.getText().toString().trim().isEmpty()) {
                    String[] frasesEmailVacio = {
                            "Ehhh, ¿y el email? 🤔",
                            "El correo no se pone solo 🙃",
                            "¿Email? ¿Hola? 📧",
                            "Sin email no hay registro 🚷",
                            "Falta algo importante... el email 📬",
                            "¿Te olvidaste del correo? 😬",
                            "Email obligatorio, amigo 🎯",
                            "Pon el email, no seas vago 😅"
                    };
                    String fraseAleatoria = frasesEmailVacio[random.nextInt(frasesEmailVacio.length)];
                    Toast.makeText(MainActivity4registro.this, fraseAleatoria, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar formato de email
                if (!validarEmail(ettCorreo4.getText().toString())) {
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
                    Toast.makeText(MainActivity4registro.this, fraseAleatoria, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar contraseña vacía
                if (ettcontrasena4.getText().toString().trim().isEmpty()) {
                    String[] frasesPassVacia = {
                            "Ehhh, ¿la contraseña? 🤔",
                            "La contraseña no se pone sola 🙃",
                            "¿Contraseña? ¿Hola? 🔐",
                            "Sin contraseña no hay cuenta 🚷",
                            "Falta la contraseña 🔑",
                            "¿Te olvidaste de la contraseña? 😬",
                            "Contraseña obligatoria 🎯",
                            "Pon una contraseña segura 🛡️"
                    };
                    String fraseAleatoria = frasesPassVacia[random.nextInt(frasesPassVacia.length)];
                    Toast.makeText(MainActivity4registro.this, fraseAleatoria, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar longitud de contraseña
                if (ettcontrasena4.getText().toString().trim().length() < 6) {
                    String[] frasesPassCorta = {
                            "⚠️ Mínimo 6 caracteres, no seas rata 😂",
                            "Muy corta, mínimo 6 caracteres 📏",
                            "¿6 caracteres es mucho pedir? 🤨",
                            "Esa contraseña es muy corta 🙏",
                            "Mínimo 6, que no es tan difícil 💪",
                            "6 caracteres o más, venga 🎯",
                            "Contraseña corta = insegura. Mín. 6 🔒",
                            "Dale más caña, mínimo 6 🚀"
                    };
                    String fraseAleatoria = frasesPassCorta[random.nextInt(frasesPassCorta.length)];
                    Toast.makeText(MainActivity4registro.this, fraseAleatoria, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar repetición de contraseña vacía
                if (ettcontrasenaOk4.getText().toString().trim().isEmpty()) {
                    String[] frasesPass2Vacia = {
                            "Repite la contraseña aquí 🔁",
                            "¿Y la confirmación? 🤔",
                            "Falta repetir la contraseña 🔐",
                            "Confirma tu contraseña 🎯",
                            "Pon la contraseña otra vez 🔑",
                            "Necesito que la repitas 😅",
                            "Confirma la contraseña porfa 🙏",
                            "Escribe la contraseña de nuevo 📝"
                    };
                    String fraseAleatoria = frasesPass2Vacia[random.nextInt(frasesPass2Vacia.length)];
                    Toast.makeText(MainActivity4registro.this, fraseAleatoria, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar que las contraseñas coincidan
                if (!ettcontrasena4.getText().toString().equals(ettcontrasenaOk4.getText().toString())) {
                    String[] frasesPassNoCoinciden = {
                            "⚠️ Las contraseñas no coinciden 🚫",
                            "Ehhh, no son iguales 🤨",
                            "Las contraseñas no match 👻",
                            "No coinciden, revísalas 😅",
                            "Contraseñas diferentes 🎯",
                            "Esas no son iguales, tío 😬",
                            "No coinciden, inténtalo otra vez 🔄",
                            "Las contraseñas deben ser iguales 🎪"
                    };
                    String fraseAleatoria = frasesPassNoCoinciden[random.nextInt(frasesPassNoCoinciden.length)];
                    Toast.makeText(MainActivity4registro.this, fraseAleatoria, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Todo validado correctamente
                String nombre = ettNombre4.getText().toString().trim();
                String apellido1 = ett1ap4.getText().toString().trim();
                String apellido2 = ett2ap4.getText().toString().trim();
                String alias = ettalias.getText().toString().trim();
                String correo = ettCorreo4.getText().toString().trim();
                String contrasena = ettcontrasena4.getText().toString().trim();

                String nombreVisible = nombre + " " + apellido1 + " " + apellido2;

// Como en tu pantalla no tienes todavía biografía ni foto:
                String biografia = "";
                String fotografia = "";

                RegisterRequest request = new RegisterRequest(
                        alias,
                        nombreVisible,
                        correo,
                        contrasena,
                        biografia,
                        fotografia
                );

                ApiService apiService = ApiClient.getClient().create(ApiService.class);

                btn4.setEnabled(false);

                apiService.register(request).enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        btn4.setEnabled(true);

                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(MainActivity4registro.this,
                                    "✅ " + response.body().getMessage(),
                                    Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(MainActivity4registro.this, MainActivity.class);
                            intent.putExtra("nombre", nombre);
                            intent.putExtra("alias", alias);
                            intent.putExtra("id", response.body().getId());
                            startActivity(intent);
                            finish();

                        } else {
                            String mensajeError = "No se pudo completar el registro";

                            try {
                                if (response.errorBody() != null) {
                                    String errorJson = response.errorBody().string();

                                    if (errorJson.contains("alias ya está en uso")) {
                                        mensajeError = "Ese alias ya está en uso";
                                    } else if (errorJson.contains("correo electrónico ya está registrado")) {
                                        mensajeError = "Ese correo ya está registrado";
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(MainActivity4registro.this,
                                    "⚠️ " + mensajeError,
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        btn4.setEnabled(true);

                        Toast.makeText(MainActivity4registro.this,
                                "❌ No se pudo conectar con el servidor",
                                Toast.LENGTH_LONG).show();

                        Log.e("REGISTER_ERROR", "Error en registro", t);
                    }
                });
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