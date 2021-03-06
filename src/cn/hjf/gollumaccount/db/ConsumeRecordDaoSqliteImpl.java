package cn.hjf.gollumaccount.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.hjf.gollumaccount.businessmodel.ConsumeType;
import cn.hjf.gollumaccount.daomodel.ConsumeRecordModel;
import cn.hjf.gollumaccount.daomodel.ConsumeTypeModel;
import cn.hjf.gollumaccount.daomodel.QueryInfoModel;
import cn.hjf.gollumaccount.util.TimeUtil;
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
    public boolean insert(ConsumeRecordModel record) {
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
    public boolean delete(int recordId) {
        boolean result = true;
        try {
            mDB.open().execSQL(mSqlBuilder.delete(recordId));
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        } finally {
            mDB.close();
        }
        return result;
    }

    @Override
    public boolean update(ConsumeRecordModel record) {
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
    public List<ConsumeRecordModel> queryAll() {
        List<ConsumeRecordModel> records = null;
        Cursor cursor = mDB.open().rawQuery(mSqlBuilder.queryAll(), null);
        records = getRecordsFromCursor(cursor);
        cursor.close();
        mDB.close();
        return records;
    }
    

    @Override
    public List<ConsumeRecordModel> queryAllByParameter(Map<String, String> paras) {
        List<ConsumeRecordModel> records = null;
        Cursor cursor = mDB.open().rawQuery(mSqlBuilder.queryAllByParameter(paras), null);
        records = getRecordsFromCursor(cursor);
        cursor.close();
        mDB.close();
        return records;
    }
    
    @Override
    public List<ConsumeRecordModel> queryRecords(QueryInfoModel queryInfo) {
        List<ConsumeRecordModel> records = null;
        Cursor cursor = mDB.open().rawQuery(mSqlBuilder.queryRecords(queryInfo), null);
        records = getRecordsFromCursor(cursor);
        cursor.close();
        mDB.close();
        return records;
    }
    

    @Override
    public Map<String, Double> statisticByType(long startDate, long endDate) {
        Map<String, Double> datas = new HashMap<String, Double>();
        Cursor cursor = mDB.open().rawQuery(mSqlBuilder.statisticByType(startDate, endDate), null);
        while (cursor.moveToNext()) {
            datas.put(cursor.getString(cursor.getColumnIndex(Table.CLM_TYPE)),
                    cursor.getDouble(cursor.getColumnIndex("sum ( " + Table.CLM_PRICE + " )") 
                            ));
        }
        cursor.close();
        mDB.close();
        return datas;
    }
    

    @Override
    public Double statisticSum(long startDate, long endDate) {
        Double sum = 0d;
        Cursor cursor = mDB.open().rawQuery(mSqlBuilder.statisticSum(startDate, endDate), null);
        while (cursor.moveToNext()) {
            sum = cursor.getDouble(cursor.getColumnIndex("sum ( " + Table.CLM_PRICE + " )"));
        }
        cursor.close();
        mDB.close();
        return sum;
    }
    

    @Override
    public Double queryMonthSumByType(long startDate, long endDate,
            ConsumeTypeModel type) {
        Double sum = 0d;
        Cursor cursor = mDB.open().rawQuery(mSqlBuilder.queryMonthSumByType(startDate, endDate, type), null);
        while (cursor.moveToNext()) {
            sum = cursor.getDouble(cursor.getColumnIndex("sum ( " + Table.CLM_PRICE + " )"));
        }
        cursor.close();
        mDB.close();
        return sum;
    }
    
    /**
     * 从Cursor中构造消费记录列表
     * @param cursor
     * @return
     */
    private List<ConsumeRecordModel> getRecordsFromCursor(Cursor cursor) {
        List<ConsumeRecordModel> records = new ArrayList<ConsumeRecordModel>();
        while (cursor.moveToNext()) {
            ConsumeRecordModel record = new ConsumeRecordModel();
            record.setId(cursor.getInt(cursor.getColumnIndex(Table.CLM_ID)));
            record.setRecordName(cursor.getString(cursor.getColumnIndex(Table.CLM_NAME)));
            record.setRecordPrice(cursor.getString(cursor.getColumnIndex(Table.CLM_PRICE)));
            record.setRecordRemark(cursor.getString(cursor.getColumnIndex(Table.CLM_REMARK)));
            record.setRecordType(cursor.getString(cursor.getColumnIndex(Table.CLM_TYPE)));
            record.setConsumer(cursor.getString(cursor.getColumnIndex(Table.CLM_CONSUMER)));
            record.setPayer(cursor.getString(cursor.getColumnIndex(Table.CLM_PAYER)));
            record.setConsumeTime(Long.valueOf(cursor.getString(cursor.getColumnIndex(Table.CLM_CONSUME_TIME))));
            record.setCreateTime(Long.valueOf(cursor.getString(cursor.getColumnIndex(Table.CLM_CREATE_TIME))));
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
            sql.append(" varchar(50) NOT NULL , ");
            sql.append(Table.CLM_PRICE);
            sql.append(" varchar(50) NOT NULL  , ");
            sql.append(Table.CLM_TYPE);
            sql.append(" varchar(50) NOT NULL  , ");
            sql.append(Table.CLM_REMARK);
            sql.append(" varchar(100) , ");
            sql.append(Table.CLM_CONSUMER);
            sql.append(" varchar(50) , ");
            sql.append(Table.CLM_PAYER);
            sql.append(" varchar(50) , ");
            sql.append(Table.CLM_CONSUME_TIME);
            sql.append(" varchar(50) NOT NULL  , ");
            sql.append(Table.CLM_CREATE_TIME);
            sql.append(" varchar(50) NOT NULL ) ");
            if (DEBUG) {
                Log.d(TAG, sql.toString());
            }
            return sql.toString();
        }
        
        /**
         * 添加消费记录
         * @return
         */
        public String insertRecord(ConsumeRecordModel record) {
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
                        sql.append(record.getRecordType());
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
        public String updateRecord(ConsumeRecordModel record) {
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
                        sql.append(record.getRecordType());
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
        public String queryRecords(QueryInfoModel queryInfo) {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT * FROM ");
            sql.append(TABLE_NAME);
            sql.append(" WHERE ");
            
            if (queryInfo.getStartTime() != 0 && !"".equals(queryInfo.getStartTime())) {
                sql.append(Table.CLM_CONSUME_TIME);
                sql.append(" >= ");
                    sql.append("'");
                    sql.append(queryInfo.getStartTime());
                    sql.append("'");
                sql.append(" AND ");
            }

            
            if (queryInfo.getEndTime() != 0 && !"".equals(queryInfo.getEndTime())) {
                sql.append(Table.CLM_CONSUME_TIME);
                sql.append(" <= ");
                    sql.append("'");
                    sql.append(queryInfo.getEndTime());
                    sql.append("'");
                sql.append(" AND ");
            }
            
            if (queryInfo.getName() != null && !"".equals(queryInfo.getName())) {
                sql.append(Table.CLM_NAME);
                sql.append(" LIKE ");
                    sql.append("'%");
                    sql.append(queryInfo.getName());
                    sql.append("%'");
                sql.append(" AND ");
            }

            if (queryInfo.getType() != null && !queryInfo.getType().getType().equals(ConsumeTypeModel.Type.ALL) ) {
                sql.append(Table.CLM_TYPE);
                sql.append(" = ");
                    sql.append("'");
                    sql.append(queryInfo.getType().getName());
                    sql.append("'");
                sql.append(" AND ");
            }

            
            sql.append("1");
            sql.append(" = ");
            sql.append("1");

            
            sql.append(" ORDER BY ");
            sql.append(Table.CLM_CONSUME_TIME);
            sql.append(" DESC ");
                    
            sql.append(" LIMIT ");
                sql.append("'");
                sql.append(queryInfo.getPageSize());
                sql.append("'");
            sql.append(" OFFSET ");
                sql.append("'");
                sql.append((queryInfo.getPageNumber() - 1) * queryInfo.getPageSize());
                sql.append("'");
                
            
            if (DEBUG) {
                Log.d(TAG, sql.toString());
            }
            return sql.toString();
        }
        
        /**
         * 按类型统计，查询
         */
        public String statisticByType(long startDate, long endDate) {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ");
            sql.append(Table.CLM_TYPE);
            sql.append(" , ");
            sql.append(" sum ( ");
            sql.append(Table.CLM_PRICE);
            sql.append(" ) ");
            sql.append(" FROM ");
            sql.append(TABLE_NAME);
            sql.append(" WHERE ");
            sql.append(Table.CLM_CONSUME_TIME);
            sql.append(" >= ");
            sql.append("'");
            sql.append(startDate);
            sql.append("'");
            sql.append(" AND ");
            sql.append(Table.CLM_CONSUME_TIME);
            sql.append(" <= ");
            sql.append("'");
            sql.append(endDate);
            sql.append("'");
            sql.append(" GROUP BY ");
            sql.append(Table.CLM_TYPE);
            if (DEBUG) {
                Log.d(TAG, sql.toString());
            }
            return sql.toString();
        }
        
        /**
         * 统计时间段内消费金额总计
         */
        public String statisticSum(long startDate, long endDate) {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ");
            sql.append(" sum ( ");
            sql.append(Table.CLM_PRICE);
            sql.append(" ) ");
            sql.append(" FROM ");
            sql.append(TABLE_NAME);
            sql.append(" WHERE ");
            sql.append(Table.CLM_CONSUME_TIME);
            sql.append(" >= ");
            sql.append("'");
            sql.append(startDate);
            sql.append("'");
            sql.append(" AND ");
            sql.append(Table.CLM_CONSUME_TIME);
            sql.append(" <= ");
            sql.append("'");
            sql.append(endDate);
            sql.append("'");
            if (DEBUG) {
                Log.d(TAG, sql.toString());
            }
            return sql.toString();
        }
        
        /**
         * 统计时间段内消费金额总计
         */
        public String queryMonthSumByType(long startDate, long endDate, ConsumeTypeModel type) {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ");
            sql.append(" sum ( ");
            sql.append(Table.CLM_PRICE);
            sql.append(" ) ");
            sql.append(" FROM ");
            sql.append(TABLE_NAME);
            sql.append(" WHERE ");
            sql.append(Table.CLM_CONSUME_TIME);
            sql.append(" >= ");
            sql.append("'");
            sql.append(startDate);
            sql.append("'");
            sql.append(" AND ");
            sql.append(Table.CLM_CONSUME_TIME);
            sql.append(" <= ");
            sql.append("'");
            sql.append(endDate);
            sql.append("'");
            
            if (!type.getType().equals(ConsumeTypeModel.Type.ALL)) {
                sql.append(" AND ");
                sql.append(Table.CLM_TYPE);
                sql.append(" = ");
                sql.append("'");
                sql.append(type.getName());
                sql.append("'");
            }
            
            sql.append(" AND 1 = 1 ");
            if (DEBUG) {
                Log.d(TAG, sql.toString());
                Log.d(TAG, TimeUtil.getTimeString(startDate) + " -- " + TimeUtil.getTimeString(endDate));
            }
            return sql.toString();
        }
        
        /**
         * 删除消费记录
         */
        public String delete(int id) {
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE FROM ");
            sql.append(TABLE_NAME);
            sql.append(" WHERE ");
            sql.append(Table.CLM_ID);
            sql.append(" = ");
            sql.append("'");
            sql.append(id);
            sql.append("'");
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
        static final String CLM_TYPE = "recordType"; //消费类型id
        static final String CLM_REMARK = "recordRemark"; //备注信息
        static final String CLM_CONSUMER = "consumer"; //消费者
        static final String CLM_PAYER = "payer"; //付款者
        static final String CLM_CONSUME_TIME = "consumeTime"; //消费时间
        static final String CLM_CREATE_TIME = "createTime"; //消费记录创建时间
    }

}
