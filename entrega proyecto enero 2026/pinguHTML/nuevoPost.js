const API_BASE = "http://localhost:8080/api/rest/pingu";

document.addEventListener("DOMContentLoaded", () => {

    const idUsuario = localStorage.getItem("id_usuario");

    if(!idUsuario){
        alert("No hay sesión iniciada");
        window.location.href = "index.html";
    }

});

function desconexion(){
    localStorage.removeItem("id_usuario");
    window.location.href="index.html";
}

function cancelarPost(){
    window.location.href="home.html";
}

async function publicarPost(){

    const contenido = document.getElementById("contenidoPost").value.trim();
    const idUsuario = localStorage.getItem("id_usuario");

    if(!contenido){
        alert("Escribe algo antes de publicar");
        return;
    }

    const body = {
        contenido: contenido,
        urlMultimedia: "",
        id_autor: idUsuario,
        idPostPadre: null
    };

    try{

        const response = await fetch(`${API_BASE}/posts`,{
            method:"POST",
            headers:{
                "Content-Type":"application/json"
            },
            body:JSON.stringify(body)
        });

        if(!response.ok){
            const texto = await response.text();
            throw new Error(texto);
        }

        alert("Post publicado");

        window.location.href="home.html";

    }catch(error){

        console.error(error);
        alert("Error publicando el post");

    }

}