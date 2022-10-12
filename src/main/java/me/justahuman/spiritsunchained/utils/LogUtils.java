package me.justahuman.spiritsunchained.utils;

import me.justahuman.spiritsunchained.SpiritsUnchained;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.logging.Logger;

public class LogUtils {

    private final static SpiritsUnchained instance = SpiritsUnchained.getInstance();

    private final static Logger logger = instance.getLogger();

    @ParametersAreNonnullByDefault
    public static void LogInfo(String toLog) {
        logger.info(toLog);
    }
}
