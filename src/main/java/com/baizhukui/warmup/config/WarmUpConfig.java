package com.baizhukui.warmup.config;

import com.baizhukui.warmup.web.WarmUpController;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author songxychina@gmail.com
 * @date 2024/09/08
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(WarmUpProperties.class)
public class WarmUpConfig {

    @Bean
    @ConditionalOnMissingBean
    public WarmUpListener warmUpListener(ApplicationContext applicationContext, ServerProperties serverProperties, WarmUpProperties warmUpProperties) {
        return new WarmUpListener(serverProperties, applicationContext, warmUpProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public WarmUpController warmUpController() {
        return new WarmUpController();
    }

}
