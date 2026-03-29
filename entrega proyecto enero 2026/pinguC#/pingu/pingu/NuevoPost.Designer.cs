namespace pingu
{
    partial class NuevoPost
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>

        private void InitializeComponent()
        {
            this.lblTitulo = new System.Windows.Forms.Label();
            this.panelCard = new System.Windows.Forms.Panel();
            this.btnCancelar = new System.Windows.Forms.Button();
            this.btnPublicar = new System.Windows.Forms.Button();
            this.txtContenidoPost = new System.Windows.Forms.TextBox();
            this.lblSubtitulo = new System.Windows.Forms.Label();
            this.panelCard.SuspendLayout();
            this.SuspendLayout();
            // 
            // lblTitulo
            // 
            this.lblTitulo.AutoSize = true;
            this.lblTitulo.Font = new System.Drawing.Font("Segoe UI", 18F, System.Drawing.FontStyle.Bold);
            this.lblTitulo.ForeColor = System.Drawing.Color.Black;
            this.lblTitulo.Location = new System.Drawing.Point(39, 28);
            this.lblTitulo.Name = "lblTitulo";
            this.lblTitulo.Size = new System.Drawing.Size(231, 41);
            this.lblTitulo.TabIndex = 0;
            this.lblTitulo.Text = "Crear publicación";
            // 
            // panelCard
            // 
            this.panelCard.BackColor = System.Drawing.Color.White;
            this.panelCard.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.panelCard.Controls.Add(this.btnCancelar);
            this.panelCard.Controls.Add(this.btnPublicar);
            this.panelCard.Controls.Add(this.txtContenidoPost);
            this.panelCard.Controls.Add(this.lblSubtitulo);
            this.panelCard.Location = new System.Drawing.Point(46, 90);
            this.panelCard.Name = "panelCard";
            this.panelCard.Size = new System.Drawing.Size(860, 360);
            this.panelCard.TabIndex = 1;
            // 
            // btnCancelar
            // 
            this.btnCancelar.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(97)))), ((int)(((byte)(81)))), ((int)(((byte)(155)))));
            this.btnCancelar.FlatStyle = System.Windows.Forms.FlatStyle.Standard;
            this.btnCancelar.ForeColor = System.Drawing.Color.White;
            this.btnCancelar.Location = new System.Drawing.Point(502, 282);
            this.btnCancelar.Name = "btnCancelar";
            this.btnCancelar.Size = new System.Drawing.Size(135, 42);
            this.btnCancelar.TabIndex = 3;
            this.btnCancelar.Text = "Cancelar";
            this.btnCancelar.UseVisualStyleBackColor = false;
            this.btnCancelar.Click += new System.EventHandler(this.btnCancelar_Click);
            this.btnCancelar.MouseEnter += new System.EventHandler(this.btnCancelar_MouseEnter);
            this.btnCancelar.MouseLeave += new System.EventHandler(this.btnCancelar_MouseLeave);
            // 
            // btnPublicar
            // 
            this.btnPublicar.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(97)))), ((int)(((byte)(81)))), ((int)(((byte)(155)))));
            this.btnPublicar.FlatStyle = System.Windows.Forms.FlatStyle.Standard;
            this.btnPublicar.ForeColor = System.Drawing.Color.White;
            this.btnPublicar.Location = new System.Drawing.Point(661, 282);
            this.btnPublicar.Name = "btnPublicar";
            this.btnPublicar.Size = new System.Drawing.Size(135, 42);
            this.btnPublicar.TabIndex = 2;
            this.btnPublicar.Text = "Publicar";
            this.btnPublicar.UseVisualStyleBackColor = false;
            this.btnPublicar.Click += new System.EventHandler(this.btnPublicar_Click);
            this.btnPublicar.MouseEnter += new System.EventHandler(this.btnPublicar_MouseEnter);
            this.btnPublicar.MouseLeave += new System.EventHandler(this.btnPublicar_MouseLeave);
            // 
            // txtContenidoPost
            // 
            this.txtContenidoPost.Font = new System.Drawing.Font("Segoe UI", 11F);
            this.txtContenidoPost.Location = new System.Drawing.Point(35, 76);
            this.txtContenidoPost.Multiline = true;
            this.txtContenidoPost.Name = "txtContenidoPost";
            this.txtContenidoPost.ScrollBars = System.Windows.Forms.ScrollBars.Vertical;
            this.txtContenidoPost.Size = new System.Drawing.Size(761, 180);
            this.txtContenidoPost.TabIndex = 1;
            // 
            // lblSubtitulo
            // 
            this.lblSubtitulo.AutoSize = true;
            this.lblSubtitulo.Font = new System.Drawing.Font("Segoe UI", 14F, System.Drawing.FontStyle.Bold);
            this.lblSubtitulo.Location = new System.Drawing.Point(29, 24);
            this.lblSubtitulo.Name = "lblSubtitulo";
            this.lblSubtitulo.Size = new System.Drawing.Size(251, 32);
            this.lblSubtitulo.TabIndex = 0;
            this.lblSubtitulo.Text = "¿Qué estás pensando?";
            // 
            // NuevoPost
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(4)))), ((int)(((byte)(228)))), ((int)(((byte)(140)))));
            this.ClientSize = new System.Drawing.Size(957, 497);
            this.Controls.Add(this.panelCard);
            this.Controls.Add(this.lblTitulo);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.Fixed3D;
            this.MaximizeBox = false;
            this.Name = "NuevoPost";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "Nuevo post";
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.NuevoPost_FormClosed);
            this.panelCard.ResumeLayout(false);
            this.panelCard.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label lblTitulo;
        private System.Windows.Forms.Panel panelCard;
        private System.Windows.Forms.Button btnCancelar;
        private System.Windows.Forms.Button btnPublicar;
        private System.Windows.Forms.TextBox txtContenidoPost;
        private System.Windows.Forms.Label lblSubtitulo;




    }
}