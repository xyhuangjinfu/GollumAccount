package cn.hjf.gollumaccount.db;

import java.io.File;
import java.io.IOException;

import cn.hjf.gollumaccount.PathHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class GASQLiteDatabase {
    
    private SQLiteDatabase mDB;
    private Context mContext;
    private String mDbPath;
    private static final String DB_NAME = "account.db"; 

    public GASQLiteDatabase(Context context) {
        this.mContext = context;
        initDbPath();
    }
    
    private void initDbPath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mDbPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/gollum/account/database/" + DB_NAME;
        } else {
            mDbPath = mContext.getDatabasePath(DB_NAME).getParent();
        }
    }
    
    public SQLiteDatabase openOrCreateDatabase() {
        File f = new File(mDbPath);
        if (!f.exists()) {
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        mDB = SQLiteDatabase.openOrCreateDatabase(f, null);
        return mDB;
    }
    
    public void close() {
        if (mDB.isOpen()) {
            mDB.close();
        }
    }
}
