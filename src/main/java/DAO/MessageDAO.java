package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> Messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM Message;";
            PreparedStatement p_statement = connection.prepareStatement(sql);
            ResultSet rs = p_statement.executeQuery();
            while (rs.next()){
                Message add_message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                Messages.add(add_message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return Messages;
    }

    public Message getMessagebyID(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM Message WHERE message_id = ?;";
            PreparedStatement p_statement = connection.prepareStatement(sql);

            p_statement.setInt(1, message_id);

            ResultSet rs = p_statement.executeQuery();
            while(rs.next()){
                Message ret_message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                return ret_message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void deleteMessagebyID(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "DELETE FROM Message WHERE message_id = ?;";
            PreparedStatement p_statement = connection.prepareStatement(sql);

            p_statement.setInt(1, message_id);

            p_statement.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void updateMessagebyID(int message_id, Message new_message){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement p_statement = connection.prepareStatement(sql);

            p_statement.setInt(2, message_id);
            p_statement.setString(1, new_message.message_text);

            p_statement.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public List<Message> getAllMessagesFromID(int user_id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> Messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM Message WHERE posted_by = ?;";
            PreparedStatement p_statement = connection.prepareStatement(sql);
            p_statement.setInt(1, user_id);
            ResultSet rs = p_statement.executeQuery();
            while (rs.next()){
                Message add_message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                Messages.add(add_message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return Messages;
    }

    public void new_Message(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?);";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, message.posted_by);
            ps.setString(2, message.message_text);
            ps.setLong(3, message.time_posted_epoch);

            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}
