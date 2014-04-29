package me.joeyandtom.communitycraft.core.player;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class DatabaseConnectException extends Exception {
    private final String message;
    private final Exception cause;
    private final CDatabase database;
}
