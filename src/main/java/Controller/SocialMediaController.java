package Controller;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountsService;
import Service.MessagesService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountsService accountsService;
    MessagesService messagesService;

    public SocialMediaController(){
        this.accountsService = new AccountsService();
        this.messagesService = new MessagesService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postCreateAccountHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postCreateMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::patchMessageTextHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountIdHandler);
        return app;
    }

    private void postCreateAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account createdAccount = accountsService.createAccount(account);
        if(createdAccount==null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(createdAccount));
        }
    }

    private void postLoginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loginAccount = accountsService.loginAccount(account);
        if(loginAccount==null){
            ctx.status(401);
        }else{
            ctx.json(mapper.writeValueAsString(loginAccount));
        }
    }
    
    private void postCreateMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message createMessage = messagesService.createMessage(message);
        if(createMessage==null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(createMessage));
        }
    }

    private void getAllMessagesHandler(Context ctx){
        ctx.json(messagesService.getAllMessages());
    }

    private void getMessageByIdHandler(Context ctx){
        Message message = messagesService.getMessageById(Integer.parseInt(ctx.pathParam("message_id")));
        if(message == null){
            ctx.result("");
        }else{
            ctx.json(message);
        }
    }

    private void deleteMessageByIdHandler(Context ctx) {
        Message deletedMessage = messagesService.deleteMessageById(Integer.parseInt(ctx.pathParam("message_id")));
        if (deletedMessage != null) {
            ctx.json(deletedMessage);
        } else {
            ctx.result("");
        }
    }

    private void patchMessageTextHandler(Context ctx) {
        Message updatedMessage = messagesService.updateMessageText(
            Integer.parseInt(ctx.pathParam("message_id")),
            ctx.bodyAsClass(Map.class).get("message_text").toString());
        if (updatedMessage != null) {
            ctx.json(updatedMessage);
        } else {
            ctx.status(400);
        }
    }

    private void getMessagesByAccountIdHandler(Context ctx) {
        List<Message> messages = messagesService.getMessagesByAccountId(Integer.parseInt(ctx.pathParam("account_id")));
        ctx.json(messages);
    }
}