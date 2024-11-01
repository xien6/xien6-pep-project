package Service;

import Model.Account;
import Model.Message;
import DAO.DAO;
import DAO.MessageDAO;

import java.util.List;

public class Service {
    DAO UserDAO;
    MessageDAO MessageDAO;

    public Service(){
        UserDAO = new DAO();
        MessageDAO = new MessageDAO();
    }

    public Service(DAO UserDAO, MessageDAO MessageDAO){
        this.UserDAO = UserDAO;
        this.MessageDAO = MessageDAO;
    }

    public List<Account> getAllUserService() {
        return UserDAO.getAllUsers();
    }

    public Account addUserService(Account user){
        return UserDAO.new_User(user);
    }

//----------------------------------------------------------------------------------

    public List<Message> getAllMessageService() {
        return MessageDAO.getAllMessages();
    }

    public void addMessageService(Message message){
        MessageDAO.new_Message(message);
    }

    public Message getMessagebyIDService(int message_id){
        return MessageDAO.getMessagebyID(message_id);
    }

    public List<Message> getAllMessagebyIDService(int user_id){
        return MessageDAO.getAllMessagesFromID(user_id);
    }

    public void updateMessagebyIDService(int message_id, Message message){
        MessageDAO.updateMessagebyID(message_id, message);
    }

    public void deleteMessagebyIDService(int message_id){
        MessageDAO.deleteMessagebyID(message_id);
    }
}
