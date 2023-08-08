package com.diac.ydeas.ideas.repository;

import com.diac.ydeas.ideas.config.DataConfig;
import com.diac.ydeas.ideas.repository.container.PostgreSQLTestContainersExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {
        DataConfig.class
})
@ExtendWith(PostgreSQLTestContainersExtension.class)
@DirtiesContext
public abstract class AbstractRepositoryIntegrationTest {

}