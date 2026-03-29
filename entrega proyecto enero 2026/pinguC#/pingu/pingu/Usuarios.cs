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
    public partial class Usuarios : Form
    {
        private bool cerrandoSesion = false;
        private FlowLayoutPanel panelUsuarios;

        public Usuarios()
        {
            InitializeComponent();
            HacerBotonCircular(btnNuevoPost);
            button5.Enabled = false;
            button5.Visible = false;
        }

        public class User
        {
            public string alias { get; set; }
            public string nombre_visible { get; set; }
            public string nombreVisible { get; set; }
            public string nombre { get; set; }
            public string correo_electronico { get; set; }
            public string biografia { get; set; }
            public string bio { get; set; }
            public string fotografia { get; set; }
            public string fotografia_url { get; set; }

            public JsonElement user_id { get; set; }
            public JsonElement id { get; set; }
            public JsonElement idUsuario { get; set; }
            public JsonElement id_usuario { get; set; }
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

        private async void Usuarios_Load(object sender, EventArgs e)
        {
            InicializarPanelUsuarios();
            btnNuevoPost.BringToFront();
            await CargarUsuariosAsync();
        }

        private void InicializarPanelUsuarios()
        {
            panel2.Controls.Clear();

            panelUsuarios = new FlowLayoutPanel();
            panelUsuarios.Dock = DockStyle.Fill;
            panelUsuarios.FlowDirection = FlowDirection.TopDown;
            panelUsuarios.WrapContents = false;
            panelUsuarios.AutoScroll = true;
            panelUsuarios.Padding = new Padding(10);
            panelUsuarios.BackColor = Color.FromArgb(4, 228, 140);


            panel2.Controls.Add(panelUsuarios);
        }

        private void HacerBotonCircular(Button boton)
        {
            GraphicsPath path = new GraphicsPath();
            path.AddEllipse(0, 0, boton.Width, boton.Height);
            boton.Region = new Region(path);
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

            string textoLimpio = respuestaTexto;

            if (!string.IsNullOrWhiteSpace(textoLimpio) && textoLimpio.TrimStart().StartsWith("<!doctype"))
            {
                textoLimpio = response.StatusCode == System.Net.HttpStatusCode.NotFound
                    ? "Recurso no encontrado (404)."
                    : "El servidor devolvió una página HTML en lugar de JSON.";
            }

            if (response.IsSuccessStatusCode)
            {
                respuesta.message = string.IsNullOrWhiteSpace(textoLimpio)
                    ? "Operación realizada correctamente."
                    : textoLimpio;
            }
            else
            {
                respuesta.error = string.IsNullOrWhiteSpace(textoLimpio)
                    ? "Se produjo un error en la operación."
                    : textoLimpio;
            }

            return respuesta;
        }

        private async Task<List<User>> ObtenerUsuariosAsync()
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/api/rest/");
                HttpResponseMessage response = await client.GetAsync("pingu/users");
                string json = await response.Content.ReadAsStringAsync();

                if (!response.IsSuccessStatusCode)
                    return new List<User>();

           
                return JsonSerializer.Deserialize<List<User>>(
                    json,
                    new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
            }
        }
        private string ObtenerIdUsuario(User usuario)
        {
            if (usuario == null) return "";

            string valor;

            valor = JsonElementAString(usuario.user_id);
            if (!string.IsNullOrWhiteSpace(valor)) return valor;

            valor = JsonElementAString(usuario.id);
            if (!string.IsNullOrWhiteSpace(valor)) return valor;

            valor = JsonElementAString(usuario.idUsuario);
            if (!string.IsNullOrWhiteSpace(valor)) return valor;

            valor = JsonElementAString(usuario.id_usuario);
            if (!string.IsNullOrWhiteSpace(valor)) return valor;

            return "";
        }

        private string ObtenerIdSeguido(Seguidor seg)
        {
            if (seg == null) return "";

            string valor;

            valor = JsonElementAString(seg.idSeguido);
            if (!string.IsNullOrWhiteSpace(valor)) return valor;

            valor = JsonElementAString(seg.id_seguido);
            if (!string.IsNullOrWhiteSpace(valor)) return valor;

            return "";
        }

        private string ObtenerNombreVisible(User usuario)
        {
            if (usuario == null) return "Sin nombre visible";

            if (!string.IsNullOrWhiteSpace(usuario.nombre_visible)) return usuario.nombre_visible;
            if (!string.IsNullOrWhiteSpace(usuario.nombreVisible)) return usuario.nombreVisible;
            if (!string.IsNullOrWhiteSpace(usuario.nombre)) return usuario.nombre;

            return "Sin nombre visible";
        }

        private string ObtenerBiografia(User usuario)
        {
            if (usuario == null) return "Este usuario no tiene biografía.";

            if (!string.IsNullOrWhiteSpace(usuario.biografia)) return usuario.biografia;
            if (!string.IsNullOrWhiteSpace(usuario.bio)) return usuario.bio;

            return "Este usuario no tiene biografía.";
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

        private async Task<ApiResponse> SeguirAsync(string idUsuarioSeguido)
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/api/rest/");

                var body = new
                {
                    id_seguidor = Log_in.idUsuarioLogado
                };

                string json = JsonSerializer.Serialize(body);
                StringContent contenido = new StringContent(json, Encoding.UTF8, "application/json");

                HttpResponseMessage response = await client.PostAsync("pingu/users/" + idUsuarioSeguido + "/follow", contenido);
                string respuestaTexto = await response.Content.ReadAsStringAsync();

                return ParsearRespuestaApi(response, respuestaTexto);
            }
        }

        private async Task<ApiResponse> DejarDeSeguirAsync(string idUsuarioSeguido)
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/api/rest/");

                var body = new
                {
                    id_seguidor = Log_in.idUsuarioLogado
                };

                string json = JsonSerializer.Serialize(body);
                StringContent contenido = new StringContent(json, Encoding.UTF8, "application/json");

                HttpResponseMessage response = await client.PostAsync("pingu/users/" + idUsuarioSeguido + "/unfollow", contenido);
                string respuestaTexto = await response.Content.ReadAsStringAsync();

                return ParsearRespuestaApi(response, respuestaTexto);
            }
        }

        private async Task CargarUsuariosAsync()
        {
            try
            {
                if (panelUsuarios == null)
                    InicializarPanelUsuarios();

                panelUsuarios.Controls.Clear();

                List<User> usuarios = await ObtenerUsuariosAsync();
                List<Seguidor> seguidos = await ObtenerSeguidosAsync();

                HashSet<string> idsSeguidos = new HashSet<string>(
                    seguidos
                        .Select(s => ObtenerIdSeguido(s))
                        .Where(id => !string.IsNullOrWhiteSpace(id))
                );

                foreach (User usuario in usuarios)
                {
                    string idUsuario = ObtenerIdUsuario(usuario);

                    if (string.IsNullOrWhiteSpace(idUsuario))
                        continue;

                    if (idUsuario == Log_in.idUsuarioLogado)
                        continue;

                    Panel card = CrearTarjetaUsuario(usuario, idsSeguidos.Contains(idUsuario));
                    panelUsuarios.Controls.Add(card);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(
                    "Error al cargar los usuarios.\n\n" + ex.Message,
                    "Error",
                    MessageBoxButtons.OK,
                    MessageBoxIcon.Error);
            }
        }

        private Panel CrearTarjetaUsuario(User usuario, bool yaLoSigo)
        {
            string idUsuario = ObtenerIdUsuario(usuario);

            Panel card = new Panel();
            card.Width = 800;
            card.Height = 170;
            card.BackColor = Color.White;
            card.BorderStyle = BorderStyle.FixedSingle;
            card.Margin = new Padding(8);

            Label lblAlias = new Label();
            lblAlias.Text = "@" + (string.IsNullOrWhiteSpace(usuario.alias) ? "sin_alias" : usuario.alias);
            lblAlias.Font = new Font("Segoe UI", 11, FontStyle.Bold);
            lblAlias.AutoSize = true;
            lblAlias.Location = new Point(15, 15);

            Label lblNombre = new Label();
            lblNombre.Text = ObtenerNombreVisible(usuario);
            lblNombre.Font = new Font("Segoe UI", 10, FontStyle.Regular);
            lblNombre.AutoSize = true;
            lblNombre.Location = new Point(15, 45);

            Label lblBioTitulo = new Label();
            lblBioTitulo.Text = "Biografía:";
            lblBioTitulo.Font = new Font("Segoe UI", 9, FontStyle.Bold);
            lblBioTitulo.AutoSize = true;
            lblBioTitulo.Location = new Point(15, 78);

            Label lblBio = new Label();
            lblBio.Text = ObtenerBiografia(usuario);
            lblBio.Font = new Font("Segoe UI", 9, FontStyle.Regular);
            lblBio.MaximumSize = new Size(580, 0);
            lblBio.AutoSize = true;
            lblBio.Location = new Point(15, 100);

            Button btnSeguir = new Button();
            btnSeguir.Text = yaLoSigo ? "Dejar de seguir" : "Seguir";
            btnSeguir.BackColor = yaLoSigo ? Color.Firebrick : Color.FromArgb(97, 81, 155);
            btnSeguir.ForeColor = Color.White;
            btnSeguir.Size = new Size(150, 38);
            btnSeguir.Location = new Point(620, 60);

            btnSeguir.Click += async (s, e) =>
            {
                try
                {
                    if (string.IsNullOrWhiteSpace(idUsuario))
                    {
                        MessageBox.Show("No se pudo identificar el usuario.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                        return;
                    }

                    btnSeguir.Enabled = false;

                    ApiResponse resultado;

                    if (btnSeguir.Text == "Seguir")
                    {
                        resultado = await SeguirAsync(idUsuario);
                    }
                    else
                    {
                        resultado = await DejarDeSeguirAsync(idUsuario);
                    }

                    if (!string.IsNullOrEmpty(resultado.error))
                    {
                        MessageBox.Show(resultado.error, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                        btnSeguir.Enabled = true;
                        return;
                    }

                    await CargarUsuariosAsync();
                }
                catch (Exception ex)
                {
                    MessageBox.Show(
                        "Error al cambiar el seguimiento.\n\n" + ex.Message,
                        "Error",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Error);

                    btnSeguir.Enabled = true;
                }
            };

            card.Controls.Add(lblAlias);
            card.Controls.Add(lblNombre);
            card.Controls.Add(lblBioTitulo);
            card.Controls.Add(lblBio);
            card.Controls.Add(btnSeguir);

            return card;
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
            Perfil f = new Perfil();
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

        private async void btnUsers_Click(object sender, EventArgs e)
        {
            await CargarUsuariosAsync();
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

            if (respuesta == DialogResult.OK || respuesta == DialogResult.Abort)
            {
                this.Close();
            }
            else if (respuesta == DialogResult.Cancel)
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
                await CargarUsuariosAsync();
            }
        }

        private void btnSalir_Click(object sender, EventArgs e)
        {
            cerrandoSesion = true;
            this.DialogResult = DialogResult.Cancel;
            this.Close();
        }

        private void Usuarios_FormClosed(object sender, FormClosedEventArgs e)
        {
            if (cerrandoSesion)
            {
                this.DialogResult = DialogResult.Cancel;
            }
            else if (this.DialogResult != DialogResult.OK && this.DialogResult != DialogResult.Cancel)
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

        private string JsonElementAString(JsonElement element)
        {
            if (element.ValueKind == JsonValueKind.String)
                return element.GetString();

            if (element.ValueKind == JsonValueKind.Number)
                return element.GetRawText();

            if (element.ValueKind == JsonValueKind.Null || element.ValueKind == JsonValueKind.Undefined)
                return "";

            return element.ToString();
        }
    }
}