using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using System.Windows.Forms;


namespace pingu
{
    public partial class Perfil : Form
    {
        private bool cerrandoSesion = false;
        private string biografiaOriginal = "";
        private string nombreVisibleOriginal = "";

        public Perfil()
        {
            InitializeComponent();
            HacerBotonCircular(btnNuevoPost);
            btnGuardar.TextAlign = ContentAlignment.MiddleCenter;
            button5.Enabled = false;
            button5.Visible = false;
            btnNuevoPost.Visible=false;
        }

        public class User
        {
            public string id { get; set; }
            public string id_usuario { get; set; }
            public string alias { get; set; }
            public string nombre_visible { get; set; }
            public string correo_electronico { get; set; }
            public string biografia { get; set; }
            public string fotografia_url { get; set; }
            public JsonElement fecha_alta { get; set; }
        }

        public class Seguidor
        {
            public JsonElement idSeguidor { get; set; }
            public JsonElement id_seguidor { get; set; }
            public JsonElement idSeguido { get; set; }
            public JsonElement id_seguido { get; set; }
        }

        public class ApiResponse
        {
            public string message { get; set; }
            public string mensaje { get; set; }
            public string error { get; set; }
            public string id { get; set; }
        }

        private async void Perfil_Load(object sender, EventArgs e)
        {
            btnNuevoPost.BringToFront();
            await CargarPerfilAsync();
        }

        private void HacerBotonCircular(Button boton)
        {
            GraphicsPath path = new GraphicsPath();
            path.AddEllipse(0, 0, boton.Width, boton.Height);
            boton.Region = new Region(path);
        }

        private string JsonElementAString(JsonElement element)
        {
            if (element.ValueKind == JsonValueKind.String)
                return element.GetString();

            if (element.ValueKind == JsonValueKind.Number)
            {
                long numero;

                if (element.TryGetInt64(out numero))
                {
                    if (numero > 100000000000) // timestamp en milisegundos
                    {
                        DateTime fecha = DateTimeOffset.FromUnixTimeMilliseconds(numero).DateTime;
                        return fecha.ToString("dd/MM/yyyy");
                    }

                    return numero.ToString();
                }

                return element.GetRawText();
            }

            if (element.ValueKind == JsonValueKind.Null || element.ValueKind == JsonValueKind.Undefined)
                return "";

            return element.ToString();
        }

        private ApiResponse ParsearRespuestaApi(HttpResponseMessage response, string respuestaTexto)
        {
            try
            {
                if (!string.IsNullOrWhiteSpace(respuestaTexto) &&
                    (respuestaTexto.Trim().StartsWith("{") || respuestaTexto.Trim().StartsWith("[")))
                {
                    ApiResponse obj = JsonSerializer.Deserialize<ApiResponse>(
                        respuestaTexto,
                        new JsonSerializerOptions { PropertyNameCaseInsensitive = true });

                    if (obj != null)
                        return obj;
                }
            }
            catch
            {
            }

            ApiResponse respuesta = new ApiResponse();

            if (response.IsSuccessStatusCode)
            {
                respuesta.message = string.IsNullOrWhiteSpace(respuestaTexto)
                    ? "Operación realizada correctamente."
                    : respuestaTexto;
            }
            else
            {
                respuesta.error = string.IsNullOrWhiteSpace(respuestaTexto)
                    ? "Se produjo un error en la operación."
                    : respuestaTexto;
            }

            return respuesta;
        }

        private async Task<User> ObtenerMiUsuarioAsync()
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/api/rest/");
                HttpResponseMessage response = await client.GetAsync("pingu/users/" + Log_in.idUsuarioLogado);
                string json = await response.Content.ReadAsStringAsync();

                if (!response.IsSuccessStatusCode)
                    return null;

                return JsonSerializer.Deserialize<User>(
                    json,
                    new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
            }
        }

        private async Task<List<Seguidor>> ObtenerSeguidoresAsync()
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/api/rest/");
                HttpResponseMessage response = await client.GetAsync("pingu/users/" + Log_in.idUsuarioLogado + "/followers");
                string json = await response.Content.ReadAsStringAsync();

                if (!response.IsSuccessStatusCode)
                    return new List<Seguidor>();

                return JsonSerializer.Deserialize<List<Seguidor>>(
                    json,
                    new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
            }
        }

        private async Task<List<Seguidor>> ObtenerSeguidosAsync()
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/api/rest/");
                HttpResponseMessage response = await client.GetAsync("pingu/users/" + Log_in.idUsuarioLogado + "/followed");
                string json = await response.Content.ReadAsStringAsync();

                if (!response.IsSuccessStatusCode)
                    return new List<Seguidor>();

                return JsonSerializer.Deserialize<List<Seguidor>>(
                    json,
                    new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
            }
        }

        private async Task<ApiResponse> ActualizarPerfilAsync(string nombreVisible, string biografia, string contrasena)
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/api/rest/");

                var body = new
                {
                    nombre_visible = nombreVisible,
                    biografia = biografia,
                    contrasena = contrasena,
                    fotografia = ""
                };

                string json = JsonSerializer.Serialize(body);
                StringContent contenido = new StringContent(json, Encoding.UTF8, "application/json");

                HttpResponseMessage response = await client.PutAsync("pingu/users/" + Log_in.idUsuarioLogado, contenido);
                string respuestaTexto = await response.Content.ReadAsStringAsync();

                return ParsearRespuestaApi(response, respuestaTexto);
            }
        }

        private async Task<ApiResponse> EliminarCuentaAsync()
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/api/rest/");
                HttpResponseMessage response = await client.DeleteAsync("pingu/users/" + Log_in.idUsuarioLogado);
                string respuestaTexto = await response.Content.ReadAsStringAsync();

                return ParsearRespuestaApi(response, respuestaTexto);
            }
        }

        private async Task CargarPerfilAsync()
        {
            try
            {
                User usuario = await ObtenerMiUsuarioAsync();
                List<Seguidor> seguidores = await ObtenerSeguidoresAsync();
                List<Seguidor> seguidos = await ObtenerSeguidosAsync();

                if (usuario == null)
                {
                    MessageBox.Show("No se pudo cargar el perfil.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }

                lblAliasRespuesta.Text = "@" + usuario.alias;
       
                txtNombreVisible.Text = usuario.nombre_visible;
                lblCorreoRespuesta.Text = usuario.correo_electronico;
                string fechaAltaTexto = JsonElementAString(usuario.fecha_alta);
                lblMiembroRespuesta.Text = string.IsNullOrWhiteSpace(fechaAltaTexto) ? "-" : fechaAltaTexto;
                lblSeguidoresRespuesta.Text = seguidores.Count.ToString();
                lblSeguidosRespuesta.Text = seguidos.Count.ToString();
                txtBiografia.Text = usuario.biografia ?? "";

                nombreVisibleOriginal = usuario.nombre_visible ?? "";
                biografiaOriginal = usuario.biografia ?? "";
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error al cargar el perfil.\n\n" + ex.Message,
                    "Error",
                    MessageBoxButtons.OK,
                    MessageBoxIcon.Error);
            }
        }

        private async void btnGuardar_Click(object sender, EventArgs e)
        {
            string nombreVisible = txtNombreVisible.Text.Trim();
            string bio = txtBiografia.Text.Trim();
            string pass1 = txtPassNueva.Text.Trim();
            string pass2 = txtPassConfirmar.Text.Trim();

            if (string.IsNullOrWhiteSpace(nombreVisible))
            {
                MessageBox.Show("El nombre visible no puede estar vacío.", "Validación", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            string contrasenaAEnviar = pass1;

            if (!string.IsNullOrWhiteSpace(pass1) || !string.IsNullOrWhiteSpace(pass2))
            {
                if (pass1.Length < 6)
                {
                    MessageBox.Show("La nueva contraseña debe tener al menos 6 caracteres.", "Validación", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                    return;
                }

                if (pass1 != pass2)
                {
                    MessageBox.Show("Las contraseñas no coinciden.", "Validación", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                    return;
                }
            }
            else
            {
                MessageBox.Show("Para guardar cambios, introduce también una contraseña nueva.", "Validación", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            try
            {
                btnGuardar.Enabled = false;

                ApiResponse resultado = await ActualizarPerfilAsync(nombreVisible, bio, contrasenaAEnviar);

                if (!string.IsNullOrEmpty(resultado.error))
                {
                    MessageBox.Show(resultado.error, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }

                MessageBox.Show("Perfil actualizado correctamente.", "Correcto", MessageBoxButtons.OK, MessageBoxIcon.Information);

                txtPassNueva.Text = "";
                txtPassConfirmar.Text = "";
                await CargarPerfilAsync();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error al guardar cambios.\n\n" + ex.Message,
                    "Error",
                    MessageBoxButtons.OK,
                    MessageBoxIcon.Error);
            }
            finally
            {
                btnGuardar.Enabled = true;
            }
        }

        private void btnCancel_Click(object sender, EventArgs e)
        {
            this.DialogResult = DialogResult.Abort;
            this.Close();
        }

        private async void btnEliminarCuenta_Click(object sender, EventArgs e)
        {
            DialogResult confirmar = MessageBox.Show(
                "¿Seguro que quieres eliminar tu cuenta? Esta acción no se puede deshacer.",
                "Eliminar cuenta",
                MessageBoxButtons.YesNo,
                MessageBoxIcon.Warning);

            if (confirmar != DialogResult.Yes)
                return;

            try
            {
                btnEliminarCuenta.Enabled = false;

                ApiResponse resultado = await EliminarCuentaAsync();

                if (!string.IsNullOrEmpty(resultado.error))
                {
                    MessageBox.Show(resultado.error, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }

                Log_in.idUsuarioLogado = "";
                this.DialogResult = DialogResult.Cancel;
                this.Close();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error al eliminar la cuenta.\n\n" + ex.Message,
                    "Error",
                    MessageBoxButtons.OK,
                    MessageBoxIcon.Error);
            }
            finally
            {
                btnEliminarCuenta.Enabled = true;
            }
        }

        private void btnInicio_Click(object sender, EventArgs e)
        {
            Principal f = new Principal();
            this.Hide();
            DialogResult respuesta = f.ShowDialog();

            if (respuesta == DialogResult.OK || respuesta == DialogResult.Abort)
            {
                this.Close();
            }
            else if (respuesta == DialogResult.Cancel)
            {
                this.Close();
            }
        }

        private void btnPerfil_Click(object sender, EventArgs e)
        {
        }

        private void btnUsers_Click(object sender, EventArgs e)
        {
            Usuarios f = new Usuarios();
            this.Hide();
            DialogResult respuesta = f.ShowDialog();

            if (respuesta == DialogResult.OK || respuesta == DialogResult.Abort || respuesta == DialogResult.Cancel)
            {
                this.Close();
            }
        }

        private void btnSeguidores_Click(object sender, EventArgs e)
        {
            Seguidores f = new Seguidores();
            this.Hide();
            DialogResult respuesta = f.ShowDialog();

            if (respuesta == DialogResult.OK || respuesta == DialogResult.Abort || respuesta == DialogResult.Cancel)
            {
                this.Close();
            }
        }

        private void btnSeguidos_Click(object sender, EventArgs e)
        {
            Seguidos f = new Seguidos();
            this.Hide();
            DialogResult respuesta = f.ShowDialog();

            if (respuesta == DialogResult.OK || respuesta == DialogResult.Abort || respuesta == DialogResult.Cancel)
            {
                this.Close();
            }
        }

        private void btnPublicaciones_Click(object sender, EventArgs e)
        {
            MisPost f = new MisPost();
            this.Hide();
            DialogResult respuesta = f.ShowDialog();

            if (respuesta == DialogResult.OK || respuesta == DialogResult.Abort || respuesta == DialogResult.Cancel)
            {
                this.Close();
            }
        }

        private async void btnNuevoPost_Click(object sender, EventArgs e)
        {
            NuevoPost f = new NuevoPost();
            this.Hide();
            DialogResult respuesta = f.ShowDialog();
            this.Show();

            if (respuesta == DialogResult.OK)
            {
                await CargarPerfilAsync();
            }
        }

        private void btnSalir_Click(object sender, EventArgs e)
        {
            cerrandoSesion = true;
            this.DialogResult = DialogResult.Cancel;
            this.Close();
        }

        private void Perfil_FormClosed(object sender, FormClosedEventArgs e)
        {
            if (cerrandoSesion)
            {
                this.DialogResult = DialogResult.Cancel;
            }
            else if (this.DialogResult != DialogResult.OK &&
                     this.DialogResult != DialogResult.Cancel &&
                     this.DialogResult != DialogResult.Abort)
            {
                this.DialogResult = DialogResult.Abort;
            }
        }

        private void btnSalir_MouseEnter(object sender, EventArgs e)
        {
            btnSalir.BackColor = Color.Red;
        }

        private void btnSalir_MouseLeave(object sender, EventArgs e)
        {
            btnSalir.BackColor = Color.FromArgb(97, 81, 155);
        }

        private void button_MouseHover(object sender, EventArgs e)
        {
            ((Button)sender).BackColor = Color.DarkGreen;
        }

        private void button_MouseLeave(object sender, EventArgs e)
        {
            ((Button)sender).BackColor = Color.FromArgb(97, 81, 155);
        }

        private void btnCancel_MouseEnter(object sender, EventArgs e)
        {
             btnCancel.BackColor = Color.Firebrick;
        }
        private void btnCancel_MouseLeave(object sender, EventArgs e)
        {
            btnCancel.BackColor = Color.FromArgb(97, 81, 155);
        }

        private void btnGuardar_MouseEnter(object sender, EventArgs e)
        {
            btnGuardar.BackColor = Color.DarkGreen;
        }

        private void btnGuardar_MouseLeave(object sender, EventArgs e)
        {
            btnGuardar.BackColor = Color.FromArgb(97, 81, 155);
        }

        private void label8_Click(object sender, EventArgs e)
        {

        }

        private void label6_Click(object sender, EventArgs e)
        {

        }
    }
}