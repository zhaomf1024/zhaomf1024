package com.lagou.sqlSession;

import java.lang.reflect.Type;
import java.util.List;

public interface SqlSession {

    //查询所有
    public <E> List<E> selectList(String statementId, Object... params) throws Exception;

    //根据条件查询单个
    public <T> T selectOne(String statementId, Object... params) throws Exception;

    public int delete(String statementId, Object... params) throws Exception;

    public int insert(String statementId, Object... params) throws Exception;

    public int update(String statementId, Object... params) throws Exception;

    //为Dao接口生成代理实现类
    public <T> T getMapper(Class<?> mapperClass);


    public Object execute(String statementId, Type genericReturnType, Object... params) throws Exception;
}
