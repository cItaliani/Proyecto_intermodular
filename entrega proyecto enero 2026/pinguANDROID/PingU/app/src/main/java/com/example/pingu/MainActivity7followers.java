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

public class MainActivity7followers extends AppCompatActivity implements UsuarioActionListener {

    Toolbar tb7;
    RecyclerView rvFollowers;

    UsuariosAdapter adapter;
    List<UsuarioLista> listaFollowers = new ArrayList<>();

    ApiService apiService;
    String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_activity7followers);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tb7 = findViewById(R.id.tb7);
        rvFollowers = findViewById(R.id.rvFollowers);

        setSupportActionBar(tb7);

        rvFollowers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsuariosAdapter(listaFollowers, this, false);
        rvFollowers.setAdapter(adapter);

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
        cargarFollowers();
    }

    private void cargarFollowers() {
        listaFollowers.clear();
        adapter.notifyDataSetChanged();

        apiService.getFollowers(idUsuarioLogado).enqueue(new Callback<List<SeguidorResponse>>() {
            @Override
            public void onResponse(Call<List<SeguidorResponse>> call, Response<List<SeguidorResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SeguidorResponse> followers = response.body();

                    tb7.setSubtitle(followers.size() + " seguidores");

                    if (followers.isEmpty()) {
                        Toast.makeText(MainActivity7followers.this,
                                "Todavía no tienes seguidores",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (SeguidorResponse relacion : followers) {
                        cargarDatosFollower(relacion.getIdSeguidor());
                    }
                } else {
                    Toast.makeText(MainActivity7followers.this,
                            "No se pudieron cargar los seguidores",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<SeguidorResponse>> call, Throwable t) {
                Toast.makeText(MainActivity7followers.this,
                        "Error de conexión al cargar seguidores",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarDatosFollower(String idFollower) {
        if (idFollower == null || idFollower.trim().isEmpty()) {
            return;
        }

        apiService.getUserById(idFollower).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse user = response.body();

                    listaFollowers.add(new UsuarioLista(
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
            startActivity(new Intent(MainActivity7followers.this, MainActivity2perfil.class));
            return true;

        } else if (id == R.id.btnFollow) {
            startActivity(new Intent(MainActivity7followers.this, MainActivity8follows.class));
            return true;

        } else if (id == R.id.btnFollowers) {
            Toast.makeText(this, "Actualizando seguidores", Toast.LENGTH_SHORT).show();
            cargarFollowers();
            return true;

        } else if (id == R.id.btnPosts) {
            startActivity(new Intent(MainActivity7followers.this, MainActivity5muro.class));
            return true;

        } else if (id == R.id.btnHome) {
            startActivity(new Intent(MainActivity7followers.this, MainActivity1Home.class));
            return true;

        } else if (id == R.id.btnUsuarios) {
            startActivity(new Intent(MainActivity7followers.this, MainActivity10Usuarios.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}