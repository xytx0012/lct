package com.xytx;

import com.xytx.service.IncomeService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JcbDataserviceApplicationTests {
@Resource
private IncomeService incomeService;
    @Test
    void contextLoads() {
    }

}
