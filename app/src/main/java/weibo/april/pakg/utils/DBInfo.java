package weibo.april.pakg.utils;

/**
 * Created by Duke on 3/29/2017.
 */

public class DBInfo {
    private static final String DBINFO = "DBINFO";
    public static class DB{
        public static final int VERSION = 1;
        public static final String DB_NAME = "aprilWeibo.db";
    }
    public static class TABLE{
        public static final String USERINO_TABLE = "UserInfo";
        public static final String TABLE_CREATE_USERINFO="CREATE TABLE IF NOT EXISTS " +
                USERINO_TABLE+" (_id integer primary key autoincrement, userId TEXT, Token TEXT, RefreshToken TEXT, "+
                "ExpiresTime integer, PhoneNum TEXT)";
    }
}
