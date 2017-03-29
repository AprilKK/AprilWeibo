package weibo.april.pakg.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import weibo.april.pakg.model.Bean.UserInfo;
import weibo.april.pakg.utils.DBInfo;

/**
 * Created by Duke on 3/29/2017.
 */

public class UserInfoServices {
    private  DBHelper dbHelper = null;
    public UserInfoServices(Context context)
    {
        dbHelper=new DBHelper(context, DBInfo.DB.DB_NAME,null, DBInfo.DB.VERSION);
    }
    public  void InsertUserInfoToTable(UserInfo userInfo)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues(5);
        values.put(UserInfo.USERID,userInfo.getUserId());
        values.put(UserInfo.TOKEN,userInfo.getToken());
        values.put(UserInfo.REFRESHTOKEN,userInfo.getRefreshToken());
        values.put(UserInfo.EXPIRESTIME,userInfo.getExpiresTime());
        values.put(UserInfo.PHONENUM,userInfo.getPhoneNum());
        db.insert(DBInfo.TABLE.USERINO_TABLE,null,values);
        db.close();
    }
    public UserInfo getUserInfoFromTable(String userId)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        UserInfo userInfo = new UserInfo();
        Cursor cursor = db.query(DBInfo.TABLE.USERINO_TABLE,new String[]{UserInfo.USERID,UserInfo.TOKEN,UserInfo.REFRESHTOKEN,
                        UserInfo.EXPIRESTIME,UserInfo.PHONENUM},UserInfo.USERID+"=?",new String[]{userId},null,null,null);
        if(cursor.getCount()>0)
        {
            userInfo.setUserId(cursor.getString(cursor.getColumnIndex(UserInfo.USERID)));
            userInfo.setToken(cursor.getString(cursor.getColumnIndex(UserInfo.TOKEN)));
            userInfo.setRefreshToken(cursor.getString(cursor.getColumnIndex(UserInfo.REFRESHTOKEN)));
            userInfo.setExpiresTime(cursor.getLong(cursor.getColumnIndex(UserInfo.EXPIRESTIME)));
            userInfo.setPhoneNum(cursor.getString(cursor.getColumnIndex(UserInfo.PHONENUM)));
            Log.v("UserInfoServices","the userId is "+userInfo.getUserId());
            Log.v("UserInfoServices","the token is "+userInfo.getToken());
            db.close();
            return userInfo;
        }
        db.close();
        return  null;
    }
}
