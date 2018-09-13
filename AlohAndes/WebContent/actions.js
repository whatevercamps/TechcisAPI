
function getClientes(form) {
	var url = "http://localhost:8080/RedetekAPIRest/rest/clientes/";

	$.getJSON( url, function( json ) {
		console.log( "JSON Data: " + json.nombre );
		document.getElementById("rta").innerHTML = json.nombre;
	});
}