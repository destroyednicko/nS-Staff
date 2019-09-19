package kun.ynicko.nstaff.commands;

import com.google.common.base.Joiner;

import kun.ynicko.nstaff.nStaff;
import kun.ynicko.nstaff.utils.Style;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReportCommand implements CommandExecutor {

    List<UUID> cannotReport = new ArrayList<UUID>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (!(args.length >= 2)) {
                p.sendMessage(Style.c("&cSixtase: /report <player> <motivo>"));

                return true;
            }

            if (cannotReport.contains(p.getUniqueId())) {
                p.sendMessage(Style.c(nStaff.getInstance().getConfig().getString("mensagens.cooldown")));
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                p.sendMessage(Style.c("&c" + args[0] + " não foi encontrado!"));
                return true;
            }

            String message = Joiner.on(' ').join(args).replaceFirst(args[0] + " ", "");
            String reportMessage = Style.c(nStaff.getInstance().getConfig().getString("mensagens.report_message")
            .replace("%player%", p.getName())
            .replace("%server%", nStaff.getServerName())
            .replace("%reported%", target.getName())
            .replace("%message%", message));

            nStaff.getMessage().sendMessagePermission(reportMessage, nStaff.getPermission());

            p.sendMessage(Style.c(nStaff.getInstance().getConfig().getString("mensagens.report_sent")));

            cannotReport.add(p.getUniqueId());

            Bukkit.getScheduler().scheduleAsyncDelayedTask(nStaff.getInstance(), new Runnable() {
                @Override
                public void run() {
                    cannotReport.remove(p.getUniqueId());
                }
            }, nStaff.getInstance().getConfig().getInt("settings.delay_between_commands"));

        } else{
            nStaff.log("Somente console.");
        }
        return false;
    }
}
