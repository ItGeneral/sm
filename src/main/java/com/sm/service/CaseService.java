package com.sm.service;

import com.sm.model.Case;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * ·½·¨ÃèÊö£º
 *
 * @author Administrator on 2015/11/12.
 */
public interface CaseService {

    public int insertCase(Case cases);

    public Case queryCaseById(Integer id);

}
