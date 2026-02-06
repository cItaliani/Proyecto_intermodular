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
    public partial class Perfil : Form
    {

        private string biografia;
        private string fechaEntrada;
        private int seguidoresEntrada;
        private int seguidosRespuesta;

        public Perfil()
        {
            InitializeComponent();
        }

        private void Perfil_Load(object sender, EventArgs e)
        {
            this.Text += txtAliasRespuesta.Text;
            fechaEntrada = txtMiembro_respuesta.Text;
            seguidoresEntrada = int.Parse(txtSeguidoresRespuesta.Text);
            seguidosRespuesta = int.Parse(txtSeguidosRespuesta.Text);
            biografia = textBox2.Text;

        }

        private void btnGuardar_Click(object sender, EventArgs e)
        {
            textBox2.Text = textBox2.Text;
        }

        private void btnCancel_Click(object sender, EventArgs e)
        {
            textBox2.Text = biografia;
        }

        private void textBox2_TextChanged(object sender, EventArgs e)
        {

        }
    }
}
