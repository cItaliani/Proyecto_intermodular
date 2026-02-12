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
            linkLabel1.TabStop = false;
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
            else if (txtpass.Text.Length < 6)
            {
                MessageBox.Show("contraseña demasiado corta","error", MessageBoxButtons.OK, MessageBoxIcon.Information);
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
            if (string.IsNullOrEmpty(txtUsuario.Text.Trim()) || string.IsNullOrEmpty(txtpass.Text.Trim()) || txtpass.Text.Length<6)
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
    }
}
