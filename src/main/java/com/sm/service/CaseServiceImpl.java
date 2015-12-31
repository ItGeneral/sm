package com.sm.service;


import com.sm.dao.CaseDao;
import com.sm.model.Case;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ∑Ω∑®√Ë ˆ£∫
 *
 * @author Administrator on 2015/11/16.
 */
@Service
public class CaseServiceImpl implements CaseService {

    @Autowired
    private CaseDao caseDao;



    @Override
    public int insertCase(Case cases) {
        return caseDao.insertCase(cases);
    }

    @Override
    public Case queryCaseById(Integer id) {
        return caseDao.queryCaseById(id);
    }
}
