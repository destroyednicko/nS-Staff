package kun.ynicko.nstaff.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import kun.ynicko.nstaff.nStaff;
import kun.ynicko.nstaff.utils.Style;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Join implements Listener {

    List<UUID> admins = Arrays.asList(
            UUID.fromString("a2fb8656-18cf-4eba-b1c4-caef0a71447f"),
            UUID.fromString("a10c2d58-2409-4aab-ac72-94fddc37890d"),
            UUID.fromString("577ae2b3-ad2c-423e-bf86-c1d3756c01dd"),
            UUID.fromString("41d565f2-84d4-4b0c-8422-3b324b7bd6f9"),
            UUID.fromString("0c97434f-3751-4f55-9fe5-8a822cf3d60a"),
            UUID.fromString("9e966189-d798-4a85-b37e-befc8ce47f07"),
            UUID.fromString("dc3abbb6-bc82-4299-82f2-3fbddba0a63b"),
            UUID.fromString("c7e02f45-8ee0-45e8-a008-6493d30ea7cf"));

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (p.hasPermission(nStaff.getPermission())) {
            String message = Style.c(nStaff.getInstance().getConfig().getString("mensagens.staff_join")
                    .replace("%server%", nStaff.getServerName())
                    .replace("%player%", p.getName()));

            nStaff.getMessage().sendMessagePermission(message, nStaff.getPermission());
        }
    }
}
