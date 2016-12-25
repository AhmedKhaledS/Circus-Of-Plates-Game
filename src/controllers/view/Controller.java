package controllers.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    int clownXPos;
    @FXML
    Rectangle clown;
    /**
     * Called to initialize a controllers after its root element has been
     * completely processed.
     * @param location  The location used to resolve relative paths for the root
     *                  object, or <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or
     *                  <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void move() {
        System.out.println("Clown is detected!");
    }
}
