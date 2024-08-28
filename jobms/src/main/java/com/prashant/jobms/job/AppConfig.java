package com.prashant.jobms.job;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

//another way of using restTemplate as we need to communicate with microservice with it ids/names from the eureka cloud
//with OpenFeign we do not need this configuration
//as we are using OpenFeign we can remove it as a Bean
@Configuration
public class AppConfig {
   // @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
