# PJV Game

This project is a small JavaFX action game developed as a semester assignment for the PJV course. The player explores a grid-based board, moves with keyboard controls, fights enemies, collects items, and tries to survive until all enemies are defeated.

## What the game is about

The game is a top-down survival / dungeon arena where the player:

- moves around the map using WASD
- shoots projectiles toward enemies
- collects coins, keys, and healing potions
- opens chests when carrying a key
- upgrades stats during the game flow
- wins by clearing all enemies and losing if the player dies

The gameplay is built around a custom board model and JSON-based level configuration. The main logic lives in the `Model` class, the UI and rendering are handled in `View`, and the game loop / state transitions are coordinated in `Controller`.

## Main project structure

- `src/main/java/cz/cvut/fel/pjv/game/Controller.java` - starts the JavaFX app, manages input and game loop
- `src/main/java/cz/cvut/fel/pjv/game/model/` - gameplay logic and entities such as Player, Enemy, Item, Board, Projectile
- `src/main/java/cz/cvut/fel/pjv/game/view/View.java` - drawing and UI screens
- `src/main/resources/` - sprites and configuration files like `init.json` and `easyinit.json`
- `src/test/java/` - unit tests for movement, combat, item collection and chest interaction

## Controls

- `W` / `A` / `S` / `D` - move the player
- `Esc` - pause or resume the game
- Start the game from the main menu button

## Running the application

### Prerequisites

- Java 17 or newer
- Maven

### From the project root

1. Open a terminal in the project directory.
2. Run:

   ```bash
   mvn clean test
   ```

3. Start the game:

   ```bash
   mvn exec:java -Dexec.mainClass=cz.cvut.fel.pjv.game.Controller
   ```

If you want to test a shorter or easier map, the project contains an `easyinit.json` configuration file, and the code comments point to it as a quick way to play through the game faster.

## Notes

- The project is configured as a Maven JavaFX application.
- Player progress is saved to `src/main/resources/player.txt` when the run ends successfully.
- The project includes generated Javadoc in the `javadoc/` folder for reference.
