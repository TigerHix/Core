package net.communitycraft.core.player;

import lombok.NonNull;
import net.communitycraft.core.asset.Asset;

import java.util.*;

/**
 * This represents both an online and offline player and can be obtained via a CPlayerManager implementation.
 *
 * This provides access to a player's settings, assets, and various metadata, along with a method to resolve the online player.
 *
 * This class should be most polymorphic friendly type for players, if you can get by using this to represent a player anywhere, you must.
 */
public interface COfflinePlayer extends CPermissible {
    /**
     * Gets the known usernames. All usernames on login are logged and placed into storage. This will return all current usernames.
     * @return All usernames in a {@link java.util.List}.
     */
    List<String> getKnownUsernames();

    /**
     * Gets the last known username, this is best for commands involving offline usage of a player, or for confirmation.
     * @return The last known username
     */
    String getLastKnownUsername();

    /**
     * Gets the unique identifier for the player that is generated by mojang and managed by us to identify the player.
     * @return A {@link java.util.UUID} object for the Minecraft UUID.
     */
    UUID getUniqueIdentifier();

    /**
     * Gets all the known IP addresses in a {@link java.util.List} of {@link java.lang.String}s.
     * @return Known IP Addresses.
     */
    List<String> getKnownIPAddresses();

    /**
     * Gets the first time the player was recorded as online in a java {@link java.util.Date} object.
     * @return The first time the player joined the server.
     */
    Date getFirstTimeOnline();

    /**
     * Gets the most recent time the player disconnected from the server in a java {@link java.util.Date} object.
     * @return The most recent time the player disconnected from the server.
     */
    Date getLastTimeOnline();

    /**
     * Gets the amount of time the player has spent online.
     *
     * This is recorded in milliseconds, and can be converted into seconds by dividing by {@code 1000}.
     *
     * @return The amount of time the player has spent online.
     */
    Long getMillisecondsOnline();


    /**
     * Gets the value of a setting by key with a default value and type.
     * @param key The key that is used to uniquely identify the setting entry.
     * @param type The type of the entry, this will force the entry to be cast to this type and return the default value if a {@link java.lang.ClassCastException} is caught.
     * @param defaultValue The default value of this setting in the case it does not exist, or a {@link java.lang.ClassCastException} is caught.
     * @param <T> The type parameter for the setting.
     * @return The value of the setting as specified by the above parameters.
     */
    <T> T getSettingValue(@NonNull String key, @NonNull Class<T> type, T defaultValue);

    /**
     * Gets the value of a setting by key. Returns null if it cannot be found.
     * @param key The key that is used to uniquely identify the setting entry.
     * @param type The type of the entry, this will force the entry to be cast to this type and return null if it cannot be.
     * @param <T> The type parameter for the setting.
     * @return The value of the setting as specified by the above parameters.
     */
    <T> T getSettingValue(@NonNull String key, @NonNull Class<T> type);

    /**
     * Stores a setting value.
     * @param key The key to store the setting as.
     * @param value The value to set the key to.
     */
    void storeSettingValue(@NonNull String key, Object value);

    /**
     * Completely removes a setting from the user.
     * @param key The key to remove the value from.
     */
    void removeSettingValue(@NonNull String key);

    /**
     * Checks if the value at the key is present. If it is not, this will return {@code false}.
     * @param key The key to find a value for.
     * @return A {@code true} or {@code false} value based on the presence of {@code key}.
     */
    boolean containsSetting(@NonNull String key);

    /**
     * Grants a player an asset.
     * @param asset The asset to give the player.
     */
    void giveAsset(@NonNull Asset asset);

    /**
     * Removes an asset from a player.
     * @param asset The asset to remove from the player.
     */
    void removeAsset(@NonNull Asset asset);

    /**
     * Returns a list of all assets owned by a player.
     * @return All the assets owned by a player
     */
    Collection<Asset> getAssets();

    /**
     * If the player is online, this will return the instance of {@link net.communitycraft.core.player.CPlayer}, whereas if they are not online this will return a {@code null}.
     * @return A {@link net.communitycraft.core.player.CPlayer} object or {@code null}.
     */
    CPlayer getPlayer();

    /**
     * Updates data about a player with the current values from the database.
     * @throws DatabaseConnectException When we cannot connect to the database to make an update.
     */
    void updateFromDatabase() throws DatabaseConnectException;

    /**
     * Saves the current values (settings, assets, metadata) into the database.
     * @throws DatabaseConnectException When we cannot save the data into the database.
     */
    void saveIntoDatabase() throws DatabaseConnectException;

    /* Permissions */
    //Inherits Permissible

    /**
     * Adds a player to a group, and will reload permissions implicitly.
     * @param group The group you want to add the player to. This will not let you add a player to the same group twice.
     */
    void addToGroup(CGroup group);

    /**
     * Removes a player from a group, and will reload permissions implicitly.
     * @param group The group you want to remove the player from. This will not let you remove a player from the same group twice.
     */
    void removeFromGroup(CGroup group);

    /**
     * This will return a copy of the list of groups the player is in currently. You can modify this, but it will not modify the actual groups the player is in.
     * @return A {@link java.util.List} of {@link net.communitycraft.core.player.CGroup} instances representing the groups that the player is in.
     */
    List<CGroup> getGroups();

    /**
     * This will return all permissions that the player has. This cannot be modified, as it is a copy of the original {@link java.util.Map} that holds permissions.
     * @return The {@link java.util.Map} of permission that the player has defined.
     */
    Map<String, Boolean> getAllPermissions();
}
