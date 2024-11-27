package DAO;

import java.sql.*;

import Model.Account;
import Util.ConnectionUtil;

public class AccountsDAO {
    public Account createAccount(Account account){
        if(!account.getUsername().isBlank() && account.getPassword().length() >= 4){
            Connection connection = ConnectionUtil.getConnection();
            try {
                String sql = "INSERT INTO account (username, password) VALUES (?, ?);" ;
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    
                ps.setString(1, account.getUsername());
                ps.setString(2, account.getPassword());
                
                ps.executeUpdate();
    
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()){
                    int id = (int) rs.getLong(1);
                    return new Account(id, account.getUsername(), account.getPassword());
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public Account loginAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int accountId = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
    
                return new Account(accountId, username, password);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
