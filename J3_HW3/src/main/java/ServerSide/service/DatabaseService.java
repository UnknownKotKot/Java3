package ServerSide.service;
import java.sql.*;

public class DatabaseService {

    private Connection connection;
    private Statement statement;

    public void startDB(){
        try{
        connect();
        createDB();
        createDefaultUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            disconnect();
        }
        System.out.println("table created successfully");
    }

    public void dropDB(){
        try{
            connect();
            drop();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        } finally {
            disconnect();
        }
        System.out.println("DB dropped successfully");
    }

    private void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:chatDB.db");
        statement = connection.createStatement();
    }

    private void disconnect(){
        try {
            if(statement!=null) statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if(connection!=null) connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void createDB() throws SQLException {
        statement.executeUpdate("create table if not exists users (id integer not null primary key autoincrement, login text not null, password text not null, nickname text not null);");
        statement.executeUpdate("create unique index user_name on users (login, nickname);");
    }

    private void createDefaultUsers() throws SQLException {
        statement.executeUpdate("insert into users (login, password, nickname) values ('A', 'A', 'A'), ('B', 'B', 'B'), ('C', 'C', 'C');");
    }

    private void drop() throws SQLException {
        statement.executeUpdate("drop table if exists users;");
    }

    public String loginCheck(String login, String password) {
        String result = "-1";
        try {
            connect();
            try (ResultSet rs = statement.executeQuery("select nickname from users where login == '"+ login + "' and password == '"+ password + "';")) {
                while (rs.next()) {
                    result = "/authok " + rs.getString("nickname");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
        return result;
    }

    public String nickNameChange(String oldNickName, String newNickName) {
        String result = "-1";
        try {
            connect();
            try (ResultSet rs = statement.executeQuery("select id from users where nickname == '" + oldNickName + "';")) {
                while (rs.next()) {
                    result = rs.getString("id");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if(!result.equalsIgnoreCase("-1")){
                statement.executeUpdate("update users set nickname == '" + newNickName + "' where id == '" + result + "';");
                result = "1";
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            disconnect();
        }
        return result;
    }

    public String userReg(String login, String password, String nickName) {
        String result = "-1";
        try {
            connect();
            try (ResultSet rs = statement.executeQuery("select nickname, login from users where login == '"+ login + "' or nickname == '"+ nickName + "';")) {
                while (rs.next()) {
                    result = rs.getString("id");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if(result.equalsIgnoreCase("-1")){
                statement.executeUpdate("insert into users (login, password, nickname) values ('"+login+"', '"+password+"', '"+nickName+"');");
                result = "1";
            }else{
                result = "-1";
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
        return result;
    }

    public void start() {
        System.out.println("Start");
    }

    public void stop() {
        System.out.println("Stop");
    }

}