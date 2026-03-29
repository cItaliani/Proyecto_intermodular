using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Drawing.Drawing2D;


namespace pingu
{
    public partial class Principal : Form
    {
        private FlowLayoutPanel panelPosts;

        public Principal()
        {
            InitializeComponent();
            button5.Enabled = false;
            button5.Visible = false;
            HacerBotonCircular(btnNuevoPost);
        }

        public class Post
        {
            public string id { get; set; }
            public string id_autor { get; set; }
            public string contenido { get; set; }
            public string urlMultimedia { get; set; }
            public string idPostPadre { get; set; }
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
        }

        public class Reaccion
        {
            public string idUsuario { get; set; }
            public string id_post { get; set; }

        }

        public class ApiResponse
        {
            public string message { get; set; }
            public string mensaje { get; set; }
            public string error { get; set; }
            public string id { get; set; }
        }

        private async void Form3_Load(object sender, EventArgs e)
        {
            InicializarPanelPosts();
            btnNuevoPost.BringToFront();
            await CargarHomeAsync();
        }

        private void InicializarPanelPosts()
        {
            panel2.Controls.Clear();

            panelPosts = new FlowLayoutPanel();
            panelPosts.Dock = DockStyle.Fill;
            panelPosts.FlowDirection = FlowDirection.TopDown;
            panelPosts.WrapContents = false;
            panelPosts.AutoScroll = true;
            panelPosts.Padding = new Padding(10);
            panelPosts.BackColor = Color.FromArgb(4, 228, 140);

            panel2.Controls.Add(panelPosts);
        }

        private async Task<List<Post>> ObtenerPostsAsync()
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/api/rest/");
                HttpResponseMessage response = await client.GetAsync("pingu/posts");
                string json = await response.Content.ReadAsStringAsync();

                if (!response.IsSuccessStatusCode)
                    return new List<Post>();

                return JsonSerializer.Deserialize<List<Post>>(json,
                    new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
            }
        }

        private async Task<User> ObtenerUsuarioAsync(string userId)
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/api/rest/");
                HttpResponseMessage response = await client.GetAsync("pingu/users/" + userId);
                string json = await response.Content.ReadAsStringAsync();

                if (!response.IsSuccessStatusCode)
                    return null;

                return JsonSerializer.Deserialize<User>(json,
                    new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
            }
        }

        private async Task<List<Reaccion>> ObtenerLikesAsync(string postId)
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/api/rest/");
                HttpResponseMessage response = await client.GetAsync("pingu/posts/" + postId + "/likes");
                string json = await response.Content.ReadAsStringAsync();

                if (!response.IsSuccessStatusCode)
                    return new List<Reaccion>();

                return JsonSerializer.Deserialize<List<Reaccion>>(json,
                    new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
            }
        }

        private async Task<ApiResponse> DarLikeAsync(string postId)
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/api/rest/");

                var body = new
                {
                    idUsuario = Log_in.idUsuarioLogado
                };

                string json = JsonSerializer.Serialize(body);
                StringContent contenido = new StringContent(json, Encoding.UTF8, "application/json");

                HttpResponseMessage response = await client.PostAsync("pingu/posts/" + postId + "/like", contenido);
                string respuestaJson = await response.Content.ReadAsStringAsync();

                return ParsearRespuestaApi(response, respuestaJson);
            }
        }

        private async Task<ApiResponse> QuitarLikeAsync(string postId)
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/api/rest/");

                var body = new
                {
                    idUsuario = Log_in.idUsuarioLogado
                };

                string json = JsonSerializer.Serialize(body);
                StringContent contenido = new StringContent(json, Encoding.UTF8, "application/json");

                HttpResponseMessage response = await client.PostAsync("pingu/posts/" + postId + "/dislike", contenido);
                string respuestaJson = await response.Content.ReadAsStringAsync();

                return ParsearRespuestaApi(response, respuestaJson);
            }
        }

        private async Task<ApiResponse> BorrarPostAsync(string postId)
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/api/rest/");
                HttpResponseMessage response = await client.DeleteAsync("pingu/posts/" + postId);
                string respuestaJson = await response.Content.ReadAsStringAsync();

                return ParsearRespuestaApi(response, respuestaJson);
            }
        }

        private async Task<List<Post>> ObtenerRespuestasAsync(string postId)
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/api/rest/");
                HttpResponseMessage response = await client.GetAsync("pingu/posts/" + postId + "/replies");
                string json = await response.Content.ReadAsStringAsync();

                if (!response.IsSuccessStatusCode)
                    return new List<Post>();

                return JsonSerializer.Deserialize<List<Post>>(json,
                    new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
            }
        }

        private async Task<ApiResponse> EnviarRespuestaAsync(string postPadreId, string textoRespuesta)
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/api/rest/");

                var body = new
                {
                    contenido = textoRespuesta,
                    urlMultimedia = "",
                    id_autor = Log_in.idUsuarioLogado,
                    idPostPadre = postPadreId
                };

                string json = JsonSerializer.Serialize(body);
                StringContent contenidoJson = new StringContent(json, Encoding.UTF8, "application/json");

                HttpResponseMessage response = await client.PostAsync("pingu/posts", contenidoJson);
                string respuestaJson = await response.Content.ReadAsStringAsync();

                return ParsearRespuestaApi(response, respuestaJson);
            }
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


        private async Task CargarHomeAsync()
        {
            try
            {
                if (panelPosts == null)
                    InicializarPanelPosts();

                panelPosts.Controls.Clear();

                List<Post> posts = await ObtenerPostsAsync();

                foreach (Post post in posts)
                {
                    Panel card = await CrearTarjetaPostAsync(post);
                    panelPosts.Controls.Add(card);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error al cargar los posts.\n\n" + ex.Message,
                    "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        

        private async Task<Panel> CrearTarjetaPostAsync(Post post)
        {
            string postId = post.id;

            User autor = await ObtenerUsuarioAsync(post.id_autor);
            List<Reaccion> likes = await ObtenerLikesAsync(postId);

            bool yaLeDiLike = likes.Any(x => x.idUsuario == Log_in.idUsuarioLogado);
            bool esMio = post.id_autor == Log_in.idUsuarioLogado;

            Panel card = new Panel();
            card.Width = 800;
            card.Height = 250;
            card.BackColor = Color.White;
            card.BorderStyle = BorderStyle.FixedSingle;
            card.Margin = new Padding(8);

            Label lblAutor = new Label();
            lblAutor.Text = autor != null ? autor.nombre_visible : "Usuario desconocido";
            lblAutor.Font = new Font("Segoe UI", 11, FontStyle.Bold);
            lblAutor.AutoSize = true;
            lblAutor.Location = new Point(15, 15);

            Label lblContenido = new Label();
            lblContenido.Text = post.contenido;
            lblContenido.Font = new Font("Segoe UI", 10, FontStyle.Regular);
            lblContenido.MaximumSize = new Size(760, 0);
            lblContenido.AutoSize = true;
            lblContenido.Location = new Point(15, 50);

            Label lblLikes = new Label();
            lblLikes.Text = "Likes: " + likes.Count;
            lblLikes.AutoSize = true;
            lblLikes.Location = new Point(15, 120);

            Label lblEstadoLike = new Label();
            lblEstadoLike.Text = yaLeDiLike ? "Ya le has dado like" : "Aún no le has dado like";
            lblEstadoLike.AutoSize = true;
            lblEstadoLike.ForeColor = yaLeDiLike ? Color.DarkGreen : Color.DarkSlateGray;
            lblEstadoLike.Location = new Point(15, 145);

            Button btnLike = new Button();
            btnLike.Text = yaLeDiLike ? "Quitar like" : "Dar like";
            btnLike.BackColor = Color.FromArgb(97, 81, 155);
            btnLike.ForeColor = Color.White;
            btnLike.Size = new Size(100, 32);
            btnLike.Location = new Point(15, 175);

            btnLike.Click += async (s, e) =>
            {
                try
                {
                    ApiResponse resultado;

                    if (btnLike.Text == "Dar like")
                        resultado = await DarLikeAsync(postId);
                    else
                        resultado = await QuitarLikeAsync(postId);

                    if (!string.IsNullOrEmpty(resultado.error))
                    {
                        MessageBox.Show(resultado.error, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                        return;
                    }

                    await CargarHomeAsync();
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Error con los likes.\n\n" + ex.Message);
                }
            };

            Button btnBorrar = new Button();
            btnBorrar.Text = "Borrar";
            btnBorrar.BackColor = Color.Firebrick;
            btnBorrar.ForeColor = Color.White;
            btnBorrar.Size = new Size(90, 32);
            btnBorrar.Location = new Point(125, 175);
            btnBorrar.Visible = esMio;

            btnBorrar.Click += async (s, e) =>
            {
                DialogResult confirmar = MessageBox.Show(
                    "¿Quieres borrar este post?",
                    "Confirmar borrado",
                    MessageBoxButtons.YesNo,
                    MessageBoxIcon.Question);

                if (confirmar == DialogResult.Yes)
                {
                    ApiResponse resultado = await BorrarPostAsync(postId);

                    if (!string.IsNullOrEmpty(resultado.error))
                    {
                        MessageBox.Show(resultado.error, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                        return;
                    }

                    await CargarHomeAsync();
                }
            };

            Button btnResponder = new Button();
            btnResponder.Text = "Responder";
            btnResponder.BackColor = Color.FromArgb(97, 81, 155);
            btnResponder.ForeColor = Color.White;
            btnResponder.Size = new Size(100, 32);
            btnResponder.Location = new Point(esMio ? 225 : 125, 175);

            TextBox txtRespuesta = new TextBox();
            txtRespuesta.Multiline = true;
            txtRespuesta.Size = new Size(240, 45);
            txtRespuesta.Location = new Point(340, 170);
            txtRespuesta.Visible = false;

            Button btnEnviarRespuesta = new Button();
            btnEnviarRespuesta.Text = "Enviar";
            btnEnviarRespuesta.BackColor = Color.DarkGreen;
            btnEnviarRespuesta.ForeColor = Color.White;
            btnEnviarRespuesta.Size = new Size(70, 32);
            btnEnviarRespuesta.Location = new Point(590, 178);
            btnEnviarRespuesta.Visible = false;

            btnResponder.Click += (s, e) =>
            {
                txtRespuesta.Visible = !txtRespuesta.Visible;
                btnEnviarRespuesta.Visible = !btnEnviarRespuesta.Visible;
            };

            btnEnviarRespuesta.Click += async (s, e) =>
            {
                if (string.IsNullOrWhiteSpace(txtRespuesta.Text))
                {
                    MessageBox.Show("Escribe una respuesta.");
                    return;
                }

                ApiResponse resultado = await EnviarRespuestaAsync(postId, txtRespuesta.Text.Trim());

                if (!string.IsNullOrEmpty(resultado.error))
                {
                    MessageBox.Show(resultado.error, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }

                txtRespuesta.Clear();
                txtRespuesta.Visible = false;
                btnEnviarRespuesta.Visible = false;
                await CargarHomeAsync();
            };

            Button btnVerRespuestas = new Button();
            btnVerRespuestas.Text = "Ver respuestas";
            btnVerRespuestas.BackColor = Color.FromArgb(97, 81, 155);
            btnVerRespuestas.ForeColor = Color.White;
            btnVerRespuestas.Size = new Size(120, 32);
            btnVerRespuestas.Location = new Point(670, 175);

            Panel panelRespuestas = new Panel();
            panelRespuestas.Location = new Point(15, 215);
            panelRespuestas.Size = new Size(775, 0);
            panelRespuestas.AutoScroll = true;
            panelRespuestas.Visible = false;
            panelRespuestas.BorderStyle = BorderStyle.FixedSingle;

            btnVerRespuestas.Click += async (s, e) =>
            {
                if (!panelRespuestas.Visible)
                {
                    panelRespuestas.Controls.Clear();

                    List<Post> respuestas = await ObtenerRespuestasAsync(postId);
                    int y = 10;

                    if (respuestas.Count == 0)
                    {
                        Label lblSin = new Label();
                        lblSin.Text = "No hay respuestas todavía.";
                        lblSin.AutoSize = true;
                        lblSin.Location = new Point(10, y);
                        panelRespuestas.Controls.Add(lblSin);
                        y += 25;
                    }
                    else
                    {
                        foreach (Post respuesta in respuestas)
                        {
                            User autorRespuesta = await ObtenerUsuarioAsync(respuesta.id_autor);

                            Label lblRespAutor = new Label();
                            lblRespAutor.Text = (autorRespuesta != null ? autorRespuesta.nombre_visible : "Usuario") + ":";
                            lblRespAutor.Font = new Font("Segoe UI", 9, FontStyle.Bold);
                            lblRespAutor.AutoSize = true;
                            lblRespAutor.Location = new Point(10, y);

                            y += 20;

                            Label lblRespTexto = new Label();
                            lblRespTexto.Text = respuesta.contenido;
                            lblRespTexto.MaximumSize = new Size(720, 0);
                            lblRespTexto.AutoSize = true;
                            lblRespTexto.Location = new Point(25, y);

                            panelRespuestas.Controls.Add(lblRespAutor);
                            panelRespuestas.Controls.Add(lblRespTexto);

                            y += lblRespTexto.Height + 15;
                        }
                    }

                    panelRespuestas.Height = Math.Min(120, y + 10);
                    panelRespuestas.Visible = true;
                    btnVerRespuestas.Text = "Ocultar respuestas";
                    card.Height = 250 + panelRespuestas.Height;
                }
                else
                {
                    panelRespuestas.Visible = false;
                    btnVerRespuestas.Text = "Ver respuestas";
                    card.Height = 250;
                }
            };

            card.Controls.Add(lblAutor);
            card.Controls.Add(lblContenido);
            card.Controls.Add(lblLikes);
            card.Controls.Add(lblEstadoLike);
            card.Controls.Add(btnLike);
            card.Controls.Add(btnBorrar);
            card.Controls.Add(btnResponder);
            card.Controls.Add(txtRespuesta);
            card.Controls.Add(btnEnviarRespuesta);
            card.Controls.Add(btnVerRespuestas);
            card.Controls.Add(panelRespuestas);

            return card;
        }

        private async void btnInicio_Click(object sender, EventArgs e)
        {
            await CargarHomeAsync();
        }

        private void btnPerfil_Click(object sender, EventArgs e)
        {
            Perfil f = new Perfil();
            this.Hide();
            DialogResult respuesta = f.ShowDialog();

            if (respuesta == DialogResult.OK || respuesta == DialogResult.Abort)
            {
                this.Show();
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
                await CargarHomeAsync();
            }
        }

        private void btnUsers_Click(object sender, EventArgs e)
        {
            Usuarios f = new Usuarios();
            this.Hide();
            DialogResult respuesta = f.ShowDialog();

            if (respuesta == DialogResult.OK || respuesta == DialogResult.Abort)
            {
                this.Show();
            }
            else if (respuesta == DialogResult.Cancel)
            {
                this.Close();
            }
        }

        private void btnSeguidores_Click(object sender, EventArgs e)
        {
            Seguidores f = new Seguidores();
            this.Hide();
            DialogResult respuesta = f.ShowDialog();

            if (respuesta == DialogResult.OK || respuesta == DialogResult.Abort)
            {
                this.Show();
            }
            else if (respuesta == DialogResult.Cancel)
            {
                this.Close();
            }
        }

        private void btnSeguidos_Click(object sender, EventArgs e)
        {
            Seguidos f = new Seguidos();
            this.Hide();
            DialogResult respuesta = f.ShowDialog();

            if (respuesta == DialogResult.OK || respuesta == DialogResult.Abort)
            {
                this.Show();
            }
            else if (respuesta == DialogResult.Cancel)
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
                this.Show();
            }
            else if (respuesta == DialogResult.Cancel)
            {
                this.Close();
            }
        }


        private void Principal_FormClosed(object sender, FormClosedEventArgs e)
        {
            if (this.DialogResult != DialogResult.Cancel)
                this.DialogResult = DialogResult.Abort;
        }

        private void HacerBotonCircular(Button boton)
        {
            GraphicsPath path = new GraphicsPath();
            path.AddEllipse(0, 0, boton.Width, boton.Height);
            boton.Region = new Region(path);
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

        private void button4_MouseLeave(object sender, EventArgs e)
        {
            ((Button)sender).BackColor = Color.FromArgb(97, 81, 155);
        }
    }
}