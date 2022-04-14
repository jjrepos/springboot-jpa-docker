package com.jjrepos.profileimage.core.service;

import com.jjrepos.profileimage.core.entity.ImageEntity;
import com.jjrepos.profileimage.core.repository.RepositoryConfig;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;


@DataJpaTest
@ContextConfiguration(classes = {RepositoryConfig.class, ServiceConfig.class})
@EntityScan(basePackageClasses = {ImageEntity.class})
@ComponentScan(basePackageClasses = ProfileImageService.class)
@Transactional
public abstract class AbstractServiceTest {

}
