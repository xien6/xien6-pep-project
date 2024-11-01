package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    
    public List<Account> getAllUsers(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> Users = new ArrayList<>();
        try{
            String sql = "SELECT * FROM Account;";
            PreparedStatement p_statement = connection.prepareStatement(sql);
            ResultSet rs = p_statement.executeQuery();
            while (rs.next()){
                Account user = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                Users.add(user);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return Users;
    }
    
    public Account new_User(Account new_user){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Account (username, password) VALUES (?,?);";
            PreparedStatement p_statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            p_statement.setString(1, new_user.username);
            p_statement.setString(2, new_user.password);

            p_statement.executeUpdate();
            ResultSet pkeyResultSet = p_statement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_user_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_user_id, new_user.username, new_user.password);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}
