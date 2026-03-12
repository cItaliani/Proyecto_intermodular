package com.example.pingu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class MainActivity2perfil extends AppCompatActivity {

    Button btnCambiaFoto;
    Button btnGuardarCambios;
    Button btnEliminarCuenta;
    TextView lblDesconectar;

    EditText edtNombre;
    EditText edtBiografia;
    EditText edtNuevaPass;
    EditText edtRepetirPass;

    TextView txtAliasRespuesta;
    TextView txtCorreoRespuesta;
    TextView txtMiembroRespuesta;
    TextView txtSeguidoresRespuesta;
    TextView txtSeguidosRespuesta;

    ApiService apiService;
    String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_activity2perfil);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnCambiaFoto = findViewById(R.id.btnCambiaFoto);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
        lblDesconectar = findViewById(R.id.lblDesconectar);
        btnEliminarCuenta = findViewById(R.id.btnEliminarCuenta);
        edtNombre = findViewById(R.id.edtNombre);
        edtBiografia = findViewById(R.id.edtBiografia);
        edtNuevaPass = findViewById(R.id.edtNuevaPass);
        edtRepetirPass = findViewById(R.id.edtRepetirPass);

        txtAliasRespuesta = findViewById(R.id.txtAlias_respuesta);
        txtCorreoRespuesta = findViewById(R.id.txtCorreo_respuesta);
        txtMiembroRespuesta = findViewById(R.id.txtMiembro_respuesta);
        txtSeguidoresRespuesta = findViewById(R.id.txtSeguidores_respuesta);
        txtSeguidosRespuesta = findViewById(R.id.txtSeguidos_respuesta);

        apiService = ApiClient.getClient().create(ApiService.class);

        idUsuarioLogado = getSharedPreferences("PinguPrefs", MODE_PRIVATE)
                .getString("id_usuario", null);

        if (idUsuarioLogado == null || idUsuarioLogado.isEmpty()) {
            Toast.makeText(this, "No se ha encontrado el usuario logado", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        cargarPerfil();
        cargarNumeroSeguidores();
        cargarNumeroSeguidos();

        btnCambiaFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity2perfil.this,
                        "La actualización de foto estará disponible en próximas versiones",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambios();
            }
        });

        lblDesconectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences("PinguPrefs", MODE_PRIVATE)
                        .edit()
                        .clear()
                        .apply();

                Intent intent = new Intent(MainActivity2perfil.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        btnEliminarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity2perfil.this)
                        .setTitle("Eliminar cuenta")
                        .setMessage("Esta acción no se puede deshacer. ¿Seguro que quieres eliminar tu cuenta?")
                        .setPositiveButton("Sí, eliminar", (dialog, which) -> eliminarCuenta())
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });
    }

    private void cargarPerfil() {
        apiService.getUserById(idUsuarioLogado).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse user = response.body();

                    edtNombre.setText(user.getNombreVisible() != null ? user.getNombreVisible() : "");
                    edtBiografia.setText(user.getBiografia() != null ? user.getBiografia() : "");

                    txtAliasRespuesta.setText(user.getAlias() != null ? user.getAlias() : "");
                    txtCorreoRespuesta.setText(user.getCorreoElectronico() != null ? user.getCorreoElectronico() : "");
                    if (user.getFechaAlta() > 0) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String fechaFormateada = sdf.format(new Date(user.getFechaAlta()));
                        txtMiembroRespuesta.setText(fechaFormateada);
                    } else {
                        txtMiembroRespuesta.setText("No disponible");
                    }
                } else {
                    Toast.makeText(MainActivity2perfil.this,
                            "No se pudo cargar el perfil",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(MainActivity2perfil.this,
                        "Error de conexión al cargar el perfil",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void guardarCambios() {
        String nombre = edtNombre.getText().toString().trim();
        String bio = edtBiografia.getText().toString().trim();
        String pass = edtNuevaPass.getText().toString().trim();
        String repetirPass = edtRepetirPass.getText().toString().trim();

        if (nombre.isEmpty()) {
            Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.isEmpty() || !repetirPass.isEmpty()) {
            if (!pass.equals(repetirPass)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            if (pass.length() < 6) {
                Toast.makeText(this, "La nueva contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(this, "No puedes dejar la contraseañ vacia", Toast.LENGTH_SHORT).show();
            return;
        }

        UpdateUserRequest request = new UpdateUserRequest(
                nombre,
                bio,
                pass,
                ""
        );

        apiService.updateUser(idUsuarioLogado, request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    cargarPerfil();
                    cargarNumeroSeguidores();
                    cargarNumeroSeguidos();
                    Toast.makeText(MainActivity2perfil.this,
                            "Perfil actualizado correctamente",
                            Toast.LENGTH_SHORT).show();

                    edtNuevaPass.setText("");
                    edtRepetirPass.setText("");
                    cargarPerfil();
                } else {
                    Toast.makeText(MainActivity2perfil.this,
                            "No se pudo actualizar el perfil",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(MainActivity2perfil.this,
                        "Error de conexión al actualizar el perfil",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private void eliminarCuenta() {
        if (idUsuarioLogado == null || idUsuarioLogado.isEmpty()) {
            Toast.makeText(this, "No se ha encontrado el usuario logado", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.deleteUser(idUsuarioLogado).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity2perfil.this,
                            "Cuenta eliminada correctamente",
                            Toast.LENGTH_LONG).show();

                    getSharedPreferences("PinguPrefs", MODE_PRIVATE)
                            .edit()
                            .clear()
                            .apply();

                    Intent intent = new Intent(MainActivity2perfil.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity2perfil.this,
                            "No se pudo eliminar la cuenta",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(MainActivity2perfil.this,
                        "Error de conexión al eliminar la cuenta",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    private void cargarNumeroSeguidores() {
        apiService.getFollowers(idUsuarioLogado).enqueue(new Callback<List<SeguidorResponse>>() {
            @Override
            public void onResponse(Call<List<SeguidorResponse>> call, Response<List<SeguidorResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    txtSeguidoresRespuesta.setText(String.valueOf(response.body().size()));
                } else {
                    txtSeguidoresRespuesta.setText("0");
                }
            }

            @Override
            public void onFailure(Call<List<SeguidorResponse>> call, Throwable t) {
                txtSeguidoresRespuesta.setText("0");
            }
        });
    }

    private void cargarNumeroSeguidos() {
        apiService.getFollowedUsers(idUsuarioLogado).enqueue(new Callback<List<SeguidorResponse>>() {
            @Override
            public void onResponse(Call<List<SeguidorResponse>> call, Response<List<SeguidorResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    txtSeguidosRespuesta.setText(String.valueOf(response.body().size()));
                } else {
                    txtSeguidosRespuesta.setText("0");
                }
            }

            @Override
            public void onFailure(Call<List<SeguidorResponse>> call, Throwable t) {
                txtSeguidosRespuesta.setText("0");
            }
        });
    }
}