package com.example.bai12.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class UserDAO {
protected Connection getConnection(){
    Connection connection =null;
    Class.forName("com.mysql.jdbc.Driver");
    Connection= DriverManager.getConnection()
}
}
