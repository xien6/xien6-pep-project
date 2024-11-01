package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Account;
import Model.Message;
import Service.Service;

import static org.mockito.ArgumentMatchers.contains;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    Service Service;

    public SocialMediaController(){
        this.Service = new Service();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postUserRegistrationHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postNewMessageHandler);
        app.get("/messages", this::getAllMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesFromUserHandler);
        return app;
    }

    private void postUserRegistrationHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account new_user = mapper.readValue(context.body(), Account.class);
        if(new_user.username.length() > 0 && new_user.password.length() >= 4 && Does_User_Exists_Helper(new_user) == null){
            Service.addUserService(new_user);
            context.json(Does_User_Exists_Helper(new_user));
        }else{
            context.status(400);
        }
    }

    private void postLoginHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account login = mapper.readValue(context.body(), Account.class);
        if(Does_User_Exists_Helper(login) == null || !Does_User_Exists_Helper(login).password.equals(login.password)){
            context.status(401);
        }else{
            context.json(Does_User_Exists_Helper(login));
        }
    }

    private void postNewMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        if(message.message_text.length() > 0 && message.message_text.length() < 255 && Does_UserID_Exists_Helper(message)){
            Service.addMessageService(message);
            context.json(Does_Message_Exists_Helper(message));
        }else{
            context.status(400);
        }
    }

    private void getAllMessageHandler(Context context) throws JsonProcessingException{
        context.json(Service.getAllMessageService());
    }

    private void getMessageByIDHandler(Context context) throws JsonProcessingException{
        Message ret = Service.getMessagebyIDService(Integer.parseInt(context.pathParam(("message_id"))));
        if(ret != null){
            context.json(ret);
        }else{
            context.json("");
        }
    }

    private void deleteMessageByIDHandler(Context context) throws JsonProcessingException{
        Message ret = Service.getMessagebyIDService(Integer.parseInt(context.pathParam(("message_id"))));
        if(ret != null){
            Service.deleteMessagebyIDService(Integer.parseInt(context.pathParam(("message_id"))));
            context.json(ret);
        }else{
            context.json("");
        }
    }

    private void updateMessageByIDHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message ret = Service.getMessagebyIDService(Integer.parseInt(context.pathParam(("message_id"))));
        if (ret != null && message.message_text.length() > 0 && message.message_text.length() < 255){
            Service.updateMessagebyIDService(Integer.parseInt(context.pathParam(("message_id"))), message);
            Message test = Service.getMessagebyIDService(Integer.parseInt(context.pathParam(("message_id"))));
            context.json(Service.getMessagebyIDService(Integer.parseInt(context.pathParam(("message_id")))));
        }else{
            context.status(400);
        }
    }

    private void getAllMessagesFromUserHandler(Context context) throws JsonProcessingException{
        int ID = Integer.parseInt(context.pathParam(("account_id")));
        context.json(Service.getAllMessagebyIDService(ID));
    }

//---------------------------------------------------
//Helpers

    private Account Does_User_Exists_Helper(Account user){
        List<Account> check = Service.getAllUserService();
        for (Account element : check){
            if (element.username.equals(user.username)){
                return element;
            }
        }
        return null;
    }

    private Message Does_Message_Exists_Helper(Message message){
        List<Message> check = Service.getAllMessageService();
        for (Message element : check){
            if (element.message_text.equals(message.message_text) && element.posted_by == message.posted_by){
                return element;
            }
        }
        return null;
    }

    private Boolean Does_UserID_Exists_Helper(Message message){
        List<Account> check = Service.getAllUserService();
        for (Account element : check){
            if (element.account_id == message.posted_by){
                return true;
            }
        }
        return false;
    }
}