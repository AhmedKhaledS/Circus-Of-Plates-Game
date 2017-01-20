package models.shapes;

import models.ShapeFactory;
import models.levels.LevelOne;
import models.levels.LevelTwo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.print.DocFlavor;

/**
 * Created by Ahmed Khaled on 19/01/2017.
 */
public class PlateShape extends Shape {
    private static final String URL = "INSERT_URL_IN_HERE";
    private static final double HORIZONTAL_VELOCITY = 1.0;
    private static final double VERTICAL_VELOCITY = 1.2;
    public static final String KEY = PlateShape.class.getName();
    private static Logger logger = LogManager.getLogger();
    static {
        ShapeFactory.registerShape(KEY, PlateShape.class);
        LevelOne.registerShape(KEY);
        LevelTwo.registerShape(KEY);
        logger.debug("Class " + KEY + " Initialized");
    }
    public PlateShape() {
        setKey(KEY);
        setHorizontalVelocity(HORIZONTAL_VELOCITY);
        setVerticalVelocity(VERTICAL_VELOCITY);
    }
    @Override
    public String getShapeURL() {
        return URL;
    }

}
