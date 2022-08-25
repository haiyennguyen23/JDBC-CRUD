package com.example.bai12.dao;

import com.example.bai12.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements  IUserDAO {
    private String jdbcURL= "jdbc:mysql://localhost:3306/demo?useSSL=false";
    private String jdbcUsername= "root";
    private String jdbcPassword="123456";
    private static final String INSERT_USERS_SQL="INSERT INTO users (name, email, country) VALUES (?, ?, ?);";
    private static final String SELECT_USER_BY_ID="select id,name,email,country from users where id =?";
    private static final String SELECT_ALL_USERS="select * from users";
    private static final String DELETE_USERS_SQL="delete from users where id=?;";
    private static final  String UPDATE_USERS_SQL="update users set name=?,email=?,country=? where id=?;";
    public UserDAO() {
    }
    protected Connection getConnection(){
    Connection connection =null;
    try {
        Class.forName("com.mysql.jdbc.Driver");
        connection= DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    } catch (SQLException e) {
       e.printStackTrace();
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
    return connection;
}
public void insertUser(User user) throws SQLException{
    System.out.println(INSERT_USERS_SQL);
    try(Connection connection =getConnection(); PreparedStatement preparedStatement =connection.prepareStatement(INSERT_USERS_SQL)){
        preparedStatement.setString(1,user.getName());
        preparedStatement.setString(2,user.getEmail());
        preparedStatement.setString(3,user.getCountry());
        System.out.println(preparedStatement);
        preparedStatement.executeUpdate();
    }catch(SQLException e){
        printSQLException(e);
    }

}
public User selectUser(int id){
        User user =null;
        try(Connection connection =getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);){
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String name =resultSet.getString("name");
                String email =resultSet.getString("email");
                String country=resultSet.getString("country");
                user=new User(id,name,email,country);
            }

        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
}

    @Override
    public List<User> selectAllUser() {
        List<User>users = new ArrayList<>();
        try(Connection connection = getConnection() ;
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);){
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                users.add(new User(id,name,email,country));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }

public  boolean deleteUser(int id) throws SQLException{
        boolean rowDeleted;
        try(Connection connection=getConnection();PreparedStatement statement=connection.prepareStatement(DELETE_USERS_SQL);){
            statement.setInt(1,id);
            rowDeleted =statement.executeUpdate()>0;
        }
    return rowDeleted;
}
public boolean updateUser(User user)throws SQLException{
        boolean rowUpdate;
        try(Connection connection=getConnection();PreparedStatement statement= connection.prepareStatement(UPDATE_USERS_SQL);){
            statement.setString(1,user.getName());
            statement.setString(2,user.getEmail());
            statement.setString(3,user.getCountry());
            statement.setInt(4,user.getId());
            rowUpdate= statement.executeUpdate()>0;
        }
        return rowUpdate;
}

    private void printSQLException(SQLException ex) {
        for(Throwable e: ex){
          if (e instanceof SQLException){
              e.printStackTrace(System.err);
              System.err.println("SQLState: " + ((SQLException) e).getSQLState());
              System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
              System.err.println("Message: " + e.getMessage());
              Throwable t =e.getCause();
                while (t !=null){
                    System.out.println("Cause:"+t);
                    t=t.getCause();
                }
          }
        }
    }
}
