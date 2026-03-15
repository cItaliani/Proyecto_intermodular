const API_BASE = "http://localhost:8080/api/rest/pingu";

document.addEventListener("DOMContentLoaded", () => {
    const idUsuario = localStorage.getItem("id_usuario");

    if (!idUsuario) {
        alert("No hay sesión iniciada");
        window.location.href = "index.html";
        return;
    }
    const contenedorHome = document.getElementById("home");
    if (contenedorHome) {
        contenedorHome.style.display = "block";
    }
    cargarHomePosts();
});

function desconexion() {
    localStorage.removeItem("id_usuario");
    window.location.href = "index.html";
}

// =========================
// HOME POSTS
// =========================

async function cargarHomePosts() {
    const contenedor = document.getElementById("home");
    contenedor.innerHTML = "<p>Cargando publicaciones...</p>";

    try {
        const response = await fetch(`${API_BASE}/posts`);
        if (!response.ok) throw new Error("No se pudieron cargar los posts");

        const posts = await response.json();

        if (!posts || posts.length === 0) {
            contenedor.innerHTML = "<p>No hay publicaciones disponibles.</p>";
            return;
        }

        const tarjetasHtml = await Promise.all(posts.map(post => construirPostHTML(post)));
        contenedor.innerHTML = tarjetasHtml.join("");
    } catch (error) {
        console.error(error);
        contenedor.innerHTML = "<p>Error cargando publicaciones.</p>";
    }
}

async function construirPostHTML(post) {
    try {
        const [autor, likes] = await Promise.all([
            obtenerUsuario(post.id_autor),
            obtenerLikesDePost(post.id)
        ]);

        const idUsuarioLogueado = localStorage.getItem("id_usuario");
        const esMio = String(post.id_autor) === String(idUsuarioLogueado);
        const yoHeDadoLike = likes.some(like => String(like.idUsuario) === String(idUsuarioLogueado));

        const nombreAutor = autor
            ? `${autor.nombre_visible || ""}`.trim()
            : "Autor desconocido";

        return `
            <div class="post-card" id="post-${post.id}">
                <div class="post-header">
                    <h3>${escapeHtml(nombreAutor)}</h3>
                </div>

                <div class="post-body">
                    <p>${escapeHtml(post.contenido || "")}</p>
                    <small>Likes: <span id="likes-count-${post.id}">${likes.length}</span></small>
                </div>

                <div class="post-actions-custom">
                    ${yoHeDadoLike
                ? `<button onclick="quitarLike('${post.id}')">Quitar like</button>`
                : `<button onclick="darLike('${post.id}')">Dar like</button>`}
                    ${esMio ? `<button onclick="borrarPost('${post.id}')">Borrar</button>` : ""}
                    <button onclick="toggleResponder('${post.id}')">Responder</button>
                    <button onclick="toggleRespuestas('${post.id}')">Ver respuestas</button>
                </div>

                <div class="like-status">
                    <small id="like-status-${post.id}">
                        ${yoHeDadoLike ? "Ya le has dado like" : "Aún no le has dado like"}
                    </small>
                </div>

                <div id="reply-form-${post.id}" class="reply-form" style="display:none;">
                    <textarea id="reply-text-${post.id}" placeholder="Escribe tu respuesta..."></textarea>
                    <button onclick="enviarRespuesta('${post.id}')">Enviar respuesta</button>
                </div>

                <div id="replies-${post.id}" class="replies-box" style="display:none;">
                    <p>Cargando respuestas...</p>
                </div>
            </div>
        `;
    } catch (error) {
        console.error("Error construyendo post", post.id, error);
        return `
            <div class="post-card">
                <p>Error cargando el post ${post.id}</p>
            </div>
        `;
    }
}

async function obtenerUsuario(idUsuario) {
    try {
        const response = await fetch(`${API_BASE}/users/${idUsuario}`);
        if (!response.ok) return null;
        return await response.json();
    } catch (error) {
        console.error("Error obteniendo usuario:", error);
        return null;
    }
}

async function obtenerLikesDePost(idPost) {
    try {
        const response = await fetch(`${API_BASE}/posts/${idPost}/likes`);
        if (!response.ok) return [];
        return await response.json();
    } catch (error) {
        console.error("Error obteniendo likes:", error);
        return [];
    }
}

// =========================
// LIKES
// =========================

async function darLike(idPost) {
    const idUsuario = localStorage.getItem("id_usuario");

    try {
        const response = await fetch(`${API_BASE}/posts/${idPost}/like`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                idUsuario: idUsuario
            })
        });

        if (!response.ok) {
            const texto = await response.text();
            throw new Error(texto || "No se pudo dar like");
        }

        await refrescarLikes(idPost);
    } catch (error) {
        console.error(error);
        alert("Error al dar like");
    }
}

async function quitarLike(idPost) {
    const idUsuario = localStorage.getItem("id_usuario");

    try {
        const response = await fetch(`${API_BASE}/posts/${idPost}/dislike`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                idUsuario: idUsuario
            })
        });

        if (!response.ok) {
            const texto = await response.text();
            throw new Error(texto || "No se pudo quitar like");
        }

        await refrescarLikes(idPost);
    } catch (error) {
        console.error(error);
        alert("Error al quitar like");
    }
}

async function refrescarLikes(idPost) {

    try {
        const response = await fetch(`${API_BASE}/posts/${idPost}`);
        const post = await response.json();

        const postHTML = await construirPostHTML(post);

        const tarjeta = document.getElementById(`post-${idPost}`);
        tarjeta.outerHTML = postHTML;

    } catch (error) {
        console.error("Error refrescando post:", error);
    }
}

// =========================
// BORRAR POST
// =========================

async function borrarPost(idPost) {
    const tarjeta = document.getElementById(`post-${idPost}`);
    if (!tarjeta) return;

    const confirmar = confirm("¿Seguro que quieres borrar este post?");
    if (!confirmar) return;

    try {
        const response = await fetch(`${API_BASE}/posts/${idPost}`, {
            method: "DELETE"
        });

        if (!response.ok) {
            const texto = await response.text();
            throw new Error(texto || "No se pudo borrar el post");
        }

        tarjeta.remove();
    } catch (error) {
        console.error(error);
        alert("Error al borrar el post");
    }
}

// =========================
// RESPUESTAS
// =========================

function toggleResponder(idPost) {
    const caja = document.getElementById(`reply-form-${idPost}`);
    if (!caja) return;

    caja.style.display = caja.style.display === "none" ? "block" : "none";
}

async function enviarRespuesta(idPostPadre) {
    const textarea = document.getElementById(`reply-text-${idPostPadre}`);
    const contenido = textarea.value.trim();
    const idUsuario = localStorage.getItem("id_usuario");

    if (!contenido) {
        alert("Escribe una respuesta antes de enviarla");
        return;
    }

    const body = {
        contenido: contenido,
        urlMultimedia: "",
        id_autor: idUsuario,
        idPostPadre: idPostPadre
    };

    try {
        const response = await fetch(`${API_BASE}/posts`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        });

        if (!response.ok) {
            const texto = await response.text();
            throw new Error(texto || "No se pudo enviar la respuesta");
        }

        textarea.value = "";
        alert("Respuesta enviada");

        const repliesBox = document.getElementById(`replies-${idPostPadre}`);
        if (repliesBox && repliesBox.style.display === "block") {
            cargarRespuestas(idPostPadre);
        }
    } catch (error) {
        console.error(error);
        alert("Error al responder");
    }
}

async function toggleRespuestas(idPost) {
    const caja = document.getElementById(`replies-${idPost}`);
    if (!caja) return;

    if (caja.style.display === "none") {
        caja.style.display = "block";
        await cargarRespuestas(idPost);
    } else {
        caja.style.display = "none";
    }
}

async function cargarRespuestas(idPost) {
    const caja = document.getElementById(`replies-${idPost}`);
    if (!caja) return;

    caja.innerHTML = "<p>Cargando respuestas...</p>";

    try {
        const response = await fetch(`${API_BASE}/posts/${idPost}/replies`);
        if (!response.ok) throw new Error("No se pudieron cargar las respuestas");

        const respuestas = await response.json();

        if (!respuestas || respuestas.length === 0) {
            caja.innerHTML = "<p>Este post no tiene respuestas.</p>";
            return;
        }

        const respuestasHtml = await Promise.all(
            respuestas.map(async (respuesta) => {
                const autor = await obtenerUsuario(respuesta.id_autor);
                const nombreAutor = autor ? `${autor.nombre_visible || ""}`.trim() : "Autor desconocido";

                return `
                    <div class="reply-item">
                        <strong>${escapeHtml(nombreAutor)}</strong>
                        <p>${escapeHtml(respuesta.contenido || "")}</p>
                    </div>
                `;
            })
        );

        caja.innerHTML = respuestasHtml.join("");
    } catch (error) {
        console.error(error);
        caja.innerHTML = "<p>Error cargando respuestas.</p>";
    }
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