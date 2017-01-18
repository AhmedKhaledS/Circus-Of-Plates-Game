package controllers.menus;

import controllers.input.joystick.Joystick;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadGame extends MenuController {
    private VBox menu;
    private static LoadGame instance;

    public LoadGame() {
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
        menu = loadGameMenu;
        Joystick.getInstance().registerClassForInputAction(getClass(), instance);

    }

    @FXML
    private VBox loadGameMenu; // // TODO: 12/29/16 package access

    @Override
    void handle(String id) {
        menu.setVisible(false);
        switch (id) {
            case "back":
                Start.getInstance().getMenu().setVisible(true);
                updateCurrentMenu(Start.getInstance());
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
