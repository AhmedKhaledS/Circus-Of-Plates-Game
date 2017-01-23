package models.shapes;

import models.ShapeFactory;
import models.levels.LevelOne;
import models.levels.LevelTwo;
import models.shapes.util.ColorNameAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;

/**
 * Created by Ahmed Khaled on 19/01/2017.
 */
public class PlateShape extends Shape {
    private static final String URL = "src/assets"
            + "/images/plates/";
    private static final String FILE_NAME = "PlateWithoutBase.png";
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
        super();
        setKey(KEY);
        setHorizontalVelocity(HORIZONTAL_VELOCITY);
        setVerticalVelocity(VERTICAL_VELOCITY);
    }
    @Override
    public String getShapeURL() {
        System.out.println(new File(URL).getAbsolutePath());
        String colorString = new ColorNameAdapter().getColorName(color);
        try {
            return new File(URL + colorString + FILE_NAME).toURI().toURL()
                    .toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
