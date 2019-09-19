package kun.ynicko.nstaff.database;

import lombok.Getter;

import org.bukkit.entity.Player;

import kun.ynicko.nstaff.nStaff;

import java.sql.*;

public class MySQLManager {

    @Getter private Connection connection;

    String username = nStaff.getInstance().getConfig().getString("server.mysql.username");
    String password = nStaff.getInstance().getConfig().getString("server.mysql.password");
    String database = nStaff.getInstance().getConfig().getString("server.mysql.database");
    String host = nStaff.getInstance().getConfig().getString("server.mysql.host");
    int port = nStaff.getInstance().getConfig().getInt("server.mysql.port");

    public void setupConnection() {
        synchronized (this) {
            try {
                if (connection != null && !connection.isClosed()) {
                    return;
                }

                Class.forName("com.mysql.jdbc.Driver");

                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useUnicode=yes&characterEncoding=UTF-8&failOverReadOnly=false&maxReconnects=10&autoReconnect=true", username, password);

                nStaff.log("Conectado ao MySQL! DB: " + database);

                getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS staffdata(id INT NOT NULL AUTO_INCREMENT, uuid VARCHAR(255), mensagens_toggled INT(1), staffchat_toggled INT(1), PRIMARY KEY (id))").executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hasMessagesEnabled(Player p) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM staffdata WHERE uuid=? AND mensagens_toggled=?");
            statement.setString(1, p.getUniqueId().toString());
            statement.setBoolean(2, true);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean dataExist(Player p) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM staffdata WHERE uuid=?");
            statement.setString(1, p.getUniqueId().toString());

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createData(Player p) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("INSERT INTO staffdata (uuid, mensagens_toggled, staffchat_toggled) VALUES (?, ?, ?)");
            statement.setString(1, p.getUniqueId().toString());
            statement.setInt(2, 1);
            statement.setInt(3, 0);

            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean hasStaffChatToggled(Player p) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM staffdata WHERE uuid=? AND staffchat_toggled=?");
            statement.setString(1, p.getUniqueId().toString());
            statement.setInt(2, 1);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void toggleStaffChat(Player p, boolean toggled) {
        try {
            PreparedStatement statement = null;
            if (toggled) {
                statement = getConnection().prepareStatement("UPDATE staffdata SET staffchat_toggled=1 WHERE uuid=?");
                statement.setString(1, p.getUniqueId().toString());
            } else {
                statement = getConnection().prepareStatement("UPDATE staffdata SET staffchat_toggled=0 WHERE uuid=?");
                statement.setString(1, p.getUniqueId().toString());
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void toggleMessages(Player p, boolean toggled) {
        try {
            PreparedStatement statement = null;
            if (toggled) {
                statement = getConnection().prepareStatement("UPDATE staffdata SET mensagens_toggled=1 WHERE uuid=?");
                statement.setString(1, p.getUniqueId().toString());
            } else {
                statement = getConnection().prepareStatement("UPDATE staffdata SET mensagens_toggled=0 WHERE uuid=?");
                statement.setString(1, p.getUniqueId().toString());
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
