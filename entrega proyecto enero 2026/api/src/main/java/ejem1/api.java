package ejem1;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Pattern;
import java.util.ArrayList;
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

@Path("/PingU")
public class API {

    String url = "jdbc:mariadb://sql.freedb.tech:3306/freedb_PingU_db";
    String usuario = "freedb_Atlas";
    String password = "xzwcW#V28cK#j*x";

    public static boolean comprobarCorreo(String correo) {
        // Expresión regular estándar para email
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(correo);

        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    // regiones terminadas
    // region USERS
    @POST
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String alias, String nombre_visible, String correo_electronico, String contrasena,
            String biografia, String foto_url) throws ClassNotFoundException {
        if (alias == null || alias.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("datos invalidos").build();
        }
        if (correo_electronico == null || correo_electronico.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("datos invalidos").build();
        } else {
            boolean isMail = comprobarCorreo(correo_electronico);
            if (!isMail) {
                return Response.status(Response.Status.BAD_REQUEST).entity("datos invalidos").build();
            }
        }
        if (contrasena == null || contrasena.trim().isEmpty() || contrasena.length() < 6) {
            return Response.status(Response.Status.BAD_REQUEST).entity("datos invalidos").build();
        }
        Class.forName("org.mariadb.jdbc.Driver");
        try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {
            PreparedStatement ps = conexion.prepareStatement(
                    "insert into usuarios(alias,nombre_visible,correo_electronico,contrasena,biografia,Foto_url) values (?,?,?,?,?,?)");
            ps.setString(1, alias);
            ps.setString(2, nombre_visible);
            ps.setString(3, correo_electronico);
            ps.setString(4, contrasena);
            ps.setString(5, biografia);
            ps.setString(6, foto_url);
            ps.executeUpdate();
            return Response.ok("registro creado satisfactoriamente").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("no reconoce el driver").build();
        }
    }

    @GET
    @Path("/users/{user-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserDetails(@PathParam("user-id") String idConsulta) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {
                String query = "SELECT id, alias, nombre_visible, correo_electronico, biografía, fotografia FROM usuarios WHERE id = ?";
                try (PreparedStatement ps = conexion.prepareStatement(query)) {
                    ps.setString(1, idConsulta);
                    ResultSet respuesta = ps.executeQuery();

                    if (respuesta.next()) {
                        String id = respuesta.getString("id");
                        String alias = respuesta.getString("alias");
                        String nombre = respuesta.getString("nombre_visible");
                        String mail = respuesta.getString("correo_electronico");
                        String bio = respuesta.getString("biografía");
                        String foto = respuesta.getString("fotografia");

                        User user = new User(id, alias, nombre, mail, bio, foto);
                        return Response.ok(user).build();
                    }
                }
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("users/{user-id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyUser(@PathParam("user-id") String idConsulta, String aliasParam, String nombreParam,
            String correoParam, String biografiaParam, String contrasenaParam, String fotografiaParam) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {
                PreparedStatement ps = conexion.prepareStatement(
                        String.format(
                                "update usuario set nombre_visible = ?, correo_electronico=?, biografia=?, contrasena=?, fotografia_url=? where id_usuario = %s",
                                idConsulta));
                ps.setString(1, aliasParam);
                ps.setString(2, nombreParam);
                ps.setString(3, correoParam);
                ps.setString(4, biografiaParam);
                ps.setString(5, contrasenaParam);
                ps.setString(6, fotografiaParam);
                int filas = ps.executeUpdate();
                if (filas == 0)
                    return Response.status(Response.Status.NOT_FOUND).build();
                return Response.ok("usuario actualizado correctamente").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(" no reconoce el driver").build();
        }
    }

    @DELETE
    @Path("users/{user-id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("user-id") String idConsulta) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {
                PreparedStatement ps = conexion
                        .prepareStatement(String.format("delete from usuario where id=?", idConsulta));
                int filas = ps.executeUpdate();
                if (filas == 0)
                    return Response.status(Response.Status.NOT_FOUND).build();
                return Response.ok("usuario eliminado correctamente").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("no se ha podido borrar el usuario").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("no reconoce el Driver").build();
        }
    }

    // endregion
    // region REACTIONS
    @POST
    @Path("/posts/{post-id}/like")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response likeAPost(@PathParam("post-id") String idPost, String idUsuario) {
        LocalDate fecha_local = LocalDate.now();
        Date fecha = Date.valueOf(fecha_local);
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario,
                    password)) {
                PreparedStatement ps = conexion
                        .prepareStatement("insert into reaccionar(id_usuario,id_post,fecha_Reaccion)values(?,?,?)");
                ps.setString(1, idUsuario);
                ps.setString(2, idPost);
                ps.setDate(3, fecha);
                int filas = ps.executeUpdate();
                if (filas == 0)
                    return Response.status(Response.Status.NOT_FOUND).build();
                return Response.ok("has dado like").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("no reconoce el Driver").build();
        }
    }

    @POST
    @Path("/posts/{post-id}/unlike")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response unlikeAPost(@PathParam("post-id") String idPost, String idUsuario) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {
                PreparedStatement ps = conexion
                        .prepareStatement("delete from reacionar where id_post =? and id_usuario=?");
                ps.setString(1, idPost);
                ps.setString(2, idUsuario);
                ps.executeQuery();
                return Response.ok("registro unlike").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("no reconoce el Driver").build();
        }
    }

    @GET
    @Path("/posts/{post-id}/likes ")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listaLike(@PathParam("id_post") String idPost) {
        ArrayList<String> lista = new ArrayList<String>();
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario,
                    password)) {
                PreparedStatement ps = conexion
                        .prepareStatement(String.format("SELECT count(*) where id_post = ? ",
                                idPost));
                ResultSet resultado = ps.executeQuery();
                while (resultado.next()) {
                    if (lista.contains(resultado.getString(1))) {
                        lista.add(resultado.getString(1));
                    }
                }
                return Response.ok(lista).build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("no reconoce eldriver").build();
        }
    }

    // endregion
    // region SOCIAL GRAPH
    @GET
    @Path("/users/{user-id}/followers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listaSeguidores(@PathParam("user-id") int idUsuario) {

        ArrayList<Seguidor> listaSeguidores = new ArrayList<Seguidor>();

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {

                PreparedStatement ps = conexion.prepareStatement(
                        "SELECT id_seguidor, id_seguido, fecha FROM seguir WHERE id_seguido = ?");

                ps.setInt(1, idUsuario);

                ResultSet respuesta = ps.executeQuery();

                while (respuesta.next()) {
                    Seguidor nuevoSeguidor = new Seguidor(
                            respuesta.getInt("id_seguidor"),
                            respuesta.getInt("id_seguido"),
                            respuesta.getDate("fecha"));

                    listaSeguidores.add(nuevoSeguidor);
                }

                return Response.ok(listaSeguidores).build();

            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("error").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("no reconoce el driver").build();
        }
    }

    @GET
    @Path("/users/{user-id}/followed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listaSeguidos(@PathParam("user-id") int idUsuario) {
        ArrayList<Seguidor> listaSeguidos = new ArrayList<Seguidor>();
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {
                PreparedStatement ps = conexion.prepareStatement(
                        "SELECT id_seguidor, id_seguido, fecha FROM seguir WHERE id_seguidor = ?");
                ps.setInt(1, idUsuario);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Seguidor seguido = new Seguidor(
                            rs.getInt("id_seguidor"),
                            rs.getInt("id_seguido"),
                            rs.getDate("fecha"));
                    listaSeguidos.add(seguido);
                }
                return Response.ok(listaSeguidos).build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("error").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("no reconoce el driver").build();
        }
    }

    @POST
    @Path("/users/{user-id}/follow")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response follow(@PathParam("user-id") String idUser, String alias) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {
                PreparedStatement ps = conexion.prepareStatement("SELECT * FROM usuario WHERE alias=?");
                ps.setString(1, alias);
                ResultSet datos = ps.executeQuery();

                if (datos.next()) {
                    String id = datos.getString("id");
                    String aliasDb = datos.getString("alias");
                    String nombre = datos.getString("nombre");
                    String correo = datos.getString("correo");
                    String biografia = datos.getString("biografia");
                    String fotografia = datos.getString("fotografia");

                    PreparedStatement psFollow = conexion.prepareStatement(
                            "INSERT INTO seguir (id_seguidor, id_seguido) VALUES (?, ?)");
                    psFollow.setString(1, idUser);
                    psFollow.setString(2, id);
                    psFollow.executeUpdate();

                    User user = new User(id, aliasDb, nombre, correo, biografia, fotografia);
                    return Response.ok(user).build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Usuario no encontrado").build();
                }
            } catch (SQLException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Error en la base de datos: " + e.getMessage()).build();
            }
        } catch (ClassNotFoundException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("No reconoce el driver").build();
        }
    }

    @POST
    @Path("/users/{user-id}/unfollow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unfollow(@PathParam("user-id") String idUser) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {
                PreparedStatement ps = conexion.prepareStatement("delete from seguir where id=?");
                ps.setString(1, idUser);
                ps.executeUpdate();
                return Response.ok().build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("no se reconoce el driver").build();
        }
    }

    // endregion
    // region POSTS

    @POST
    @Path("/posts")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPost(String contenido, String url_multimedia, int IdPostPadre) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {
                PreparedStatement ps = conexion
                        .prepareStatement("INSERT INTO post(contenido,url_multimedia,id_post_padre) VALUES (?,?,?)");
                ps.setString(1, contenido);
                ps.setString(2, url_multimedia);
                ps.setInt(3, IdPostPadre);
                ps.executeUpdate();
                return Response.ok().build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("no reconoce el driver").build();
        }
    }

    @GET
    @Path("/posts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response recuperarPosts() {
        ArrayList<String> listadoPosts = new ArrayList<String>();
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {
                PreparedStatement ps = conexion
                        .prepareStatement("SELECT contenido FROM post WHERE id_post_padre IS NULL LIMIT 10");
                ResultSet respuesta = ps.executeQuery();
                while (respuesta.next()) {
                    listadoPosts.add(respuesta.getString("contenido"));
                }
                return Response.ok(listadoPosts).build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error").build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("noreconoce el driver").build();
        }
    }

    @GET
    @Path("/posts/{post-id}")
    public Response readPost(@PathParam("post-id") int idPostLeido) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {
                PreparedStatement ps = conexion.prepareStatement("select contenido from post where id=%d ",
                        idPostLeido);
                ps.executeQuery();
                return Response.ok().build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("no reconoce el driver").build();
        }
    }

    @DELETE
    @Path("/posts/{post-id}")
    public Response deletePost(@PathParam("post-id") int idPostBorrado) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {
                PreparedStatement ps = conexion.prepareStatement("delete from posts where id =%d", idPostBorrado);
                ps.executeUpdate();
                return Response.ok().build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("no reconoce el driver").build();
        }
    }

    @GET
    @Path("/posts/{post-id}/replies")
    @Produces(MediaType.APPLICATION_JSON)
    public Response replies(@PathParam("post-id") int idPostComentado) {
        ArrayList<String> comentariosSobrePostComentado = new ArrayList<String>();
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {
                PreparedStatement ps = conexion.prepareStatement("select contenido from post where id_post_padre = %s",
                        idPostComentado);
                ResultSet respuesta = ps.executeQuery();
                while (respuesta.next()) {
                    comentariosSobrePostComentado.add(respuesta.getString("contenido"));
                }
                return Response.ok(comentariosSobrePostComentado).build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("no reconoce el driver").build();
        }
    }

    // endregion
}
