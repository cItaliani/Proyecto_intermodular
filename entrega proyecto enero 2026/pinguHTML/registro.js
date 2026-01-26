document.querySelector("form").addEventListener("submit", e => {
    const p1 = document.getElementById("pass").value;
    const p2 = document.getElementById("pass2").value;

    if (p1 !== p2) {
        e.preventDefault();
        alert("Las contraseñas no coinciden");
    }
});
