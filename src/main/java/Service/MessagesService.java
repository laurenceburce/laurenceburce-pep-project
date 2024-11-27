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
}
