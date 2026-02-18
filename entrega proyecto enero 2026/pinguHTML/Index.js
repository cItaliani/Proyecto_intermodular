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
        // Si hay usuario Y contraseña, recordar todo
        usuario.value = usuarioGuardado;
        password.value = passwordGuardada;
        chkRecordarTodo.checked = true;
    } else if (usuarioGuardado && !passwordGuardada) {
        // Si solo hay usuario, recordar solo usuario
        usuario.value = usuarioGuardado;
        chkRecordar.checked = true;
    }

    // Validación al enviar el formulario
    form.addEventListener("submit", (e) => {
        let error = false;
        
        const usuarioValue = usuario.value.trim();
        const passwordValue = password.value.trim();

        // Validar usuario
        if (usuarioValue === "") {
            usuario.value = "";
            
            // Array de frases aleatorias para usuario vacío
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
            
            // Seleccionar frase aleatoria
            const fraseAleatoria = frasesUsuario[Math.floor(Math.random() * frasesUsuario.length)];
            usuario.placeholder = fraseAleatoria;
            
            usuario.style.border = "2px solid #ff4d4d";
            usuario.style.backgroundColor = "#fff0f0";
            error = true;
        }

        // Validar contraseña vacía
        if (passwordValue === "") {
            password.value = "";
            
            // Array de frases aleatorias para contraseña vacía
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
            
            const fraseAleatoria = frasesPassword[Math.floor(Math.random() * frasesPassword.length)];
            password.placeholder = fraseAleatoria;
            
            password.style.border = "2px solid #ff4d4d";
            password.style.backgroundColor = "#fff0f0";
            error = true;
        } 
        // Validar longitud de contraseña
        else if (passwordValue.length < 6) {
            password.value = "";
            
            // Array de frases aleatorias para contraseña corta
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
            
            const fraseAleatoria = frasesPasswordCorta[Math.floor(Math.random() * frasesPasswordCorta.length)];
            password.placeholder = fraseAleatoria;
            
            password.style.border = "2px solid #ff4d4d";
            password.style.backgroundColor = "#fff0f0";
            error = true;
        }

        // Si hay errores, prevenir el envío
        if (error) {
            e.preventDefault();
            return;
        }

        // Si no hay errores, guardar credenciales según la opción elegida
        if (chkRecordarTodo.checked) {
            // Guardar ambos
            localStorage.setItem("usuario", usuarioValue);
            localStorage.setItem("password", passwordValue);
        } else if (chkRecordar.checked) {
            // Guardar solo usuario
            localStorage.setItem("usuario", usuarioValue);
            localStorage.removeItem("password");
        } else {
            // No guardar nada
            localStorage.removeItem("usuario");
            localStorage.removeItem("password");
        }

        // Permitir el envío del formulario
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
            chkRecordarTodo.checked = false;

            // Redirigir al login
            window.location.href = "index.html";
        });
    }
});