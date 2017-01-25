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
import controllers.input.keyboard.KeyboardEvent;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import models.GameMode;
import models.data.ModelDataHolder;
import models.levels.Level;
import models.levels.LevelOne;
import models.players.Player;
import models.players.PlayerFactory;
import models.players.Stick;
import models.settings.FileConstants;
import models.shapes.util.ShapeLoader;
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


public class GameController implements Initializable, ScoreObserver {

    private static GameController instance;
    private MenuController currentMenu;
    private BooleanProperty newGameStarted;
    private Map<KeyCode, Boolean> keyMap;
    private volatile boolean gamePaused = false;
    private ModelDataHolder modelDataHolder;
    private FileHandler handler;
    private Double currentX;
    private Game currentGame;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private AnchorPane menuPane;

    @FXML
    private AnchorPane mainGame;

    private static Logger logger = LogManager.getLogger(GameController.class);
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

        // Controllers
        currentMenu = Start.getInstance();
        handler = FileHandler.getInstance();
        currentGame = new Game();
        modelDataHolder = new ModelDataHolder();

        newGameStarted = new SimpleBooleanProperty(false);
        initilizeKeyMaps();

        registerLevels();
        registerShapes();
        Joystick.getInstance().registerClassForInputAction(getClass(),
                instance);
    }

    private void initilizeKeyMaps() {
        keyMap = new HashMap<>();
        keyMap.put(KeyCode.A, false);
        keyMap.put(KeyCode.D, false);
        keyMap.put(KeyCode.LEFT, false);
        keyMap.put(KeyCode.RIGHT, false);
    }

    public void registerLevels() {
        try {
            Class.forName("models.levels.LevelOne");
            Class.forName("models.levels.LevelTwo");
            Class.forName("models.levels.LevelThree");
            Class.forName("models.levels.LevelFour");
            Class.forName("models.levels.LevelFive");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void registerShapes() {
        ShapeLoader.loadShapes(new File(FileConstants.CLASS_LOADING_PATH));
    }

    public void setCurrentMenu(MenuController currentMenu) {
        this.currentMenu = currentMenu;
    }

    public MenuController getCurrentMenu() {
        return currentMenu;
    }

    public AnchorPane getMainGame() {
        return mainGame;
    }

    public AnchorPane getRootPane() {
        return rootPane;
    }

    public ModelDataHolder getModelDataHolder() {
        return modelDataHolder;
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
            // KEYBOARD_PRIMARY
//            case LEFT:
//                if (mainGame.isVisible()) {
//                    String playerName = PlayerFactory.getFactory()
//                            .getPlayerNameWithController(InputType
// .KEYBOARD_PRIMARY);
//                    if (playerName != null) {
//                        playersController.moveLeft(playerName);
//                    }
//                }
//                break;
//            case RIGHT:
//                if (mainGame.isVisible()) {
//                    String playerName = PlayerFactory.getFactory()
//                            .getPlayerNameWithController(InputType
// .KEYBOARD_PRIMARY);
//                    if (playerName != null) {
//                        playersController.moveRight(playerName);
//                    }
//                }
//                break;
            case ESCAPE:
                if (newGameStarted.get()) {
                    if (currentMenu.isVisible()) {
                        continueGame();
                    } else {
                        pauseGame();
                    }
                }
                break;
//            // keyboard_two
//            case A:
//                if (mainGame.isVisible()) {
//                    String playerName = PlayerFactory.getFactory()
//                            .getPlayerNameWithController(InputType
// .KEYBOARD_SECONDARY);
//                    if (playerName != null) {
//                        playersController.moveLeft(playerName);
//                    }
//                }
//                break;
//            case D:
//                if (mainGame.isVisible()) {
//                    String playerName = PlayerFactory.getFactory()
//                            .getPlayerNameWithController(InputType
// .KEYBOARD_SECONDARY);
//                    if (playerName != null) {
//                        playersController.moveRight(playerName);
//                    }
//                }
//                break;
//            default:
//                break;
        }
    }

//    @InputAction(ACTION_TYPE = ActionType.BEGIN, INPUT_TYPE = InputType
// .KEYBOARD_PRIMARY)
//    public void primaryKeyboardHandler(KeyboardEvent keyboardEvent) {
//        Platform.runLater(() -> {
//            switch (keyboardEvent.getKeyboardCode()) {
//                case LEFT:
//                    playersController.moveLeft(PlayerFactory
//                            .getFactory().getPlayerNameWithController
// (InputType.KEYBOARD_PRIMARY));
//                    break;
//                case RIGHT:
//                    playersController.moveRight(PlayerFactory
//                            .getFactory().getPlayerNameWithController
// (InputType.KEYBOARD_PRIMARY));
//            }
//        });
//    }

    @InputAction(ACTION_TYPE = ActionType.BEGIN, INPUT_TYPE = InputType
            .KEYBOARD_SECONDARY)
    public void secondaryKeyboardHandler(KeyboardEvent keyboardEvent) {
        if (!mainGame.isVisible()) {
            return;
        }
        Platform.runLater(() -> {
            switch (keyboardEvent.getKeyboardCode()) {
                case A:
                    currentGame.getPlayersController().moveLeft(PlayerFactory
                            .getFactory().getPlayerNameWithController
                                    (InputType.KEYBOARD_SECONDARY));
                    break;
                case D:
                    currentGame.getPlayersController().moveRight(PlayerFactory
                            .getFactory().getPlayerNameWithController
                                    (InputType.KEYBOARD_SECONDARY));
                    break;
                case LEFT:
                    currentGame.getPlayersController().moveLeft(PlayerFactory
                            .getFactory().getPlayerNameWithController
                                    (InputType.KEYBOARD_PRIMARY));
                    break;
                case RIGHT:
                    currentGame.getPlayersController().moveRight(PlayerFactory
                            .getFactory().getPlayerNameWithController
                                    (InputType.KEYBOARD_PRIMARY));
            }
        });
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
        this.handler.write(modelDataHolder, "." + File.separator +
                        FileConstants.SAVE_PATH,
                fileName);
    }


    public double getStageWidth() {
        return mainGame.getWidth();
    }

    public void startGame(GameMode gameMode) {
        ((Start) Start.getInstance()).activeDisabledButtons();

        GameController.getInstance().getMainGame().setVisible(true);
        AudioPlayer.backgroundMediaPlayer.play();
        newGameStarted.set(true);

        resetGame();

        // ===========================
        //TODO: Replace Level with level from level chooser with default
        // value set to 1
        Level level = new
                LevelOne(rootPane.getLayoutX(),
                rootPane.getLayoutY(), rootPane.getLayoutX()
                + rootPane.getWidth(), rootPane.getLayoutY()
                + rootPane.getHeight());
        modelDataHolder.setActiveLevel(level);
        // ===========================

        switch (gameMode) {
            case NORMAL:
                currentGame.startNormalGame(level);
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
    }

    public synchronized void startKeyboardListener() {
        Thread thread;
        thread = new Thread(() -> {
            while (!gamePaused) {
                Platform.runLater(this::updatePlayers);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    logger.info("keyboard listener thread interrupted");
                    if (gamePaused) {
                        break;
                    } else {
                        continue;
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void startNewLoadGame(ModelDataHolder modelDataHolder) {
        resetGame();
        try {
            for (Player player : modelDataHolder.getPlayers()) {
                System.out.printf("%s has %d Shapes on his Right Stack\n",
                        player.getName(), player.getRightStack().size());
                System.out.printf("%s has %d Shapes on his Left Stack\n",
                        player.getName(), player.getLeftStack().size());
                currentGame.createPlayer(player);
                GameBoard.getInstance().reset();
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
                    new ShapeController<>(shapeView, shapePlatformPair
                            .getShape(), shapePlatformPair.getPlatform())
                            .startMoving();
                    break;
                case ON_THE_STACK:
                    System.out.println("ERRROROOROROROR");//TODO: LOG.
                    break;
                default:
                    break;
            }
        }
        this.modelDataHolder = modelDataHolder;
        currentMenu.setMenuVisible(false);
        currentGame.startNormalGame(modelDataHolder.getActiveLevel());
        continueGame();
        newGameStarted.set(true);
        ((Start) Start.getInstance()).activeDisabledButtons();
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

    @Override
    public void update(int score, String playerName, Stick stick) {
        currentGame.getPlayersController().removeShapes(playerName, stick);
        currentGame.updateScore(score, playerName, stick);
    }
}