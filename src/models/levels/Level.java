package models.levels;

import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import models.Platform;
import models.shapes.Shape;

public interface Level {
    
    // TODO not an object :3
    public Object getBackground();
    public void setBackground(Image background);
    

    public double getPlatesSpeed();
    public double getPlayerSpeed();

    public List<String> getSupportedShapes();
    public boolean isSupportedShape(String shape);

    public List<models.states.Color> getSupportedColors();
    public boolean isSupportedColor(models.states.Color color);

    public int getNumPlatforms();
    public List<Platform> getPlatforms();

    public void setNumberOfPlatforms(int size);
}
