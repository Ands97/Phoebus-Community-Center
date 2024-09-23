package com.community_center.community_center.application.config;

import com.community_center.community_center.application.service.CommunityCenterService;
import com.community_center.community_center.application.service.CommunityCenterServiceImpl;
import org.springframework.context.annotation.Bean;

public class ServiceConfig {

    @Bean
    public CommunityCenterService communityCenterService() {
        return new CommunityCenterServiceImpl();
    }
}
