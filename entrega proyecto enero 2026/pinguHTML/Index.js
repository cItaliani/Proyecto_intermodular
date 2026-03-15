document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("loginForm");
    const usuario = document.getElementById("usuario");
    const password = document.getElementById("password");
    const chkRecordar = document.getElementById("recordarUsuario");
    const chkRecordarTodo = document.getElementById("recordarCredenciales");
    const btnLogin = document.querySelector(".btnLogin");
    const btnLogout = document.getElementById("btnLogout");
    const chkMostrar = document.getElementById("mostrar");

    // Mostrar/ocultar contraseña 
    chkMostrar.addEventListener("change", () => {
        password.type = chkMostrar.checked ? "text" : "password";
    });

    // Checkboxes excluyentes 
    chkRecordar.addEventListener("change", () => {
        if (chkRecordar.checked) {
            chkRecordarTodo.checked = false;
        }
    });
    
    chkRecordarTodo.addEventListener("change", () => {
        if (chkRecordarTodo.checked) {
            chkRecordar.checked = false;
        }
    });

    // Cargar usuario y contraseña guardados si existen
    const usuarioGuardado = localStorage.getItem("usuario");
    const passwordGuardada = localStorage.getItem("password");

    // Primero desmarcar ambos checkboxes
    chkRecordar.checked = false;
    chkRecordarTodo.checked = false;

    // Luego cargar según lo que haya guardado
    if (usuarioGuardado && passwordGuardada) {
        usuario.value = usuarioGuardado;
        password.value = passwordGuardada;
        chkRecordarTodo.checked = true;
    } else if (usuarioGuardado && !passwordGuardada) {
        usuario.value = usuarioGuardado;
        chkRecordar.checked = true;
    }

    // Validación al enviar el formulario
    form.addEventListener("submit", (e) => {
        e.preventDefault();
        let error = false;
        
        const usuarioValue = usuario.value.trim();
        const passwordValue = password.value.trim();

        // Validar usuario
        if (usuarioValue === "") {
            usuario.value = "";
            
            const frasesUsuario = [
                "⚠️ Sin usuario no entras, colega 🚫",
                "Ehhh, ¿el usuario? 🤨 No te lo saltes",
                "¿Usuario invisible? No funciona así 👻",
                "Pon tu usuario aquí, porfa 😅",
                "⚠️ Campo obligatorio, campeón",
                "Tío, el usuario... ¿dónde está? 🤷‍♂️",
                "No seas tímido, pon tu usuario 😏",
                "Adivina: necesitas un usuario 🎯",
                "El usuario no es opcional, crack 🎪",
                "¿Olvidaste algo? Sí, el usuario 🧠"
            ];
            
            usuario.placeholder = frasesUsuario[Math.floor(Math.random() * frasesUsuario.length)];
            usuario.style.border = "2px solid #ff4d4d";
            usuario.style.backgroundColor = "#fff0f0";
            error = true;
        }

        // Validar contraseña vacía
        if (passwordValue === "") {
            password.value = "";
            
            const frasesPassword = [
                "Ehhh, ¿y la contraseña? 🤔",
                "La contraseña no se pone sola 🙃",
                "¿Contraseña? ¿Hola? 👋",
                "Sin contraseña no hay login, sorry 🚷",
                "Falta algo importante... la contraseña 🔑",
                "¿Te olvidaste de la contraseña? 😬",
                "Contraseña obligatoria, amigo 🎯",
                "Pon la contraseña, no seas vago 😅"
            ];
            
            password.placeholder = frasesPassword[Math.floor(Math.random() * frasesPassword.length)];
            password.style.border = "2px solid #ff4d4d";
            password.style.backgroundColor = "#fff0f0";
            error = true;
        } 
        // Validar longitud de contraseña
        else if (passwordValue.length < 6) {
            password.value = "";
            
            const frasesPasswordCorta = [
                "⚠️ ¿En serio? Mínimo 6, no seas rata 😂",
                "Muy corta, mínimo 6 caracteres 📏",
                "¿6 caracteres es mucho pedir? 🤨",
                "Esa contraseña es más corta que... 6+ porfa 🙏",
                "Mínimo 6, que no es tan difícil 💪",
                "6 caracteres o más, venga 🎯",
                "Corta contraseña = insegura. Mín. 6 🔒",
                "Dale más caña, mínimo 6 caracteres 🚀"
            ];
            
            password.placeholder = frasesPasswordCorta[Math.floor(Math.random() * frasesPasswordCorta.length)];
            password.style.border = "2px solid #ff4d4d";
            password.style.backgroundColor = "#fff0f0";
            error = true;
        }

        // Si hay errores, no seguimos
        if (error) return;

        // Guardar credenciales según la opción elegida
        if (chkRecordarTodo.checked) {
            localStorage.setItem("usuario", usuarioValue);
            localStorage.setItem("password", passwordValue);
        } else if (chkRecordar.checked) {
            localStorage.setItem("usuario", usuarioValue);
            localStorage.removeItem("password");
        } else {
            localStorage.removeItem("usuario");
            localStorage.removeItem("password");
        }

        // Llamada a la API de login
        const loginData = {
            alias: usuarioValue,
            contrasena: passwordValue
        };

        fetch("http://localhost:8080/api/rest/pingu/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(loginData)
        })
        .then(response => {
            if (response.status === 200) {
                return response.json().then(data => {
                    localStorage.setItem("id_usuario", data.id);
                    window.location.href = "misPost.html";
                });
            } else if (response.status === 401) {
                alert("⚠️ Credenciales incorrectas");
            } else {
                alert("❌ Error inesperado del servidor");
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("🚨 No se pudo conectar con el servidor");
        });
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
            localStorage.removeItem("id_usuario");
            usuario.value = "";
            password.value = "";
            chkRecordar.checked = false;
            chkRecordarTodo.checked = false;
            window.location.href = "index.html";
        });
    }
});