package cn.hjf.gollumaccount.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cn.hjf.gollumaccount.daomodel.ConsumeTypeModel;

/**
 * 消费类型数据访问类，依赖于具体的数据库
 * @author huangjinfu
 *
 */
public class ConsumeTypeDaoSqliteImpl implements IConsumeTypeDao, GASQLiteDatabase.OnDbUpgradeListener {

    private static final boolean DEBUG = true;
    private static final String TAG = "ConsumeTypeDaoSqliteImpl";
    
    private static final String TABLE_NAME = "consume_type";
    
    private GASQLiteDatabase mDB;
    private SqlBuilder mSqlBuilder;
    
    private OnConsumeTypeUpgradeListener mListener;
    
    public ConsumeTypeDaoSqliteImpl(Context context) {
        mDB = new GASQLiteDatabase(context);
        mDB.setOnDbUpgradeListener(this);
        mSqlBuilder = new SqlBuilder();
    }
    
    @Override
    public void setOnConsumeTypeUpgradeListener(OnConsumeTypeUpgradeListener onConsumeTypeUpgradeListener) {
        this.mListener = onConsumeTypeUpgradeListener;
    }
    
    @Override
    public boolean isTableExist() {
        boolean isExist = false;
        Cursor cursor = mDB.open().rawQuery(mSqlBuilder.isTableExist(), null);
        while (cursor.moveToNext()) {
            isExist =  true;
        }
        cursor.close();
        mDB.close();
        return isExist;
    }
    
    @Override
    public boolean createTable() {
        boolean result = true;
        try {
            mDB.open().execSQL(mSqlBuilder.createTable());
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        } finally {
            mDB.close();
        }
        return result;
    }
    
    @Override
    public boolean insert(ConsumeTypeModel type) {
        boolean result = true;
        try {
            mDB.open().execSQL(mSqlBuilder.insertType(type));
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        } finally {
            mDB.close();
        }
        return result;
    }
    

    @Override
    public boolean insertAll(List<ConsumeTypeModel> types) {
        boolean result = true;
        SQLiteDatabase db = null;
        try {
            db = mDB.open();
            db.beginTransaction();
            for (ConsumeTypeModel consumeType : types) {
                db.execSQL(mSqlBuilder.insertType(consumeType));
            }
            db.setTransactionSuccessful();
            
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        } finally {
            db.endTransaction();
            mDB.close();
        }
        return false;
    }
    

    /**
     * 删除消费类型
     */
    @Override
    public boolean delete(ConsumeTypeModel type) {
        boolean result = true;
        try {
            mDB.open().execSQL(mSqlBuilder.delete(type));
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        } finally {
            mDB.close();
        }
        return result;
    }
    
    /**
     * 
     */
    @Override
    public ConsumeTypeModel queryByName(String name) {
        Cursor cursor = mDB.open().rawQuery(mSqlBuilder.queryByName(name), null);
        ConsumeTypeModel type = null;
        while (cursor.moveToNext()) {
            type = new ConsumeTypeModel();
            type.setId(cursor.getInt(cursor.getColumnIndex(Table.CLM_ID)));
            type.setName(cursor.getString(cursor.getColumnIndex(Table.CLM_NAME)));
            type.setType(ConsumeTypeModel.Type.valueOf(cursor.getString(cursor.getColumnIndex(Table.CLM_TYPE))));
            type.setIcon(cursor.getString(cursor.getColumnIndex(Table.CLM_ICON)));
        }
        cursor.close();
        mDB.close();
        return type;
    }
    

    @Override
    public ConsumeTypeModel queryById(int id) {
        Cursor cursor = mDB.open().rawQuery(mSqlBuilder.queryById(id), null);
        ConsumeTypeModel type = null;
        while (cursor.moveToNext()) {
            type = new ConsumeTypeModel();
            type.setId(cursor.getInt(cursor.getColumnIndex(Table.CLM_ID)));
            type.setName(cursor.getString(cursor.getColumnIndex(Table.CLM_NAME)));
            type.setType(ConsumeTypeModel.Type.valueOf(cursor.getString(cursor.getColumnIndex(Table.CLM_TYPE))));
            type.setIcon(cursor.getString(cursor.getColumnIndex(Table.CLM_ICON)));
        }
        cursor.close();
        mDB.close();
        return type;
    }

    /**
     * 查询所有消费类型
     */
    @Override
    public List<ConsumeTypeModel> queryAll() {
        List<ConsumeTypeModel> types = new ArrayList<ConsumeTypeModel>();
        Cursor cursor = mDB.open().rawQuery(mSqlBuilder.queryAll(), null);
        while (cursor.moveToNext()) {
            ConsumeTypeModel type = new ConsumeTypeModel();
            type.setId(cursor.getInt(cursor.getColumnIndex(Table.CLM_ID)));
            type.setName(cursor.getString(cursor.getColumnIndex(Table.CLM_NAME)));
            type.setType(ConsumeTypeModel.Type.valueOf(cursor.getString(cursor.getColumnIndex(Table.CLM_TYPE))));
            type.setIcon(cursor.getString(cursor.getColumnIndex(Table.CLM_ICON)));
            types.add(type);
        }
        cursor.close();
        mDB.close();
        return types;
    }
    
    /**
     * Sql语句拼装类
     * @author huangjinfu
     *
     */
    private class SqlBuilder {
        /**
         * 检测当前表是否存在
         * @return
         */
        public String isTableExist() {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM sqlite_master where type='table' and name=");
            sql.append("'");
            sql.append(TABLE_NAME);
            sql.append("'");
            if (DEBUG) {
                Log.d(TAG, sql.toString());
            }
            return sql.toString();
        }
        
        /**
         * 创建表
         * @return
         */
        public String createTable() {
            StringBuilder sql = new StringBuilder();
            sql.append("CREATE TABLE IF NOT EXISTS ");
            sql.append(TABLE_NAME);
            sql.append(" (id integer primary key AutoIncrement, ");
            sql.append(Table.CLM_NAME);
            sql.append(" varchar(30) UNIQUE , ");
            sql.append(Table.CLM_TYPE);
            sql.append(" varchar(30) NOT NULL ,");
            sql.append(Table.CLM_ICON);
            sql.append(" varchar(30) NOT NULL ) ");
            if (DEBUG) {
                Log.d(TAG, sql.toString());
            }
            return sql.toString();
        }
        
        /**
         * 添加消费类型
         * @return
         */
        public String insertType(ConsumeTypeModel type) {
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO ");
            sql.append(TABLE_NAME);
            sql.append(" (name, type, icon) ");
            sql.append(" values ( ");
            sql.append("'");
            sql.append(type.getName());
            sql.append("'");
            sql.append(" , ");
            sql.append("'");
            sql.append(type.getType());
            sql.append("'");
            sql.append(" , ");
            sql.append("'");
            sql.append(type.getIcon());
            sql.append("'");
            sql.append(" ) ");
            if (DEBUG) {
                Log.d(TAG, sql.toString());
            }
            return sql.toString();
        }
        
        /**
         * 更新消费类型
         * @return
         */
        public String updateType(ConsumeTypeModel type) {
            StringBuilder sql = new StringBuilder();
            sql.append(" UPDATE ");
            sql.append(TABLE_NAME);
            
            sql.append(" SET ");
            
            sql.append(Table.CLM_NAME);
                sql.append("=");
                sql.append("'");
                sql.append(type.getName());
                sql.append("'");
            
                sql.append(",");
                
            sql.append(Table.CLM_TYPE);
                sql.append("=");
                sql.append("'");
                sql.append(type.getType());
                sql.append("'");
                
                sql.append(",");
                
            sql.append(Table.CLM_ICON);
                sql.append("=");
                sql.append("'");
                sql.append(type.getIcon());
                sql.append("'");
                
            sql.append(" WHERE ");
                sql.append(Table.CLM_ID);
                sql.append(" = ");
                sql.append("'");
                sql.append(type.getId());
                sql.append("'");
            
            
            if (DEBUG) {
                Log.d(TAG, sql.toString());
            }
            return sql.toString();
        }
        
        /**
         * 按消费类型名称查询
         * @return
         */
        public String queryByName(String name) {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT * FROM ");
            sql.append(TABLE_NAME);
            sql.append(" where ");
            sql.append(Table.CLM_NAME);
            sql.append("=");
            sql.append("'");
            sql.append(name);
            sql.append("'");
            if (DEBUG) {
                Log.d(TAG, sql.toString());
            }
            return sql.toString();
        }
        
        /**
         * 按消费类型Id查询
         * @return
         */
        public String queryById(int id) {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT * FROM ");
            sql.append(TABLE_NAME);
            sql.append(" where ");
            sql.append(Table.CLM_ID);
            sql.append("=");
            sql.append("'");
            sql.append(id);
            sql.append("'");
            if (DEBUG) {
                Log.d(TAG, sql.toString());
            }
            return sql.toString();
        }
        
        /**
         * 查询所有消费类型
         * @return
         */
        public String queryAll() {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT * FROM ");
            sql.append(TABLE_NAME);
            if (DEBUG) {
                Log.d(TAG, sql.toString());
            }
            return sql.toString();
        }
        
        /**
         * 删除消费类型
         * @return
         */
        public String delete(ConsumeTypeModel type) {
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE FROM ");
            sql.append(TABLE_NAME);
            sql.append(" where ");
            sql.append(Table.CLM_NAME);
            sql.append("=");
            sql.append("'");
            sql.append(type.getName());
            sql.append("'");
            if (DEBUG) {
                Log.d(TAG, sql.toString());
            }
            return sql.toString();
        }
        
        /**
         * 表添加字段
         * @return
         */
        public String changeTable(String fields) {
            StringBuilder sql = new StringBuilder();
            sql.append(" ALTER TABLE ");
            sql.append(TABLE_NAME);
            sql.append(" ADD ");
            sql.append(fields);
            sql.append(" VARCHAR( 50 ) NOT NULL ");
            if (DEBUG) {
                Log.d(TAG, sql.toString());
            }
            return sql.toString();
        }
        
        /**
         * 查询所有消费记录个数
         * @return
         */
        public String typeCount() {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT COUNT(*) FROM ");
            sql.append(TABLE_NAME);
            if (DEBUG) {
                Log.d(TAG, sql.toString());
            }
            return sql.toString();
        }
    }

    /**
     * 表列信息
     * @author huangjinfu
     *
     */
    public static class Table {
        public static final String CLM_ID = "id";
        public static final String CLM_NAME = "name";
        public static final String CLM_TYPE = "type";
        public static final String CLM_ICON = "icon";
    }

    @Override
    public void onDbUpgrade(int oldVersion, int newVersion) {
        if (oldVersion == 0 && newVersion == 1) {
            mListener.onConsumeTypeUpgrade(oldVersion, newVersion);
        }
    }

    @Override
    public boolean changeTable(String fields) {
        boolean result = true;
        try {
            mDB.open().execSQL(mSqlBuilder.changeTable(fields));
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        } finally {
            mDB.close();
        }
        return result;
    }

    @Override
    public boolean update(ConsumeTypeModel type) {
        boolean result = true;
        try {
            mDB.open().execSQL(mSqlBuilder.updateType(type));
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        } finally {
            mDB.close();
        }
        return result;
    }

    /**
     * 查询所有消费记录个数
     * @return
     */
    @Override
    public int typeCount() {
        int count = 0;
        Cursor cursor = mDB.open().rawQuery(mSqlBuilder.typeCount(), null);
        ConsumeTypeModel type = null;
        while (cursor.moveToNext()) {
            count = cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
        }
        cursor.close();
        mDB.close();
        return count;
    }
    
}
