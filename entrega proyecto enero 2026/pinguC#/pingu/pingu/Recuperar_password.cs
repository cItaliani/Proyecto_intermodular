using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace pingu
{
    public partial class Recuperar_password : Form
    {
        private Random random = new Random();

        public Recuperar_password()
        {
            InitializeComponent();
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

        private void button1_Click(object sender, EventArgs e)
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

            // Si todo está bien
            this.DialogResult = DialogResult.OK;
            // comprobar datos y enviar contraseña 
        }
    }
}