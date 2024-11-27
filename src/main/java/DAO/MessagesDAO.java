package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessagesDAO {
    public Message createMessage(Message message){
        if(!message.getMessage_text().isBlank() && message.getMessage_text().length() <= 255){
            Connection connection = ConnectionUtil.getConnection();
            try {
                String selectSql = "SELECT COUNT(*) FROM account WHERE account_id = ?" ;
                PreparedStatement selectPs = connection.prepareStatement(selectSql);
                
                selectPs.setInt(1, message.getPosted_by());
            
                ResultSet selectRs = selectPs.executeQuery();
                if (selectRs.next() && selectRs.getInt(1) == 0) {
                    return null;
                }

                String insertSql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
                PreparedStatement insertPs = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);

                insertPs.setInt(1, message.getPosted_by());
                insertPs.setString(2, message.getMessage_text());
                insertPs.setLong(3, message.getTime_posted_epoch());

                insertPs.executeUpdate();

                ResultSet insertRs = insertPs.getGeneratedKeys();
                if (insertRs.next()) {
                    int id = (int) insertRs.getLong(1);
                    return new Message(id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessageById(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                return new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessageById(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement selectPs = connection.prepareStatement(selectSql);
            selectPs.setInt(1, message_id);
            ResultSet selectRs = selectPs.executeQuery();
            if (selectRs.next()) {
                Message message = new Message(
                    selectRs.getInt("message_id"),
                    selectRs.getInt("posted_by"),
                    selectRs.getString("message_text"),
                    selectRs.getLong("time_posted_epoch")
                );
    
                String deleteSql = "DELETE FROM message WHERE message_id = ?";
                PreparedStatement deletePs = connection.prepareStatement(deleteSql);
                deletePs.setInt(1, message_id);
                deletePs.executeUpdate();
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message updateMessageText(int message_id, String newMessageText) {
        Connection connection = ConnectionUtil.getConnection();
    
        try {
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement selectPs = connection.prepareStatement(selectSql);
            selectPs.setInt(1, message_id);
            ResultSet selectRs = selectPs.executeQuery();
            if (selectRs.next()) {
                if (newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
                    return null; 
                }
    
                String updateSql = "UPDATE message SET message_text = ? WHERE message_id = ?";
                PreparedStatement updatePs = connection.prepareStatement(updateSql);
                updatePs.setString(1, newMessageText);
                updatePs.setInt(2, message_id);
                updatePs.executeUpdate();
                return new Message(
                    selectRs.getInt("message_id"),
                    selectRs.getInt("posted_by"),
                    newMessageText,
                    selectRs.getLong("time_posted_epoch")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getMessagesByAccountId(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
