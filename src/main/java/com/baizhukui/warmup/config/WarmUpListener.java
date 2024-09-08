/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baizhukui.warmup.config;

import com.baizhukui.warmup.web.WarmUpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author songxychina@gmail.com
 * @date 2024/09/08
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class WarmUpListener implements ApplicationListener<ApplicationReadyEvent> {
    private final ServerProperties serverProperties;
    private final ApplicationContext applicationContext;
    private final WarmUpProperties warmUpProperties;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (!warmUpProperties.isEnabled()) {
            log.warn("Warm up is disabled.");
            return;
        }

        webWarmUp(event);
        myBatisPlusWarmUp();
        jpaWarmUp();

        log.info("Warm up completed.");
    }

    private void webWarmUp(ApplicationReadyEvent event) {
        if (event.getApplicationContext() instanceof ServletWebServerApplicationContext) {
            log.info("Starting web warm up.");

            AnnotationConfigServletWebServerApplicationContext context =
                    (AnnotationConfigServletWebServerApplicationContext) event.getApplicationContext();
            String contextPath = serverProperties.getServlet().getContextPath();
            int port = serverProperties.getPort() != null && serverProperties.getPort() != 0 ?
                    serverProperties.getPort() : context.getWebServer().getPort();
            if (contextPath == null) {
                contextPath = "";
            }
            final String url = "http://localhost:" + port + contextPath + "/actuator/warm-up";
            log.info("Web warm up endpoint: {}", url);

            RestTemplate restTemplate = new RestTemplate();

            final WarmUpRequest warmUpRequest = new WarmUpRequest();
            warmUpRequest.setValidTrue(true);
            warmUpRequest.setValidFalse(false);
            warmUpRequest.setValidString("warm up");
            warmUpRequest.setValidNumber(15);
            warmUpRequest.setValidBigDecimal(BigDecimal.TEN);

            ResponseEntity<String> response = restTemplate.postForEntity(url, warmUpRequest, String.class);
            log.info("Web warm up response: {}", response);
        }
    }

    private void myBatisPlusWarmUp() {
        log.info("Starting mybatis plus warm up.");
        try {
            Class<?> baseMapperClass = Class.forName("com.baomidou.mybatisplus.core.mapper.BaseMapper");
            Map<String, ?> baseMapperMap = applicationContext.getBeansOfType(baseMapperClass);
            baseMapperMap.forEach((mapperName, baseMapper) -> {
                Method method = ReflectionUtils.findMethod(baseMapperClass, "selectById", Serializable.class);
                ReflectionUtils.invokeMethod(method, baseMapper, 1);
                log.info("mybatis plus warm up completed for {}", mapperName);
            });
        } catch (ClassNotFoundException ignored) {
            log.info("Mybatis plus warm up skipped because Mybatis Plus is not on the classpath");
        } catch (Exception e) {
            log.error("Failed to complete mybatis plus warm up", e);
        }
        log.info("mybatis plus warm up completed.");
    }

    private void jpaWarmUp() {
        log.info("Starting jpa warm up.");
        try {
            Class<?> crudRepositoryClass = Class.forName("org.springframework.data.repository.CrudRepository");
            Map<String, ?> crudRepositoryMap = applicationContext.getBeansOfType(crudRepositoryClass);
            crudRepositoryMap.forEach((repositoryName, crudRepository) -> {
                Method method = ReflectionUtils.findMethod(crudRepositoryClass, "findById", Object.class);
                ReflectionUtils.invokeMethod(method, crudRepository, 1);
                log.info("jpa warm up completed for {}", repositoryName);
            });
        } catch (ClassNotFoundException ignored) {
            log.info("JPA warm up skipped because Spring Data JPA is not on the classpath");
        } catch (Exception e) {
            log.error("Failed to complete jpa warm up", e);
        }
        log.info("jpa warm up completed.");
    }

}
