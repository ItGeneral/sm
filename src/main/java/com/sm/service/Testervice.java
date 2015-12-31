package com.sm.service;

import com.sm.base.BaseDao;
import com.sm.base.BaseService;
import com.sm.model.Case;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ∑Ω∑®√Ë ˆ£∫
 *
 * @author Administrator on 2015/11/16.
 */
@Service
public class Testervice extends BaseService<Case>{

    public Case queryCaseById(Integer id){
        return dao.selectOne("queryCaseById",id);
    }

}
