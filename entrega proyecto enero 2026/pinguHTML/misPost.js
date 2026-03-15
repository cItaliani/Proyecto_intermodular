const API_BASE = "http://localhost:8080/api/rest/pingu";

function mostrar(seccion) {

    const titulos = {
        perfil: "Mi perfil",
        seguido: "Usuarios seguidos",
        seguidores: "Mis seguidores",
        posts: "Mis publicaciones"
    };

    document.getElementById("tituloSeccion").textContent = titulos[seccion] || "PingU";

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

        if (seccion === "posts") {
            cargarMisPosts();
        }
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const idUsuario = localStorage.getItem("id_usuario");

    if (!idUsuario) {
        alert("No hay sesión iniciada");
        window.location.href = "index.html";
        return;
    }

    mostrar("posts");
});

// Función para desconexión
function desconexion() {
    localStorage.removeItem("id_usuario");
    window.location.href = "index.html";
}

// =========================
// POSTS / MURO
// =========================

async function cargarMisPosts() {
    const contenedor = document.getElementById("posts");
    const idUsuarioLogueado = localStorage.getItem("id_usuario");

    contenedor.innerHTML = "<p>Cargando tus posts...</p>";

    try {
        const response = await fetch(`${API_BASE}/posts`);
        if (!response.ok) throw new Error("No se pudieron cargar los posts");

        const posts = await response.json();

        const misPosts = posts.filter(post => String(post.id_autor) === String(idUsuarioLogueado));

        if (misPosts.length === 0) {
            contenedor.innerHTML = "<p>No tienes publicaciones visibles en el muro.</p>";
            return;
        }

        const tarjetasHtml = await Promise.all(misPosts.map(post => construirPostHTML(post)));
        contenedor.innerHTML = tarjetasHtml.join("");
    } catch (error) {
        console.error(error);
        contenedor.innerHTML = "<p>Error cargando tus posts.</p>";
    }
}

async function construirPostHTML(post) {
    try {
        const [autor, likes] = await Promise.all([
            obtenerUsuario(post.id_autor),
            obtenerLikesDePost(post.id)
        ]);

        const idUsuarioLogueado = localStorage.getItem("id_usuario");
        const yoHeDadoLike = likes.some(like => String(like.idUsuario) === String(idUsuarioLogueado));
        const nombreAutor = autor
            ? `${autor.nombre_visible || ""}`.trim()
            : "Autor desconocido";

        return `
            <div class="post-card" id="post-${post.id}" style="border:1px solid #ccc; border-radius:12px; padding:16px; margin-bottom:16px; background:#fff;">
                <div class="post-header" style="margin-bottom:10px;">
                    <h3 style="margin:0;">${escapeHtml(nombreAutor)}</h3>
                </div>

                <div class="post-body" style="margin-bottom:10px;">
                    <p style="margin:0 0 8px 0;">${escapeHtml(post.contenido || "")}</p>
                    <small>Likes: <span id="likes-count-${post.id}">${likes.length}</span></small>
                </div>

                <div class="post-actions" style="display:flex; flex-wrap:wrap; gap:8px; margin-top:10px;">
                    
                    ${yoHeDadoLike
                ? `<button onclick="quitarLike('${post.id}')">Quitar like</button>`
                : `<button onclick="darLike('${post.id}')">Dar like</button>`}

                    <button onclick="borrarPost('${post.id}')">Borrar</button>
                    <button onclick="toggleResponder('${post.id}')">Responder</button>
                    <button onclick="toggleRespuestas('${post.id}')">Ver respuestas</button>

                </div>

                <div class="like-status" style="margin-top:8px;">
                    <small id="like-status-${post.id}">
                        ${yoHeDadoLike ? "Ya le has dado like" : "Aún no le has dado like"}
                    </small>
                </div>

                <div id="reply-form-${post.id}" style="display:none; margin-top:12px;">
                    <textarea id="reply-text-${post.id}" placeholder="Escribe tu respuesta..." 
                        style="width:100%; min-height:80px; padding:8px; border-radius:8px;"></textarea>
                    <button style="margin-top:8px;" onclick="enviarRespuesta('${post.id}')">Enviar respuesta</button>
                </div>

                <div id="replies-${post.id}" style="display:none; margin-top:12px; padding-top:12px; border-top:1px solid #ddd;">
                    <p>Cargando respuestas...</p>
                </div>
            </div>
        `;
    } catch (error) {
        console.error("Error construyendo post", post.id, error);
        return `
            <div class="post-card" style="border:1px solid #ccc; border-radius:12px; padding:16px; margin-bottom:16px;">
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

        const tarjeta = document.getElementById(`post-${idPost}`);
        if (tarjeta) {
            tarjeta.remove();
        }
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
                    <div style="border:1px solid #ddd; border-radius:8px; padding:10px; margin-bottom:8px; background:#f9f9f9;">
                        <strong>${escapeHtml(nombreAutor)}</strong>
                        <p style="margin:6px 0 0 0;">${escapeHtml(respuesta.contenido || "")}</p>
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