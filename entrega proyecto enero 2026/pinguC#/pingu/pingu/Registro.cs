using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Net.Http;
using System.Text.Json;


namespace pingu
{
    public partial class Registro : System.Windows.Forms.Form
    {
        private Random random = new Random();

        public Registro()
        {
            InitializeComponent();
        }

        public class RegisterRequest
        {
            public string alias { get; set; }
            public string nombre_visible { get; set; }
            public string correo_electronico { get; set; }
            public string contrasena { get; set; }
            public string biografia { get; set; }
            public string fotografia { get; set; }
        }

        public class ApiResponse
        {
            public string message { get; set; }
            public string error { get; set; }
            public string id { get; set; }
        }
        private void Form2_FormClosed(object sender, FormClosedEventArgs e)
        {
            this.DialogResult = DialogResult.Cancel;
        }

        private void linkLabel1_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            this.DialogResult = DialogResult.Cancel;
        }

        private async void btnAcceso_Click(object sender, EventArgs e)
        {
            // Validar nombre
            if (string.IsNullOrEmpty(txtNombre.Text.Trim()))
            {
                string[] frasesNombre = {
            "⚠️ El nombre es obligatorio, crack 🚫",
            "Ehhh, ¿tu nombre? 🤨",
            "¿Nombre invisible? No funciona 👻",
            "Pon tu nombre aquí, porfa 😅",
            "Sin nombre no hay registro 🎯",
            "¿Te olvidaste de tu nombre? 😂",
            "El nombre no es opcional, campeón 🎪",
            "Necesito saber cómo te llamas 🧠"
        };
                string fraseAleatoria = frasesNombre[random.Next(frasesNombre.Length)];
                MessageBox.Show(fraseAleatoria, "Error de validación", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            // Validar primer apellido
            if (string.IsNullOrEmpty(primerApellido.Text.Trim()))
            {
                string[] frasesApellido1 = {
            "⚠️ Primer apellido obligatorio 🚫",
            "¿Y el primer apellido? 🤨",
            "Falta el primer apellido, tío 👻",
            "Pon tu primer apellido porfa 😅",
            "Sin apellido no hay registro 🎯",
            "¿Te olvidaste del apellido? 😬",
            "El primer apellido es necesario 🎪",
            "Necesito tu primer apellido 📝"
        };
                string fraseAleatoria = frasesApellido1[random.Next(frasesApellido1.Length)];
                MessageBox.Show(fraseAleatoria, "Error de validación", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            // Validar segundo apellido
            if (string.IsNullOrEmpty(segundoApellido.Text.Trim()))
            {
                string[] frasesApellido2 = {
            "⚠️ Segundo apellido obligatorio 🚫",
            "¿Y el segundo apellido? 🤨",
            "Falta el segundo apellido 👻",
            "Pon tu segundo apellido porfa 😅",
            "Completa con el segundo apellido 🎯",
            "¿Te olvidaste del segundo? 😬",
            "El segundo apellido también va 🎪",
            "Necesito el segundo apellido 📝"
        };
                string fraseAleatoria = frasesApellido2[random.Next(frasesApellido2.Length)];
                MessageBox.Show(fraseAleatoria, "Error de validación", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            // Validar nombre de usuario (nick)
            if (string.IsNullOrEmpty(nick.Text.Trim()))
            {
                string[] frasesUsuario = {
            "⚠️ Necesitas un nombre de usuario 🚫",
            "¿Tu nombre de usuario? 🤨",
            "Falta el nombre de usuario 👻",
            "Elige un nombre de usuario 😅",
            "Sin usuario no puedes entrar 🎯",
            "¿Qué usuario quieres? 😬",
            "El nombre de usuario es clave 🔑",
            "Inventa un nombre de usuario 🎨"
        };
                string fraseAleatoria = frasesUsuario[random.Next(frasesUsuario.Length)];
                MessageBox.Show(fraseAleatoria, "Error de validación", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            // Validar correo vacío
            if (string.IsNullOrEmpty(correo.Text.Trim()))
            {
                string[] frasesEmailVacio = {
            "Ehhh, ¿y el email? 🤔",
            "El correo no se pone solo 🙃",
            "¿Email? ¿Hola? 📧",
            "Sin email no hay registro 🚷",
            "Falta algo importante... el email 📬",
            "¿Te olvidaste del correo? 😬",
            "Email obligatorio, amigo 🎯",
            "Pon el email, no seas vago 😅"
        };
                string fraseAleatoria = frasesEmailVacio[random.Next(frasesEmailVacio.Length)];
                MessageBox.Show(fraseAleatoria, "Error de validación", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            // Validar formato de email
            if (comprobarCorreoElectronico(correo.Text.Trim()) == false)
            {
                string[] frasesEmailInvalido = {
            "⚠️ Ese email no pinta bien 🤔",
            "Email inválido, revísalo porfa 📧",
            "¿Seguro que ese es tu email? 🧐",
            "Formato de email incorrecto 🚫",
            "Eso no es un email válido, crack 😅",
            "Email mal escrito, inténtalo 📝",
            "Revisa el formato del email 🔍",
            "Ese email tiene pinta rara 🤨"
        };
                string fraseAleatoria = frasesEmailInvalido[random.Next(frasesEmailInvalido.Length)];
                MessageBox.Show(fraseAleatoria, "Error de validación", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            // Validar contraseña vacía
            if (string.IsNullOrEmpty(contrasena.Text.Trim()))
            {
                string[] frasesPassVacia = {
            "Ehhh, ¿la contraseña? 🤔",
            "La contraseña no se pone sola 🙃",
            "¿Contraseña? ¿Hola? 🔐",
            "Sin contraseña no hay cuenta 🚷",
            "Falta la contraseña 🔑",
            "¿Te olvidaste de la contraseña? 😬",
            "Contraseña obligatoria 🎯",
            "Pon una contraseña segura 🛡️"
        };
                string fraseAleatoria = frasesPassVacia[random.Next(frasesPassVacia.Length)];
                MessageBox.Show(fraseAleatoria, "Error de validación", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            // Validar longitud de contraseña
            if (contrasena.Text.Trim().Length < 6)
            {
                string[] frasesPassCorta = {
            "⚠️ Mínimo 6 caracteres, no seas rata 😂",
            "Muy corta, mínimo 6 caracteres 📏",
            "¿6 caracteres es mucho pedir? 🤨",
            "Esa contraseña es muy corta 🙏",
            "Mínimo 6, que no es tan difícil 💪",
            "6 caracteres o más, venga 🎯",
            "Contraseña corta = insegura. Mín. 6 🔒",
            "Dale más caña, mínimo 6 🚀"
        };
                string fraseAleatoria = frasesPassCorta[random.Next(frasesPassCorta.Length)];
                MessageBox.Show(fraseAleatoria, "Error de validación", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            // Validar repetición de contraseña vacía
            if (string.IsNullOrEmpty(contrasenaOK.Text.Trim()))
            {
                string[] frasesPass2Vacia = {
            "Repite la contraseña aquí 🔁",
            "¿Y la confirmación? 🤔",
            "Falta repetir la contraseña 🔐",
            "Confirma tu contraseña 🎯",
            "Pon la contraseña otra vez 🔑",
            "Necesito que la repitas 😅",
            "Confirma la contraseña porfa 🙏",
            "Escribe la contraseña de nuevo 📝"
        };
                string fraseAleatoria = frasesPass2Vacia[random.Next(frasesPass2Vacia.Length)];
                MessageBox.Show(fraseAleatoria, "Error de validación", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            // Validar que las contraseñas coincidan
            if (contrasena.Text != contrasenaOK.Text)
            {
                string[] frasesPassNoCoinciden = {
            "⚠️ Las contraseñas no coinciden 🚫",
            "Ehhh, no son iguales 🤨",
            "Las contraseñas no match 👻",
            "No coinciden, revísalas 😅",
            "Contraseñas diferentes 🎯",
            "Esas no son iguales, tío 😬",
            "No coinciden, inténtalo otra vez 🔄",
            "Las contraseñas deben ser iguales 🎪"
        };
                string fraseAleatoria = frasesPassNoCoinciden[random.Next(frasesPassNoCoinciden.Length)];
                MessageBox.Show(fraseAleatoria, "Error de validación", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            try
            {
                btnAcceso.Enabled = false;

                string nombreVisible = txtNombre.Text.Trim() + " " +
                                       primerApellido.Text.Trim() + " " +
                                       segundoApellido.Text.Trim();

                RegisterRequest nuevoUsuario = new RegisterRequest
                {
                    alias = nick.Text.Trim(),
                    nombre_visible = nombreVisible,
                    correo_electronico = correo.Text.Trim(),
                    contrasena = contrasena.Text.Trim(),
                    biografia = "",
                    fotografia = ""
                };

                ApiResponse resultado = await RegistrarUsuarioAsync(nuevoUsuario);

                if (!string.IsNullOrEmpty(resultado.id))
                {
                    MessageBox.Show(
                        "✅ ¡Registro exitoso! Bienvenido a PingU 🐧",
                        "Registro completado",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Information
                    );

                    this.DialogResult = DialogResult.OK;
                    this.Close();
                }
                else
                {
                    MessageBox.Show(
                        string.IsNullOrEmpty(resultado.error) ? "No se pudo completar el registro." : resultado.error,
                        "Error de registro",
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
                btnAcceso.Enabled = true;
            }
        }

        public bool comprobarCorreoElectronico(String correo)
        {
            if (string.IsNullOrWhiteSpace(correo))
                return false;
            try
            {
                return Regex.IsMatch(correo, @"^[^@\s]+@[^@\s]+\.[^@\s]+$", RegexOptions.IgnoreCase);
            }
            catch (RegexMatchTimeoutException)
            {
                return false;
            }
        }

        private void btnAcceso_MouseEnter(object sender, EventArgs e)
        {
            if (string.IsNullOrEmpty(txtNombre.Text) || string.IsNullOrEmpty(primerApellido.Text) || string.IsNullOrEmpty(segundoApellido.Text) || string.IsNullOrEmpty(nick.Text) || string.IsNullOrEmpty(correo.Text) || string.IsNullOrEmpty(contrasena.Text) || string.IsNullOrEmpty(contrasenaOK.Text))
            {
                btnAcceso.BackColor = Color.Red;
            }
            else if (contrasena.Text != contrasenaOK.Text || comprobarCorreoElectronico(correo.Text) == false)
            {
                btnAcceso.BackColor = Color.Yellow;
            }
            else
            {
                btnAcceso.BackColor = Color.Green;
            }
        }

        private void btnAcceso_MouseLeave(object sender, EventArgs e)
        {
            btnAcceso.BackColor = Color.FromArgb(97, 81, 155);
        }

        private async Task<ApiResponse> RegistrarUsuarioAsync(RegisterRequest nuevoUsuario)
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/api/rest/");

                string json = JsonSerializer.Serialize(nuevoUsuario);
                StringContent contenido = new StringContent(json, Encoding.UTF8, "application/json");

                HttpResponseMessage response = await client.PostAsync("pingu/users", contenido);
                string respuestaJson = await response.Content.ReadAsStringAsync();

                ApiResponse resultado = JsonSerializer.Deserialize<ApiResponse>(
                    respuestaJson,
                    new JsonSerializerOptions { PropertyNameCaseInsensitive = true });

                return resultado;
            }
        }

    }
}