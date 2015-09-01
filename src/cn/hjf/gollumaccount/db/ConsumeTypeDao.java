package cn.hjf.gollumaccount.db;

import android.database.Cursor;
import cn.hjf.gollumaccount.model.ConsumeType;

public class ConsumeTypeDao {

    private GASQLiteDatabase mDB;
    private SqlBuilder mSqlBuilder;
    private static final String TABLE_NAME = "consume_type";
    
    public ConsumeTypeDao(GASQLiteDatabase db) {
        this.mDB = db;
        mSqlBuilder = new SqlBuilder();
    }
    
    public boolean isTableExist() {
        Cursor cursor = mDB.openOrCreateDatabase().rawQuery(mSqlBuilder.isTableExist(), null);
//        mDB.close();
        while (cursor.moveToNext()) {
            return true;
        }
        return false;
    }
    
    public void createTable() {
        
    }
    
    public void insert(ConsumeType type) {
        
    }
    
    
    private class SqlBuilder {
        
        public String isTableExist() {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT COUNT(*) FROM sqlite_master where type='table' and name=");
            sql.append("'");
            sql.append(TABLE_NAME);
            sql.append("'");
            return sql.toString();
        }
        
        public String createTable() {
            StringBuilder sql = new StringBuilder();
            sql.append("CREATE TABLE IF NOT EXISTS ");
            sql.append(TABLE_NAME);
            sql.append(" (id integer primary key AutoIncrement, name varchar(20), type integer");
            return sql.toString();
        }
        
        public String insertType(ConsumeType type) {
            StringBuilder sql = new StringBuilder();
            sql.append("insert into");
            return sql.toString();
        }
    }
    
    
}
