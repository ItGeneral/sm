package com.sm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * ∑Ω∑®√Ë ˆ£∫
 *
 * @author Administrator on 2015/12/3.
 */
@Controller
@RequestMapping("")
public class JavaEight {

    @RequestMapping(value = "/test")
    public void testJava8(){
      /*  List<String> collectList = Stream.of("a").collect(Collectors.toList());

        assertEquals(Arrays.asList("b","s"),collectList);*/

    }


}
