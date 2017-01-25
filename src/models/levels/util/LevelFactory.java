package models.levels.util;

import models.levels.Level;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Moham on 24-Jan-17.
 */
public class LevelFactory {
    private static LevelFactory factoryInstance;
    Map<Integer, Class<? extends Level>> registeredLevels;

    private LevelFactory() {
        registeredLevels = new LinkedHashMap<>();
    }

    public static synchronized LevelFactory getInstance() {
        if (factoryInstance == null) {
            factoryInstance = new LevelFactory();
        }
        return factoryInstance;
    }

    public void registerLevel(int levelNumber, Class<? extends Level>
            levelClass) {
        registeredLevels.put(levelNumber, levelClass);
    }

    public Level createLevel(int levelNumber, double minX, double minY, double
            maxX, double maxY) {
        Class<? extends Level> levelClass =
                registeredLevels.get(levelNumber);
        try {
            Constructor<? extends Level> levelConstructor =
                    levelClass.getConstructor(double.class, double.class,
                            double.class, double.class);
            System.out.println(levelNumber);
            Level level = levelConstructor.newInstance(minX, minY, maxX, maxY);
            System.out.println(level.getNumPlatforms());
            return level;
        } catch (NoSuchMethodException | SecurityException
                | InstantiationException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException e) {
            return null;
        }
    }
    public Collection<Integer> getRegisteredLevels() {
        return registeredLevels.keySet();
    }
}
