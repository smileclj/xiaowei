package com.clj.panda.service.impl;

import com.clj.panda.dao.test.TestStudentMapper;
import com.clj.panda.model.entity.test.TestStudent;
import com.clj.panda.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by lao on 2015/9/28.
 */
@Service
public class TestServiceImpl implements TestService {
    @Resource
    private TestStudentMapper userMapper;


}
