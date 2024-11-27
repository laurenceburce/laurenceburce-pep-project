package Controller;

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
        return app;
    }

    private void postCreateAccountHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account createdAccount = accountsService.createAccount(account);
        if(createdAccount==null){
            context.status(400);
        }else{
            context.json(mapper.writeValueAsString(createdAccount));
        }
    }

    private void postLoginHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account loginAccount = accountsService.loginAccount(account);
        if(loginAccount==null){
            context.status(401);
        }else{
            context.json(mapper.writeValueAsString(loginAccount));
        }
    }
    
    private void postCreateMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message createMessage = messagesService.createMessage(message);
        if(createMessage==null){
            context.status(400);
        }else{
            context.json(mapper.writeValueAsString(createMessage));
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
}