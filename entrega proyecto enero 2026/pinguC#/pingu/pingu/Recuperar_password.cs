using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Net.Http;
using System.Text;
using System.Text.Json;

namespace pingu
{
    public partial class Recuperar_password : Form
    {
        private Random random = new Random();

        public Recuperar_password()
        {
            InitializeComponent();
        }

        public class RememberRequest
        {
            public string correo_electronico { get; set; }
        }

        public class ApiResponse
        {
            public string message { get; set; }
            public string error { get; set; }
        }

        private void Recuperar_password_FormClosed(object sender, FormClosedEventArgs e)
        {
            volverALogIn();
        }

        private void linkLabel1_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {

        }

        private void volverALogIn()
        {

        }

        private void Recuperar_password_Load(object sender, EventArgs e)
        {
        }

        private void linkLabel1_LinkClicked_1(object sender, LinkLabelLinkClickedEventArgs e)
        {
            this.DialogResult = DialogResult.Cancel;
        }

        private async void button1_Click(object sender, EventArgs e)
        {
            // Validar usuario
            if (string.IsNullOrEmpty(txtUsuario.Text.Trim()))
            {
                string[] frasesUsuario = {
            "⚠️ Sin usuario no hay recuperación 🚫",
            "Ehhh, ¿el usuario? 🤨 Lo necesito",
            "¿Usuario invisible? No funciona así 👻",
            "Pon tu usuario aquí, porfa 😅",
            "⚠️ Campo obligatorio, campeón",
            "Tío, el usuario... ¿dónde está? 🤷‍♂️",
            "No seas tímido, pon tu usuario 😏",
            "Necesito tu usuario para ayudarte 🎯",
            "El usuario no es opcional, crack 🎪",
            "¿Olvidaste algo? Sí, el usuario 🧠"
        };

                string fraseAleatoria = frasesUsuario[random.Next(frasesUsuario.Length)];
                MessageBox.Show(fraseAleatoria, "Error de validación", MessageBoxButtons.OK, MessageBoxIcon.Information);
                return;
            }

            // Validar correo electrónico
            if (string.IsNullOrEmpty(txtCorreo.Text.Trim()))
            {
                string[] frasesEmail = {
            "Ehhh, ¿y el email? 🤔",
            "El correo no se pone solo 🙃",
            "¿Email? ¿Hola? 📧",
            "Sin email no puedo enviarte nada 🚷",
            "Falta algo importante... el email 📬",
            "¿Te olvidaste del correo? 😬",
            "Email obligatorio, amigo 🎯",
            "Pon el email, no seas vago 😅",
            "¿Dónde te envío la contraseña? 🤨",
            "Necesito tu email, campeón 💌"
        };

                string fraseAleatoria = frasesEmail[random.Next(frasesEmail.Length)];
                MessageBox.Show(fraseAleatoria, "Error de validación", MessageBoxButtons.OK, MessageBoxIcon.Information);
                return;
            }

            try
            {
                button1.Enabled = false;

                string correo = txtCorreo.Text.Trim();

                ApiResponse resultado = await RecordarPasswordAsync(correo);

                if (!string.IsNullOrEmpty(resultado.message))
                {
                    MessageBox.Show(
                        resultado.message,
                        "Recuperación de contraseña",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Information
                    );

                    this.DialogResult = DialogResult.OK;
                    this.Close();
                }
                else
                {
                    MessageBox.Show(
                        string.IsNullOrEmpty(resultado.error) ? "No se pudo recuperar la contraseña." : resultado.error,
                        "Error",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Error
                    );
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(
                    "No se pudo conectar con la API.\n\n" + ex.Message,
                    "Error de conexión",
                    MessageBoxButtons.OK,
                    MessageBoxIcon.Error
                );
            }
            finally
            {
                button1.Enabled = true;
            }
        }

        private async Task<ApiResponse> RecordarPasswordAsync(string correo)
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/api/rest/");

                RememberRequest datos = new RememberRequest
                {
                    correo_electronico = correo
                };

                string json = JsonSerializer.Serialize(datos);
                StringContent contenido = new StringContent(json, Encoding.UTF8, "application/json");

                HttpResponseMessage response = await client.PostAsync("pingu/auth/pass-remember", contenido);
                string respuestaJson = await response.Content.ReadAsStringAsync();

                ApiResponse resultado = JsonSerializer.Deserialize<ApiResponse>(
                    respuestaJson,
                    new JsonSerializerOptions { PropertyNameCaseInsensitive = true });

                return resultado;
            }
        }



    }
}