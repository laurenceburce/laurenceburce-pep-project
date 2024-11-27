package Service;

import java.util.List;

import DAO.MessagesDAO;
import Model.Message;

public class MessagesService {
    private MessagesDAO messagesDAO;
    
    public MessagesService(){
        messagesDAO = new MessagesDAO();
    }

    public Message createMessage(Message message) {
        return messagesDAO.createMessage(message);
    }

    public List<Message> getAllMessages(){
        return messagesDAO.getAllMessages();
    }

    public Message getMessageById(int message_id){
        return messagesDAO.getMessageById(message_id);
    }

    public Message deleteMessageById(int message_id) {
        return messagesDAO.deleteMessageById(message_id);
    }

    public Message updateMessageText(int message_id, String newMessageText) {
        return messagesDAO.updateMessageText(message_id, newMessageText);
    }

    public List<Message> getMessagesByAccountId(int account_id) {
        return messagesDAO.getMessagesByAccountId(account_id);
    }
}
