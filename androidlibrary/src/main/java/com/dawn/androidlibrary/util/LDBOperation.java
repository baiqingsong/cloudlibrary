package com.dawn.androidlibrary.util;

import android.content.Context;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;

import java.util.List;
@SuppressWarnings("unused")
public class LDBOperation {
    public static final String DB_NAME = "lite_orm";
    private static LiteOrm liteOrm;

    /**
     * 初始化
     * @param mContext 上下文
     * @param liteName 数据库名称
     * @param debug 是否打印日志
     */
    public static void newSingleInstance(Context mContext, String liteName, boolean debug){
        if(liteOrm == null){
//            DataBaseConfig config = new DataBaseConfig(mContext, liteName);
//            config.dbVersion = 1; // set database version
//            config.onUpdateListener = null; // set database update listener
            liteOrm = LiteOrm.newSingleInstance(mContext, liteName);
        }
        liteOrm.setDebugged(debug);
    }

    public static LiteOrm getLiteOrm(){
        return liteOrm;
    }


    /**
     * 插入数据库
     * @param t 实体类
     */
    public static <T> void insert(T t){
        if(liteOrm != null)
            liteOrm.save(t);
    }

    /**
     * 插入多条数据
     * @param list 实体类的集合
     */
    public static <T> void insertAll(List<T> list){
        if(liteOrm != null)
            liteOrm.save(list);
    }


    /**
     * 查询所有数据
     * @param cla 查询的类型
     */
    public static <T> List<T> queryAll(Class<T> cla){
        if(liteOrm != null)
            return liteOrm.query(cla);
        return null;
    }

    /**
     * 根据条件查询数据
     * @param cla 类
     * @param field 数据库字段,单个字段
     * @param value 条件的值，单个值
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> List<T> queryByWhere(Class<T> cla,String field,String value){
        String[] values = new String[]{value};
        if(liteOrm != null)
            return liteOrm.<T>query(new QueryBuilder(cla).where(field + "=?", values));
        return null;
    }

    /**
     * 多条件查询数据
     * @param cla 类
     * @param field 多个字段要写上 = ？
     * @param value 多个字段对应的值
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> List<T> queryByWhere(Class<T> cla,String field,String[] value){
        if(liteOrm != null)
            return liteOrm.<T>query(new QueryBuilder(cla).where(field, value));
        return null;
    }

    /**
     * 2个条件查询
     * @param cla 类
     * @param field1 参数
     * @param value1 值
     * @param field2 参数
     * @param value2 值
     */
    public static <T> List<T> queryByWhereTwo(Class<T> cla,String field1,String[] value1,String field2,String[] value2){
        if(liteOrm != null)
            return liteOrm.query(new QueryBuilder<>(cla).where(field1, value1).whereAppendAnd().whereAppend(field2, value2));
        return null;
    }

    /**
     * 分页查询,多个条件查询
     * @param cla 实体类的类型
     * @param field 查询的条件，需要写 = ？
     * @param value 多个查询的值
     * @param start 开始位置
     * @param length 长度最后的条数
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> List<T> queryByWhereLength(Class<T> cla,String field,String [] value,int start,int length){
        if(liteOrm != null)
            return liteOrm.<T>query(new QueryBuilder(cla).where(field, value).limit(start, length));
        return null;
    }

    /**
     * 分页查询，单个条件查询
     * @param cla 实体类的类型
     * @param field 单个数据的字段名称
     * @param value 数据对应的值
     * @param start 开始的位置
     * @param length 查询的最后条数
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> List<T> queryByWhereLength(Class<T> cla, String field, String value, int start, int length) {
        String[] values = new String[]{value};
        if(liteOrm != null)
            return liteOrm.<T>query(new QueryBuilder(cla).where(field + " = ?", values).limit(start, length));
        return null;
    }

    /**
     * 按照条件查找并且排序
     * @param cla 实体类的类型
     * @param where 单个数据的字段名称
     * @param values 数据对应的值
     * @param order 排序字段
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> List<T> queryByWhereOrder(Class<T> cla, String where, String[] values, String order){
        if(liteOrm != null)
            return liteOrm.<T>query(new QueryBuilder(cla).where(where, values).orderBy(order + " desc"));
        return null;
    }
    public static <T> List<T> queryByWhereLike(Class<T> cla){
        return null;
    }

    /**
     * 按照条件删除数据
     * @param cla 实体类的类型
     * @param field 删除的条件， 多个条件，带有 = ？
     * @param value 多个值
     */
    @SuppressWarnings("deprecation")
    public static <T> void deleteWhere(Class<T> cla,String field,String [] value){
        if(liteOrm != null)
            liteOrm.delete(cla, WhereBuilder.create(cla).where(field, value));
    }

    /**
     * 单个条件的删除
     * @param cla 实体类的类型
     * @param field 字段的名称，单个
     * @param value 字段对应的值
     */
    @SuppressWarnings("deprecation")
    public static <T> void deleteWhere(Class<T> cla,String field,String value){
        String[] values = new String[]{value};
        if(liteOrm != null)
            liteOrm.delete(cla, WhereBuilder.create(cla).where(field + "=?", values));
    }

    /**
     * 删除一个实体类
     * @param t 实体类
     */
    public static <T> void delete(T t){
        if(liteOrm != null)
            liteOrm.delete(t);
    }

    /**
     * 删除所有数据
     * @param cla 实体类的类型
     */
    public static <T> void deleteAll(Class<T> cla){
        if(liteOrm != null)
            liteOrm.deleteAll(cla);
    }
    /**
     * 连库文件一起删掉
     */
    public static <T> void deleteDatabase(){
        if(liteOrm != null)
            liteOrm.deleteDatabase();
    }
    /**
     * 重建一个新库
     */
    public static <T> void reCreateDatabase(){
        if(liteOrm != null)
            liteOrm.openOrCreateDatabase();
    }

    /**
     * 删除某个区间的数据
     * @param cla 实体类的类型
     * @param arg1 删除第arg1到第arg2条数据
     * @param arg2 第二个参数
     * @param arg3 可为null，默认按ID升序排列
     */
    public static <T> void deleteSection(Class<T> cla,long arg1, long arg2, String arg3){
        if(liteOrm != null)
            liteOrm.delete(cla, arg1, arg2, arg3);
    }

    /**删除一个实体list
     * @param list 实体类的集合
     */
    public static <T> void deleteList(List<T> list){
        if(liteOrm != null)
            liteOrm.delete(list);
    }


    /**
     * 仅在已存在的时候更新
     * @param t 实体类
     */
    public static <T> void update(T t){
        if(liteOrm != null)
            liteOrm.update(t, ConflictAlgorithm.Replace);
    }

    /**
     * 更新所有
     * @param list 实体类的集合
     */
    public static <T> void updateALL(List<T> list){
        if(liteOrm != null)
            liteOrm.update(list);
    }


    /**
     * 查询某个表的数据
     * @param cla 类
     */
    public static <T> long queryCount(Class<T> cla){
        if(liteOrm != null)
            return liteOrm.queryCount(cla);
        return -1;
    }
}
