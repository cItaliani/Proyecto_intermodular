using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace pingu
{
    public partial class NuevoPost : Form
    {
        public NuevoPost()
        {
            InitializeComponent();
        }

        public class ApiResponse
        {
            public string message { get; set; }
            public string mensaje { get; set; }
            public string error { get; set; }
            public string id { get; set; }
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

        private async Task<ApiResponse> PublicarPostAsync(string contenidoPost)
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/api/rest/");

                var body = new
                {
                    contenido = contenidoPost,
                    urlMultimedia = "",
                    id_autor = Log_in.idUsuarioLogado,
                    idPostPadre = (string)null
                };

                string json = JsonSerializer.Serialize(body);
                StringContent contenidoJson = new StringContent(json, Encoding.UTF8, "application/json");

                HttpResponseMessage response = await client.PostAsync("pingu/posts", contenidoJson);
                string respuestaJson = await response.Content.ReadAsStringAsync();

                return ParsearRespuestaApi(response, respuestaJson);
            }
        }

        private void btnCancelar_Click(object sender, EventArgs e)
        {
            this.DialogResult = DialogResult.Cancel;
            this.Close();
        }

        private async void btnPublicar_Click(object sender, EventArgs e)
        {
            string contenido = txtContenidoPost.Text.Trim();

            if (string.IsNullOrEmpty(contenido))
            {
                MessageBox.Show("Escribe algo antes de publicar.", "Validación", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            try
            {
                btnPublicar.Enabled = false;

                ApiResponse resultado = await PublicarPostAsync(contenido);

                if (!string.IsNullOrEmpty(resultado.error))
                {
                    MessageBox.Show(resultado.error, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }

                MessageBox.Show("Post publicado.", "Correcto", MessageBoxButtons.OK, MessageBoxIcon.Information);
                this.DialogResult = DialogResult.OK;
                this.Close();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error publicando el post.\n\n" + ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
            finally
            {
                btnPublicar.Enabled = true;
            }
        }

        private void NuevoPost_FormClosed(object sender, FormClosedEventArgs e)
        {
            if (this.DialogResult != DialogResult.OK && this.DialogResult != DialogResult.Cancel)
            {
                this.DialogResult = DialogResult.Cancel;
            }
        }

        private void btnPublicar_MouseEnter(object sender, EventArgs e)
        {
            btnPublicar.BackColor = Color.DarkGreen;
        }

        private void btnPublicar_MouseLeave(object sender, EventArgs e)
        {
            btnPublicar.BackColor = Color.FromArgb(97, 81, 155);
        }

        private void btnCancelar_MouseEnter(object sender, EventArgs e)
        {
            btnCancelar.BackColor = Color.Firebrick;
        }

        private void btnCancelar_MouseLeave(object sender, EventArgs e)
        {
            btnCancelar.BackColor = Color.FromArgb(97, 81, 155);
        }


    }
}
