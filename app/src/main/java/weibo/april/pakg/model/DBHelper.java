package weibo.april.pakg.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import weibo.april.pakg.utils.DBInfo;

/**
 * Created by Duke on 3/29/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private Context mContext;
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version)
    {
        super(context,name,factory,version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBInfo.TABLE.TABLE_CREATE_USERINFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
