package controllers.menus;

import controllers.input.joystick.Joystick;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Start extends MenuController {
    private VBox menu;
    private static Start instance;

    public Start() {
        super();
        instance = this;
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
        menu = startMenu;
        Joystick.getInstance().registerClassForInputAction(MenuController.class, instance);
    }

    @FXML
    private VBox startMenu;

    @Override
    void handle(String id) {
        startMenu.setVisible(false);
        System.out.println("id = " + id);
        switch (id) {
            case "newGame":
                GameMode.getInstance().getMenu().setVisible(true);
                updateCurrentMenu(GameMode.getInstance().getMenu());
                break;
            case "loadGame":
                LoadGame.getInstance().getMenu().setVisible(true);
                updateCurrentMenu(LoadGame.getInstance().getMenu());
                break;
            case "options":
                Options.getInstance().getMenu().setVisible(true);
                updateCurrentMenu(Options.getInstance().getMenu());
                break;
            case "help":
                Help.getInstance().getMenu().setVisible(true);
                updateCurrentMenu(Help.getInstance().getMenu());
                break;
            case "exit":
                System.out.println("should exit");
                System.exit(0);
                break;
            default:
                break;
        }
    }

    @Override
    public VBox getMenu() {
        return menu;
    }

    public static MenuController getInstance() {
        return instance;
    }
}
