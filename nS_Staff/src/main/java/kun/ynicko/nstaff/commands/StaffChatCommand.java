package kun.ynicko.nstaff.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import kun.ynicko.nstaff.nStaff;
import kun.ynicko.nstaff.utils.Style;

public class StaffChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission(nStaff.getPermission())) {
                if (args.length == 0) {
                    if (nStaff.getMySQLManager().hasStaffChatToggled(p)) {
                        nStaff.getMySQLManager().toggleStaffChat(p, false);
                        p.sendMessage(Style.c(nStaff.getInstance().getConfig().getString("mensagens.staffchat_toggle_off")));
                    } else {
                        nStaff.getMySQLManager().toggleStaffChat(p, true);
                        p.sendMessage(Style.c(nStaff.getInstance().getConfig().getString("mensagens.staffchat_toggle_on")));
                    }
                } else {
                    String message = "";

                    for(int i = 0; i < args.length; i++){
                        String arg = args[i] + " ";
                        message = message + arg;
                    }

                    String staffChatMessage = Style.c(nStaff.getInstance().getConfig().getString("mensagens.staff_chat")
                    .replace("%server%", nStaff.getServerName())
                    .replace("%player%", p.getName())
                    .replace("%message%", message));

                    nStaff.getMessage().sendMessagePermission(staffChatMessage, nStaff.getPermission());
                }
            } else {
                p.sendMessage(Style.c(nStaff.getNopermission()));
            }
        } else{
            nStaff.log("Somente console.");
        }
        return false;
    }
}
