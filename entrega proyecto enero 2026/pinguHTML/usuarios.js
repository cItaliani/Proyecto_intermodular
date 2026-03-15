const API_BASE = "http://localhost:8080/api/rest/pingu";

document.addEventListener("DOMContentLoaded", () => {
    const idUsuario = localStorage.getItem("id_usuario");

    if (!idUsuario) {
        alert("No hay sesión iniciada");
        window.location.href = "index.html";
        return;
    }

    cargarUsuarios();
});

function desconexion() {
    localStorage.removeItem("id_usuario");
    window.location.href = "index.html";
}

// =========================
// USUARIOS
// =========================

async function cargarUsuarios() {
    const contenedor = document.getElementById("usuarios");
    const idUsuarioLogueado = localStorage.getItem("id_usuario");

    contenedor.innerHTML = "<p>Cargando usuarios...</p>";

    try {
        const [usuarios, seguidos] = await Promise.all([
            obtenerTodosLosUsuarios(),
            obtenerSeguidos(idUsuarioLogueado)
        ]);

        const idsSeguidos = seguidos
            .map(seg => String(obtenerIdSeguido(seg)))
            .filter(Boolean);

        const usuariosFiltrados = usuarios.filter(usuario => {
            const idUsuarioLista = obtenerIdUsuario(usuario);
            return String(idUsuarioLista) !== String(idUsuarioLogueado);
        });

        if (!usuariosFiltrados.length) {
            contenedor.innerHTML = "<p>No hay usuarios disponibles.</p>";
            return;
        }

        const html = usuariosFiltrados.map(usuario => {
            const idUsuarioTarjeta = obtenerIdUsuario(usuario);
            const yaLoSigo = idsSeguidos.includes(String(idUsuarioTarjeta));

            return `
                <div class="user-card" id="user-${idUsuarioTarjeta}">
                    <div class="user-card-info">
                        <div class="user-alias">@${escapeHtml(obtenerAlias(usuario))}</div>
                        <div class="user-visible-name">${escapeHtml(obtenerNombreVisible(usuario))}</div>
                        <div class="user-bio">${escapeHtml(obtenerBiografia(usuario))}</div>
                    </div>

                    <div class="user-card-actions">
                        ${yaLoSigo
                            ? `<button class="unfollow-btn" onclick="dejarDeSeguir('${idUsuarioTarjeta}')">Dejar de seguir</button>`
                            : `<button class="follow-btn" onclick="seguirUsuario('${idUsuarioTarjeta}')">Seguir</button>`
                        }
                    </div>
                </div>
            `;
        }).join("");

        contenedor.innerHTML = `<div class="users-list">${html}</div>`;
    } catch (error) {
        console.error(error);
        contenedor.innerHTML = "<p>Error cargando usuarios.</p>";
    }
}

async function obtenerTodosLosUsuarios() {
    const response = await fetch(`${API_BASE}/users`);
    if (!response.ok) {
        throw new Error("No se pudieron cargar los usuarios");
    }
    return await response.json();
}

async function obtenerSeguidos(idUsuario) {
    const response = await fetch(`${API_BASE}/users/${idUsuario}/followed`);
    if (!response.ok) {
        return [];
    }
    return await response.json();
}

async function seguirUsuario(idUsuarioObjetivo) {
    const idUsuarioLogueado = localStorage.getItem("id_usuario");

    try {
        const response = await fetch(`${API_BASE}/users/${idUsuarioObjetivo}/follow`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                id_seguidor: idUsuarioLogueado
            })
        });

        if (!response.ok) {
            const texto = await response.text();
            throw new Error(texto || "No se pudo seguir al usuario");
        }

        await cargarUsuarios();
    } catch (error) {
        console.error(error);
        alert("Error al seguir al usuario");
    }
}

async function dejarDeSeguir(idUsuarioObjetivo) {
    const idUsuarioLogueado = localStorage.getItem("id_usuario");

    try {
        const response = await fetch(`${API_BASE}/users/${idUsuarioObjetivo}/unfollow`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                id_seguidor: idUsuarioLogueado
            })
        });

        if (!response.ok) {
            const texto = await response.text();
            throw new Error(texto || "No se pudo dejar de seguir al usuario");
        }

        await cargarUsuarios();
    } catch (error) {
        console.error(error);
        alert("Error al dejar de seguir al usuario");
    }
}

// =========================
// HELPERS DE DATOS
// =========================

function obtenerIdUsuario(usuario) {
    if (!usuario || typeof usuario !== "object") return undefined;

    if (usuario.id !== undefined && usuario.id !== null) return usuario.id;
    if (usuario.idUsuario !== undefined && usuario.idUsuario !== null) return usuario.idUsuario;
    if (usuario.id_usuario !== undefined && usuario.id_usuario !== null) return usuario.id_usuario;
    if (usuario.idusuario !== undefined && usuario.idusuario !== null) return usuario.idusuario;

    const claveId = Object.keys(usuario).find(k => k.toLowerCase().includes("id"));
    return claveId ? usuario[claveId] : undefined;
}

function obtenerIdSeguido(seg) {
    if (!seg || typeof seg !== "object") return undefined;

    if (seg.idSeguido !== undefined && seg.idSeguido !== null) return seg.idSeguido;
    if (seg.id_seguido !== undefined && seg.id_seguido !== null) return seg.id_seguido;

    const clave = Object.keys(seg).find(k => k.toLowerCase().includes("seguido"));
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

// =========================
// UTILIDAD
// =========================

function escapeHtml(texto) {
    return String(texto)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}