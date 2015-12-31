package com.sm.base;

import com.google.common.collect.Lists;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * ����������
 *
 * @author Administrator on 2015/11/16.
 */
public class BaseDao extends SqlSessionDaoSupport {

    /**
     * ����
     * @param sqlId
     * @param param ���Դ���model��map
     * @return
     */
    public int insert(String sqlId, Object param) {
        return getSqlSession().insert(sqlId, param);
    }

    /**
     * ɾ��
     * @param sqlId
     * @param param ���Դ���model��map��int��string
     * @return
     */
    public int delete(String sqlId, Object param) {
        return getSqlSession().delete(sqlId, param);
    }

    /**
     * ����
     * @param sqlId
     * @param param ���Դ���model��map
     * @return
     */
    public int update(String sqlId, Object param) {
        return getSqlSession().update(sqlId, param);
    }

    /**
     * ����
     * @param sqlId
     * @return
     */
    public int update(String sqlId) {
        return update(sqlId, null);
    }

    /**
     * ��ѯ�б�
     * @param sqlId
     * @param param ���Դ���model��map��int��string
     * @param <T>
     * @return
     */
    public <T> List<T> selectList(String sqlId, Object param) {
        return getSqlSession().selectList(sqlId, param);
    }

    /**
     * ��ѯ�б�
     * @param sqlId
     * @param <T>
     * @return
     */
    public <T> List<T> selectList(String sqlId) {
        return getSqlSession().selectList(sqlId, null);
    }

    /**
     * ��ѯ������¼
     * @param sqlId
     * @param param ���Դ���model��map��int��string
     * @param <T>
     * @return
     */
    public <T> T selectOne(String sqlId, Object param) {
        return getSqlSession().selectOne(sqlId, param);
    }

    /**
     * ��ѯ��¼��
     * @param sqlId
     * @param param ���Դ���model��map��int��string
     * @return
     */
    public int count(String sqlId, Object param) {
        return getSqlSession().selectOne(sqlId, param);
    }


}
