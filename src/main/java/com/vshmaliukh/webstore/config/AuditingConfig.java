package com.vshmaliukh.webstore.config;

import com.vshmaliukh.webstore.login.CustomAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "aware")
public class AuditingConfig {

    private final CustomAuditorAware customAuditorAware;

    public AuditingConfig(CustomAuditorAware customAuditorAware) {
        this.customAuditorAware = customAuditorAware;
    }

    @Bean(name = "aware")
    public AuditorAware<String> auditorProvider() {
        return customAuditorAware;
    }

}
