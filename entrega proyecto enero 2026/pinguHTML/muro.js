function mostrar(seccion) {
    const boxes = document.querySelectorAll('.content-box');
    boxes.forEach(box => box.style.display = 'none');

    const activo = document.getElementById(seccion);
    if (activo) {
        activo.style.display = 'block';

        if (seccion === 'perfil') {
            fetch('perfil.html')
                .then(response => {
                    if (!response.ok) throw new Error("No encontrado");
                    return response.text();
                })
                .then(html => activo.innerHTML = html)
                .catch(() => activo.innerHTML = '<p>Error cargando perfil</p>');
        }
    }
}

document.addEventListener("DOMContentLoaded", () => {
    mostrar("perfil");
});

// Función para desconexión
function desconexion() {
    window.location.href = "index.html";
}

