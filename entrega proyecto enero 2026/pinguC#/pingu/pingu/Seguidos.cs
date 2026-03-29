using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Linq;
using System.Net.Http;
using System.Text.Json;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace pingu
{
    public partial class Seguidos : Form
    {
        private bool cerrandoSesion = false;
        private FlowLayoutPanel panelUsuarios;

        public Seguidos()
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

        private async void Seguidos_Load(object sender, EventArgs e)
        {
            InicializarPanelUsuarios();
            btnNuevoPost.BringToFront();
            await CargarSeguidosAsync();
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

        private async Task CargarSeguidosAsync()
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

                List<User> usuariosSeguidos = usuarios
                    .Where(u =>
                    {
                        string idUsuario = ObtenerIdUsuario(u);
                        return !string.IsNullOrWhiteSpace(idUsuario) && idsSeguidos.Contains(idUsuario);
                    })
                    .ToList();

                if (usuariosSeguidos.Count == 0)
                {
                    Label lblVacio = new Label();
                    lblVacio.Text = "Todavía no sigues a ningún usuario.";
                    lblVacio.Font = new Font("Segoe UI", 11, FontStyle.Regular);
                    lblVacio.AutoSize = true;
                    lblVacio.Margin = new Padding(12);
                    panelUsuarios.Controls.Add(lblVacio);
                    return;
                }

                foreach (User usuario in usuariosSeguidos)
                {
                    Panel card = CrearTarjetaUsuario(usuario);
                    panelUsuarios.Controls.Add(card);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(
                    "Error al cargar los usuarios seguidos.\n\n" + ex.Message,
                    "Error",
                    MessageBoxButtons.OK,
                    MessageBoxIcon.Error);
            }
        }

        private Panel CrearTarjetaUsuario(User usuario)
        {
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
            lblBio.MaximumSize = new Size(700, 0);
            lblBio.AutoSize = true;
            lblBio.Location = new Point(15, 100);

            card.Controls.Add(lblAlias);
            card.Controls.Add(lblNombre);
            card.Controls.Add(lblBioTitulo);
            card.Controls.Add(lblBio);

            return card;
        }

        private void btnInicio_Click(object sender, EventArgs e)
        {
            Principal f = new Principal();
            this.Hide();
            DialogResult respuesta = f.ShowDialog();

            if (respuesta == DialogResult.OK || respuesta == DialogResult.Abort || respuesta == DialogResult.Cancel)
            {
                this.Close();
            }
        }

        private void btnPerfil_Click(object sender, EventArgs e)
        {
            Perfil f = new Perfil();
            this.Hide();
            DialogResult respuesta = f.ShowDialog();

            if (respuesta == DialogResult.OK || respuesta == DialogResult.Abort || respuesta == DialogResult.Cancel)
            {
                this.Close();
            }
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

        private async void btnSeguidos_Click(object sender, EventArgs e)
        {
            await CargarSeguidosAsync();
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
                await CargarSeguidosAsync();
            }
       
        }

        private void btnSalir_Click(object sender, EventArgs e)
        {
            cerrandoSesion = true;
            this.DialogResult = DialogResult.Cancel;
            this.Close();
        }

        private void Seguidos_FormClosed(object sender, FormClosedEventArgs e)
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
    }
}