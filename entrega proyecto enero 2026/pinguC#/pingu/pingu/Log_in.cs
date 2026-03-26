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
    public partial class Log_in : System.Windows.Forms.Form
    {
        private bool isUsuario = false;
        private bool isPass = false;
        private bool isCredenciales = false;
        private string usuario = "";
        private string pass = "";
        private Random random = new Random();
        public static string idUsuarioLogado = "";

        public Log_in()
        {
            InitializeComponent();
            linkLabel1.TabStop = false;
        }

        public class LoginRequest
        {
            public string alias { get; set; }
            public string contrasena { get; set; }
        }

        public class LoginResponse
        {
            public string message { get; set; }
            public string id { get; set; }
            public string error { get; set; }
        }

        private async void btnLoging_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrEmpty(txtUsuario.Text))
            {
                string[] frasesUsuario = {
            "⚠️ Sin usuario no entras, colega 🚫",
            "Ehhh, ¿el usuario? 🤨 No te lo saltes",
            "¿Usuario invisible? No funciona así 👻",
            "Pon tu usuario aquí, porfa 😅",
            "⚠️ Campo obligatorio, campeón",
            "Tío, el usuario... ¿dónde está? 🤷‍♂️",
            "No seas tímido, pon tu usuario 😏",
            "Adivina: necesitas un usuario 🎯",
            "El usuario no es opcional, crack 🎪",
            "¿Olvidaste algo? Sí, el usuario 🧠"
        };

                string fraseAleatoria = frasesUsuario[random.Next(frasesUsuario.Length)];
                MessageBox.Show(fraseAleatoria, "Error de validación", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
            else if (string.IsNullOrEmpty(txtpass.Text))
            {
                string[] frasesPassword = {
            "Ehhh, ¿y la contraseña? 🤔",
            "La contraseña no se pone sola 🙃",
            "¿Contraseña? ¿Hola? 👋",
            "Sin contraseña no hay login, sorry 🚷",
            "Falta algo importante... la contraseña 🔑",
            "¿Te olvidaste de la contraseña? 😬",
            "Contraseña obligatoria, amigo 🎯",
            "Pon la contraseña, no seas vago 😅"
        };

                string fraseAleatoria = frasesPassword[random.Next(frasesPassword.Length)];
                MessageBox.Show(fraseAleatoria, "Error de validación", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
            else if (txtpass.Text.Length < 6)
            {
                string[] frasesPasswordCorta = {
            "⚠️ ¿En serio? Mínimo 6, no seas rata 😂",
            "Muy corta, mínimo 6 caracteres 📏",
            "¿6 caracteres es mucho pedir? 🤨",
            "Esa contraseña es más corta que... 6+ porfa 🙏",
            "Mínimo 6, que no es tan difícil 💪",
            "6 caracteres o más, venga 🎯",
            "Corta contraseña = insegura. Mín. 6 🔒",
            "Dale más caña, mínimo 6 caracteres 🚀"
        };

                string fraseAleatoria = frasesPasswordCorta[random.Next(frasesPasswordCorta.Length)];
                MessageBox.Show(fraseAleatoria, "Error", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
            else
            {
                try
                {
                    btnLoging.Enabled = false;
                    usuario = txtUsuario.Text.Trim();
                    pass = txtpass.Text.Trim();

                    LoginResponse resultado = await HacerLoginAsync(usuario, pass);

                    if (!string.IsNullOrEmpty(resultado.id))
                    {
                        idUsuarioLogado = resultado.id;

                        Principal formulario_principal = new Principal();
                        this.Hide();
                        DialogResult respuesta = formulario_principal.ShowDialog();

                        if (respuesta == DialogResult.Abort)
                        {
                            this.Show();
                            txtUsuario.Text = "";
                            txtpass.Text = "";

                            if (isUsuario)
                            {
                                txtUsuario.Text = usuario;
                            }

                            if (isCredenciales)
                            {
                                txtUsuario.Text = usuario;
                                txtpass.Text = pass;
                            }
                        }
                        else if (respuesta == DialogResult.Cancel)
                        {
                            this.Close();
                        }
                    }
                    else
                    {
                        MessageBox.Show(
                            string.IsNullOrEmpty(resultado.error) ? "Credenciales incorrectas" : resultado.error,
                            "Error de login",
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
                    btnLoging.Enabled = true;
                }
            }
        }

        private void lblRecuperarPass_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            Recuperar_password formulario_password = new Recuperar_password();
            this.Hide();
            DialogResult respuesta = formulario_password.ShowDialog();
            if (respuesta == DialogResult.Cancel)
            {
                this.Show();
            }
            else if (respuesta == DialogResult.OK)
            {
                if (string.IsNullOrEmpty(formulario_password.txtUsuario.Text.Trim()) || string.IsNullOrEmpty(formulario_password.txtCorreo.Text.Trim()))
                {
                    this.Show();
                }
                else
                {
                    Registro r = new Registro();
                    bool comprobacion = r.comprobarCorreoElectronico(formulario_password.txtCorreo.Text.Trim());
                    if (comprobacion)
                    {
                        // enviar la contraseña al correo electronico 
                        this.Show();
                    }
                    else
                    {
                        MessageBox.Show("el correo electronico no es valido. revisalo por favor", "error de validacion", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    }
                }
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            Registro formulario_registro = new Registro();
            this.Visible = false;
            DialogResult respuesta = formulario_registro.ShowDialog();
            this.Visible = true;

            if (respuesta == DialogResult.OK)
            {
                MessageBox.Show("Ya puedes iniciar sesión con tu nueva cuenta 🐧", "Registro correcto", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
        }

        private void label2_Click(object sender, EventArgs e)
        {
            Riddler f10 = new Riddler();
            f10.Show();
        }

        private void checkBox1_CheckedChanged(object sender, EventArgs e)
        {
            if (chkUsuario.Checked)
            {
                isUsuario = true;
                chkCredenciales.Checked = false;
            }
            else if (!chkUsuario.Checked)
            {
                isUsuario = false;
            }
        }

        private void chkCredenciales_CheckedChanged(object sender, EventArgs e)
        {
            if (chkCredenciales.Checked)
            {
                isCredenciales = true;
                chkUsuario.Checked = false;
            }
            else
            {
                isCredenciales = false;
            }
        }

        private void txtUsuario_TextChanged(object sender, EventArgs e)
        {

        }

        private void chkpass_CheckedChanged(object sender, EventArgs e)
        {
            if (chkpass.Checked)
            {
                txtpass.UseSystemPasswordChar = false;
            }
            else
            {
                txtpass.UseSystemPasswordChar = true;
            }
        }

        private void txtpass_TextChanged(object sender, EventArgs e)
        {

        }

        private void chkUsuario_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == (char)Keys.Enter)
            {
                if (chkUsuario.Checked)
                {
                    chkUsuario.Checked = false;
                }
                else
                {
                    chkUsuario.Checked = true;
                }
            }
        }

        private void chkpass_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == (char)Keys.Enter)
            {
                if (chkpass.Checked)
                {
                    chkpass.Checked = false;
                }
                else
                {
                    chkpass.Checked = true;
                }
            }
        }

        private void chkCredenciales_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == (char)Keys.Enter)
            {
                if (chkCredenciales.Checked)
                {
                    chkCredenciales.Checked = false;
                }
                else
                {
                    chkCredenciales.Checked = true;
                    chkUsuario.Checked = false;
                }
            }
        }

        private void pictureBox1_Click(object sender, EventArgs e)
        {

        }

        private void Log_in_Load(object sender, EventArgs e)
        {

        }

        private void btnLoging_MouseEnter(object sender, EventArgs e)
        {
            if (string.IsNullOrEmpty(txtUsuario.Text.Trim()) || string.IsNullOrEmpty(txtpass.Text.Trim()) || txtpass.Text.Length < 6)
            {
                btnLoging.BackColor = Color.Red;
            }
            else
            {
                btnLoging.BackColor = Color.Green;
            }
        }

        private void btnLoging_MouseLeave(object sender, EventArgs e)
        {
            btnLoging.BackColor = Color.FromArgb(97, 81, 155);
        }


        private async Task<LoginResponse> HacerLoginAsync(string alias, string contrasena)
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/api/rest/");

                LoginRequest datos = new LoginRequest
                {
                    alias = alias,
                    contrasena = contrasena
                };

                string json = JsonSerializer.Serialize(datos);
                StringContent contenido = new StringContent(json, Encoding.UTF8, "application/json");

                HttpResponseMessage response = await client.PostAsync("pingu/auth/login", contenido);

                string respuestaJson = await response.Content.ReadAsStringAsync();

                LoginResponse resultado = JsonSerializer.Deserialize<LoginResponse>(
                    respuestaJson,
                    new JsonSerializerOptions { PropertyNameCaseInsensitive = true }
                );

                return resultado;
            }
        }



    }
}