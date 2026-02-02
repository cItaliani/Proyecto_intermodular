package ejem1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Pattern;
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
import jakarta.ws.rs.core.Response.Status;

@Path("/PingU")
public class api {

    String url = "http://sql.freedb.tech:3306/freedb_PingU_db";
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
                String query = "SELECT alias, nombre_visible, correo_electronico, biografía, fotografia FROM usuarios WHERE id = ?";
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
    public Response modifyUser(@PathParam("user-id") String idConsulta) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {
                PreparedStatement ps = conexion.prepareStatement(
                        "update usuario set nombre_visible = ?, correo_electronico=?, biografia=?, contrasena=?, fotografia_url=?");
                ps.setString(1, "alias");
                ps.setString(2, "nombre_visible");
                ps.setString(3, "correo_electronico");
                ps.setString(4, "biografia");
                ps.setString(5, "contrasena");
                ps.setString(6, "fotografia_url");
                ps.executeUpdate();
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
                ps.executeQuery();
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

    // regiones en construccion
    // region SOCIAL GRAPH
    // endregion
    // region POSTS
    // endregion
    // region REACTIONS
@POST
@Path("/posts/{post-id}/like")
@





    // endregion

}