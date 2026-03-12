package com.example.pingu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity1Home extends AppCompatActivity implements PostActionListener{
Toolbar tb1;
Button btn2;

    RecyclerView rvPosts;

    PostAdapter adapter;
    List<PostMuro> listaMuro = new ArrayList<>();

    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_activity1_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tb1 = findViewById(R.id.tb1);
        setSupportActionBar(tb1);

       btn2 = findViewById(R.id.button2);
       rvPosts = findViewById(R.id.rvPosts);

        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostAdapter(listaMuro,this);
        rvPosts.setAdapter(adapter);

        apiService = ApiClient.getClient().create(ApiService.class);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity1Home.this, MainActivity9formulario_post.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        cargarPosts();
    }
    private void cargarPosts() {
        listaMuro.clear();
        adapter.notifyDataSetChanged();

        apiService.getPosts().enqueue(new Callback<List<PostResponse>>() {
            @Override
            public void onResponse(Call<List<PostResponse>> call, Response<List<PostResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PostResponse> posts = response.body();

                    if (posts.isEmpty()) {
                        Toast.makeText(MainActivity1Home.this, "Todavía no hay posts", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (PostResponse post : posts) {
                        cargarAutorYAgregarPost(post);
                    }
                } else {
                    Toast.makeText(MainActivity1Home.this, "No se pudo cargar el muro", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<PostResponse>> call, Throwable t) {
                Toast.makeText(MainActivity1Home.this, "Error de conexión al cargar posts", Toast.LENGTH_LONG).show();
                Log.e("MURO", "Error cargando posts", t);
            }
        });
    }

    private void cargarAutorYAgregarPost(PostResponse post) {
        String idAutor = post.getIdAutor();

        String idUsuarioLogado = getSharedPreferences("PinguPrefs", MODE_PRIVATE)
                .getString("id_usuario", "");

        boolean esMio = idUsuarioLogado.equals(post.getIdAutor());

        if (idAutor == null || idAutor.trim().isEmpty()) {
            comprobarSiHeDadoLikeYAgregarPost(post, "Usuario desconocido", esMio, idUsuarioLogado);
            return;
        }

        apiService.getUserById(idAutor).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                String nombreAutor = "Usuario desconocido";

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getNombreVisible() != null && !response.body().getNombreVisible().trim().isEmpty()) {
                        nombreAutor = response.body().getNombreVisible();
                    } else if (response.body().getAlias() != null && !response.body().getAlias().trim().isEmpty()) {
                        nombreAutor = response.body().getAlias();
                    }
                }

                comprobarSiHeDadoLikeYAgregarPost(post, nombreAutor, esMio, idUsuarioLogado);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                comprobarSiHeDadoLikeYAgregarPost(post, "Usuario desconocido", esMio, idUsuarioLogado);
            }
        });
    }

    private void comprobarSiHeDadoLikeYAgregarPost(PostResponse post, String nombreAutor, boolean esMio, String idUsuarioLogado) {
        apiService.getLikes(post.getId()).enqueue(new Callback<List<LikeResponse>>() {
            @Override
            public void onResponse(Call<List<LikeResponse>> call, Response<List<LikeResponse>> response) {
                boolean usuarioHaDadoLike = false;
                int numeroLikes = 0;

                if (response.isSuccessful() && response.body() != null) {
                    List<LikeResponse> likes = response.body();
                    numeroLikes = likes.size();

                    for (LikeResponse like : likes) {
                        if (idUsuarioLogado.equals(like.getIdUsuario())) {
                            usuarioHaDadoLike = true;
                            break;
                        }
                    }
                }

                listaMuro.add(new PostMuro(
                        post.getId(),
                        post.getIdAutor(),
                        nombreAutor,
                        post.getContenido(),
                        esMio,
                        usuarioHaDadoLike,
                        numeroLikes
                ));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<LikeResponse>> call, Throwable t) {
                listaMuro.add(new PostMuro(
                        post.getId(),
                        post.getIdAutor(),
                        nombreAutor,
                        post.getContenido(),
                        esMio,
                        false,
                        0
                ));
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        MenuItem homeItem = menu.findItem(R.id.btnHome);
        homeItem.setEnabled(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.btnPerfil){
            Intent intent=new Intent(MainActivity1Home.this, MainActivity2perfil.class);
            startActivity(intent);
            return true;
        }else if (id==R.id.btnFollow){
            Intent intent = new Intent(MainActivity1Home.this, MainActivity8follows.class);
            startActivity(intent);
        }else if (id==R.id.btnFollowers){
            Intent intent = new Intent(MainActivity1Home.this, MainActivity7followers.class);
            startActivity(intent);

        }else if (id==R.id.btnPosts){
            Intent intent = new Intent(MainActivity1Home.this, MainActivity5muro.class);
            startActivity(intent);
        }else if (id==R.id.btnUsuarios){
            Intent intent = new Intent(MainActivity1Home.this, MainActivity10Usuarios.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLike(PostMuro post) {
        String idUsuario = getSharedPreferences("PinguPrefs", MODE_PRIVATE)
                .getString("id_usuario", null);

        if (idUsuario == null) {
            Toast.makeText(MainActivity1Home.this, "No se ha encontrado el usuario logado", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("LIKE", "Intentando dar like al post " + post.getIdPost() + " con usuario " + idUsuario);

        apiService.likePost(post.getIdPost(), new ReaccionRequest(idUsuario)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    post.setUsuarioHaDadoLike(true);
                    post.setNumeroLikes(post.getNumeroLikes() + 1);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity1Home.this, "Like enviado", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String error = response.errorBody() != null ? response.errorBody().string() : "Sin detalle";
                        Log.e("LIKE", "Error HTTP " + response.code() + ": " + error);
                    } catch (Exception e) {
                        Log.e("LIKE", "No se pudo leer el errorBody", e);
                    }

                    Toast.makeText(MainActivity1Home.this,
                            "No se pudo dar like. Código: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("LIKE", "Fallo de red al dar like", t);
                Toast.makeText(MainActivity1Home.this, "No se pudo dar like", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDislike(PostMuro post) {
        String idUsuario = getSharedPreferences("PinguPrefs", MODE_PRIVATE)
                .getString("id_usuario", null);

        if (idUsuario == null) {
            Toast.makeText(MainActivity1Home.this, "No se ha encontrado el usuario logado", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("DISLIKE", "Intentando quitar like al post " + post.getIdPost() + " con usuario " + idUsuario);

        apiService.dislikePost(post.getIdPost(), new ReaccionRequest(idUsuario)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    post.setUsuarioHaDadoLike(false);

                    if (post.getNumeroLikes() > 0) {
                        post.setNumeroLikes(post.getNumeroLikes() - 1);
                    }

                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity1Home.this, "Like eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String error = response.errorBody() != null ? response.errorBody().string() : "Sin detalle";
                        Log.e("DISLIKE", "Error HTTP " + response.code() + ": " + error);
                    } catch (Exception e) {
                        Log.e("DISLIKE", "No se pudo leer el errorBody", e);
                    }

                    Toast.makeText(MainActivity1Home.this,
                            "No se pudo quitar el like. Código: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("DISLIKE", "Fallo de red al quitar like", t);
                Toast.makeText(MainActivity1Home.this, "No se pudo quitar el like", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEliminar(PostMuro post) {
        apiService.deletePost(post.getIdPost()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Toast.makeText(MainActivity1Home.this, "Post eliminado", Toast.LENGTH_SHORT).show();
                cargarPosts();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(MainActivity1Home.this, "No se pudo eliminar el post", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResponder(PostMuro post) {
        Intent intent = new Intent(MainActivity1Home.this, MainActivity9formulario_post.class);
        intent.putExtra("id_post_padre", post.getIdPost());
        startActivity(intent);
    }

    @Override
    public void onVerRespuestas(PostMuro post, TextView tvRespuestas) {
        if (tvRespuestas.getVisibility() == View.VISIBLE) {
            tvRespuestas.setVisibility(View.GONE);
            tvRespuestas.setText("");
            return;
        }

        apiService.getReplies(post.getIdPost()).enqueue(new Callback<List<PostResponse>>() {
            @Override
            public void onResponse(Call<List<PostResponse>> call, Response<List<PostResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PostResponse> replies = response.body();

                    if (replies.isEmpty()) {
                        tvRespuestas.setText("Sin respuestas");
                        tvRespuestas.setVisibility(View.VISIBLE);
                        return;
                    }

                    StringBuilder sb = new StringBuilder();
                    for (PostResponse reply : replies) {
                        sb.append("• ").append(reply.getContenido()).append("\n");
                    }

                    tvRespuestas.setText(sb.toString());
                    tvRespuestas.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<PostResponse>> call, Throwable t) {
                Toast.makeText(MainActivity1Home.this, "No se pudieron cargar las respuestas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}