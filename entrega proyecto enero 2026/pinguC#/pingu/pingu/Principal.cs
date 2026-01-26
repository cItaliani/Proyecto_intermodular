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
    public partial class Principal : Form
    {
        public Principal()
        {
            InitializeComponent();
            FormClosed += button5_Click;
            button5.Enabled = false;
            button5.Visible = false;
            
            
        }

        private void Form3_Load(object sender, EventArgs e)
        {

        }

        private void label1_Click(object sender, EventArgs e)
        {

        }

        private void button5_Click(object sender, EventArgs e)
        {

        }

        private void button2_Click(object sender, EventArgs e)
        {

        }

        private void button3_Click(object sender, EventArgs e)
        {

        }

        private void panel1_Paint(object sender, PaintEventArgs e)
        {
            
        }

       

        private void Principal_FormClosed(object sender, FormClosedEventArgs e)
        {
            DialogResult = DialogResult.Abort;
        }

        private void button5_Click_1(object sender, EventArgs e)
        {

        }

        private void button1_Click(object sender, EventArgs e)
        {
            Perfil f1 = new Perfil();
            this.Hide();
           DialogResult respuesta= f1.ShowDialog();
            if (respuesta == DialogResult.Cancel|| respuesta==DialogResult.OK)
            {
                this.Show();
            }
        
        }

        private void btnSalir_MouseEnter(object sender, EventArgs e)
        {
            btnSalir.BackColor = Color.Red;
        }

        private void btnSalir_MouseLeave(object sender, EventArgs e)
        {
           btnSalir.BackColor= Color.FromArgb(97,81,155);
        }
    }
}
