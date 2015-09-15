package cn.hjf.gollumaccount.db;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

/**
 * 自定义的 SQLiteDatabase 包装类
 * @author huangjinfu
 *
 */
public class GASQLiteDatabase {
    
    private static final boolean DEBUG = true;
    private static final String TAG = "GASQLiteDatabase";
    
    private static final String DB_NAME = "account.db";
    private static final int DB_VERSION = 1;
    
    private SQLiteDatabase mDB;
    private Context mContext;
    private String mDbPath;
    private OnDbUpgradeListener mListener;
    
    public interface OnDbUpgradeListener {
        public abstract void onDbUpgrade(int oldVersion, int newVersion);
    }

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
    
    
    public SQLiteDatabase open() {
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
        
        //要升级了
        if (mDB.getVersion() < DB_VERSION) {
            if (mListener != null) {
                mListener.onDbUpgrade(mDB.getVersion(), DB_VERSION);
            }
            mDB.setVersion(DB_VERSION);
        }
        return mDB;
    }
    
    public void setOnDbUpgradeListener(OnDbUpgradeListener onDbUpgradeListener) {
        this.mListener = onDbUpgradeListener;
    }
    
    public boolean close() {
        if (mDB.isOpen()) {
            mDB.close();
        }
        return true;
    }
    
}
