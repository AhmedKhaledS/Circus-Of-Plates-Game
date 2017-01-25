package controllers.main;

import controllers.AudioPlayer;
import controllers.board.GameBoard;
import controllers.input.ActionType;
import controllers.input.InputAction;
import controllers.input.InputType;
import controllers.input.joystick.Joystick;
import controllers.input.joystick.JoystickCode;
import controllers.input.joystick.JoystickEvent;
import controllers.input.joystick.JoystickType;
import controllers.menus.MenuController;
import controllers.menus.Start;
import controllers.player.ScoreObserver;
import controllers.shape.ShapeBuilder;
import controllers.shape.ShapeController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import models.GameMode;
import models.data.ModelDataHolder;
import models.players.Player;
import models.players.PlayerFactory;
import models.players.Stick;
import models.settings.FileConstants;
import models.shapes.util.ShapePlatformPair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.file.FileHandler;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class GameController implements Initializable, ScoreObserver {

    private static GameController instance;
    private MenuController currentMenu;
    private BooleanProperty newGameStarted;
    private Map<KeyCode, Boolean> keyMap;
    private volatile boolean gamePaused = false;
    private FileHandler handler;
    private Double currentX;
    private Game currentGame;
    private int currentLevel;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private AnchorPane menuPane;

    @FXML
    private AnchorPane mainGame;

    @FXML
    private AnchorPane winPane;

    private static Logger logger = LogManager.getLogger(GameController.class);

    /**
     * Gets the instance of the GameController using Singleton.
     * @return returns the instance of GameController class.
     */
    public synchronized static GameController getInstance() {
        return instance;
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root
     *                  object, or <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or
     *                  <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;

        currentLevel = 1;
        currentMenu = Start.getInstance();
        handler = FileHandler.getInstance();
        currentGame = new Game();
        currentGame.setLevel(currentLevel);
        newGameStarted = new SimpleBooleanProperty(false);
        initializeKeyMaps();
        Joystick.getInstance().registerClassForInputAction(getClass(),
                instance);
    }

    private void initializeKeyMaps() {
        keyMap = new HashMap<>();
        keyMap.put(KeyCode.A, false);
        keyMap.put(KeyCode.D, false);
        keyMap.put(KeyCode.LEFT, false);
        keyMap.put(KeyCode.RIGHT, false);
    }

    /**
     * Sets the current menu that is loaded on screen.
     * @param currentMenu {@link MenuController} the curren menu that is on
     * screen.
     */
    public void setCurrentMenu(MenuController currentMenu) {
        this.currentMenu = currentMenu;
    }

    /**
     * Gets the main game Anchor pane.
     * @return {@link AnchorPane} returns the anchor pane containing the game.
     */
    public AnchorPane getMainGame() {
        return mainGame;
    }

    /**
     * Gets the root pane of the game Anchor pane.
     * @return {@link AnchorPane} returns the anchor pane of thhe game.
     */
    public AnchorPane getRootPane() {
        return rootPane;
    }


    private synchronized void updatePlayers() {
        if (keyMap.get(KeyCode.A)) {
            currentGame.getPlayersController().moveLeft(PlayerFactory.getFactory()
                    .getPlayerNameWithController(InputType.KEYBOARD_SECONDARY));
        }
        if (keyMap.get(KeyCode.D)) {
            currentGame.getPlayersController().moveRight(PlayerFactory.getFactory()
                    .getPlayerNameWithController(InputType.KEYBOARD_SECONDARY));
        }
        if (keyMap.get(KeyCode.LEFT)) {
            currentGame.getPlayersController().moveLeft(PlayerFactory.getFactory()
                    .getPlayerNameWithController(InputType.KEYBOARD_PRIMARY));
        }
        if (keyMap.get(KeyCode.RIGHT)) {
            currentGame.getPlayersController().moveRight(PlayerFactory.getFactory()
                    .getPlayerNameWithController(InputType.KEYBOARD_PRIMARY));
        }
    }

    @FXML
    public synchronized void keyHandlerReleased(KeyEvent event) {
        keyMap.put(event.getCode(), false);
    }

    @FXML
    public synchronized void keyHandler(KeyEvent event) {
        keyMap.put(event.getCode(), true);
        switch (event.getCode()) {
            case ESCAPE:
                winPane.setVisible(false);
                AudioPlayer.winMediaPlayer.stop();
                if (newGameStarted.get()) {
                    if (currentMenu.isVisible()) {
                        continueGame();
                        logger.info("Game is continued.");
                    } else {
                        pauseGame();
                        logger.info("Game is paused.");
                    }
                }
                break;
        }
    }


    @InputAction(ACTION_TYPE = ActionType.BEGIN, INPUT_TYPE = InputType
            .JOYSTICK)
    public void performJoystickAction(JoystickEvent event) {
        String playerName2 = PlayerFactory.getFactory()
                .getPlayerNameWithController(InputType.JOYSTICK_SECONDARY);
        String playerName1 = PlayerFactory.getFactory()
                .getPlayerNameWithController(InputType.JOYSTICK_PRIMARY);

        Platform.runLater(() -> {
            if (event.getJoystickType() == JoystickType.PRIMARY) {
                if (event.getJoystickCode() == JoystickCode.LEFT) {
                    if (playerName1 != null) {
                        currentGame.getPlayersController().moveLeft(playerName1);
                    }
                } else if (event.getJoystickCode() == JoystickCode.RIGHT) {
                    if (playerName1 != null) {
                        currentGame.getPlayersController().moveRight(playerName1);
                    }
                }
            } else {
                if (event.getJoystickCode() == JoystickCode.LEFT) {
                    if (playerName2 != null) {
                        currentGame.getPlayersController().moveLeft(playerName2);
                    }
                } else if (event.getJoystickCode() == JoystickCode.RIGHT) {
                    if (playerName2 != null) {
                        currentGame.getPlayersController().moveRight(playerName2);
                    }
                }
            }
        });
    }

    @FXML
    public void onMousePressedHandler(MouseEvent event) {
        currentX = event.getSceneX();
    }

    @FXML
    public void onMouseDraggedHandler(MouseEvent event) {
        if (currentX > event.getSceneX()) {
            currentGame.getPlayersController().moveLeft(PlayerFactory
                    .getFactory().getPlayerNameWithController
                            (InputType.MOUSE));
        } else {
            currentGame.getPlayersController().moveLeft(PlayerFactory
                    .getFactory().getPlayerNameWithController
                            (InputType.MOUSE));
        }
    }

    public void saveGame(String name) {
        System.err.println(name);
        DateFormat dateFormat = new SimpleDateFormat("dd_MM_yy HH,mm,ss");
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        String fileName = name + " - " + currentDate;
        ModelDataHolder modelData = new ModelDataHolder();
        modelData.setActiveLevel(currentGame.getCurrentLevel());
        //TODO: add player
//        modelData.addPlayer(cu)
        for (ShapeController<? extends Node> shapeController : currentGame
                .getShapeControllers()) {
            modelData.addShape(new ShapePlatformPair(shapeController
                    .getShapeModel(), shapeController.getPlatform()));
        }
        for (String player : currentGame.getPlayersController()
                .getPlayersNames()) {
            modelData.addPlayer(currentGame.getPlayersController()
                    .getPlayerModel(player));
        }
        modelData.setGeneratorCounter(currentGame.getShapeGeneratorCounter());
        this.handler.write(modelData, "." + File.separator +
                        FileConstants.SAVE_PATH,
                fileName);
        logger.info("Game is saved successfully.");
    }


    public double getStageWidth() {
        return mainGame.getWidth();
    }

    public void startGame(GameMode gameMode) {
        ((Start) Start.getInstance()).activeDisabledButtons(true);
        GameController.getInstance().getMainGame().setVisible(true);

        resetGame();
        newGameStarted.set(true);

        logger.info("Game is launched successfully.");
        switch (gameMode) {
            case NORMAL:
                currentGame.startNormalGame();
                break;
            case TIME_ATTACK:
                break;
            case LEVEL:
                break;
            case SANDBOX:
                break;
        }
    }

    public void resetGame() {
        currentGame.destroy();
        currentGame = new Game();
        currentGame.setLevel(currentLevel);
    }

    void startKeyboardListener() {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(() -> {
            while (!gamePaused) {
                Platform.runLater(() -> updatePlayers());
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    logger.info("keyboard listener thread interrupted");
                    if (gamePaused) {
                        break;
                    }
                }
            }
        });
        exec.shutdown();
    }

    public void startNewLoadGame(ModelDataHolder modelDataHolder) {
        resetGame();
        try {
            GameBoard.getInstance().reset();
            for (Player player : modelDataHolder.getPlayers()) {
                System.out.printf("%s has %d Shapes on his Right Stack\n",
                        player.getName(), player.getRightStack().size());
                System.out.printf("%s has %d Shapes on his Left Stack\n",
                        player.getName(), player.getLeftStack().size());
                currentGame.createPlayer(player);
                GameBoard.getInstance().addPlayerPanel(player.getName());
                GameBoard.getInstance().updateScore(player.getScore(), player.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (ShapePlatformPair shapePlatformPair : modelDataHolder.getShapes
                ()) {
            switch (shapePlatformPair.getShape().getState()) {
                case MOVING_HORIZONTALLY:
                case FALLING:
                    Node shapeView = ShapeBuilder.getInstance().build
                            (shapePlatformPair.getShape());
                    mainGame.getChildren().add(shapeView);
                    ShapeController<? extends Node> shapeController = new
                            ShapeController<>
                    (shapeView, shapePlatformPair
                            .getShape(), shapePlatformPair.getPlatform());
                    shapeController.startMoving();
                    currentGame.getShapeControllers().add(shapeController);
                    break;
                case ON_THE_STACK:
                    logger.error("A Shape That is on the Stack Should not be"
                            + " saved with moving shapes");
                    break;
                default:
                    break;
            }
        }
        currentMenu.setMenuVisible(false);
        currentGame.setCurrentLevel(modelDataHolder.getActiveLevel());
        currentGame.startNormalGame(modelDataHolder.getGeneratorCounter());
        newGameStarted.set(true);
        ((Start) Start.getInstance()).activeDisabledButtons(true);
        System.out.println(modelDataHolder.getGeneratorCounter());
    }

    public synchronized boolean checkIntersection(
            ShapeController<? extends Node> shapeController) {
        if (currentGame.getPlayersController().checkIntersection(shapeController)) {
            shapeController.shapeFellOnTheStack();
            return true;
        }
        return false;
    }

    public void pauseGame() {
        gamePaused = true;
        currentMenu = Start.getInstance();
        currentMenu.setMenuVisible(true);
        currentMenu.requestFocus(0);
        mainGame.setVisible(false);

        currentGame.pause();
        AudioPlayer.backgroundMediaPlayer.pause();
    }


    public void continueGame() {
        gamePaused = false;
        currentMenu.setMenuVisible(false);
        mainGame.requestFocus();
        mainGame.setVisible(true);
        currentGame.resume();
        startKeyboardListener();
        AudioPlayer.backgroundMediaPlayer.play();
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGameLevel(int level) {
        currentLevel = level;
        currentGame.setLevel(level);
    }

    public synchronized void playerLost(String playerName) {
        resetGame();
        AudioPlayer.backgroundMediaPlayer.stop();
        AudioPlayer.winMediaPlayer.play();
        AudioPlayer.winMediaPlayer.seek(Duration.ZERO);

        ((Start) Start.getInstance()).activeDisabledButtons(false);

        int maxScore = 0;
        String winner = "";

        Collection<String> playerNames = currentGame.getPlayersController().getPlayersNames();
        for (String name : playerNames) {
            Player playerModel = currentGame.getPlayersController().getPlayerModel(name);
            if (name.equals(playerName)) {
                if (maxScore < playerModel.getScore() / 2) {
                    maxScore = playerModel.getScore() / 2;
                    winner = name;
                }
            } else {
                if (maxScore < playerModel.getScore()) {
                    maxScore = playerModel.getScore();
                    winner = name;
                }
            }
        }
        winPane.setVisible(true);
        ((Label) winPane.getChildren().get(0)).setText("Player: "
                + winner + " has won with score " + maxScore);
        logger.info("Player: " + winner + " has won with score " + maxScore);
    }

    @Override
    public void update(int score, String playerName, Stick stick) {
        currentGame.getPlayersController().removeShapes(playerName, stick);
        currentGame.updateScore(score, playerName, stick);
    }
}