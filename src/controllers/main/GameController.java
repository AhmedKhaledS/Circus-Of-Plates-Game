package controllers.main;

import controllers.input.ActionType;
import controllers.input.Input;
import controllers.input.InputAction;
import controllers.input.InputType;
import controllers.input.joystick.Joystick;
import controllers.input.joystick.JoystickCode;
import controllers.input.joystick.JoystickEvent;
import controllers.input.joystick.JoystickType;
import controllers.menus.MenuController;
import controllers.menus.Start;
import controllers.player.PlayerController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import models.GameMode;
import models.players.PlayerFactory;


public class GameController implements Initializable {
    private static GameController instance;
    private MenuController currentMenu;
    private PlayerController playerController;
    // TODO: 1/19/17 plate Controller

    private Input joystickInput;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private AnchorPane menuPane;

    @FXML
    private AnchorPane mainGame;

    @FXML
    private Rectangle plate1;

    @FXML
    private Rectangle plate2;

    @FXML
    private Rectangle rightRod;

    @FXML
    private Rectangle leftRod;


    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }


    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Controllers
        currentMenu = Start.getInstance();
        playerController = new PlayerController();
        try {
            URL url = new File("src/views/clown.fxml").toURI().toURL();
            PlayerFactory.getFactory().registerPlayer("player1").setInputType(InputType.JOYSTICK_ONE);
            PlayerFactory.getFactory().registerPlayer("player2").setInputType(InputType.JOYSTICK_TWO);

            Node node1 = playerController.createPlayer("player1", url);
            Node node2 = playerController.createPlayer("player2", url);
            node2.setLayoutX(node2.getLayoutX() + 500);
            mainGame.getChildren().add(node1);
            mainGame.getChildren().add(node2);

        } catch (IOException e) {
            e.printStackTrace();
        }

        instance  = this;

        joystickInput = Joystick.getInstance();
        joystickInput.registerClassForInputAction(getClass(), instance);
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


    @FXML
    public void keyHandler(KeyEvent event) {
        switch (event.getCode()) {
            // KEYBOARD_ONE
            case LEFT:
                if (mainGame.isVisible()) {
                    String playerName = PlayerFactory.getFactory()
                            .getPlayerNameWithController(InputType.KEYBOARD_ONE);
                    if (playerName != null) {
                        playerController.moveLeft(playerName);
                    }
                }
                break;
            case RIGHT:
                if (mainGame.isVisible()) {
                    String playerName = PlayerFactory.getFactory()
                            .getPlayerNameWithController(InputType.KEYBOARD_ONE);
                    if (playerName != null) {
                        playerController.moveRight(playerName);
                    }
                }
                break;
            case ESCAPE:
                Platform.runLater(() -> {
                    if (currentMenu.isVisible()) {
                        currentMenu.setMenuVisible(false);
                        mainGame.requestFocus();
                        mainGame.setVisible(true);
                    } else {
                        currentMenu = Start.getInstance();
                        currentMenu.setMenuVisible(true);
                        currentMenu.requestFocus(0);
                        mainGame.setVisible(false);
                    }
                });
                break;
            // keyboard_two
            case A:
                if (mainGame.isVisible()) {
                    String playerName = PlayerFactory.getFactory()
                            .getPlayerNameWithController(InputType.KEYBOARD_TWO);
                    if (playerName != null) {
                        playerController.moveLeft(playerName);
                    }
                }
                break;
            case D:
                if (mainGame.isVisible()) {
                    String playerName = PlayerFactory.getFactory()
                            .getPlayerNameWithController(InputType.KEYBOARD_TWO);
                    if (playerName != null) {
                        playerController.moveRight(playerName);
                    }
                }
                break;
            default:
                break;
        }
    }


    @InputAction(ACTION_TYPE = ActionType.BEGIN, INPUT_TYPE = InputType.JOYSTICK)
    public void performJoystickAction(JoystickEvent event) {
        String playerName2 = PlayerFactory.getFactory().getPlayerNameWithController(InputType.JOYSTICK_TWO);
        String playerName1 = PlayerFactory.getFactory().getPlayerNameWithController(InputType.JOYSTICK_ONE);

        Platform.runLater(() -> {
            if (event.getJoystickType() == JoystickType.PRIMARY) {
                if (event.getJoystickCode() == JoystickCode.LEFT) {
                    if (playerName1 != null) {
                        playerController.moveLeft(playerName1);
                    }
                } else if (event.getJoystickCode() == JoystickCode.RIGHT) {
                    if (playerName1 != null) {
                        playerController.moveRight(playerName1);
                    }
                }
            } else {
                if (event.getJoystickCode() == JoystickCode.LEFT) {
                    if (playerName2 != null) {
                        playerController.moveLeft(playerName2);
                    }
                } else if (event.getJoystickCode() == JoystickCode.RIGHT) {
                    if (playerName2 != null) {
                        playerController.moveRight(playerName2);
                    }
                }
            }
        });
    }

    public double getStageWidth() {
        return mainGame.getWidth();
    }


    public void startGame(GameMode gameMode) {
        switch (gameMode) {
            case NORMAL:
                startNormalGame();
                break;
            case TIMEATTACK:
                break;
            case LEVEL:
                break;
            case SANDBOX:
                break;
        }
    }

    private void startNormalGame() {
    }

    // TODO: Mouse handler
    private Double currentX;
    @FXML
    public void mouseHandler(MouseEvent event) {
        if (currentX == null) {
            currentX = event.getX();
        } else {
            if (currentX > event.getX()) {
                // rect.setLayoutX(Math.max(rect.getLayoutX() - CLOWNSPEED, -350 + rect.getWidth() / 2.0));
            } else {
                // rect.setLayoutX(Math.min(rect.getLayoutX() + CLOWNSPEED, 350 - rect.getWidth() / 2.0));
            }
        }
    }
}

