package com.example.pingu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity8follows extends AppCompatActivity implements UsuarioActionListener {

    Toolbar tb8;
    RecyclerView rvFollows;

    UsuariosAdapter adapter;
    List<UsuarioLista> listaFollows = new ArrayList<>();

    ApiService apiService;
    String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_activity8follows);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tb8 = findViewById(R.id.tb8);
        rvFollows = findViewById(R.id.rvFollows);

        setSupportActionBar(tb8);

        rvFollows.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsuariosAdapter(listaFollows, this, false);
        rvFollows.setAdapter(adapter);

        apiService = ApiClient.getClient().create(ApiService.class);

        idUsuarioLogado = getSharedPreferences("PinguPrefs", MODE_PRIVATE)
                .getString("id_usuario", null);

        if (idUsuarioLogado == null || idUsuarioLogado.isEmpty()) {
            Toast.makeText(this, "No se ha encontrado el usuario logado", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarFollows();
    }

    private void cargarFollows() {
        listaFollows.clear();
        adapter.notifyDataSetChanged();

        apiService.getFollowedUsers(idUsuarioLogado).enqueue(new Callback<List<SeguidorResponse>>() {
            @Override
            public void onResponse(Call<List<SeguidorResponse>> call, Response<List<SeguidorResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SeguidorResponse> follows = response.body();

                    tb8.setSubtitle(follows.size() + " seguidos");

                    if (follows.isEmpty()) {
                        Toast.makeText(MainActivity8follows.this,
                                "Todavía no sigues a nadie",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (SeguidorResponse relacion : follows) {
                        cargarDatosSeguido(relacion.getIdSeguido());
                    }
                } else {
                    Toast.makeText(MainActivity8follows.this,
                            "No se pudieron cargar los seguidos",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<SeguidorResponse>> call, Throwable t) {
                Toast.makeText(MainActivity8follows.this,
                        "Error de conexión al cargar seguidos",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarDatosSeguido(String idSeguido) {
        if (idSeguido == null || idSeguido.trim().isEmpty()) {
            return;
        }

        apiService.getUserById(idSeguido).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse user = response.body();

                    listaFollows.add(new UsuarioLista(
                            user.getId(),
                            user.getAlias() != null ? user.getAlias() : "",
                            user.getNombreVisible() != null ? user.getNombreVisible() : "Usuario",
                            false
                    ));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // no rompemos la pantalla si falla un usuario
            }
        });
    }

    @Override
    public void onSeguirClick(UsuarioLista usuario) {

    }
    @Override
    public void onDejarSeguirClick(UsuarioLista usuario) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btnPerfil) {
            startActivity(new Intent(MainActivity8follows.this, MainActivity2perfil.class));
            return true;

        } else if (id == R.id.btnFollow) {
            Toast.makeText(this, "Actualizando seguidos", Toast.LENGTH_SHORT).show();
            cargarFollows();
            return true;

        } else if (id == R.id.btnFollowers) {
            startActivity(new Intent(MainActivity8follows.this, MainActivity7followers.class));
            return true;

        } else if (id == R.id.btnPosts) {
            startActivity(new Intent(MainActivity8follows.this, MainActivity5muro.class));
            return true;

        } else if (id == R.id.btnHome) {
            startActivity(new Intent(MainActivity8follows.this, MainActivity1Home.class));
            return true;

        } else if (id == R.id.btnUsuarios) {
            startActivity(new Intent(MainActivity8follows.this, MainActivity10Usuarios.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}