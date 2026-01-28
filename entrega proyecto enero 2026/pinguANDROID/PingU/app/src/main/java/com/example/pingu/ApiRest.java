package com.example.pingu;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class ApiRest {

    public void verSeguidores() {
        new Thread(() -> { // crear un hilo
            try {
                URL url = new URL("http://192.130.0.3:8080/tema5maven/rest/deportistas/android");
                HttpURLConnection con = (HttpURLConnection) url.openConnection(); //abrir conexion
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true); // voy a escribir en el body

                JSONObject json = new JSONObject();
//                json.put("id",id);
//                json.put("nombre",nombre);
//                json.put("nombre usuario",nombre_visible);
//                json.put("alta",fecha_alta);
//                json.put("correo",correo_electronico);
//                json.put("biografia",biografia);
//                json.put("pass",contrasena);
//                json.put("foto",fotografia);


                System.err.println(json);

                try (OutputStream os = con.getOutputStream()) {
                    os.write(json.toString().getBytes(StandardCharsets.UTF_8)); // enviar el body
                } catch (IOException e) {

                }
                int code = con.getResponseCode(); // forzar envio
                Log.i("CODIGO APIREST:", "EL CODIGO RESULTANTE ES:" + code); //SACA UN CODIGO EN EL LOGCAT

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }).start();
    }

    public void subirUsuario(String nombre, String alias, String mail, String biografia, String pass, String foto) {
        new Thread(() -> { // crear un hilo
            try {
                URL url = new URL("http://192.130.0.3:8080/api/rest/android");
                HttpURLConnection con = (HttpURLConnection) url.openConnection(); //abrir conexion
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true); // voy a escribir en el body
                JSONObject json = new JSONObject();
                json.put("nombre", nombre);
                json.put("alias", alias);
                json.put("correo", mail);
                json.put("biografia", biografia);
                json.put("contrasena", pass);
                json.put("foto", foto);

                System.err.println(json);

                try (OutputStream os = con.getOutputStream()) {
                    os.write(json.toString().getBytes(StandardCharsets.UTF_8)); // enviar el body
                } catch (IOException e) {

                }
                int code = con.getResponseCode(); // forzar envio
                Log.i("CODIGO APIREST:", "EL CODIGO RESULTANTE ES:" + code); //SACA UN CODIGO EN EL LOGCAT

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }).start();
    }

}

