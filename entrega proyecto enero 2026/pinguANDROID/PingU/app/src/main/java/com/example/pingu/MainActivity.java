package com.example.pingu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import java.util.Random;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    ImageView imLogo;
    EditText ettUsuario;
    EditText ettContrasena;
    Button btnDisfruta;
    Button btnRegistro;
    TextView tv;
    TextView textView2;
    CheckBox chkUsuario;
    CheckBox chkPass;
    CheckBox chkCredenciales;
    SharedPreferences preferencias;
    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imLogo = findViewById(R.id.imLogo);
        ettUsuario = findViewById(R.id.ettUsuario);
        ettContrasena = findViewById(R.id.ettContrasena);
        btnDisfruta = findViewById(R.id.btnDisfruta);
        btnRegistro = findViewById(R.id.btnRegistro);
        tv = findViewById(R.id.tv);
        textView2 = findViewById(R.id.textView2);
        chkUsuario = findViewById(R.id.chkUsuario);
        chkPass = findViewById(R.id.chkPass);
        chkCredenciales = findViewById(R.id.chkCredenciales);
        preferencias = getSharedPreferences("login_preferencias", MODE_PRIVATE);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity3recuperarContrasena.class);
                startActivity(intent);
            }
        });

        btnDisfruta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validar usuario vacío
                if (ettUsuario.getText().toString().trim().isEmpty()) {
                    String[] frasesUsuario = {
                            "⚠️ Sin usuario no entras, colega 🚫",
                            "Ehhh, ¿el usuario? 🤨 No te lo saltes",
                            "¿Usuario invisible? No funciona así 👻",
                            "Pon tu usuario, porfa 😅",
                            "⚠️ Campo obligatorio, campeón",
                            "Tío, el usuario... ¿dónde está? 🤷‍♂️",
                            "No seas tímido, pon tu usuario 😏",
                            "Adivina: necesitas un usuario 🎯",
                            "El usuario no es opcional, crack 🎪",
                            "¿Olvidaste algo? Sí, el usuario 🧠"
                    };
                    String fraseAleatoria = frasesUsuario[random.nextInt(frasesUsuario.length)];
                    Toast.makeText(MainActivity.this, fraseAleatoria, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar contraseña vacía
                if (ettContrasena.getText().toString().trim().isEmpty()) {
                    String[] frasesPassword = {
                            "Ehhh, ¿y la contraseña? 🤔",
                            "La contraseña no se pone sola 🙃",
                            "¿Contraseña? ¿Hola? 👋",
                            "Sin contraseña no hay login, sorry 🚷",
                            "Falta algo importante... la contraseña 🔑",
                            "¿Te olvidaste de la contraseña? 😬",
                            "Contraseña obligatoria, amigo 🎯",
                            "Pon la contraseña, no seas vago 😅"
                    };
                    String fraseAleatoria = frasesPassword[random.nextInt(frasesPassword.length)];
                    Toast.makeText(MainActivity.this, fraseAleatoria, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar longitud de contraseña
                if (ettContrasena.getText().toString().trim().length() < 6) {
                    String[] frasesPasswordCorta = {
                            "⚠️ ¿En serio? Mínimo 6, no seas rata 😂",
                            "Muy corta, mínimo 6 caracteres 📏",
                            "¿6 caracteres es mucho pedir? 🤨",
                            "Esa contraseña es más corta que... 6+ porfa 🙏",
                            "Mínimo 6, que no es tan difícil 💪",
                            "6 caracteres o más, venga 🎯",
                            "Corta contraseña = insegura. Mín. 6 🔒",
                            "Dale más caña, mínimo 6 caracteres 🚀"
                    };
                    String fraseAleatoria = frasesPasswordCorta[random.nextInt(frasesPasswordCorta.length)];
                    Toast.makeText(MainActivity.this, fraseAleatoria, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Si todo está bien hacemos la llamada a la API
                String usuario = ettUsuario.getText().toString();
                String pass = ettContrasena.getText().toString();

                ApiService apiService = ApiClient.getClient().create(ApiService.class);

                LoginRequest request = new LoginRequest(usuario, pass);

                apiService.login(request).enqueue(new retrofit2.Callback<LoginResponse>() {

                    @Override
                    public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            Toast.makeText(MainActivity.this,
                                    "Login correcto",
                                    Toast.LENGTH_SHORT).show();

                            getSharedPreferences("PinguPrefs", MODE_PRIVATE)
                                    .edit()
                                    .putString("id_usuario", response.body().getId())
                                    .apply();
                            guardarPreferencias();

                            Intent intent = new Intent(MainActivity.this, MainActivity5muro.class);
                            startActivity(intent);

                        } else {

                            Toast.makeText(MainActivity.this,
                                    "Credenciales incorrectas",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {

                        Toast.makeText(MainActivity.this,
                                "Error conexión API",
                                Toast.LENGTH_LONG).show();

                        t.printStackTrace();
                    }
                });
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity4registro.class);
                startActivity(intent);
            }
        });

        imLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity6riddlerCompany.class);
                startActivity(intent);
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity6riddlerCompany.class);
                startActivity(intent);
            }
        });

        ettContrasena.setTransformationMethod(PasswordTransformationMethod.getInstance());
        chkPass.setChecked(false);
        chkCredenciales.setChecked(false);
        chkPass.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (isChecked) {
                ettContrasena.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ettContrasena.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            ettContrasena.setSelection(ettContrasena.getText().length());
        }));

        cargarPreferencias();
    }

    public void cargarPreferencias() {
        boolean recordarUsuario = preferencias.getBoolean("recordar_usuario", false);
        boolean mostrarPass = preferencias.getBoolean("mostrar_pass", false);
        boolean recodarCredenciales = preferencias.getBoolean("recordar_credenciales", false);

        chkUsuario.setChecked(recordarUsuario);
        chkPass.setChecked(mostrarPass);
        chkCredenciales.setChecked(recodarCredenciales);

        if (recodarCredenciales) {
            ettUsuario.setText(preferencias.getString("usuario", ""));
            ettContrasena.setText((preferencias.getString("pass", "")));
        } else {
            if (recordarUsuario) {
                ettUsuario.setText(preferencias.getString("usuario", ""));
            }
        }
    }

    public void guardarPreferencias() {

        SharedPreferences.Editor editor = preferencias.edit();
        editor.putBoolean("recordar_usuario", chkUsuario.isChecked());
        editor.putBoolean("mostrar_pass", chkPass.isChecked());
        editor.putBoolean("recordar_credenciales", chkCredenciales.isChecked());
        if (chkCredenciales.isChecked()) {
            editor.putString("usuario", ettUsuario.getText().toString());
            editor.putString("pass", ettContrasena.getText().toString());
            chkUsuario.setChecked(false);
        } else {
            if (chkUsuario.isChecked()) {
                editor.putString("usuario", ettUsuario.getText().toString());
                editor.remove("pass");
                chkCredenciales.setChecked(false);
            } else {
                editor.remove("usuario");
            }
        }
        editor.apply();
    }
}