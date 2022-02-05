# Obiettivo

Costruire un sistema client/server di chat che scambia messaggi JSON
utilizzando WebSocket:
* I server sono scritti sia in javascript (per Node) 
che in java (per un container tipo Tomcat).
* I client sono scritti sia in javascript (per browser) che in java 
(applicazione JavaFX).


## Dipendenze software

In questo progetto alcune cartelle non sono gestite da git:

- jdk-17 un jdk versione 17, la versione "full" di 
  [liberica](https://bell-sw.com/pages/downloads/) per 
  semplicità siccome il client usa JavaFX

- Eclipse.app/eclipse è [Eclipse](https://www.eclipse.org/) 
  versione minima 2021-12 per il supporto a java17. Eclipse 
  è configurato per usare un jdk 17

- [Tomcat](http://tomcat.apache.org/) versione 10


## Server node
È un server javascript che verra eseguito da [node](https://nodejs.org/) 

```
npm init
npm install websocket
```

## Server Java
È una applicazione che gira in un container tipo Tomcat. L'applicazione 
è gestita da [Maven](https://maven.apache.org/)
quindi per generare il pacchetto da far caricare a Tomcat 
basta scriver `mvn clean` e poi `mvn package`, nella cartella `target`
sarà possibile trovare un file `serverchat.war` che va copiato
nella cartella `webapps` di Tomcat.


## Client per Browser
Brevissima applicazione che permettedi scegliere con quale server connettersi
e invia/riceve i messaggi.

## Client Java
