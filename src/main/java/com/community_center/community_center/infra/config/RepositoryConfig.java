package com.community_center.community_center.infra.config;

import com.community_center.community_center.domain.repository.CommunityCenterRepository;
import com.community_center.community_center.domain.repository.ResourceExchangeRepository;
import com.community_center.community_center.infra.data.repository.CommunityCenterRepositoryImpl;
import com.community_center.community_center.infra.data.repository.ResourceExchangeRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public CommunityCenterRepository communityCenterRepository () {
        return new CommunityCenterRepositoryImpl();
    }

    @Bean
    public ResourceExchangeRepository resourceExchangeRepository () {
        return new ResourceExchangeRepositoryImpl();
    }

}
