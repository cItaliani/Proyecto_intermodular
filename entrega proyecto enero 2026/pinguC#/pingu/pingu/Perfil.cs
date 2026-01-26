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
        public Perfil()
        {
            InitializeComponent();
        }

        private void Perfil_Load(object sender, EventArgs e)
        {
            this.Text += txtAliasRespuesta.Text;
            String fechaEntrada = txtMiembro_respuesta.Text;
            int seguidoresEntrada =int.Parse(txtSeguidoresRespuesta.Text);
            int seguidosRespuesta = int.Parse(txtSeguidosRespuesta.Text);


        }

        private void textBox2_TextChanged(object sender, EventArgs e)
        {

        }
    }
}
