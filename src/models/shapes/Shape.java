package models.shapes;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import models.Point;
import models.states.Color;
import models.states.ShapeState;


public abstract class Shape {

    protected Color color;
    protected Point position;
    protected Point initialPosition;
    protected double xVelocity;
    protected double yVelocity;
    protected volatile ShapeState state;
    protected DoubleProperty width;
    protected DoubleProperty height;
    protected String key;

    public Shape() {
        this.height = new SimpleDoubleProperty();
        this.width = new SimpleDoubleProperty();
        this.state = ShapeState.MOVING_HORIZONTALLY;
        this.position = new Point();
        this.initialPosition = new Point();
    }

    public String getIdentifier() {
        return key;
    }

    protected void setKey(String key) {
        this.key = key;
    }

    public ShapeState getState() {
        return state;
    }

    public void setState(ShapeState newState) {
        state = newState;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color newColor) {
        color = newColor;
    }

    public DoubleProperty getWidth() {
        return width;
    }

    public void setWidth(double newWidth) {
        width.setValue(newWidth);
    }

    public DoubleProperty getHeight() {
        return height;
    }

    public void setHeight(double newHeight) {
        height.setValue(newHeight);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public double getHorizontalVelocity() {
        return xVelocity;
    }

    public void setHorizontalVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    public double getVerticalVelocity() {
        return yVelocity;
    }

    public void setVerticalVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }

    public Point getInitialPosition() {
        return initialPosition;
    }

    public void setInitialPosition(Point initialPosition) {
        this.initialPosition = initialPosition;
    }

    public abstract String getShapeURL();

    public void translate(double x, double y) {
        position.xProperty().add(x);
        position.yProperty().add(y);
    }

    public String getKey() {
        return key;
    }
}
