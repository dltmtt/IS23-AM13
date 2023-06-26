# IS23-AM13

Members of the group:

- Corbo Simone (simone.corbo@mail.polimi.it)
- De Gennaro Valeria (valeria.degennaro@mail.polimi.it)
- Delton Matteo (matteo.delton@mail.polimi.it)
- Di Raimondo Metallo Beatrice (beatrice.diraimondo@mail.polimi.it)

## Implemented features

Legend:

- 🟢: implemented
- 🟡: work in progress
- 🔴: not implemented

| Feature        | Status |
|----------------|--------|
| Complete rules | 🟢     |
| TCP            | 🟢     |
| RMI            | 🟢     |
| CLI            | 🟢     |
| GUI            | 🟢     |
| Resilience     | 🟢     |
| Persistence    | 🟢     |
| Multiple games | 🔴     |
| Chat           | 🔴     |

## Documentation

The UML diagrams and our peer reviews can be found in the [deliverables](deliverables) folder.

### How to play

To play My Shelfie, follow these steps:

1. Make sure to have the `java` executable in your `PATH`;
2. Open a terminal and type `cd <project-root>`, where `<project-root>` is the root of this repository;
3. Run the server with:
    ```bash
    java -jar AM13_Server.jar
    ```
4. In another terminal, run the client with:
    ```bash
    java -jar AM13_Client.jar
    ```

When launching the client, you can specify the following options (after `AM13_Client.jar`):

| Option               |      Possible value(s)       |        Default value         | Description                        |
|----------------------|:----------------------------:|:----------------------------:|------------------------------------|
| `-h` or `--help`     |              -               |              -               | Prints the help message and exits  |
| `-v` or `--view`     |        `cli` or `gui`        |            `gui`             | Selects the view to use            |
| `-p` or `--protocol` |        `tcp` or `rmi`        |            `tcp`             | Selects the protocol to use        |
| `-n` or `--hostname` | an IP address or `localhost` |        `localhost`           | Selects the hostname of the server |

Options that require an argument must be followed by a space or an equal sign and the argument itself.
The default values are set in the `settings.properties` in [src/main/resources/it/polimi/ingsw](src/main/resources/it/polimi/ingsw) folder.