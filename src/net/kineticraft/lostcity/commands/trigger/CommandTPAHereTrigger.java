package net.kineticraft.lostcity.commands.trigger;

import net.kineticraft.lostcity.commands.TriggerCommand;
import net.kineticraft.lostcity.data.KCPlayer;
import net.kineticraft.lostcity.mechanics.Callbacks;
import net.kineticraft.lostcity.mechanics.CurrentEvent;
import net.kineticraft.lostcity.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Trigger to teleport a player to you.
 * Created by Kneesnap on 7/26/2017.
 */
public class CommandTPAHereTrigger extends TriggerCommand {
    public CommandTPAHereTrigger() {
        super("tpahere");
    }

    @Override
    protected void onCommand(Player player, int value) {
        KCPlayer other = KCPlayer.getById(value);
        KCPlayer sender = KCPlayer.getWrapper(player);

        if (!Utils.isVisible(player, other))
            return;

        if(!CurrentEvent.canPlayerTPOut(other.getPlayer())) {
            player.sendMessage(ChatColor.RED + "This player is currently in an event. They cannot teleport out.");
            return;
        }

        Player receiver = other.getPlayer();
        player.sendMessage(ChatColor.GOLD + "Request sent!");
        if (KCPlayer.getWrapper(receiver).isIgnoring(player))
            return;

        receiver.sendMessage(sender.getDisplayName() + ChatColor.GOLD + " has requested you teleport to them.");

        Callbacks.promptConfirm(receiver, () -> {
            if (!player.isOnline()) {
                receiver.sendMessage(ChatColor.RED + "The requestor has gone offline.");
                return;
            }

            receiver.sendMessage(ChatColor.GOLD + "Request accepted.");
            sender.sendMessage(ChatColor.GOLD + receiver.getName() + "has accepted your tpahere request.");
            Utils.teleport(receiver, sender.getDisplayName(), player.getLocation());
        }, () -> receiver.sendMessage(ChatColor.GOLD + "Request denied."), "Accept", "Decline");
    }
}
