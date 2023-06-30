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
2. Open a terminal and type `cd <project-root>`, where `<project-root>` is the root of this
   repository;
3. Run the server with:
    ```bash
    java -jar AM13_Server.jar
    ```
4. In another terminal, run the client, specific to the operating system it being used, with:
    - Windows:
        ```bash
        java -jar AM13_Client_Windows.jar
        ```
    - macOS:
       ```bash
       java -jar AM13_Client_macOS.jar
        ```

When launching the client, you can specify the following options (after `AM13_Client.jar`):

| Option           | Possible value(s) | Default value | Description                       |
|------------------|:-----------------:|:-------------:|-----------------------------------|
| `-h` or `--help` |         -         |       -       | Prints the help message and exits |
| `-v` or `--view` |  `cli` or `gui`   |     `gui`     | Selects the view to use           |

Options that require an argument must be followed by a space or an equal sign and the argument itself.
The default values are set in the `settings.properties`
in [src/main/resources/it/polimi/ingsw](src/main/resources/it/polimi/ingsw) folder.

## Notable Features

### Multiple Languages

After connecting to the server, in the login screen under the 'Settings' tab, the user can select the language they want
to use.\
The available languages are English, Italian, French, Spanish, Catalan, Japanese, Bergamasco, Siciliano and Pugliese.
The last three being the spoken Italian dialects of our group.

### Highly Customizable

Our vision, since the beginning of the project, was to create a highly modular and customizable game, making it easy to
add new features and change existing ones.\
In the folder [src/main/resources/it/polimi/ingsw](src/main/resources/it/polimi/ingsw) there are all the settings files
and configurations:

- `settings.properties` and `settings.json` contain the default values for the `Bookshelf` class, such as the number of
  rows and columns, making it possible to (maybe one day…) play with a custom-built bookshelf;
- `usable_cells.json` contains the usable `Board` cells for each player configuration;
- `common_goals.json` and `personal_goals.json` contain the goals for the game, leaving open the possibility to add new
  ones.
  In the `CommonGoal` creation, it is possible to set different parameters when creating a specific `Layout`, as the
  foundation to a more customizable experience for the player.
