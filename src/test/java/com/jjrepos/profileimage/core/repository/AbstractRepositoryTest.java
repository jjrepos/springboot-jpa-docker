package com.jjrepos.profileimage.core.repository;

import com.jjrepos.profileimage.core.entity.ImageEntity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;


@DataJpaTest
@ContextConfiguration(classes = {RepositoryConfig.class})
@EntityScan(basePackageClasses = {ImageEntity.class})
public abstract class AbstractRepositoryTest {
}
