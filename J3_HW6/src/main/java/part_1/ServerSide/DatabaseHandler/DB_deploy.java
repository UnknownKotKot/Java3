package part_1.ServerSide.DatabaseHandler;

import part_1.ServerSide.service.DatabaseService;

public class DB_deploy {
    public static void main(String[] args) {
        DatabaseService dbs = new DatabaseService();
        dbs.startDB();
    }
}
