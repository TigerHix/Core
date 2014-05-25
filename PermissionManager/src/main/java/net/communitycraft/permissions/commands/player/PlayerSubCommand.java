package net.communitycraft.permissions.commands.player;

import net.cogzmc.core.modular.command.ModuleCommand;
import net.communitycraft.permissions.commands.permissibile.*;

public final class PlayerSubCommand extends ModuleCommand {
    public static final PlayerResolutionDelegate PLAYER_RESOLUTION_DELEGATE = new PlayerResolutionDelegate();
    public PlayerSubCommand() {
        super("player",
                new SetGroupCommand(),
                new AddGroupCommand(),
                new DelGroupCommand(),
                new HasSubCommand<>(PLAYER_RESOLUTION_DELEGATE),
                new UnsetSubCommand<>(PLAYER_RESOLUTION_DELEGATE),
                new SetSubCommand<>(PLAYER_RESOLUTION_DELEGATE),
                new ChatColorSubCommand<>(PLAYER_RESOLUTION_DELEGATE)
        );
    }

    @Override
    protected boolean isUsingSubCommandsOnly() {
        return true;
    }
}
