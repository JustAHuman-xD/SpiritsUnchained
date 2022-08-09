package me.justahuman.spiritsunchained.utils;

import me.justahuman.spiritsunchained.SpiritsUnchained;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.logging.Logger;

public class LogUtils {

    private static final SpiritsUnchained instance = SpiritsUnchained.getInstance();

    private static final Logger logger = instance.getLogger();

    @ParametersAreNonnullByDefault
    public static void LogInfo(String toLog) {
        logger.info(toLog);
    }
}
