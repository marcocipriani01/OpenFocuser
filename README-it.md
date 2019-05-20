<img align="left" width="100" src="assets/logo.png" style="position: relative; top: 8px; margin-bottom: 8px;" alt="OpenFocuser logo">

# OpenFocuser, un progetto di Marco Cipriani

<span class="no-print">**[Sito web](https://marcocipriani01.github.io/projects/OpenFocuser) |**</span> Focheggiatore compatibile MoonLite con posizionamento assoluto e relativo, half e full step. Compatibile con Linux e MacOS (INDI) e Windows (ASCOM). Non supporta la compensazione per temperatura. Disponibile in due edizioni: standard e Plus, la quale supporta una funzione non-MoonLite per controllare tutti i pin digitali e PWM dell'Arduino, in modo da poter accendere, spegnere o regolare in corrente altri dispositivi come fasce anti condensa, ventole per specchi o Raspberry Pi direttamente dal computer, o in remoto con un server INDI.

<aside>
<div class="contents no-print">
<b style="margin-left:16px;">Indice</b>

</div>
</aside>

## Utilizzo

### Focheggiatore

OpenFocuser è completamente compatibile con software MoonLite, quindi fare riferimento alle guide officiali per MoonLite, INDI, KStars, ASCOM o qualunque altra piattaforma si usi per l'astrofotografia

### OpenFocuser-Manager

OpenFocuser-Manager è un'applicazione Java 8 che permette all'utente finale di aggiornare facilmente il firmware dell'Arduino (eliminando la necessità di installare l'intera IDE Arduino, compilare e caricare lo sketch) e controllare i pin digitali della scheda se si sta utilizzando l'edizione Plus. Contiene integrati `avrdude` e gli ultimi firmware `.hex` per Arduino Nano, librerie seriali, il server INDI4Java e utility di auto-aggiornamento.

#### Installazione

-   Debian e Ubuntu:
    -   Installare il pacchetto Debian: `dpkg` installerà automaticamente `socat` e `avrdude` per te, una icona desktop verrà creata e si è pronti per lavorare.
    -   I pacchetti `openssh-client` e `openssh-server` sono richiesti se si vogliono inviare i file di configurazione, vedere sotto per ulteriori informazioni.
-   Altre distribuzioni Linux:
    -   Scaricare l'archivio `jar` ed eseguirlo. `socat` e `avrdude` **devono** essere installati a seconda del package manager e nella path.
    -   `ssh` client e server sono richiesti se si vogliono inviare i file di configurazione, vedere sotto per ulteriori informazioni.
-   Windows:
    -   Scaricare l'archivio `jar` ed eseguirlo. `avrdude.exe` e `libusb0.dll` sono inclusi, non c'è bisogno di un'installazione aggiuntiva. Il server INDI, il driver e l'invio delle impostazioni non sono disponibili, solo firmware update.
-   MacOS: non possiedo un Mac, quindi non posso distribuire un pacchetto per questo sistema operativo. Sentiti libero di contribuire!

<a class="no-print" href="assets/FW-update.png"><img src="assets/FW-update.png" width="400" align="right"></a>

#### Updating the firmware

L'aggiornamento del firmware con `avrdude` è supportato in Windows (`avrdude.exe` è  incluso nello `jar` e verrà spacchettato durante l'esecuzione nella cartella temporanea predefinita di sistema) e in Linux, indifferentemente dalla distribuzione, se `avrdude` è nella path (in Debian, installarlo con `sudo apt-get install avrdude`). La configurazione predefinita di `avrdude` è scelta automaticamente dal programma.<br>
Nella sezione Firmware update è possible selezionare la porta seriale della scheda, il tipo di scheda (al momento, il firmware è compilato solo per Arduino Nano, nuovo e vecchio bootloader), e l'edizione del firmware: standard (solo focheggiatore) o Plus (pin controllabili e illuminatore polare). Un'etichetta di testo sotto la selezione del firmware mostra la versione del software selezionato. Premere Update per eseguire il flash della scheda

<a class="no-print" href="assets/Plus-config.png"><img src="assets/Plus-config.png" width="400" align="left"></a>

#### Gestione dei pin nell'edizione Plus

**Nota: i focheggiatori standard non supportano la gestione dei pin! Non sono compatibili con OpenFocuser**
<br>Se si è nuovi ai driver INDI, leggere prima la documentazione sul <a href="http://indilib.org/about/discover-indi.html">sito INDI</a> e su <a href="https://en.wikipedia.org/wiki/Instrument_Neutral_Distributed_Interface">Wikipedia</a>.
<br>Nella sezione "Plus edition configuration" è possibile selezionare una porta per il server INDI (predefinita 7625 per lasciare la 7624 agli altri server INDI) e definire la lista dei pin digitali e PWM.
Premi "Add" per aggiungere una definizione di pin: aggiungere un pin digitale significa aggiungere un elemento switch INDI (una casella di selezione ON/OFF nel pannello di controllo INDI del client) che permette all'utente di cambiare lo stato del pin ON e OFF; invece, alla creazione di un pin PWM OpenFocuser aggiungerà un elemento INDI numerico al suo driver, permettendo all'utilizzatore di scrivere il valore del pin (0→100%) direttamente dal pannello di controllo INDI nel client.
Una porta pin verrà richiesta (per esempio, pin 13). **Nota: puoi aggiungere solo quei pin precedentemente selezionati nella configurazione dell'Arduino!**
È poi possibile premere "Edit" per modificare le proprietà del pin: un nome personalizzato (es. "Fascia anticondensa") e un valore predefinito, applicato quando si avvia il driver.
Dopo aver definito tutti i pin, salvare la configurazione ed avviare il server direttamente dal pannello di controllo o chiuderlo per avviarlo dalla linea di comando (si veda utilizzo avanzato).
Per utilizzare il manger dei pin è necessario un client INDI.
In KStars, aprire Ekos dalla toolbar e create un nuovo profilo contenente la tua montatura, CCD o Reflex e un focheggiatore MoonLite, e nel campo driver "Remote:" scrivere `INDI Arduino pin driver@localhost:7625`.
Cambiare `localhost:7625` con la giusta porta e host.
**Deselezionare** l'auto connessione dei dispositivi e dare al profilo un nome.
Ora avviare il server OpenFocuser (dal pannello di controllo o dalla linea di comando).
Avviare il server INDI di Ekos ed aprire il pannello di controllo INDI.
Collegare tutti i device, andare nella sezione "INDI Arduino pin driver" e connettere il driver.
Nella sottosezione "Serial connection" selezionare una porta seriale e connettere.
Una nuova scheda chiamata "Manage pins" comparirà, nella quale è possibile modificare lo stato dei pin selezionati nel pannello di controllo.
Copiare la porta del focheggiatore MoonLite ed incollarla nel campo della porta del driver MoonLite.
Selezionare come veloctà baud 9600 e connettere il focheggiatore.
Se tutto funziona a dovere l'intero pannello di controllo MoonLite comparirà.
Altrimentri controllare se il server OpenFocuser è in esecuzione, se la porta virtuale esiste e se la velocità è 9600.

## Utilizzo avanzato e risoluzione dei problemi

### Inviare la configurazione ad un altro computer.

È possibile inviare la configurazione dei pin e le impostazioni ad un altro computer. Assicurarsi che OpenFocuser-Manager sia installato su entrambi i computer con tutte le dipendenze. Dal computer che deve inviare la configurazione aprire il pannello di controllo e premere "Send configuration".
Verranno richiesti l'host remoto, l'username e la password.
Se il processo fallisce a causa di una cartella remota non trovata, aprire e chiudere una volta il pannello di controllo nel computer remoto e riprovare (questo ricreerà la cartella di configurazione nella cartella remota, che deve essere presente per permettere al primo computer di inviare il file di impostazioni).

### Server stand-alone da riga di comando

Usare `openfocuser -p=xxxx`, con `xxxx` la porta del server (o `0` per usare l'ultima porta salvata), per avviare il server da riga di comando senza interfaccia utente.
La configurazione è la stessa di quella salvata nel pannello di contrllo.

### INDI driver da solo in un altro server INDI

The INDI driver can be run inside another INDI server executing `openfocuser -d`.
No GUI will be loaded, nor the server will be set up: the driver will communicate with your external server with stdin/out, just like any other INDI driver.

### Starting from the command line

-   `bash`: `openfocuser <options>`
-   Windows: `java -jar OpenFocuser-Manager.jar <options>`

| Short option | Long option       | Param               | Description                                                                                           |
| ------------ | ----------------- | ------------------- | ----------------------------------------------------------------------------------------------------- |
| `-a`         | `--serial-port`   | e.g. `/dev/ttyUSB0` | Specifies a serial port and connects to it if possible. Otherwise it will be stored to settings only. |
| `-c`         | `--control-panel` |                     | Shows the control panel.                                                                              |
| `-d`         | `--driver`        |                     | Driver-only mode (no server, stdin/stdout)                                                            |
| `-p`         | `--indi-port`     | e.g. `7625`         | Stand-alone server mode, CLI. If port=0, fetch the last used port from the settings.                  |
| `-v`         | `--verbose`       |                     | Verbose logging mode.                                                                                 |

### Common errors

| Exit code | Error                                  | Solution                                                                                                                                                                                                                   |
| --------- | -------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 0         | Java not found                         | Install Java >8 and ensure it's in path                                                                                                                                                                                    |
| 3         | `socat` not found                      | Install `socat` (Linux)                                                                                                                                                                                                    |
| 4         | INDI not found                         | Install `indiserver` (Linux)                                                                                                                                                                                               |
| 5         | `avrdude` not found                    | Install `avrdude` (Linux)                                                                                                                                                                                                  |
| 6         | `jar` not found                        | Where did you put `OpenFocuser-Manager.jar`? Did you build the project and artifacts?                                                                                                                                      |
| 8         | Config folder could not be initialized | OpenFocuser-Manager wasn't able to create the folder where it'll put the configuration. Using your file manager, go to you home directory and check if the `.config/OpenFocuser-Manager` folders exist and are directories |
| 9         | Unable to parse parameters             | Check the command line arguments                                                                                                                                                                                           |
| 10        | Invalid options                        | Invalid combination of command line arguments (for example, you can't run the driver and the control panel or the stand-alone server at the same time)                                                                     |
| 11        | `socat` error                          | `socat` could not be started. Check if it's installed, up-to-date and in path. No solution for operating systems other that Linux, but shouldn't happen: report an issue if so.                                            |

<a class="no-print" href="assets/Circuit-1.jpg"><img align="left" src="assets/Circuit-1.jpg" width="250"></a>

## Hardware

### Autodesk Eagle circuit

In the "Eagle" directory you can find the full circuit project, both schematics and PCB for the standard and Plus editions.
Feel free to modify it to accomplish your necessities: for example, you could add another dew heater controller, or remove the Newton mirror cooler MOSFET.
<br>**Made with Eagle 9.2.2 Premium**

<a class="no-print" href="assets/Motor-holder-1.jpg"><img align="right" src="assets/Motor-holder-1.jpg" width="250"></a>

### Motor holders

I included the mounting brackets I made for the common SkyWatcher
Dual-Speed Crayford focuser (I have a 200mm f/5 Newton OTA).
<br>**AutoCAD 2019** project, STL and IGS exported files ready for 3D printing.

### Focuser motor and drivers

I use a Nema 11 motor that moves the focuser knob using a belt.
If you don't have a heavy focuser, also consider using a Nema 8.
A 20 teeth pulley and a 6mm wide 160mm long belt are enough.
Supported motor drivers via the [StepperDriver](https://github.com/laurb9/StepperDriver) library:

-   Generic 2-pin drivers
-   DRV8825
-   A4988
-   DRV8834

# Developer's guide
See ENGLIGH

## License

OpenFocuser is a project by Marco Cipriani<span class="no-print"> - [GitHub profile](https://github.com/marcocipriani01) - [website](https://marcocipriani01.github.io/)</span>
<br>Licensed under the [Apache License, Version 2.0](LICENSE.md)
<br>Google, The Apache Software Foundation, Java, GitHub and MoonLite trademarks belong to their respective owners. I'm not affiliated with these manufacturers, companies and software foundations.

## Forking and issues

Feel free to submit pull requests, report an issue or suggest new features!
Also, new mounting brackets are welcome!