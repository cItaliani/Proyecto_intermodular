package com.example.pingu;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import android.view.Menu;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity10Usuarios extends AppCompatActivity implements UsuarioActionListener {

    Toolbar tbUsuarios;
    RecyclerView rvUsuarios;

    UsuariosAdapter adapter;
    List<UsuarioLista> listaUsuarios = new ArrayList<>();

    ApiService apiService;
    String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_activity10_usuarios);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tbUsuarios = findViewById(R.id.tbUsuarios);
        setSupportActionBar(tbUsuarios);

        rvUsuarios = findViewById(R.id.rvUsuarios);

        rvUsuarios.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsuariosAdapter(listaUsuarios, this,true);
        rvUsuarios.setAdapter(adapter);

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
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        apiService.getFollowedUsers(idUsuarioLogado).enqueue(new Callback<List<SeguidorResponse>>() {
            @Override
            public void onResponse(Call<List<SeguidorResponse>> call, Response<List<SeguidorResponse>> responseFollowed) {
                Set<String> idsSeguidos = new HashSet<>();

                android.util.Log.d("USUARIOS_FOLLOW", "Código followed: " + responseFollowed.code());

                if (responseFollowed.isSuccessful() && responseFollowed.body() != null) {
                    for (SeguidorResponse relacion : responseFollowed.body()) {
                        android.util.Log.d("USUARIOS_FOLLOW",
                                "Relación -> idSeguidor=" + relacion.getIdSeguidor() +
                                        " | idSeguido=" + relacion.getIdSeguido());

                        idsSeguidos.add(relacion.getIdSeguido());
                    }
                }

                android.util.Log.d("USUARIOS_FOLLOW", "IDs seguidos detectados: " + idsSeguidos);

                apiService.getUsers().enqueue(new Callback<List<UserResponse>>() {
                    @Override
                    public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> responseUsers) {
                        if (responseUsers.isSuccessful() && responseUsers.body() != null) {
                            listaUsuarios.clear();

                            for (UserResponse user : responseUsers.body()) {
                                if (user.getId() == null || user.getId().equals(idUsuarioLogado)) {
                                    continue;
                                }

                                boolean loSigo = idsSeguidos.contains(user.getId());

                                android.util.Log.d("USUARIOS_FOLLOW",
                                        "Usuario " + user.getAlias() +
                                                " id=" + user.getId() +
                                                " loSigo=" + loSigo);

                                listaUsuarios.add(new UsuarioLista(
                                        user.getId(),
                                        user.getAlias() != null ? user.getAlias() : "",
                                        user.getNombreVisible() != null ? user.getNombreVisible() : "Usuario",
                                        loSigo
                                ));
                            }

                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MainActivity10Usuarios.this,
                                    "No se pudieron cargar los usuarios",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UserResponse>> call, Throwable t) {
                        Toast.makeText(MainActivity10Usuarios.this,
                                "Error al cargar los usuarios",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<SeguidorResponse>> call, Throwable t) {
                android.util.Log.e("USUARIOS_FOLLOW", "Error real en getFollowedUsers", t);
                Toast.makeText(MainActivity10Usuarios.this,
                        "Error al cargar relaciones de seguimiento",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onSeguirClick(UsuarioLista usuario) {
        apiService.followUser(usuario.getId(), new FollowRequest(idUsuarioLogado)).enqueue(new Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(Call<okhttp3.ResponseBody> call, Response<okhttp3.ResponseBody> response) {
                if (response.isSuccessful()) {
                    usuario.setLoSigo(true);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity10Usuarios.this,
                            "Ahora sigues a @" + usuario.getAlias(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity10Usuarios.this,
                            "No se pudo seguir al usuario. Código: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity10Usuarios.this,
                        "Error de conexión",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDejarSeguirClick(UsuarioLista usuario) {
        apiService.unfollowUser(usuario.getId(), new FollowRequest(idUsuarioLogado)).enqueue(new Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(Call<okhttp3.ResponseBody> call, Response<okhttp3.ResponseBody> response) {
                if (response.isSuccessful()) {
                    usuario.setLoSigo(false);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity10Usuarios.this,
                            "Has dejado de seguir a @" + usuario.getAlias(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity10Usuarios.this,
                            "No se pudo dejar de seguir",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity10Usuarios.this,
                        "Error de conexión",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.btnPerfil){
            Intent intent=new Intent(MainActivity10Usuarios.this, MainActivity2perfil.class);
            startActivity(intent);
            return true;
        }else if (id==R.id.btnFollow){
            Intent intent = new Intent(MainActivity10Usuarios.this, MainActivity8follows.class);
            startActivity(intent);
        }else if (id==R.id.btnFollowers){
            Intent intent = new Intent(MainActivity10Usuarios.this, MainActivity7followers.class);
            startActivity(intent);

        }else if (id==R.id.btnPosts){
            Intent intent = new Intent(MainActivity10Usuarios.this, MainActivity5muro.class);
            startActivity(intent);
        }else if (id==R.id.btnUsuarios){
            Toast.makeText(this, "ya estas en Usuarios", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}