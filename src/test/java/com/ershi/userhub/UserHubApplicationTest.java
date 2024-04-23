package com.ershi.userhub;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class UserHubApplicationTest {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test(){
        log.info("Datasource1 = {}", jdbcTemplate.getDataSource());
    }

}
