package com.ll.rsv.global.initData;

import com.ll.rsv.global.app.AppConfig;
import com.ll.rsv.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

@Profile("dev")
@Configuration
@RequiredArgsConstructor
public class Dev {
    @Bean
    @Order(4)
    // 실행 시 자동으로 변환하게 만듬
    ApplicationRunner initDev() {
        return args -> {
            String backUrl = AppConfig.getSiteBackUrl();
            String cmd = "npx openapi-typescript " + backUrl + "/v3/api-docs/apiV1 -o ./front/src/lib/types/api/v1/schema.d.ts";
            Ut.cmd.runAsync(cmd);
        };
    }
}