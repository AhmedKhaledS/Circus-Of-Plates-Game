package models.players;

import java.io.File;
import java.net.MalformedURLException;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import com.sun.jmx.remote.internal.ArrayQueue;
import controllers.input.InputType;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import models.GameRules;
import models.Point;
import models.shapes.Shape;

public class Player {

    private static int numOfPlayers = 0;

    private Object avatar; // TODO
    private Stack<Shape> leftStick;
    private Stack<Shape> rightStick;
    private String playerName;
    private Point position;
    private int score;
    private DoubleProperty speed;
    private InputType inputType;
    private String playerUrl;
    private String leftStickUrl;
    private String rightStickUrl;
    private static final String LEFT_STICK_URL = "src/views/sticks/stick.fxml";
    private static final String RIGHT_STICK_URL = "src/views/sticks/stick.fxml";
    public Player() {
        this.playerName = "PlayerController " + numOfPlayers;
        this.score = 0;
        this.position = new Point(0, 0);
        this.leftStick = new Stack<>();
        this.rightStick = new Stack<>();
        this.speed = new SimpleDoubleProperty();
        this.leftStickUrl = LEFT_STICK_URL;
        this.rightStickUrl = RIGHT_STICK_URL;
        this.playerUrl = "";
    }

    public Player(String name) {
        this();
        this.playerName = name;
    }

    public Stack<Shape> getLeftStack() {
        return leftStick;
    }

    public Stack<Shape> getRightStack() {
        return rightStick;
    }

    public boolean pushPlateLeft(Shape shape) {
        return pushPlate(this.leftStick, shape);
    }
    
    public boolean pushPlateRight(Shape shape) {
        return pushPlate(this.rightStick, shape);
    }
    
    private void popPlate(Stack<Shape> stack) throws EmptyStackException {
        stack.pop();
    }
    
    // returns true if got consecutive plates and the score increased
    private boolean pushPlate(Stack<Shape> stack, Shape shape) {
        stack.add(shape);
        if (stack.size() >= GameRules.NUM_OF_CONSECUTIVE_PLATES) {
            Queue<Shape> queue = new LinkedList<>();
            for (int i = 0; i < GameRules.NUM_OF_CONSECUTIVE_PLATES; i++) {
                if (stack.peek().getColor() == shape.getColor()) {
                    queue.add(stack.pop());
                }
            }
            if (queue.size() < GameRules.NUM_OF_CONSECUTIVE_PLATES) {
                while (!queue.isEmpty()) {
                    stack.push(queue.poll());
                }
            } else {
                this.addScore(GameRules.CONSECUTIVE_PLATES_ADDED_SCORE);
                return true;
            }
            return false;
        }
        stack.push(shape);
        return false;
    }
    
    public void addScore(int change) {
        this.score += change;
    }

    public int getNumOfLeftPlates() {
        return this.leftStick.size();
    }
    
    public int getNumOfRightPlates() {
        return this.rightStick.size();
    }

    public void setName(String newName) {
        this.playerName = newName;
    }

    public String getName() {
        return this.playerName;
    }

    public void setPosition(Point newPosition) {
        this.position = newPosition;
    }

    public Point getPosition() {
        return this.position;
    }

    public int getScore() {
        return this.score;
    }

    public double getSpeed() {
        return speed.get();
    }

    public void setSpeed(double speed) {
        this.speed.set(speed);
    }

    public void setInputType(InputType inputType) {
        this.inputType = inputType;
    }

    public InputType getInputType() {
        return inputType;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }
    
    public Object getAvatar() {
        return this.avatar;
    }

    public String getPlayerUrl() {
        return playerUrl;
    }

    public void setPlayerUrl(String playerUrl) {
        this.playerUrl = playerUrl;
    }

    public String getLeftStickUrl() {
        return leftStickUrl;
    }

    public void setLeftStickUrl(String leftStickUrl) {
        this.leftStickUrl = leftStickUrl;
    }

    public String getRightStickUrl() {
        return rightStickUrl;
    }

    public void setRightStickUrl(String rightStickUrl) {
        this.rightStickUrl = rightStickUrl;
    }

    private String urlFromPath(String path) {
        try {
            return new File(path).toURI().toURL()
                    .toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
