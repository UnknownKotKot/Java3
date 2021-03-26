package ServerSide.DatabaseHandler;

import ServerSide.service.DatabaseService;

public class DB_drop {

    public static void main(String[] args) {
        DatabaseService dbs = new DatabaseService();
        dbs.dropDB();
    }
}
