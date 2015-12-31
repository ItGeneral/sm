package com.sm.controller;

import com.sm.model.Case;
import com.sm.service.CaseServiceImpl;
import com.sm.service.Testervice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * ∑Ω∑®√Ë ˆ£∫
 *
 * @author Administrator on 2015/11/12.
 */
@Controller
@RequestMapping("/case")
public class CaseController {

    @Autowired
    private CaseServiceImpl caseServiceImpl;
    @Autowired
    private Testervice testervice;

    @RequestMapping("/queryCase")
    public String queryCase(HttpServletRequest request,Model model){
        Integer id = 207;
        Case c = caseServiceImpl.queryCaseById(id);
        model.addAttribute(c);
        return "hello";
    }

    @RequestMapping("/query")
    public String query(HttpServletRequest request,Model model){
        Integer id = 207;
        Case c = testervice.queryCaseById(id);
        model.addAttribute(c);
        return "hello";
    }

}
