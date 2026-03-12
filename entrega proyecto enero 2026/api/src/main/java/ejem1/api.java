package ejem1;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Pattern;
import java.util.ArrayList;

import java.util.Random;
import java.util.regex.Matcher;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;
import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.exceptions.MailerSendException;
import com.mailersend.sdk.emails.Email;

/**
 * REST API for PingU social network.
 * 
 * This API manages:
 * - users
 * - posts
 * - likes
 * - followers
 * - login and password recovery
 * 
 * The API uses JSON and connects to a MariaDB database.
 */
@Path("/pingu")
public class api {
    // region server_antiguo
    // server funcionando pero limitacion de conexiones por hora
    // String servidor = "sql.freedb.tech";
    // String puerto = "3306";
    // String base_datos = "freedb_PingU_db";
    // String url = String.format("jdbc:mariadb://%s:%s/%s", servidor, puerto,
    // base_datos);
    // String usuario = "freedb_Atlas";
    // String contrasena = "xzwcW#V28cK#j*x";
    // endregion
    // nuevo server (beta) @gmail
    // web: https://panel.filess.io/shared/09490efe-3c3c-46ca-abf2-23494a5cde3b
    String servidor = "3i6ibg.h.filess.io";
    String puerto = "61000";
    String base_datos = "pingu_whichslept";
    String url = String.format("jdbc:mariadb://%s:%s/%s", servidor, puerto, base_datos);
    String usuario = "pingu_whichslept";
    String contrasena = "fb49efd066e70bdef2f1ed47d24180aa3be99214";

    /**
     * Checks if the given email is valid.
     * 
     * This method uses a regular expression to validate the email format.
     * 
     * @param correo The email address to be checked.
     * @return true if the email is valid, if retunrs false is invalid.
     */
    public static boolean comprobarCorreo(String correo) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();
    }

    // region USERS
    /**
     * Create a new user in the system.
     * 
     * Endpoint: POST /pingu/users
     * 
     * The method receives user data in JSON.
     * It checks if alias or email already exist.
     * The password is encrypted with BCrypt.
     * Then the user is saved in the database.
     * 
     * @param usuarioParam user information
     * @return HTTP response with result message
     */
    @POST
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User usuarioParam) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
                // Verificar si el alias ya existe
                PreparedStatement checkAlias = conexion
                        .prepareStatement("SELECT COUNT(*) FROM USUARIO WHERE alias = ?");
                checkAlias.setString(1, usuarioParam.getAlias());
                ResultSet rs = checkAlias.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return Response.status(Response.Status.CONFLICT)
                            .entity("{\"error\": \"El alias ya está en uso\"}").build();
                }

                // Verificar si el correo ya existe
                PreparedStatement checkEmail = conexion
                        .prepareStatement("SELECT COUNT(*) FROM USUARIO WHERE correo_electronico = ?");
                checkEmail.setString(1, usuarioParam.getCorreo_electronico());
                ResultSet rsEmail = checkEmail.executeQuery();
                if (rsEmail.next() && rsEmail.getInt(1) > 0) {
                    return Response.status(Response.Status.CONFLICT)
                            .entity("{\"error\": \"El correo electrónico ya está registrado\"}").build();
                }

                // Insertar usuario
                PreparedStatement ps = conexion.prepareStatement(
                        "INSERT INTO USUARIO(alias, nombre_visible, correo_electronico, contrasena, biografia, fotografia_url,fecha_alta) VALUES (?,?,?,?,?,?,?)",
                        PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, usuarioParam.getAlias());
                ps.setString(2, usuarioParam.getNombre_visible());
                ps.setString(3, usuarioParam.getCorreo_electronico());
                String contrasenaEncriptada = BCrypt.hashpw(usuarioParam.getContrasena(), BCrypt.gensalt());
                ps.setString(4, contrasenaEncriptada);
                ps.setString(5, usuarioParam.getBiografia());
                ps.setString(6, usuarioParam.getFotografia());
                Date fecha = new Date(System.currentTimeMillis());
                ps.setDate(7, fecha);
                int affectedRows = ps.executeUpdate();

                if (affectedRows > 0) {
                    ResultSet generatedKeys = ps.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        String userId = generatedKeys.getString(1);
                        return Response.status(Response.Status.CREATED)
                                .entity("{\"message\": \"Usuario creado exitosamente\", \"id\": \"" + userId + "\"}")
                                .build();
                    }
                }

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\": \"Error al crear el usuario\"}").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error en el servidor: " + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Get information of all users.
     * 
     * Endpoint: GET /pingu/users
     * 
     * The method searches all users
     * It returns the users data.
     * 
     * @return HTTP response with users data or error
     */
    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        ArrayList<User> usuarios = new ArrayList<>();

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
                PreparedStatement ps = conexion.prepareStatement(
                        "SELECT id_usuario, alias, nombre_visible, correo_electronico, biografia, fotografia_url FROM USUARIO");

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    User user = new User(
                            rs.getString("id_usuario"),
                            rs.getString("alias"),
                            rs.getString("nombre_visible"),
                            rs.getString("correo_electronico"),
                            rs.getString("biografia"),
                            rs.getString("fotografia_url"));
                    usuarios.add(user);
                }

                return Response.ok(usuarios).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Get information of one user.
     * 
     * Endpoint: GET /pingu/users/{user-id}
     * 
     * The method searches a user by ID.
     * If the user exists, it returns the user data.
     * 
     * @param idConsulta id of the user
     * @return HTTP response with user data or error
     */
    @GET
    @Path("/users/{user-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserDetails(@PathParam("user-id") String idConsulta) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
                String query = "SELECT id_usuario, alias, nombre_visible, correo_electronico, biografia, fotografia_url, fecha_alta FROM USUARIO WHERE id_usuario = ?";
                try (PreparedStatement ps = conexion.prepareStatement(query)) {
                    ps.setString(1, idConsulta);
                    ResultSet respuesta = ps.executeQuery();

                    if (respuesta.next()) {
                        String id = respuesta.getString("id_usuario");
                        String alias = respuesta.getString("alias");
                        String nombre = respuesta.getString("nombre_visible");
                        String mail = respuesta.getString("correo_electronico");
                        String bio = respuesta.getString("biografia");
                        String foto = respuesta.getString("fotografia_url");
                        Date fecha = respuesta.getDate("fecha_alta");

                        User user = new User(id, alias, nombre, mail, bio, foto,fecha);
                        return Response.ok(user).build();
                    } else {
                        return Response.status(Response.Status.NOT_FOUND)
                                .entity("{\"error\": \"Usuario no encontrado\"}").build();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Update user information.
     * 
     * Endpoint: PUT /pingu/users/{user-id}
     * 
     * The method updates:
     * - visible name
     * - biography
     * - password
     * - profile photo
     * 
     * @param idConsulta   id of the user
     * @param usuarioParam new user data
     * @return HTTP response with update result
     */
    @PUT
    @Path("/users/{user-id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyUser(@PathParam("user-id") String idConsulta, User usuarioParam) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
                PreparedStatement ps = conexion.prepareStatement(
                        "UPDATE USUARIO SET nombre_visible = ?, biografia = ?, contrasena = ?, fotografia_url = ? WHERE id_usuario = ?");
                ps.setString(1, usuarioParam.getNombre_visible());
                ps.setString(2, usuarioParam.getBiografia());
                String contrasenaEncriptada = BCrypt.hashpw(usuarioParam.getContrasena(), BCrypt.gensalt());
                ps.setString(3, contrasenaEncriptada);
                ps.setString(4, usuarioParam.getFotografia());
                ps.setString(5, idConsulta);

                int filas = ps.executeUpdate();

                if (filas == 0) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Usuario no encontrado\"}").build();
                }

                return Response.ok("{\"message\": \"Usuario actualizado correctamente\"}").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Delete a user from the system.
     * 
     * Endpoint: DELETE /pingu/users/{user-id}
     * 
     * The method removes the user from database.
     * 
     * @param idConsulta id of the user
     * @return HTTP response with result message
     */
    @DELETE
    @Path("/users/{user-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("user-id") String idConsulta) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
                PreparedStatement ps = conexion.prepareStatement("DELETE FROM USUARIO WHERE id_usuario = ?");
                ps.setString(1, idConsulta);

                int filas = ps.executeUpdate();

                if (filas == 0) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Usuario no encontrado\"}").build();
                }

                return Response.ok("{\"mensaje\": \"Usuario eliminado correctamente\"}").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }
    // endregion

    // region REACTIONS

    /**
     * Add a like to a post.
     * 
     * Endpoint: POST /pingu/posts/{post-id}/like
     * 
     * A user can like a post.
     * The like is saved in the database.
     * 
     * @param idPost   id of the post
     * @param reaccion user reaction information
     * @return HTTP response with result message
     */
    @POST
    @Path("/posts/{post-id}/like")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response likeAPost(@PathParam("post-id") String idPost, Reaccion reaccion) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
                PreparedStatement ps = conexion.prepareStatement(
                        "INSERT INTO REACCIONAR (id_usuario, id_post, fecha_Reaccion) VALUES (?, ?, ?)");
                ps.setString(1, reaccion.getIdUsuario());
                ps.setString(2, idPost);
                Date fecha = new Date(System.currentTimeMillis());
                ps.setDate(3, fecha);

                int filas = ps.executeUpdate();

                if (filas == 0) {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("{\"error\": \"Error al dar like\"}").build();
                }

                return Response.ok("{\"message\": \"Like registrado\"}").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Remove a like from a post.
     * 
     * Endpoint: POST /pingu/posts/{post-id}/dislike
     * 
     * The method deletes the like from database.
     * 
     * @param idPost   id of the post
     * @param reaccion user reaction information
     * @return HTTP response with result
     */
    @POST
    @Path("/posts/{post-id}/dislike")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response unlikeAPost(@PathParam("post-id") String idPost, Reaccion reaccion) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
                PreparedStatement ps = conexion.prepareStatement(
                        "DELETE FROM REACCIONAR WHERE id_post = ? AND id_usuario = ?");
                ps.setString(1, idPost);
                ps.setString(2, reaccion.getIdUsuario());

                int filas = ps.executeUpdate();

                if (filas == 0) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Like no encontrado\"}").build();
                }

                return Response.ok("{\"message\": \"Unlike realizado\"}").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Get the list of likes of a post.
     * 
     * Endpoint: GET /pingu/posts/{post-id}/likes
     * 
     * The method returns all users that liked the post.
     * 
     * @param idPost id of the post
     * @return list of reactions
     */
    @GET
    @Path("/posts/{post-id}/likes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listaLike(@PathParam("post-id") String idPost) {
        ArrayList<Reaccion> usuariosLikes = new ArrayList<Reaccion>();
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
                PreparedStatement ps = conexion.prepareStatement(
                        "SELECT * FROM REACCIONAR WHERE id_post = ?");
                ps.setString(1, idPost);
                ResultSet resultado = ps.executeQuery();
                while (resultado.next()) {
                    Reaccion nr = new Reaccion();
                    nr.setIdUsuario(resultado.getString("id_usuario"));
                    nr.setId_post(resultado.getString("id_post"));
                    nr.setFecha_creacion(resultado.getDate("fecha_reaccion"));
                    usuariosLikes.add(nr);
                }

                return Response.ok(usuariosLikes).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }
    // endregion

    // region SOCIAL GRAPH

    /**
     * Get the list of followers of a user.
     * 
     * Endpoint: GET /pingu/users/{user-id}/followers
     * 
     * The method returns users that follow this user.
     * 
     * @param idUsuario id of the user
     * @return list of followers
     */
    @GET
    @Path("/users/{user-id}/followers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listaSeguidores(@PathParam("user-id") int idUsuario) {
        ArrayList<Seguidor> listaSeguidores = new ArrayList<Seguidor>();

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
                PreparedStatement ps = conexion.prepareStatement(
                        "SELECT id_seguidor, id_seguido, fecha_inicio_follow FROM SEGUIR WHERE id_seguido = ?");
                ps.setInt(1, idUsuario);

                ResultSet respuesta = ps.executeQuery();

                while (respuesta.next()) {
                    Seguidor nuevoSeguidor = new Seguidor(
                            respuesta.getInt("id_seguidor"),
                            respuesta.getInt("id_seguido"),
                            respuesta.getDate("fecha_inicio_follow"));
                    listaSeguidores.add(nuevoSeguidor);
                }

                return Response.ok(listaSeguidores).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Get the list of users followed by a user.
     * 
     * Endpoint: GET /pingu/users/{user-id}/followed
     * 
     * The method returns users that this user follows.
     * 
     * @param idUsuario id of the user
     * @return list of followed users
     */
    @GET
    @Path("/users/{user-id}/followed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listaSeguidos(@PathParam("user-id") int idUsuario) {
        ArrayList<Seguidor> listaSeguidos = new ArrayList<Seguidor>();

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
                PreparedStatement ps = conexion.prepareStatement(
                        "SELECT id_seguidor, id_seguido, fecha_inicio_follow FROM SEGUIR WHERE id_seguidor = ?");
                ps.setInt(1, idUsuario);

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Seguidor seguido = new Seguidor(
                            rs.getInt("id_seguidor"),
                            rs.getInt("id_seguido"),
                            rs.getDate("fecha_inicio_follow"));
                    listaSeguidos.add(seguido);
                }

                return Response.ok(listaSeguidos).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Follow another user.
     * 
     * Endpoint: POST /pingu/users/{user-id}/follow
     * 
     * A user starts following another user.
     * The relation is saved in the database.
     * 
     * @param idUserSeguido user to follow
     * @param followRequest follower information
     * @return HTTP response with result
     */
    @POST
    @Path("/users/{user-id}/follow")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response follow(@PathParam("user-id") String idUserSeguido, FollowRequest followRequest) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
                if (idUserSeguido.equals(followRequest.getId_seguidor())) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"error\":\"No te puedes seguir a ti mismo\"}").build();
                }
                PreparedStatement ps = conexion.prepareStatement(
                        "SELECT id_usuario FROM USUARIO WHERE id_usuario = ?");
                ps.setString(1, idUserSeguido);
                ResultSet datos = ps.executeQuery();

                if (datos.next()) {
                    PreparedStatement check = conexion.prepareStatement(
                            "SELECT 1 FROM SEGUIR WHERE id_seguidor = ? AND id_seguido = ?");
                    check.setString(1, followRequest.getId_seguidor());
                    check.setString(2, idUserSeguido);
                    ResultSet rsCheck = check.executeQuery();

                    if (rsCheck.next()) {
                        return Response.status(Response.Status.CONFLICT)
                                .entity("{\"error\":\"Ya sigues a este usuario\"}")
                                .build();
                    }
                    PreparedStatement psFollow = conexion.prepareStatement(
                            "INSERT INTO SEGUIR (id_seguidor, id_seguido, fecha_inicio_follow) VALUES (?, ?, ?)");
                    psFollow.setString(1, followRequest.getId_seguidor());
                    psFollow.setString(2, idUserSeguido);
                    Date fecha = new Date(System.currentTimeMillis());
                    psFollow.setDate(3, fecha);
                    psFollow.executeUpdate();
                    return Response.ok("{\"message\":\"Desde ahora sigues a este usuario\"}").build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\":\"Usuario no encontrado\"}").build();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Stop following a user.
     * 
     * Endpoint: POST /pingu/users/{user-id}/unfollow
     * 
     * The follow relation is deleted from database.
     * 
     * @param idUserSeguido user to unfollow
     * @param followRequest follower information
     * @return HTTP response with result
     */
    @POST
    @Path("/users/{user-id}/unfollow")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response unfollow(@PathParam("user-id") String idUserSeguido, FollowRequest followRequest) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
                PreparedStatement ps = conexion.prepareStatement(
                        "DELETE FROM SEGUIR WHERE id_seguidor = ? AND id_seguido = ?");
                ps.setString(1, followRequest.getId_seguidor());
                ps.setString(2, idUserSeguido);

                int filas = ps.executeUpdate();

                if (filas == 0) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\":\"Relación de seguimiento no encontrada\"}").build();
                }

                return Response.ok("{\"message\":\"Dejaste de seguir al usuario\"}").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
    // endregion

    // region POSTS

    /**
     * Create a new post.
     * 
     * Endpoint: POST /pingu/posts
     * 
     * The user can create a post or a reply.
     * The post is saved in the database.
     * 
     * @param post post information
     * @return HTTP response with created post id
     */
    @POST
    @Path("/posts")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPost(Post post) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
                String query = post.getIdPostPadre() != null && post.getIdPostPadre() != ""
                        ? "INSERT INTO POST(contenido, url_multimedia,id_autor,fecha_creacion ,id_post_padre) VALUES (?, ?, ?,?,?)"
                        : "INSERT INTO POST(contenido, url_multimedia, id_autor, fecha_creacion) VALUES (?,?,?,?)";

                PreparedStatement ps = conexion.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, post.getContenido());
                ps.setString(2, post.getUrlMultimedia());
                ps.setString(3, post.getId_autor());
                Date fecha = new Date(System.currentTimeMillis());
                ps.setDate(4, fecha);
                if (post.getIdPostPadre() != null && post.getIdPostPadre() != "") {
                    ps.setString(5, post.getIdPostPadre());
                }

                int affectedRows = ps.executeUpdate();

                if (affectedRows > 0) {
                    ResultSet generatedKeys = ps.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        String postId = generatedKeys.getString(1);
                        return Response.status(Response.Status.CREATED)
                                .entity("{\"message\": \"Post creado\", \"id\": \"" + postId + "\"}").build();
                    }
                }

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\": \"Error al crear el post\"}").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Get recent posts.
     * 
     * Endpoint: GET /pingu/posts
     * 
     * The method returns the last 10 posts.
     * 
     * @return list of posts
     */
    @GET
    @Path("/posts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response recuperarPosts() {
        ArrayList<Post> listadoPosts = new ArrayList<Post>();

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
                PreparedStatement ps = conexion.prepareStatement(
                        "SELECT * FROM POST WHERE id_post_padre IS NULL ORDER BY id_post DESC LIMIT 10");
                ResultSet respuesta = ps.executeQuery();

                while (respuesta.next()) {
                    Post post = new Post();
                    post.setId(respuesta.getString("id_post"));
                    post.setId_autor(respuesta.getString("id_autor"));
                    post.setContenido(respuesta.getString("contenido"));
                    post.setUrlMultimedia(respuesta.getString("url_multimedia"));
                    post.setIdPostPadre(respuesta.getString("id_post_padre"));
                    post.setFecha_creacion(respuesta.getDate("fecha_creacion"));
                    listadoPosts.add(post);
                }

                return Response.ok(listadoPosts).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Get a single post by ID.
     * 
     * Endpoint: GET /pingu/posts/{post-id}
     * 
     * @param idPostLeido id of the post to read
     * @return HTTP response with post data or error
     */
    @GET
    @Path("/posts/{post-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readPost(@PathParam("post-id") int idPostLeido) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
                PreparedStatement ps = conexion.prepareStatement(
                        "SELECT * FROM POST WHERE id_post = ?");
                ps.setInt(1, idPostLeido);

                ResultSet respuesta = ps.executeQuery();

                if (respuesta.next()) {
                    Post post = new Post();
                    post.setId(respuesta.getString("id_post"));
                    post.setId_autor(respuesta.getString("id_autor"));
                    post.setContenido(respuesta.getString("contenido"));
                    post.setUrlMultimedia(respuesta.getString("url_multimedia"));
                    post.setIdPostPadre(respuesta.getString("id_post_padre"));
                    post.setFecha_creacion(respuesta.getDate("fecha_creacion"));
                    return Response.ok(post).build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Post no encontrado\"}").build();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Delete a post.
     * 
     * Endpoint: DELETE /pingu/posts/{post-id}
     * 
     * The post is removed from the database.
     * 
     * @param idPostBorrado id of the post
     * @return HTTP response with result
     */
    @DELETE
    @Path("/posts/{post-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePost(@PathParam("post-id") int idPostBorrado) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
                PreparedStatement ps = conexion.prepareStatement("DELETE FROM POST WHERE id_post = ?");
                ps.setInt(1, idPostBorrado);

                int filas = ps.executeUpdate();

                if (filas == 0) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Post no encontrado\"}").build();
                }

                return Response.ok("{\"message\": \"Post eliminado correctamente\"}").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Get replies of a post.
     * 
     * Endpoint: GET /pingu/posts/{post-id}/replies
     * 
     * The method returns comments of a post.
     * 
     * @param idPostComentado id of the post
     * @return list of replies
     */
    @GET
    @Path("/posts/{post-id}/replies")
    @Produces(MediaType.APPLICATION_JSON)
    public Response replies(@PathParam("post-id") int idPostComentado) {
        ArrayList<Post> comentariosSobrePostComentado = new ArrayList<Post>();

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
                PreparedStatement ps = conexion.prepareStatement(
                        "SELECT * FROM POST WHERE id_post_padre = ?");
                ps.setInt(1, idPostComentado);

                ResultSet respuesta = ps.executeQuery();

                while (respuesta.next()) {
                    Post post = new Post();
                    post.setId(respuesta.getString("id_post"));
                    post.setId_autor(respuesta.getString("id_autor"));
                    post.setContenido(respuesta.getString("contenido"));
                    post.setUrlMultimedia(respuesta.getString("url_multimedia"));
                    post.setIdPostPadre(respuesta.getString("id_post_padre"));
                    post.setFecha_creacion(respuesta.getDate("fecha_creacion"));
                    comentariosSobrePostComentado.add(post);
                }

                return Response.ok(comentariosSobrePostComentado).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }
    // endregion

    // region LOGIN-PASSWORD

    /**
     * User login.
     * 
     * Endpoint: POST /pingu/auth/login
     * 
     * The method checks alias and password.
     * If correct, the login is successful.
     * 
     * @param loginData login information
     * @return HTTP response with login result
     */
    @POST
    @Path("/auth/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest loginData) {

        try {
            Class.forName("org.mariadb.jdbc.Driver");

            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {

                PreparedStatement ps = conexion.prepareStatement(
                        "SELECT id_usuario, contrasena FROM USUARIO WHERE alias = ?");

                ps.setString(1, loginData.getAlias());

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    String hashGuardado = rs.getString("contrasena");

                    if (BCrypt.checkpw(loginData.getContrasena(), hashGuardado)) {

                        String id = rs.getString("id_usuario");

                        return Response.ok(
                                "{\"message\":\"Login correcto\",\"id\":\"" + id + "\"}")
                                .build();
                    }
                }

                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\":\"Credenciales incorrectas\"}")
                        .build();

            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Generate a temporary password.
     * 
     * The password has random letters and numbers.
     * It is used for password recovery.
     * 
     * @return temporary password
     */
    public static String generarPasswordTemporal() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            sb.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }

        return sb.toString();
    }

    /**
     * Send a recovery email to the user.
     * 
     * The email contains a temporary password.
     * The message is sent using Gmail SMTP.
     * 
     * @param destinatario user email
     * @param nuevaPass    temporary password
     */
    public static void enviarEmail(String destinatario, String nuevaPass) throws Exception {

        Email email = new Email();

        // ⚠️ Debe ser un dominio verificado en MailerSend
        email.setFrom("Pingu Support", "contrasena@test-y7zpl98d9n345vx6.mlsender.net");

        // Destinatario dinámico
        email.addRecipient("Usuario", destinatario);

        email.setSubject("Recuperación de contraseña");

        email.setPlain("Tu nueva contraseña temporal es: " + nuevaPass);

        email.setHtml(
                "<h2>Recuperación de contraseña</h2>"
                        + "<p>Tu nueva contraseña temporal es:</p>"
                        + "<h3>" + nuevaPass + "</h3>"
                        + "<p>Te recomendamos cambiarla después de iniciar sesión.</p>"
                        + "<p style='color:red; font-size:12px;'><b>¿No has solicitado esto? Ignora este mensaje</b></p>");

        MailerSend ms = new MailerSend();

        // ⚠️ NUNCA hardcodear en producción
        ms.setToken("mlsn.cc749b8612ba777f1efeae6deb1472f0f852ae32c20376acca823b671a48bb89");

        try {
            MailerSendResponse response = ms.emails().send(email);
            ;
            System.out.println("Email enviado. ID: " + response.messageId);

        } catch (MailerSendException e) {
            e.printStackTrace();
            throw new RuntimeException("Error enviando email: " + e.getMessage());
        }
    }

    /**
     * Recover user password.
     * 
     * Endpoint: POST /pingu/auth/pass-remember
     * 
     * The system creates a temporary password.
     * The password is saved in database and sent by email.
     * 
     * @param request user email request
     * @return HTTP response with result message
     */
    @POST
    @Path("/auth/pass-remember")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response rememberPassword(RememberRequest request) {

        try {
            Class.forName("org.mariadb.jdbc.Driver");

            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {

                PreparedStatement ps = conexion.prepareStatement(
                        "SELECT id_usuario FROM USUARIO WHERE correo_electronico = ?");

                ps.setString(1, request.getCorreo_electronico());
                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\":\"Correo no encontrado\"}")
                            .build();
                }

                String nuevaPass = generarPasswordTemporal();
                String hash = BCrypt.hashpw(nuevaPass, BCrypt.gensalt());

                PreparedStatement update = conexion.prepareStatement(
                        "UPDATE USUARIO SET contrasena = ? WHERE correo_electronico = ?");

                update.setString(1, hash);
                update.setString(2, request.getCorreo_electronico());
                update.executeUpdate();

                enviarEmail(request.getCorreo_electronico(), nuevaPass);

                return Response.ok("{\"message\":\"Se ha enviado una nueva contraseña al email\"}")
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    // endregion

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public Response holaMundo() {
        return Response.ok("Hola mundo, la API funciona correctamente").build();
    }
}