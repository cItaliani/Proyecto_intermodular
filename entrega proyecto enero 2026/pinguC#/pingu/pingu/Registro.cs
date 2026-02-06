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
using static System.Windows.Forms.VisualStyles.VisualStyleElement.ListView;

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

        private void Registro_Load(object sender, EventArgs e)
        {

        }

        private void btnAcceso_Click(object sender, EventArgs e)
        {
            if(string.IsNullOrEmpty(txtNombre.Text)||string.IsNullOrEmpty(primerApellido.Text)|| string.IsNullOrEmpty(segundoApellido.Text)||string.IsNullOrEmpty(nick.Text) || string.IsNullOrEmpty(correo.Text) || string.IsNullOrEmpty(contrasena.Text) || string.IsNullOrEmpty(contrasenaOK.Text))
            {
                MessageBox.Show("todos los datos deben estar cubiertos. revisalo por favor","error de validacion",MessageBoxButtons.OK,MessageBoxIcon.Warning);

            }else if (contrasena.Text!=contrasenaOK.Text)
            {
                MessageBox.Show("Las contreseñas deben de ser iguales. revisalo por favor", "error de validacion", MessageBoxButtons.OK, MessageBoxIcon.Error);

            }
            else if(comprobarCorreoElectronico(correo.Text)==false)
            {
                MessageBox.Show("el correo electronico no es valido. revisalo por favor", "error de validacion", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
            else
            {
                //enviar los datos
            }
        }

        private bool comprobarCorreoElectronico(String correo)
        {
            if (string.IsNullOrWhiteSpace(correo))
                return false;

            try
            {
                
                return Regex.IsMatch(correo,@"^[^@\s]+@[^@\s]+\.[^@\s]+$",RegexOptions.IgnoreCase);
            }
            catch (RegexMatchTimeoutException)
            {
                return false;
            }
        }

    }
}
