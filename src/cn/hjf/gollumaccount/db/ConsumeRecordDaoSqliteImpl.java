package cn.hjf.gollumaccount.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.hjf.gollumaccount.model.ConsumeRecord;
import cn.hjf.gollumaccount.model.ConsumeType;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

/**
 * 消费记录数据访问类，依赖于具体的数据库
 * @author huangjinfu
 *
 */
public class ConsumeRecordDaoSqliteImpl implements IConsumeRecordDao {
    
    private static final boolean DEBUG = true;
    private static final String TAG = "ConsumeRecordDaoSqliteImpl";
    
    private static final String TABLE_NAME = "consume_record";
    
    private GASQLiteDatabase mDB;
    private SqlBuilder mSqlBuilder;
    
    public ConsumeRecordDaoSqliteImpl(Context context) {
        mDB = new GASQLiteDatabase(context);
        mSqlBuilder = new SqlBuilder();
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
    public boolean insert(ConsumeRecord record) {
        boolean result = true;
        try {
            mDB.open().execSQL(mSqlBuilder.insertRecord(record));
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        } finally {
            mDB.close();
        }
        return result;
    }

    @Override
    public boolean update(ConsumeRecord record) {
        boolean result = true;
        try {
            mDB.open().execSQL(mSqlBuilder.updateRecord(record));
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        } finally {
            mDB.close();
        }
        return result;
    }

    @Override
    public List<ConsumeRecord> queryAll() {
        List<ConsumeRecord> records = null;
        Cursor cursor = mDB.open().rawQuery(mSqlBuilder.queryAll(), null);
        records = getRecordsFromCursor(cursor);
        cursor.close();
        mDB.close();
        return records;
    }
    

    @Override
    public List<ConsumeRecord> queryAllByParameter(Map<String, String> paras) {
        List<ConsumeRecord> records = null;
        Cursor cursor = mDB.open().rawQuery(mSqlBuilder.queryAllByParameter(paras), null);
        records = getRecordsFromCursor(cursor);
        cursor.close();
        mDB.close();
        return records;
    }
    
    @Override
    public List<ConsumeRecord> queryRecords(String startTime, String endTime,
            int type, String name) {
        List<ConsumeRecord> records = null;
        Cursor cursor = mDB.open().rawQuery(mSqlBuilder.queryRecords(startTime, endTime, type, name), null);
        records = getRecordsFromCursor(cursor);
        cursor.close();
        mDB.close();
        return records;
    }
    
    /**
     * 从Cursor中构造消费记录列表
     * @param cursor
     * @return
     */
    private List<ConsumeRecord> getRecordsFromCursor(Cursor cursor) {
        List<ConsumeRecord> records = new ArrayList<ConsumeRecord>();
        while (cursor.moveToNext()) {
            ConsumeRecord record = new ConsumeRecord();
            record.setId(cursor.getInt(cursor.getColumnIndex(Table.CLM_ID)));
            record.setRecordName(cursor.getString(cursor.getColumnIndex(Table.CLM_NAME)));
            record.setRecordPrice(cursor.getString(cursor.getColumnIndex(Table.CLM_PRICE)));
            record.setRecordRemark(cursor.getString(cursor.getColumnIndex(Table.CLM_REMARK)));
            record.setRecordTypeId(cursor.getInt(cursor.getColumnIndex(Table.CLM_ID)));
            record.setConsumer(cursor.getString(cursor.getColumnIndex(Table.CLM_CONSUMER)));
            record.setPayer(cursor.getString(cursor.getColumnIndex(Table.CLM_PAYER)));
            record.setConsumeTime(cursor.getString(cursor.getColumnIndex(Table.CLM_CONSUME_TIME)));
            record.setCreateTime(cursor.getString(cursor.getColumnIndex(Table.CLM_CREATE_TIME)));
            records.add(record);
        }
        return records;
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
            sql.append(" varchar(50) , ");
            sql.append(Table.CLM_PRICE);
            sql.append(" varchar(50) , ");
            sql.append(Table.CLM_TYPE);
            sql.append(" integer , ");
            sql.append(Table.CLM_REMARK);
            sql.append(" varchar(100) , ");
            sql.append(Table.CLM_CONSUMER);
            sql.append(" varchar(50) , ");
            sql.append(Table.CLM_PAYER);
            sql.append(" varchar(50) , ");
            sql.append(Table.CLM_CONSUME_TIME);
            sql.append(" varchar(50) , ");
            sql.append(Table.CLM_CREATE_TIME);
            sql.append(" varchar(50))  ");
            if (DEBUG) {
                Log.d(TAG, sql.toString());
            }
            return sql.toString();
        }
        
        /**
         * 添加消费记录
         * @return
         */
        public String insertRecord(ConsumeRecord record) {
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO ");
            sql.append(TABLE_NAME);
            
                sql.append(" ( " );
                    sql.append(Table.CLM_NAME);
                        sql.append(",");
                    sql.append(Table.CLM_PRICE);
                        sql.append(",");
                    sql.append(Table.CLM_TYPE);
                        sql.append(",");
                    sql.append(Table.CLM_REMARK);
                        sql.append(",");
                    sql.append(Table.CLM_CONSUMER);
                        sql.append(",");
                    sql.append(Table.CLM_PAYER);
                        sql.append(",");
                    sql.append(Table.CLM_CONSUME_TIME);
                        sql.append(",");
                    sql.append(Table.CLM_CREATE_TIME);
                sql.append(" ) ") ;
            
            
                sql.append(" values ( ");
                        sql.append("'");
                        sql.append(record.getRecordName());
                        sql.append("'");
                    sql.append(" , ");
                        sql.append("'");
                        sql.append(record.getRecordPrice());
                        sql.append("'");
                    sql.append(" , ");
                        sql.append("'");
                        sql.append(record.getRecordTypeId());
                        sql.append("'");
                    sql.append(" , ");
                        sql.append("'");
                        sql.append(record.getRecordRemark());
                        sql.append("'");
                    sql.append(" , ");
                        sql.append("'");
                        sql.append(record.getConsumer());
                        sql.append("'");
                    sql.append(" , ");
                        sql.append("'");
                        sql.append(record.getPayer());
                        sql.append("'");
                    sql.append(" , ");
                        sql.append("'");
                        sql.append(record.getConsumeTime());
                        sql.append("'");
                    sql.append(" , ");
                        sql.append("'");
                        sql.append(record.getCreateTime());
                        sql.append("'");
                sql.append(" ) ");
                
            if (DEBUG) {
                Log.d(TAG, sql.toString());
            }
            return sql.toString();
        }
        
        /**
         * 更新消费记录
         * @return
         */
        public String updateRecord(ConsumeRecord record) {
            StringBuilder sql = new StringBuilder();
            sql.append(" UPDATE ");
            sql.append(TABLE_NAME);
            sql.append(" SET ");
            
                    sql.append(Table.CLM_NAME);
                        sql.append("=");
                        sql.append("'");
                        sql.append(record.getRecordName());
                        sql.append("'");
                    
                        sql.append(",");
                        
                    sql.append(Table.CLM_PRICE);
                        sql.append("=");
                        sql.append("'");
                        sql.append(record.getRecordPrice());
                        sql.append("'");
                        
                        sql.append(",");
                        
                    sql.append(Table.CLM_TYPE);
                        sql.append("=");
                        sql.append("'");
                        sql.append(record.getRecordTypeId());
                        sql.append("'");
                        
                        sql.append(",");
                        
                    sql.append(Table.CLM_REMARK);
                        sql.append("=");
                        sql.append("'");
                        sql.append(record.getRecordRemark());
                        sql.append("'");
                        
                        sql.append(",");
                        
                    sql.append(Table.CLM_CONSUMER);
                        sql.append("=");
                        sql.append("'");
                        sql.append(record.getConsumer());
                        sql.append("'");
                        
                        sql.append(",");
                        
                    sql.append(Table.CLM_PAYER);
                        sql.append("=");
                        sql.append("'");
                        sql.append(record.getPayer());
                        sql.append("'");
                        
                        sql.append(",");
                        
                    sql.append(Table.CLM_CONSUME_TIME);
                        sql.append("=");
                        sql.append("'");
                        sql.append(record.getConsumeTime());
                        sql.append("'");
                        
                        sql.append(",");
                        
                    sql.append(Table.CLM_CREATE_TIME);
                        sql.append("=");
                        sql.append("'");
                        sql.append(record.getCreateTime());
                        sql.append("'");
                        
                sql.append(" WHERE ");
                sql.append(Table.CLM_ID);
                sql.append(" = ");
                sql.append("'");
                sql.append(record.getId());
                sql.append("'");
                
            if (DEBUG) {
                Log.d(TAG, sql.toString());
            }
            return sql.toString();
        }
        
        /**
         * 查询所有消费记录
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
         * 查询所有消费记录
         */
        public String queryAllByParameter(Map<String, String> paras) {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT * FROM ");
            sql.append(TABLE_NAME);
            sql.append(" WHERE ");
            for (Map.Entry<String, String> entry : paras.entrySet()) {
                sql.append(entry.getKey());
                sql.append(" = ");
                sql.append("'");
                sql.append(entry.getValue());
                sql.append("'");
                sql.append("  ");
                sql.append("AND");
            }
            sql.delete(sql.length() - 3, sql.length());
            if (DEBUG) {
                Log.d(TAG, sql.toString());
            }
            return sql.toString();
        }
        
        /**
         * 按特定条件查询所有消费记录
         */
        public String queryRecords(String startTime, String endTime, int type, String name) {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT * FROM ");
            sql.append(TABLE_NAME);
            sql.append(" WHERE ");
            sql.append(Table.CLM_CONSUME_TIME);
            sql.append(" >= ");
                sql.append("'");
                sql.append(startTime);
                sql.append("'");
            sql.append(" AND ");
            sql.append(Table.CLM_CONSUME_TIME);
            sql.append(" <= ");
                sql.append("'");
                sql.append(endTime);
                sql.append("'");
            sql.append(" AND ");
            sql.append(Table.CLM_TYPE);
            sql.append(" == ");
                sql.append("'");
                sql.append(type);
                sql.append("'");
            sql.append(" AND ");
            sql.append(Table.CLM_NAME);
                sql.append(" LIKE ");
                    sql.append("'%");
                    sql.append(name);
                    sql.append("%'");
            
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
    private class Table {
        static final String CLM_ID = "id";
        static final String CLM_NAME = "recordName"; //消费记录名称
        static final String CLM_PRICE = "recordPrice"; //消费金额
        static final String CLM_TYPE = "recordTypeId"; //消费类型id
        static final String CLM_REMARK = "recordRemark"; //备注信息
        static final String CLM_CONSUMER = "consumer"; //消费者
        static final String CLM_PAYER = "payer"; //付款者
        static final String CLM_CONSUME_TIME = "consumeTime"; //消费时间
        static final String CLM_CREATE_TIME = "createTime"; //消费记录创建时间
    }

}
