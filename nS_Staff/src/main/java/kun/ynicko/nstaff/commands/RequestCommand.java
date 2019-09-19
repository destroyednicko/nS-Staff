package kun.ynicko.nstaff.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import kun.ynicko.nstaff.nStaff;
import kun.ynicko.nstaff.utils.Style;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RequestCommand implements CommandExecutor {

    List<UUID> cannotRequest = new ArrayList<UUID>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cannotRequest.contains(p.getUniqueId())) {
                p.sendMessage(Style.c(nStaff.getInstance().getConfig().getString("mensagens.cooldown")));
                return true;
            }

            String message = "";

            for(int i = 0; i < args.length; i++){
                String arg = args[i] + " ";
                message = message + arg;
            }

            String request = Style.c(nStaff.getInstance().getConfig().getString("mensagens.request_message")
            .replace("%player%", p.getName())
            .replace("%server%", nStaff.getServerName())
            .replace("%message%", message));

            nStaff.getMessage().sendMessagePermission(request, nStaff.getPermission());

            p.sendMessage(Style.c(nStaff.getInstance().getConfig().getString("mensagens.request_sent")));

            cannotRequest.add(p.getUniqueId());

            Bukkit.getScheduler().scheduleAsyncDelayedTask(nStaff.getInstance(), new Runnable() {
                @Override
                public void run() {
                    cannotRequest.remove(p.getUniqueId());
                }
            }, nStaff.getInstance().getConfig().getInt("settings.delay_between_commands"));
        } else {
            nStaff.log("Somente console.");
        }
        return false;
    }
}
