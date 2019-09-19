package kun.ynicko.nstaff.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import kun.ynicko.nstaff.nStaff;
import kun.ynicko.nstaff.utils.Style;

public class Leave implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();

        if (p.hasPermission(nStaff.getPermission())) {
            String message = Style.c(nStaff.getInstance().getConfig().getString("mensagens.staff_leave")
                    .replace("%server%", nStaff.getServerName())
                    .replace("%player%", p.getName()));

            nStaff.getMessage().sendMessagePermission(message, nStaff.getPermission());
        }
    }
}
