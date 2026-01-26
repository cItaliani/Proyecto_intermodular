// Esperar a que el DOM esté completamente cargado
document.addEventListener('DOMContentLoaded', function() {
    
    // Botón de actualizar foto
    const updatePhotoBtn = document.querySelector('.update-photo-btn');
    const tfgLogo = document.querySelector('.tfg-logo');
    
    updatePhotoBtn.addEventListener('click', function() {
        // Crear input de tipo file
        const input = document.createElement('input');
        input.type = 'file';
        input.accept = 'image/*';
        
        input.onchange = function(e) {
            const file = e.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(event) {
                    // Reemplazar el contenido del logo con la imagen
                    tfgLogo.innerHTML = `<img src="${event.target.result}" style="width: 100%; height: 100%; object-fit: cover;" alt="Foto de perfil">`;
                };
                reader.readAsDataURL(file);
            }
        };
        
        input.click();
    });
    
    // Botón Guardar
    const btnSave = document.querySelector('.btn-save');
    btnSave.addEventListener('click', function() {
        // Recoger los datos
        const datos = {
            nombre: document.querySelector('.info-item:nth-child(2) .info-value').textContent,
            alias: document.querySelector('.info-item:nth-child(4) .info-value').textContent,
            email: document.querySelector('.info-item:nth-child(6) .info-value').textContent,
            biografia: document.querySelector('.biografia-content').textContent
        };
        
        console.log('Datos a guardar:', datos);
        
        // Aquí harías una petición al servidor para guardar los datos
        // fetch('/api/guardar-perfil', {
        //     method: 'POST',
        //     headers: { 'Content-Type': 'application/json' },
        //     body: JSON.stringify(datos)
        // })
        // .then(response => response.json())
        // .then(data => {
        //     alert('Perfil guardado correctamente');
        //     window.location.href = 'muro.html';
        // });
        
        // Por ahora solo redirige directamente
        alert('Perfil guardado correctamente');
        window.location.href = 'muro.html'; // Cambia 'muro.html' por la ruta correcta
    });
    
    // Botón Cancelar
    const btnCancel = document.querySelector('.btn-cancel');
    btnCancel.addEventListener('click', function() {
        // Redirigir al muro sin guardar cambios
        window.location.href = 'muro.html'; // Cambia 'muro.html' por la ruta correcta
    });
    
    // Hacer la biografía editable (opcional)
    const biografiaContent = document.querySelector('.biografia-content');
    biografiaContent.setAttribute('contenteditable', 'true');
    
    // Cambiar el estilo cuando esté en foco
    biografiaContent.addEventListener('focus', function() {
        this.style.outline = '2px solid #9333ea';
    });
    
    biografiaContent.addEventListener('blur', function() {
        this.style.outline = 'none';
    });
    
    // Hacer los campos de información editables (opcional)
    const infoValues = document.querySelectorAll('.info-value');
    infoValues.forEach(function(field) {
        // Excluir "Miembro desde" y "Seguidores/Seguidos" de ser editables
        const parent = field.closest('.info-item');
        const label = parent.querySelector('.info-label').textContent;
        
        if (!label.includes('Miembro desde') && 
            !label.includes('Seguidores') && 
            !label.includes('Seguidos') &&
            !label.includes('Contraseña')) {
            
            field.setAttribute('contenteditable', 'true');
            field.style.cursor = 'text';
            
            field.addEventListener('focus', function() {
                this.style.backgroundColor = '#f0f0f0';
                this.style.padding = '2px 5px';
                this.style.borderRadius = '3px';
            });
            
            field.addEventListener('blur', function() {
                this.style.backgroundColor = 'transparent';
            });
        }
    });
    
});