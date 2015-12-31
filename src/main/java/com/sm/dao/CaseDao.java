package com.sm.dao;

import com.sm.model.Case;
import org.springframework.stereotype.Repository;

/**
 * ∑Ω∑®√Ë ˆ£∫
 *
 * @author Administrator on 2015/11/16.
 */
@Repository
public interface CaseDao {

    public int insertCase(Case cases);

    public Case queryCaseById(Integer id);

}
