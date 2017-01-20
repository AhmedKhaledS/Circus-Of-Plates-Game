package controllers.shape;

import javafx.application.Platform;
import javafx.scene.Node;
import models.ShapePool;
import models.levels.Level;

import java.util.List;

/**
 * Created by Ahmed Khaled on 19/01/2017.
 */
public class ShapeGenerator<T extends Node> {

    private final long THREAD_SLEEP_TIME = 2000;
    private Level level;
    private final Thread shapeGeneratorThread;
    private boolean isActiveThread;
    private final Runnable shapeGenerator = new Runnable() {
        @Override
        public synchronized void run() {
            while(true) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        List<models.Platform> platforms = level.getPlatforms();
                        for (models.Platform platform : platforms) {
                            ShapeBuilder.getInstance().createShape(platform,
                                    ShapePool.getShape(level));
                        }
                    }
                });
                try {
                    Thread.sleep(THREAD_SLEEP_TIME);
                } catch (final InterruptedException e) {
                    System.out.println("Plate-generator Thread has been interrupted");
                    if (isActiveThread) {
                        continue;
                    } else {
                        break;
                    }
                }
            }
        }
    };

    public ShapeGenerator(Level level) {
        this.level = level;
        shapeGeneratorThread = new Thread(shapeGenerator);
        isActiveThread = true;
        shapeGeneratorThread.setDaemon(true);
        shapeGeneratorThread.start();
    }

    /**
     * Pauses the thread-generator.
     */
    public void pauseGenerator() {
        try {
            shapeGeneratorThread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resumes the thread-generator.
     */
    public void resumeGenerator() {
        shapeGeneratorThread.interrupt();
    }

    public void stopGeneration() {
        isActiveThread = false;
        shapeGeneratorThread.interrupt();
    }
}
