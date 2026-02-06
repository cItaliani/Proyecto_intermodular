namespace pingu
{
    partial class Log_in
    {
        /// <summary>
        /// Variable del diseñador necesaria.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Limpiar los recursos que se estén usando.
        /// </summary>
        /// <param name="disposing">true si los recursos administrados se deben desechar; false en caso contrario.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))

            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Código generado por el Diseñador de Windows Forms

        /// <summary>
        /// Método necesario para admitir el Diseñador. No se puede modificar
        /// el contenido de este método con el editor de código.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(Log_in));
            this.txtUsuario = new System.Windows.Forms.TextBox();
            this.txtpass = new System.Windows.Forms.TextBox();
            this.btnRegistro = new System.Windows.Forms.Button();
            this.linkLabel1 = new System.Windows.Forms.LinkLabel();
            this.btnLoging = new System.Windows.Forms.Button();
            this.pictureBox1 = new System.Windows.Forms.PictureBox();
            this.pictureBox2 = new System.Windows.Forms.PictureBox();
            this.label2 = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.chkUsuario = new System.Windows.Forms.CheckBox();
            this.chkpass = new System.Windows.Forms.CheckBox();
            this.chkCredenciales = new System.Windows.Forms.CheckBox();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox2)).BeginInit();
            this.SuspendLayout();
            // 
            // txtUsuario
            // 
            this.txtUsuario.Location = new System.Drawing.Point(262, 63);
            this.txtUsuario.Margin = new System.Windows.Forms.Padding(2);
            this.txtUsuario.Name = "txtUsuario";
            this.txtUsuario.Size = new System.Drawing.Size(278, 20);
            this.txtUsuario.TabIndex = 1;
            // 
            // txtpass
            // 
            this.txtpass.Location = new System.Drawing.Point(262, 121);
            this.txtpass.Margin = new System.Windows.Forms.Padding(2);
            this.txtpass.Name = "txtpass";
            this.txtpass.Size = new System.Drawing.Size(278, 20);
            this.txtpass.TabIndex = 3;
            this.txtpass.UseSystemPasswordChar = true;
            this.txtpass.TextChanged += new System.EventHandler(this.txtpass_TextChanged);
            // 
            // btnRegistro
            // 
            this.btnRegistro.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(97)))), ((int)(((byte)(81)))), ((int)(((byte)(155)))));
            this.btnRegistro.ForeColor = System.Drawing.Color.White;
            this.btnRegistro.Location = new System.Drawing.Point(406, 198);
            this.btnRegistro.Margin = new System.Windows.Forms.Padding(2);
            this.btnRegistro.Name = "btnRegistro";
            this.btnRegistro.Size = new System.Drawing.Size(122, 44);
            this.btnRegistro.TabIndex = 7;
            this.btnRegistro.Text = "¿primera vez por aquí? Súmate 😎";
            this.btnRegistro.UseVisualStyleBackColor = false;
            this.btnRegistro.Click += new System.EventHandler(this.button1_Click);
            // 
            // linkLabel1
            // 
            this.linkLabel1.AutoSize = true;
            this.linkLabel1.Location = new System.Drawing.Point(259, 149);
            this.linkLabel1.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.linkLabel1.Name = "linkLabel1";
            this.linkLabel1.Size = new System.Drawing.Size(149, 13);
            this.linkLabel1.TabIndex = 0;
            this.linkLabel1.Text = "¿Has olvidado tu contraseña?";
            this.linkLabel1.LinkClicked += new System.Windows.Forms.LinkLabelLinkClickedEventHandler(this.lblRecuperarPass_LinkClicked);
            // 
            // btnLoging
            // 
            this.btnLoging.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(97)))), ((int)(((byte)(81)))), ((int)(((byte)(155)))));
            this.btnLoging.ForeColor = System.Drawing.Color.White;
            this.btnLoging.Location = new System.Drawing.Point(274, 198);
            this.btnLoging.Margin = new System.Windows.Forms.Padding(2);
            this.btnLoging.Name = "btnLoging";
            this.btnLoging.Size = new System.Drawing.Size(122, 44);
            this.btnLoging.TabIndex = 6;
            this.btnLoging.Text = "¿Ya eres de la casa? Entra 🔑";
            this.btnLoging.UseVisualStyleBackColor = false;
            this.btnLoging.Click += new System.EventHandler(this.btnLoging_Click);
            // 
            // pictureBox1
            // 
            this.pictureBox1.Image = ((System.Drawing.Image)(resources.GetObject("pictureBox1.Image")));
            this.pictureBox1.Location = new System.Drawing.Point(7, 49);
            this.pictureBox1.Margin = new System.Windows.Forms.Padding(2);
            this.pictureBox1.Name = "pictureBox1";
            this.pictureBox1.Size = new System.Drawing.Size(213, 193);
            this.pictureBox1.SizeMode = System.Windows.Forms.PictureBoxSizeMode.Zoom;
            this.pictureBox1.TabIndex = 5;
            this.pictureBox1.TabStop = false;
            this.pictureBox1.Click += new System.EventHandler(this.pictureBox1_Click);
            // 
            // pictureBox2
            // 
            this.pictureBox2.Image = ((System.Drawing.Image)(resources.GetObject("pictureBox2.Image")));
            this.pictureBox2.Location = new System.Drawing.Point(540, 262);
            this.pictureBox2.Margin = new System.Windows.Forms.Padding(2);
            this.pictureBox2.Name = "pictureBox2";
            this.pictureBox2.Size = new System.Drawing.Size(16, 32);
            this.pictureBox2.SizeMode = System.Windows.Forms.PictureBoxSizeMode.StretchImage;
            this.pictureBox2.TabIndex = 6;
            this.pictureBox2.TabStop = false;
            this.pictureBox2.Click += new System.EventHandler(this.label2_Click);
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(406, 281);
            this.label2.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(134, 13);
            this.label2.TabIndex = 0;
            this.label2.Text = "Riddler Company Creations";
            this.label2.Click += new System.EventHandler(this.label2_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(259, 48);
            this.label1.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(46, 13);
            this.label1.TabIndex = 0;
            this.label1.Text = "Usuario:";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(259, 106);
            this.label3.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(61, 13);
            this.label3.TabIndex = 0;
            this.label3.Text = "Contraseña";
            // 
            // chkUsuario
            // 
            this.chkUsuario.AutoSize = true;
            this.chkUsuario.Location = new System.Drawing.Point(438, 87);
            this.chkUsuario.Margin = new System.Windows.Forms.Padding(2);
            this.chkUsuario.Name = "chkUsuario";
            this.chkUsuario.RightToLeft = System.Windows.Forms.RightToLeft.Yes;
            this.chkUsuario.Size = new System.Drawing.Size(102, 17);
            this.chkUsuario.TabIndex = 2;
            this.chkUsuario.Text = "recordar usuario";
            this.chkUsuario.UseVisualStyleBackColor = true;
            this.chkUsuario.CheckedChanged += new System.EventHandler(this.checkBox1_CheckedChanged);
            this.chkUsuario.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.chkUsuario_KeyPress);
            // 
            // chkpass
            // 
            this.chkpass.AutoSize = true;
            this.chkpass.Location = new System.Drawing.Point(424, 145);
            this.chkpass.Margin = new System.Windows.Forms.Padding(2);
            this.chkpass.Name = "chkpass";
            this.chkpass.RightToLeft = System.Windows.Forms.RightToLeft.Yes;
            this.chkpass.Size = new System.Drawing.Size(116, 17);
            this.chkpass.TabIndex = 4;
            this.chkpass.Text = "mostrar contraseña";
            this.chkpass.UseVisualStyleBackColor = true;
            this.chkpass.CheckedChanged += new System.EventHandler(this.chkpass_CheckedChanged);
            this.chkpass.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.chkpass_KeyPress);
            // 
            // chkCredenciales
            // 
            this.chkCredenciales.AutoSize = true;
            this.chkCredenciales.Location = new System.Drawing.Point(412, 166);
            this.chkCredenciales.Margin = new System.Windows.Forms.Padding(2);
            this.chkCredenciales.Name = "chkCredenciales";
            this.chkCredenciales.RightToLeft = System.Windows.Forms.RightToLeft.Yes;
            this.chkCredenciales.Size = new System.Drawing.Size(128, 17);
            this.chkCredenciales.TabIndex = 5;
            this.chkCredenciales.Text = "recordar credenciales";
            this.chkCredenciales.UseVisualStyleBackColor = true;
            this.chkCredenciales.CheckedChanged += new System.EventHandler(this.chkCredenciales_CheckedChanged);
            this.chkCredenciales.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.chkCredenciales_KeyPress);
            // 
            // Log_in
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(4)))), ((int)(((byte)(228)))), ((int)(((byte)(140)))));
            this.ClientSize = new System.Drawing.Size(562, 299);
            this.Controls.Add(this.chkCredenciales);
            this.Controls.Add(this.chkpass);
            this.Controls.Add(this.chkUsuario);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.pictureBox2);
            this.Controls.Add(this.pictureBox1);
            this.Controls.Add(this.btnLoging);
            this.Controls.Add(this.linkLabel1);
            this.Controls.Add(this.btnRegistro);
            this.Controls.Add(this.txtpass);
            this.Controls.Add(this.txtUsuario);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.Fixed3D;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Margin = new System.Windows.Forms.Padding(2);
            this.MaximizeBox = false;
            this.Name = "Log_in";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "PingU - Log In";
            this.Load += new System.EventHandler(this.Log_in_Load);
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox2)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TextBox txtUsuario;
        private System.Windows.Forms.TextBox txtpass;
        private System.Windows.Forms.Button btnRegistro;
        private System.Windows.Forms.LinkLabel linkLabel1;
        private System.Windows.Forms.Button btnLoging;
        private System.Windows.Forms.PictureBox pictureBox1;
        private System.Windows.Forms.PictureBox pictureBox2;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.CheckBox chkUsuario;
        private System.Windows.Forms.CheckBox chkpass;
        private System.Windows.Forms.CheckBox chkCredenciales;
    }
}

