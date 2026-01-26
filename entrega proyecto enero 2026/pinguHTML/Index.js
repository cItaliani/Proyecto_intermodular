// Mostrar / ocultar contraseña
const chkMostrar = document.getElementById("mostrar");
const password = document.getElementById("password");

chkMostrar.addEventListener("change", () => {
    password.type = chkMostrar.checked ? "text" : "password";
});

document.addEventListener("DOMContentLoaded", () => {
    const usuario = document.getElementById("usuario");
    const password = document.getElementById("password");
    const chkRecordar = document.getElementById("recordarUsuario");
    const chkRecordarTodo = document.getElementById("recordarCredenciales");
    const btnLogin = document.querySelector(".btnLogin");
    const btnLogout = document.getElementById("btnLogout"); // botón desconectar
    const linkLogin = btnLogin.closest("a");

    // Cargar usuario y contraseña guardados si existen
    const usuarioGuardado = localStorage.getItem("usuario");
    const passwordGuardada = localStorage.getItem("password");

    if (usuarioGuardado && !passwordGuardada) {
        usuario.value = usuarioGuardado;
        chkRecordar.checked = true;
    }
    if (usuarioGuardado && passwordGuardada) {
        usuario.value = usuarioGuardado;
        password.value = passwordGuardada;
        chkRecordarTodo.checked = true;
    }

    // Validación al hacer click en login
    linkLogin.addEventListener("click", (e) => {
        let error = false;

        if (usuario.value.trim() === "") {
            usuario.value = "";
            usuario.placeholder = "⚠️ Introduce el usuario";
            usuario.style.border = "2px solid #ff4d4d";
            usuario.style.backgroundColor = "#fff0f0";
            error = true;
        }

        if (password.value.trim() === "") {
            password.value = "";
            password.placeholder = "⚠️ Introduce la contraseña";
            password.style.border = "2px solid #ff4d4d";
            password.style.backgroundColor = "#fff0f0";
            error = true;
        }

        if (error) {
            e.preventDefault();
        } else {
            // Guardar usuario y contraseña si "Recordar usuario" está marcado
            if (chkRecordar.checked) {
                localStorage.setItem("usuario", usuario.value.trim());
                if (chkRecordarTodo.checked) {
                    localStorage.setItem("password", password.value);
                }
            }
            else if (chkRecordarTodo.checked) {
                localStorage.setItem("usuario", usuario.value.trim());
                localStorage.setItem("password", password.value);
            } else{
                localStorage.removeItem("usuario");
                localStorage.removeItem("password");
            }
        }
    });

    // Limpiar estilo al escribir
    function limpiar(input, placeholder) {
        input.style.border = "";
        input.style.backgroundColor = "";
        input.placeholder = placeholder;
    }

    usuario.addEventListener("input", () => limpiar(usuario, "usuario"));
    password.addEventListener("input", () => limpiar(password, "contraseña"));

    // Funcionalidad de desconectar
    if (btnLogout) {
        btnLogout.addEventListener("click", () => {
            localStorage.removeItem("usuario");
            localStorage.removeItem("password");
            usuario.value = "";
            password.value = "";
            chkRecordar.checked = false;

            // Redirigir al login
            window.location.href = "login.html"; // cambiar según tu ruta
        });
    }
});
