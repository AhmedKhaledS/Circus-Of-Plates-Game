package controllers.shape;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.ShapePool;
import models.levels.Level;
import models.states.Orientation;
import views.test.PlateController;

/**
 * Created by Ahmed Khaled on 19/01/2017.
 */
public class ShapeGenerator<T extends Node> {
    private Level level;
    private final Thread shapeGeneratorThread;
    private final Runnable shapeGenerator = new Runnable() {
        @Override
        public synchronized void run() {
            while(true) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        ShapeCreator.createPlate(Orientation.LEFT);
                        ShapeCreator.createPlate(Orientation.RIGHT);
                    }
                });
                try {
                    Thread.sleep(2000);
                } catch (final InterruptedException e) {
                    System.out.println("Plate-generator Thread has been interrupted");
                    return;
                }
            }
        }
    };
    public ShapeGenerator(Level level) {
        this.level = level;
        shapeGeneratorThread = new Thread(shapeGenerator);
        shapeGeneratorThread.setDaemon(true);
        shapeGeneratorThread.start();
    }





}
