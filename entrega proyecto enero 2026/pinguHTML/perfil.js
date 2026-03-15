const API_BASE = "http://localhost:8080/api/rest/pingu";

let datosOriginales = null;

document.addEventListener("DOMContentLoaded", () => {
    const idUsuario = localStorage.getItem("id_usuario");

    if (!idUsuario) {
        alert("No hay sesión iniciada");
        window.location.href = "index.html";
        return;
    }

    cargarPerfil();
});

function desconexion() {
    localStorage.removeItem("id_usuario");
    window.location.href = "index.html";
}

async function cargarPerfil() {
    const idUsuario = localStorage.getItem("id_usuario");

    try {
        const [usuario, followers, follows] = await Promise.all([
            obtenerUsuario(idUsuario),
            obtenerFollowers(idUsuario),
            obtenerFollows(idUsuario)
        ]);

        if (!usuario) {
            alert("No se pudo cargar el perfil");
            return;
        }

        datosOriginales = {
            nombre_visible: obtenerNombreVisible(usuario),
            biografia: obtenerBiografia(usuario)
        };

        document.getElementById("fechaAlta").value = formatearFecha(usuario.fecha_alta ?? usuario.fechaAlta ?? "");
        document.getElementById("alias").value = usuario.alias ?? "";
        document.getElementById("nombreVisible").value = obtenerNombreVisible(usuario);
        document.getElementById("correo").value = usuario.correo_electronico ?? usuario.correoElectronico ?? "";
        document.getElementById("seguidores").value = followers.length;
        document.getElementById("seguidos").value = follows.length;
        document.getElementById("biografia").value = obtenerBiografia(usuario);

    } catch (error) {
        console.error(error);
        alert("Error cargando el perfil");
    }
}

async function obtenerUsuario(idUsuario) {
    const response = await fetch(`${API_BASE}/users/${idUsuario}`);
    if (!response.ok) return null;
    return await response.json();
}

async function obtenerFollowers(idUsuario) {
    const response = await fetch(`${API_BASE}/users/${idUsuario}/followers`);
    if (!response.ok) return [];
    return await response.json();
}

async function obtenerFollows(idUsuario) {
    const response = await fetch(`${API_BASE}/users/${idUsuario}/followed`);
    if (!response.ok) return [];
    return await response.json();
}

async function guardarPerfil() {
    const idUsuario = localStorage.getItem("id_usuario");
    const nombreVisible = document.getElementById("nombreVisible").value.trim();
    const biografia = document.getElementById("biografia").value.trim();
    const contrasena = document.getElementById("contrasena").value.trim();
    const repetirContrasena = document.getElementById("repetirContrasena").value.trim();

    if (!nombreVisible) {
        alert("El nombre visible no puede estar vacío");
        return;
    }

    if (!contrasena || !repetirContrasena) {
        alert("Debes escribir y repetir la nueva contraseña para guardar cambios");
        return;
    }

    if (contrasena.length < 6) {
        alert("La contraseña debe tener al menos 6 caracteres");
        return;
    }

    if (contrasena !== repetirContrasena) {
        alert("Las contraseñas no coinciden");
        return;
    }

    const body = {
        nombre_visible: nombreVisible,
        biografia: biografia,
        contrasena: contrasena,
        fotografia: ""
    };

    try {
        const response = await fetch(`${API_BASE}/users/${idUsuario}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        });

        if (!response.ok) {
            const texto = await response.text();
            throw new Error(texto || "No se pudo guardar el perfil");
        }

        alert("Perfil actualizado correctamente");
        document.getElementById("contrasena").value = "";
        document.getElementById("repetirContrasena").value = "";
        await cargarPerfil();
    } catch (error) {
        console.error(error);
        alert("Error guardando el perfil");
    }
}

function cancelarCambios() {
    if (!datosOriginales) {
        window.location.href = "home.html";
        return;
    }

    document.getElementById("nombreVisible").value = datosOriginales.nombre_visible ?? "";
    document.getElementById("biografia").value = datosOriginales.biografia ?? "";
    document.getElementById("contrasena").value = "";
    document.getElementById("repetirContrasena").value = "";
}

async function eliminarCuenta() {
    const confirmacion = confirm("¿Seguro que quieres eliminar tu cuenta? Esta acción no se puede deshacer.");

    if (!confirmacion) return;

    const segundaConfirmacion = confirm("Tu cuenta y tus datos se eliminarán. ¿Quieres continuar?");

    if (!segundaConfirmacion) return;

    const idUsuario = localStorage.getItem("id_usuario");

    try {
        const response = await fetch(`${API_BASE}/users/${idUsuario}`, {
            method: "DELETE"
        });

        if (!response.ok) {
            const texto = await response.text();
            throw new Error(texto || "No se pudo eliminar la cuenta");
        }

        localStorage.removeItem("id_usuario");
        alert("Tu cuenta ha sido eliminada correctamente");
        window.location.href = "index.html";
    } catch (error) {
        console.error(error);
        alert("Error eliminando la cuenta");
    }
}

function obtenerNombreVisible(usuario) {
    return usuario.nombre_visible
        ?? usuario.nombreVisible
        ?? usuario.nombre
        ?? "";
}

function obtenerBiografia(usuario) {
    return usuario.biografia
        ?? usuario.bio
        ?? "";
}

function formatearFecha(fechaTexto) {
    if (!fechaTexto) return "";

    const fecha = new Date(fechaTexto);

    if (isNaN(fecha.getTime())) {
        return fechaTexto;
    }

    const dia = String(fecha.getDate()).padStart(2, "0");
    const mes = String(fecha.getMonth() + 1).padStart(2, "0");
    const anio = fecha.getFullYear();

    return `${dia}/${mes}/${anio}`;
}