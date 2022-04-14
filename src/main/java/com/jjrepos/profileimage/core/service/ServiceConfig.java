package com.jjrepos.profileimage.core.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;

@Profile("!test")
@Configuration
@EnableAsync
public class ServiceConfig {
}

