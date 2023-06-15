package com.diac.ydeas.ideas.repository;

import com.diac.ydeas.ideas.config.DataConfig;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@ContextConfiguration(classes = {
        DataConfig.class
})
public abstract class AbstractRepositoryIntegrationTest {
}