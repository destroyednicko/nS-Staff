package kun.ynicko.nstaff.messages.bungee;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import kun.ynicko.nstaff.nStaff;
import kun.ynicko.nstaff.messages.Message;
import kun.ynicko.nstaff.utils.Style;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class BungeeImpl implements Message, PluginMessageListener {

    @Override
    public void sendMessage(String message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("nStaffMessage");
        out.writeUTF(message);

        List<Player> onlinePlayers = new ArrayList<Player>();

        Bukkit.getServer().getOnlinePlayers().forEach(player -> { onlinePlayers.add(player); });

        Player p = Iterables.getFirst(onlinePlayers, null);

        if (p == null) {
            return;
        }

        p.sendPluginMessage(nStaff.getInstance(), "BungeeCord", out.toByteArray());
    }

    @Override
    public void sendMessagePermission(String message, String node) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("nStaffPermissionMessage");
        out.writeUTF(node + "⏐" + message);

        List<Player> onlinePlayers = new ArrayList<Player>();

        Bukkit.getServer().getOnlinePlayers().forEach(player -> { onlinePlayers.add(player); });

        Player p = Iterables.getFirst(onlinePlayers, null);

        if (p == null) {
            return;
        }

        p.sendPluginMessage(nStaff.getInstance(), "BungeeCord", out.toByteArray());
    }


    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String sub = in.readUTF();

        if (sub.equals("nStaffMessage")) {
            String msg = in.readUTF();

            Bukkit.getServer().getOnlinePlayers().forEach(p -> {
                p.sendMessage(Style.c(msg));
            });
        }

        if (sub.equals("nStaffPermissionMessage")) {
            String full = in.readUTF();

            String[] part = full.split(Pattern.quote("⏐"));
            String perm = part[0];
            String msg = part[1];

            Bukkit.getServer().getOnlinePlayers().forEach(p -> {
                if (p.hasPermission(perm) && nStaff.getMySQLManager().hasMessagesEnabled(p)) {
                    p.sendMessage(Style.c(msg));
                }
            });
        }
    }
}
