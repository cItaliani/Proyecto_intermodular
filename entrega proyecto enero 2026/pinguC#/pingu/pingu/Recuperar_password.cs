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
            if (string.IsNullOrEmpty(txtUsuario.Text))
            {
               MessageBox.Show("es requerido intorducir un usuario", "Error de validación", MessageBoxButtons.OK, MessageBoxIcon.Information);

            }
            else if (string.IsNullOrEmpty(txtCorreo.Text))
            {
                MessageBox.Show("es requerido intorducir una correo electronico", "Error de validación", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
            else
            {
                // comprobar datos y enviar contraseña 
            }
        }
    }
}
