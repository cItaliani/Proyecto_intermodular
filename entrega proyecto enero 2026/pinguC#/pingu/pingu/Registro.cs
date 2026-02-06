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

namespace pingu
{
    public partial class Registro : System.Windows.Forms.Form
    {
        public Registro()
        {
            InitializeComponent();
        }

        private void Form2_FormClosed(object sender, FormClosedEventArgs e)
        {
            this.DialogResult = DialogResult.Cancel;
        }

        private void linkLabel1_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            this.DialogResult = DialogResult.Cancel;
        }


        private void btnAcceso_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrEmpty(txtNombre.Text) || string.IsNullOrEmpty(primerApellido.Text) || string.IsNullOrEmpty(segundoApellido.Text) || string.IsNullOrEmpty(nick.Text) || string.IsNullOrEmpty(correo.Text) || string.IsNullOrEmpty(contrasena.Text) || string.IsNullOrEmpty(contrasenaOK.Text))
            {
                MessageBox.Show("Todos los campos deben estar llenos. Revísalo por favor", "Error de validación", MessageBoxButtons.OK, MessageBoxIcon.Warning);
            }
            else if (contrasena.Text != contrasenaOK.Text)
            {
                MessageBox.Show("Las contraseñas deben ser iguales. Revísalo por favor", "Error de validación", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
            else if (comprobarCorreoElectronico(correo.Text) == false)
            {
                MessageBox.Show("El correo electrónico no es válido. Revísalo por favor", "Error de validación", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
            else
            {
                // Enviar los datos
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
    }
}