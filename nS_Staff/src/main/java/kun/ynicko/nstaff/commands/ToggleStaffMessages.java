package kun.ynicko.nstaff.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import kun.ynicko.nstaff.nStaff;
import kun.ynicko.nstaff.utils.Style;

public class ToggleStaffMessages implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission(nStaff.getPermission())) {
                if (nStaff.getMySQLManager().hasMessagesEnabled(p)) {
                    nStaff.getMySQLManager().toggleMessages(p, false);
                    p.sendMessage(Style.c(nStaff.getInstance().getConfig().getString("mensagens.toggle_mensagens_off")));
                } else {
                    nStaff.getMySQLManager().toggleMessages(p, true);
                    p.sendMessage(Style.c(nStaff.getInstance().getConfig().getString("mensagens.toggle_mensagens_on")));
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
