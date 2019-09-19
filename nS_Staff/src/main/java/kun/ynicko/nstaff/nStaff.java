package kun.ynicko.nstaff;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import kun.ynicko.nstaff.commands.*;
import kun.ynicko.nstaff.database.MySQLManager;
import kun.ynicko.nstaff.listeners.Chat;
import kun.ynicko.nstaff.listeners.Join;
import kun.ynicko.nstaff.listeners.Leave;
import kun.ynicko.nstaff.messages.Message;
import kun.ynicko.nstaff.messages.bungee.BungeeImpl;
import kun.ynicko.nstaff.messages.redis.RedisImpl;
import kun.ynicko.nstaff.messages.single.SingleImpl;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.commands.RedisPipeline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class nStaff extends JavaPlugin {
@SuppressWarnings(value = { "Deprecated" })
	
    @Getter private static nStaff instance;
    @Getter private static Message message;

    @Getter private static MySQLManager mySQLManager;

    @Getter private static String permission = "";
    @Getter private static String serverName = "";
    @Getter private static String nopermission = "";

    @SuppressWarnings("deprecation")
	@Override
    public void onEnable() {
        nStaff.log("Registrando a Config.YAML...");
        this.registerConfig();
        nStaff.log("Config.YAML registrada!");
            nStaff.log("Configurando instâncias...");
            this.setupInstances();
            nStaff.log("Instâncias configuradas!");
            nStaff.log("Configurando mensagens...");
            this.setupMessage();
            nStaff.log("Mensagens configuradas!");
            nStaff.log("Configurando MySQL...");
            this.setupMySQL();
            nStaff.log("MySQL configurado!");
            nStaff.log("Registrando comandos...");
            this.registerCommands();
            nStaff.log("Comandos registrados!");
            nStaff.log("Registrando eventos...");
            this.registerEvents();
            nStaff.log("Eventos registrados!");
            Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                        if (player.hasPermission(nStaff.getPermission())) {
                            if (!nStaff.getMySQLManager().dataExist(player)) {
                                nStaff.getMySQLManager().createData(player);
                            }
                        }
                    });
                }
            }, 0L, 20L);
            nStaff.log("nS_Staff - Carregado com sucesso!");
    }

    @Override
    public void onDisable() {
        instance = null;
        message = null;
        mySQLManager = null;
    }

    private void setupInstances() {
        instance = this;
        mySQLManager = new MySQLManager();

         permission = nStaff.getInstance().getConfig().getString("opcoes.staff_permission");
         serverName = nStaff.getInstance().getConfig().getString("server.nome");
         nopermission = nStaff.getInstance().getConfig().getString("mensagens.no_permission");
    }

    private void registerConfig() {
        this.saveDefaultConfig();
        this.reloadConfig();
    }

    private void setupMessage() {
        String message_type = this.getConfig().getString("server.message_type");

        switch (message_type) {
            case "SINGLE":
                message = new SingleImpl();

                break;
            case "REDIS":
                RedisImpl redis = new RedisImpl();
                ClassLoader prev = Thread.currentThread().getContextClassLoader();
                Thread.currentThread().setContextClassLoader(RedisPipeline.class.getClassLoader());
                redis.jedisPool = new JedisPool(getConfig().getString("server.redis.host"), getConfig().getInt("server.redis.port"));
                Thread.currentThread().setContextClassLoader(prev);

                String[] channels = new String[] {"nStaffMessage", "nStaffPermissionMessage"};

                redis.subscribe(channels);

                message = redis;

                break;
            case "BUNGEE":
                Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
                Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeImpl());
                message = new BungeeImpl();

                break;
            default:
                nStaff.log("Tipo invalido - REDIS, SINGLE ou BUNGEE!");
                nStaff.log("Disativando plugin!");
                Bukkit.getPluginManager().disablePlugin(this);
                break;
        }
    }

    private void setupMySQL() {
        nStaff.getMySQLManager().setupConnection();
    }

    private void registerCommands() {
        getCommand("staffchat").setExecutor(new StaffChatCommand());
        getCommand("receberstaff").setExecutor(new ToggleStaffMessages());
        getCommand("request").setExecutor(new RequestCommand());
        getCommand("report").setExecutor(new ReportCommand());
        getCommand("nstaff").setExecutor(new nStaffCommand());
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new Join(), this);
        pm.registerEvents(new Leave(), this);
        pm.registerEvents(new Chat(), this);
    }


    public static void log(String msg) {
        System.out.println("[nStaff] " + msg);
    }
}
