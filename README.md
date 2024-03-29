# Prova Finale di Ingegneria del Software - AA 2022-2023
![alt text](https://github.com/martiiiss/ing-sw-2023-pinessi-riva-quaregna-vai/blob/main/src/main/resources/resources/my-shelfie.jpg)

## Implementazione del gioco da tavolo MyShelfie.

Il progetto consiste nell’implementazione di un sistema distribuito composto da un singolo server in grado di gestire più partita alla volta e multipli client che possono partecipare ad una sola partita alla volta utilizzando il pattern MVC (Model-View-Controller). La rete è stata gestita con l'utilizzo del protocollo RMI e Socket.

Interazione e gameplay: linea di comando (CLI) e grafica (GUI).

## Documentazione

### UML
I seguenti diagrammi rappresentano il diagramma delle classi generato automaticamente da IntelliJ e quello che rappresenta il progetto
- [UML di dettaglio](https://github.com/martiiiss/ing-sw-2023-pinessi-riva-quaregna-vai/blob/main/deliveries/documentation/UML_Detail.png)
- [UML Generale](https://github.com/martiiiss/ing-sw-2023-pinessi-riva-quaregna-vai/blob/main/deliveries/documentation/UML_General.svg)

### Coverage report
Al seguente link è possibile consultare il report della coverage dei test effettuati con JUnit: [Report](https://github.com/martiiiss/ing-sw-2023-pinessi-riva-quaregna-vai/blob/main/deliveries/documentation/test_coverage.png)


### JavaDoc
La seguente documentazione include una descrizione per la maggior parte delle classi e dei metodi utilizzati, segue le tecniche di documentazione di Java e può essere consultata al seguente indirizzo: [Javadoc](https://github.com/martiiiss/ing-sw-2023-pinessi-riva-quaregna-vai/tree/main/deliveries/documentation/javadoc)

### Librerie e Plugins
|Libreria/Plugin|Descrizione|
|---------------|-----------|
|__Maven__|Strumento di automazione della compilazione utilizzato principalmente per progetti Java.|
|__GSON__| Libreria Java utilizzata per convertire JSONs nella loro rappresentazione ad Oggetti Java.|
|__Java Swing__| Swing è un framework per Java orientato allo sviluppo di interfacce grafiche.|
|__JUnit__|Framework di unit testing.|

Per una corretta esecuzione del gioco attraverso CLI, assicurarsi di poter visualizzare i colori tramite i _Codici di escape ANSI_

## Funzionalità implementate
- Regole complete
- Versione CLI
- Versione GUI
- RMI
- Socket
- Funzionalità avanzata:
    - _Partite multiple:_ il server supporta più partite contemporaneamente.
 
### Jars
I Jar del progetto possono essere scaricati al seguente link: [JAR](https://github.com/martiiiss/ing-sw-2023-pinessi-riva-quaregna-vai/tree/main/deliveries/jar)

## Esecuzione
Questo progetto richiede una versione della JDK 19 o superiore per essere eseguito correttamente.

### Avvio Partita
Per avviare una partita, posizionarsi nella directory deliveries/jar, un utente dovrà aprire il Server tramite questo comando: ```java -jar Server.jar```

Quindi inserire come _Address_ il proprio indirizzo LAN IP. L'indirizzo IP può essere trovato digitando da terminale il comando:
```ipconfig```

Una volta aperto il Server gli altri utenti potranno connettersi aprendo i Client.jar attraverso il seguente comando: ```java -jar Client.jar```

Una volta aperto ogni client dovrà inserire l'address del Server.
Inizierà quindi la partita seguendo le istruzioni sullo schermo

## Componenti del gruppo
- [__Andrea Pinessi__](https://github.com/AndreaPinessi)
- [__Martina Quaregna__](https://github.com/MartyQ17)
- [__Martina Riva__](https://github.com/martiiiss)
- [__Alessandro Vai__](https://github.com/Darhale01)

_NOTA: My Shelfie è un gioco da tavolo sviluppato ed edito da Cranio Creations Srl. I contenuti grafici di questo progetto riconducibili al prodotto editoriale da tavolo sono utilizzati previa approvazione di Cranio Creations Srl a solo scopo didattico. È vietata la distribuzione, la copia o la riproduzione dei contenuti e immagini in qualsiasi forma al di fuori del progetto, così come la redistribuzione e la pubblicazione dei contenuti e immagini a fini diversi da quello sopracitato. È inoltre vietato l'utilizzo commerciale di suddetti contenuti._
