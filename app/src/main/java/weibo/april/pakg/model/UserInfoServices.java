package weibo.april.pakg.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

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
    public void updateUserInfoToTable(UserInfo userInfo)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(UserInfo.TOKEN,userInfo.getToken());
        cv.put(UserInfo.REFRESHTOKEN,userInfo.getRefreshToken());
        cv.put(UserInfo.EXPIRESTIME,userInfo.getExpiresTime());
        db.update(DBInfo.TABLE.USERINO_TABLE,cv,UserInfo.USERID+"=?",new String[]{userInfo.getUserId()});
        db.close();
    }
    public UserInfo getUserInfoFromTable(String userId)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        UserInfo userInfo = null;
        Cursor cursor = db.query(DBInfo.TABLE.USERINO_TABLE,new String[]{UserInfo.USERID,UserInfo.TOKEN,UserInfo.REFRESHTOKEN,
                        UserInfo.EXPIRESTIME,UserInfo.PHONENUM},UserInfo.USERID +"=?",new String[]{userId},null,null,null);
        if(cursor.moveToFirst())
        {
            userInfo = new UserInfo();
            userInfo.setUserId(cursor.getString(cursor.getColumnIndex(UserInfo.USERID)));
            userInfo.setToken(cursor.getString(cursor.getColumnIndex(UserInfo.TOKEN)));
            userInfo.setRefreshToken(cursor.getString(cursor.getColumnIndex(UserInfo.REFRESHTOKEN)));
            userInfo.setExpiresTime(cursor.getLong(cursor.getColumnIndex(UserInfo.EXPIRESTIME)));
            userInfo.setPhoneNum(cursor.getString(cursor.getColumnIndex(UserInfo.PHONENUM)));
            Log.v("UserInfoServices","the userId is "+userInfo.getUserId());
            Log.v("UserInfoServices","the token is "+userInfo.getToken());
        }
        db.close();
        cursor.close();
        return  userInfo;
    }
    public ArrayList<UserInfo> getAllUserInfoFromTable()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<UserInfo> userInfoArrayList = new ArrayList<>();
        Cursor cursor = db.query(DBInfo.TABLE.USERINO_TABLE,new String[]{UserInfo.USERID,UserInfo.TOKEN,UserInfo.REFRESHTOKEN,
                UserInfo.EXPIRESTIME,UserInfo.PHONENUM},null,null,null,null,null);
        while (cursor.moveToNext())
        {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(cursor.getString(cursor.getColumnIndex(UserInfo.USERID)));
            userInfo.setToken(cursor.getString(cursor.getColumnIndex(UserInfo.TOKEN)));
            userInfo.setRefreshToken(cursor.getString(cursor.getColumnIndex(UserInfo.REFRESHTOKEN)));
            userInfo.setExpiresTime(cursor.getLong(cursor.getColumnIndex(UserInfo.EXPIRESTIME)));
            userInfo.setPhoneNum(cursor.getString(cursor.getColumnIndex(UserInfo.PHONENUM)));
            Log.v("UserInfoServices","the userId is "+userInfo.getUserId());
            userInfoArrayList.add(userInfo);
        }
        cursor.close();
        db.close();
        return userInfoArrayList;
    }
}
