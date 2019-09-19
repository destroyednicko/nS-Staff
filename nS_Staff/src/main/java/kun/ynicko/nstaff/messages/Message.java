package kun.ynicko.nstaff.messages;

public interface Message {

    void sendMessage(String message);
    void sendMessagePermission(String message, String node);

}
