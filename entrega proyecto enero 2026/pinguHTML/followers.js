const API_BASE = "http://localhost:8080/api/rest/pingu";

document.addEventListener("DOMContentLoaded", () => {
    const idUsuario = localStorage.getItem("id_usuario");

    if (!idUsuario) {
        alert("No hay sesión iniciada");
        window.location.href = "index.html";
        return;
    }

    cargarFollowers();
});

function desconexion() {
    localStorage.removeItem("id_usuario");
    window.location.href = "index.html";
}

async function cargarFollowers() {
    const contenedor = document.getElementById("followers");
    const idUsuarioLogueado = localStorage.getItem("id_usuario");

    contenedor.innerHTML = "<p>Cargando seguidores...</p>";

    try {
        const [seguidores, usuarios] = await Promise.all([
            obtenerFollowers(idUsuarioLogueado),
            obtenerTodosLosUsuarios()
        ]);

        const idsSeguidores = seguidores.map(seg => String(obtenerIdSeguidor(seg)));

        const usuariosSeguidores = usuarios.filter(usuario => {
            const idUsuario = String(obtenerIdUsuario(usuario));
            return idsSeguidores.includes(idUsuario);
        });

        if (!usuariosSeguidores.length) {
            contenedor.innerHTML = "<p>No tienes seguidores.</p>";
            return;
        }

        const html = usuariosSeguidores.map(usuario => `
            <div class="user-card">
                <div class="user-card-info">
                    <div class="user-visible-name">${escapeHtml(obtenerNombreVisible(usuario))}</div>
                    <div class="user-alias">@${escapeHtml(obtenerAlias(usuario))}</div>
                    <div class="user-bio">${escapeHtml(obtenerBiografia(usuario))}</div>
                </div>
            </div>
        `).join("");

        contenedor.innerHTML = `<div class="users-list">${html}</div>`;
    } catch (error) {
        console.error(error);
        contenedor.innerHTML = "<p>Error cargando seguidores.</p>";
    }
}

async function obtenerFollowers(idUsuario) {
    const response = await fetch(`${API_BASE}/users/${idUsuario}/followers`);
    if (!response.ok) return [];
    return await response.json();
}

async function obtenerTodosLosUsuarios() {
    const response = await fetch(`${API_BASE}/users`);
    if (!response.ok) throw new Error("No se pudieron cargar los usuarios");
    return await response.json();
}

function obtenerIdUsuario(usuario) {
    if (!usuario || typeof usuario !== "object") return undefined;
    if (usuario.id !== undefined && usuario.id !== null) return usuario.id;
    if (usuario.idUsuario !== undefined && usuario.idUsuario !== null) return usuario.idUsuario;
    if (usuario.id_usuario !== undefined && usuario.id_usuario !== null) return usuario.id_usuario;
    if (usuario.idusuario !== undefined && usuario.idusuario !== null) return usuario.idusuario;

    const claveId = Object.keys(usuario).find(k => k.toLowerCase().includes("id"));
    return claveId ? usuario[claveId] : undefined;
}

function obtenerIdSeguidor(seg) {
    if (!seg || typeof seg !== "object") return undefined;
    if (seg.idSeguidor !== undefined && seg.idSeguidor !== null) return seg.idSeguidor;
    if (seg.id_seguidor !== undefined && seg.id_seguidor !== null) return seg.id_seguidor;

    const clave = Object.keys(seg).find(k => k.toLowerCase().includes("seguidor"));
    return clave ? seg[clave] : undefined;
}

function obtenerAlias(usuario) {
    return usuario.alias ?? usuario.Alias ?? "";
}

function obtenerNombreVisible(usuario) {
    return usuario.nombre_visible
        ?? usuario.nombreVisible
        ?? usuario.nombre
        ?? "Sin nombre visible";
}

function obtenerBiografia(usuario) {
    return usuario.biografia
        ?? usuario.bio
        ?? "Este usuario no tiene biografía.";
}

function escapeHtml(texto) {
    return String(texto)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}