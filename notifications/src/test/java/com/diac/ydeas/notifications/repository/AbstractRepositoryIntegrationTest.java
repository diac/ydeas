package com.diac.ydeas.notifications.repository;

import com.diac.ydeas.notifications.config.DataConfig;
import com.diac.ydeas.notifications.repository.container.PostgreSQLTestContainersExtension;
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