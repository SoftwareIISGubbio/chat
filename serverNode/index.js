"use strict";

let webSocketsServerPort = 7000;
let webSocketServer = require('websocket').server;
let http = require('http');

// l'elenco dei client attualemente connessi
let clients = [];

// ========== funzione che viene chiamata quando si connette un client ==========================
function gestoreConnessione(request) {
    // accetto da qualsiasi origine, in generale meglio fare un controllo Same_origin_policy
    let connessione = request.accept(null, request.origin);
    let id = connessione.remoteAddress;
    console.log("nuovo client: "+id);
    clients.push(connessione);

    // callback per la ricezione dei dati
    connessione.on('message', function (message) {
        if (message.type === 'utf8') { // accetto soltanto il testo
            console.log("messaggio: "+message.utf8Data);
            broadcast(id, message.utf8Data);
        }
    });

    // callback per la disconnessione
    connessione.on('close', function (ragione) {
        // rimuovo l'utente dall'elenco degli utenti connessi
        for (let i = 0; i < clients.length; i++) {
            if(clients[i]==connessione){
                clients.splice(i, 1);
                break;
            }
        }
        console.log(id, `client disconnesso (${ragione}), ${clients.length} client rimanenti`);
    });
}

// invio messaggi in broadcast
function broadcast(id, messaggio){
    for (var i = 0; i < clients.length; i++) {
        clients[i].sendUTF(messaggio);
    }
}
// ========== server HTTP =======================================================================
// creo il server Web che fornisce sempre lo stesso contenuto
let serverHTTP = http.createServer();
// metto in ascolto il server, la callback viene chiamata dopo l'avvio
serverHTTP.listen(webSocketsServerPort);
// ========== server Websocket ==================================================================
let wsServer = new webSocketServer({
    httpServer: serverHTTP // il server WebSocket è legato ad un server HTTP.
});
wsServer.on('request', gestoreConnessione);
console.log("In ascolto sulla porta "+webSocketsServerPort);