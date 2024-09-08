package com.baizhukui.warmup.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author songxychina@gmail.com
 * @date 2024/09/08
 */
@ConfigurationProperties(prefix = "warmup")
@Configuration
@Setter
@Getter
public class WarmUpProperties {
    private boolean enabled = true;
}
