package kun.ynicko.nstaff.messages.redis;

import org.bukkit.Bukkit;

import kun.ynicko.nstaff.nStaff;
import kun.ynicko.nstaff.messages.Message;
import kun.ynicko.nstaff.utils.Style;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.regex.Pattern;

public class RedisImpl implements Message {

    public JedisPool jedisPool;

    public Jedis subscriberJedis;

    @Override
    public void sendMessage(String message) {
        this.publish("nStaffMessage", message);
    }

    @Override
    public void sendMessagePermission(String message, String node) {
        this.publish("nStaffPermissionMessage", node + "⏐" + message);
    }

    public void publish(String channel, String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try (Jedis jedis = jedisPool.getResource()) {
                    if (nStaff.getInstance().getConfig().getBoolean("server.redis.auth.enabled")) {
                        jedis.auth(nStaff.getInstance().getConfig().getString("server.redis.auth.password"));
                    }

                    jedis.publish(channel, message);
                }
            }
        }, "nStaffPubThread").start();
    }

    public void subscribe(String... channels) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                subscriberJedis = new Jedis(nStaff.getInstance().getConfig().getString("server.redis.host"), nStaff.getInstance().getConfig().getInt("server.redis.port"));
                if (nStaff.getInstance().getConfig().getBoolean("server.redis.auth.enabled")) {
                    subscriberJedis.auth(nStaff.getInstance().getConfig().getString("server.redis.auth.password"));
                }
                try {
                    subscriberJedis.subscribe(new JedisPubSub() {
                        @Override
                        public void onMessage(String channel, String message) {
                            if (channel.equals("nStaffMessage")) {
                                Bukkit.getServer().getOnlinePlayers().forEach(p -> {
                                    p.sendMessage(Style.c(message));
                                });
                            } else if (channel.equals("nStaffPermissionMessage")) {
                                String[] part = message.split(Pattern.quote("⏐"));
                                String perm = part[0];
                                String msg = part[1];

                                Bukkit.getServer().getOnlinePlayers().forEach(p -> {
                                    if (p.hasPermission(perm) && nStaff.getMySQLManager().hasMessagesEnabled(p)) {
                                        p.sendMessage(Style.c(msg));
                                    }
                                });
                            }
                        }
                    }, channels);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
