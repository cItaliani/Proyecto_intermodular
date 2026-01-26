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
    public partial class Log_in : System.Windows.Forms.Form
    {
        private bool isUsuario = false;
        private bool isPass = false;
        private bool isCredenciales = false;
        private string usuario = "";
        private string pass = "";
        public Log_in()
        {
            InitializeComponent();
        }

        private void btnLoging_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrEmpty(txtUsuario.Text))
            {
                MessageBox.Show("es requerido intorducir un usuario", "Error de validación", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
            else if (string.IsNullOrEmpty(txtpass.Text))
            {
                MessageBox.Show("es requerido intorducir una contraseña", "Error de validación", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
            else
            {
                usuario = txtUsuario.Text;
                pass = txtpass.Text;

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
                if (respuesta == DialogResult.Cancel)
                {
                    this.Close();
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
                // enviar la contraseña al correo electronico 
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            Registro formulario_registro = new Registro();
            this.Visible = false;
            DialogResult respuesta = formulario_registro.ShowDialog();
            if (respuesta == DialogResult.Cancel)
            {
                this.Visible = true;
            }


        }

        private void label2_Click(object sender, EventArgs e)
        {
            Riddler f10 = new Riddler();
            f10.Show();

        }

        private void Log_in_Load(object sender, EventArgs e)
        {

        }



        private void checkBox1_CheckedChanged(object sender, EventArgs e)
        {
            if (chkUsuario.Checked)
            {
                isUsuario = true;
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
    }
}
