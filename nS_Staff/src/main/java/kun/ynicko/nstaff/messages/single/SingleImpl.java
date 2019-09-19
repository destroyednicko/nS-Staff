package kun.ynicko.nstaff.messages.single;

import org.bukkit.Bukkit;

import kun.ynicko.nstaff.messages.Message;
import kun.ynicko.nstaff.utils.Style;

public class SingleImpl implements Message {

    @Override
    public void sendMessage(String message) {
        Bukkit.getServer().getOnlinePlayers().forEach(p -> {
            p.sendMessage(Style.c(message));
        });
    }

    @Override
    public void sendMessagePermission(String message, String node) {
        Bukkit.getServer().getOnlinePlayers().forEach(p -> {
            if (p.hasPermission(node)) {
                p.sendMessage(message);
            }
        });
    }
}
