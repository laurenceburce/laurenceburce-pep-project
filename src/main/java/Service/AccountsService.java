package Service;

import DAO.AccountsDAO;
import Model.Account;

public class AccountsService {
    private AccountsDAO accountsDAO;
    
    public AccountsService(){
        accountsDAO = new AccountsDAO();
    }

    public AccountsService(AccountsDAO accountsDAO){
        this.accountsDAO = accountsDAO;
    }

    public Account createAccount(Account account) {
        return accountsDAO.createAccount(account);
    }

    public Account loginAccount(Account account){
        return accountsDAO.loginAccount(account);
    }
}
