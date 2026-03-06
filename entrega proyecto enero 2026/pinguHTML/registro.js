document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("form");
    const nombre = document.querySelector('input[placeholder="nombre"]');
    const apellido1 = document.querySelector('input[placeholder="primer apellido"]');
    const apellido2 = document.querySelector('input[placeholder="segundo apellido"]');
    const usuario = document.querySelector('input[placeholder="nombre de usuario"]');
    const email = document.querySelector('input[placeholder=" correo electronico"]');
    const pass = document.getElementById("pass");
    const pass2 = document.getElementById("pass2");

    // Validación al enviar el formulario
    form.addEventListener("submit", (e) => {
        let error = false;

        // Validar nombre
        if (nombre.value.trim() === "") {
            nombre.value = "";

            const frasesNombre = [
                "⚠️ El nombre es obligatorio, crack 🚫",
                "Ehhh, ¿tu nombre? 🤨",
                "¿Nombre invisible? No funciona 👻",
                "Pon tu nombre aquí, porfa 😅",
                "Sin nombre no hay registro 🎯",
                "¿Te olvidaste de tu nombre? 😂",
                "El nombre no es opcional, campeón 🎪",
                "Necesito saber cómo te llamas 🧠"
            ];

            nombre.placeholder = frasesNombre[Math.floor(Math.random() * frasesNombre.length)];
            nombre.style.border = "2px solid #ff4d4d";
            nombre.style.backgroundColor = "#fff0f0";
            error = true;
        }

        // Validar primer apellido
        if (apellido1.value.trim() === "") {
            apellido1.value = "";

            const frasesApellido1 = [
                "⚠️ Primer apellido obligatorio 🚫",
                "¿Y el primer apellido? 🤨",
                "Falta el primer apellido, tío 👻",
                "Pon tu primer apellido porfa 😅",
                "Sin apellido no hay registro 🎯",
                "¿Te olvidaste del apellido? 😬",
                "El primer apellido es necesario 🎪",
                "Necesito tu primer apellido 📝"
            ];

            apellido1.placeholder = frasesApellido1[Math.floor(Math.random() * frasesApellido1.length)];
            apellido1.style.border = "2px solid #ff4d4d";
            apellido1.style.backgroundColor = "#fff0f0";
            error = true;
        }

        // Validar segundo apellido
        if (apellido2.value.trim() === "") {
            apellido2.value = "";

            const frasesApellido2 = [
                "⚠️ Segundo apellido obligatorio 🚫",
                "¿Y el segundo apellido? 🤨",
                "Falta el segundo apellido 👻",
                "Pon tu segundo apellido porfa 😅",
                "Completa con el segundo apellido 🎯",
                "¿Te olvidaste del segundo? 😬",
                "El segundo apellido también va 🎪",
                "Necesito el segundo apellido 📝"
            ];

            apellido2.placeholder = frasesApellido2[Math.floor(Math.random() * frasesApellido2.length)];
            apellido2.style.border = "2px solid #ff4d4d";
            apellido2.style.backgroundColor = "#fff0f0";
            error = true;
        }

        // Validar nombre de usuario
        if (usuario.value.trim() === "") {
            usuario.value = "";

            const frasesUsuario = [
                "⚠️ Necesitas un nombre de usuario 🚫",
                "¿Tu nombre de usuario? 🤨",
                "Falta el nombre de usuario 👻",
                "Elige un nombre de usuario 😅",
                "Sin usuario no puedes entrar 🎯",
                "¿Qué usuario quieres? 😬",
                "El nombre de usuario es clave 🔑",
                "Inventa un nombre de usuario 🎨"
            ];

            usuario.placeholder = frasesUsuario[Math.floor(Math.random() * frasesUsuario.length)];
            usuario.style.border = "2px solid #ff4d4d";
            usuario.style.backgroundColor = "#fff0f0";
            error = true;
        }

        // Validar email vacío
        if (email.value.trim() === "") {
            email.value = "";

            const frasesEmailVacio = [
                "Ehhh, ¿y el email? 🤔",
                "El correo no se pone solo 🙃",
                "¿Email? ¿Hola? 📧",
                "Sin email no hay registro 🚷",
                "Falta algo importante... el email 📬",
                "¿Te olvidaste del correo? 😬",
                "Email obligatorio, amigo 🎯",
                "Pon el email, no seas vago 😅"
            ];

            email.placeholder = frasesEmailVacio[Math.floor(Math.random() * frasesEmailVacio.length)];
            email.style.border = "2px solid #ff4d4d";
            email.style.backgroundColor = "#fff0f0";
            error = true;
        }
        // Validar formato de email
        else if (!validarEmail(email.value.trim())) {
            email.value = "";

            const frasesEmailInvalido = [
                "⚠️ Ese email no pinta bien 🤔",
                "Email inválido, revísalo porfa 📧",
                "¿Seguro que ese es tu email? 🧐",
                "Formato de email incorrecto 🚫",
                "Eso no es un email válido, crack 😅",
                "Email mal escrito, inténtalo 📝",
                "Revisa el formato del email 🔍",
                "Ese email tiene pinta rara 🤨"
            ];

            email.placeholder = frasesEmailInvalido[Math.floor(Math.random() * frasesEmailInvalido.length)];
            email.style.border = "2px solid #ff4d4d";
            email.style.backgroundColor = "#fff0f0";
            error = true;
        }

        // Validar contraseña vacía
        if (pass.value.trim() === "") {
            pass.value = "";

            const frasesPassVacia = [
                "Ehhh, ¿la contraseña? 🤔",
                "La contraseña no se pone sola 🙃",
                "¿Contraseña? ¿Hola? 🔐",
                "Sin contraseña no hay cuenta 🚷",
                "Falta la contraseña 🔑",
                "¿Te olvidaste de la contraseña? 😬",
                "Contraseña obligatoria 🎯",
                "Pon una contraseña segura 🛡️"
            ];

            pass.placeholder = frasesPassVacia[Math.floor(Math.random() * frasesPassVacia.length)];
            pass.style.border = "2px solid #ff4d4d";
            pass.style.backgroundColor = "#fff0f0";
            error = true;
        }
        // Validar longitud de contraseña
        else if (pass.value.trim().length < 6) {
            pass.value = "";

            const frasesPassCorta = [
                "⚠️ Mínimo 6 caracteres, no seas rata 😂",
                "Muy corta, mínimo 6 caracteres 📏",
                "¿6 caracteres es mucho pedir? 🤨",
                "Esa contraseña es muy corta 🙏",
                "Mínimo 6, que no es tan difícil 💪",
                "6 caracteres o más, venga 🎯",
                "Contraseña corta = insegura. Mín. 6 🔒",
                "Dale más caña, mínimo 6 🚀"
            ];

            pass.placeholder = frasesPassCorta[Math.floor(Math.random() * frasesPassCorta.length)];
            pass.style.border = "2px solid #ff4d4d";
            pass.style.backgroundColor = "#fff0f0";
            error = true;
        }

        // Validar repetición de contraseña vacía
        if (pass2.value.trim() === "") {
            pass2.value = "";

            const frasesPass2Vacia = [
                "Repite la contraseña aquí 🔁",
                "¿Y la confirmación? 🤔",
                "Falta repetir la contraseña 🔐",
                "Confirma tu contraseña 🎯",
                "Pon la contraseña otra vez 🔑",
                "Necesito que la repitas 😅",
                "Confirma la contraseña porfa 🙏",
                "Escribe la contraseña de nuevo 📝"
            ];

            pass2.placeholder = frasesPass2Vacia[Math.floor(Math.random() * frasesPass2Vacia.length)];
            pass2.style.border = "2px solid #ff4d4d";
            pass2.style.backgroundColor = "#fff0f0";
            error = true;
        }
        // Validar que las contraseñas coincidan
        else if (pass.value !== pass2.value) {
            pass2.value = "";

            const frasesPassNoCoinciden = [
                "⚠️ Las contraseñas no coinciden 🚫",
                "Ehhh, no son iguales 🤨",
                "Las contraseñas no match 👻",
                "No coinciden, revísalas 😅",
                "Contraseñas diferentes 🎯",
                "Esas no son iguales, tío 😬",
                "No coinciden, inténtalo otra vez 🔄",
                "Las contraseñas deben ser iguales 🎪"
            ];

            pass2.placeholder = frasesPassNoCoinciden[Math.floor(Math.random() * frasesPassNoCoinciden.length)];
            pass2.style.border = "2px solid #ff4d4d";
            pass2.style.backgroundColor = "#fff0f0";
            error = true;
        }

        // Si hay errores, prevenir el envío
        if (error) {
            e.preventDefault();
            return;
        }

        // Si todo está bien
        e.preventDefault();

        const nombreVisible = nombre.value.trim() + " " +
            apellido1.value.trim() + " " +
            apellido2.value.trim();

        const userData = {
            alias: usuario.value.trim(),
            nombre_visible: nombreVisible,
            correo_electronico: email.value.trim(),
            contrasena: pass.value.trim(),
            biografia: "",
            fotografia: ""
        };

        fetch("http://localhost:8080/api/rest/pingu/users", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(userData)
        })
            .then(response => {
                if (response.status === 201) {
                    form.reset();
                    window.location.href = "index.html";
                }
                else if (response.status === 409) {
                    alert("⚠️ El alias o el email ya existen");
                }
                else {
                    alert("❌ Error inesperado del servidor");
                }
            })
            .catch(error => {
                console.error("Error:", error);
                alert("🚨 No se pudo conectar con el servidor");
            });
    });

    // Función para validar formato de email
    function validarEmail(email) {
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email);
    }

    // Limpiar estilo al escribir
    function limpiar(input, placeholderOriginal) {
        input.style.border = "";
        input.style.backgroundColor = "";
        input.placeholder = placeholderOriginal;
    }

    nombre.addEventListener("input", () => limpiar(nombre, "nombre"));
    apellido1.addEventListener("input", () => limpiar(apellido1, "primer apellido"));
    apellido2.addEventListener("input", () => limpiar(apellido2, "segundo apellido"));
    usuario.addEventListener("input", () => limpiar(usuario, "nombre de usuario"));
    email.addEventListener("input", () => limpiar(email, " correo electronico"));
    pass.addEventListener("input", () => limpiar(pass, "contraseña"));
    pass2.addEventListener("input", () => limpiar(pass2, "repite la contraseña"));
});