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
                if (ettUsuario.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Debes introducir tu usuario", Toast.LENGTH_SHORT).show();
                } else if (ettContrasena.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Debes introducir tu contraseña", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, MainActivity5muro.class);
                    guardarPreferencias();
                    //falta el enviar datos para que pueda acceder
                    startActivity(intent);
                }

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
