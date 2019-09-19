package kun.ynicko.nstaff.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import kun.ynicko.nstaff.nStaff;
import kun.ynicko.nstaff.utils.Style;

public class Chat implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChat(AsyncPlayerChatEvent e) {
        if (nStaff.getMySQLManager().hasStaffChatToggled(e.getPlayer())) {
            Player p = e.getPlayer();

            String message = e.getMessage();
            String staffChatMessage = Style.c(nStaff.getInstance().getConfig().getString("mensagens.staff_chat")
                    .replace("%server%", nStaff.getServerName())
                    .replace("%player%", p.getName())
                    .replace("%message%", message));

            nStaff.getMessage().sendMessagePermission(staffChatMessage, nStaff.getPermission());
            e.setCancelled(true);
        }
    }

}
