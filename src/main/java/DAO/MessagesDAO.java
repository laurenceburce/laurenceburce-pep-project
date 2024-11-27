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
                String sql1 = "SELECT COUNT(*) FROM account WHERE account_id = ?" ;
                PreparedStatement ps1 = connection.prepareStatement(sql1);
                
                ps1.setInt(1, message.getPosted_by());
            
                ResultSet rs1 = ps1.executeQuery();
                if (rs1.next() && rs1.getInt(1) == 0) {
                    return null;
                }

                String sql2 = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
                PreparedStatement ps2 = connection.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);

                ps2.setInt(1, message.getPosted_by());
                ps2.setString(2, message.getMessage_text());
                ps2.setLong(3, message.getTime_posted_epoch());

                ps2.executeUpdate();

                ResultSet rs2 = ps2.getGeneratedKeys();
                if (rs2.next()) {
                    int id = (int) rs2.getLong(1);
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
}
