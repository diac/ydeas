package repository.container;

import org.testcontainers.containers.PostgreSQLContainer;

public class NotificationsPostgreSQLContainer extends PostgreSQLContainer<NotificationsPostgreSQLContainer> {

    private static final String DB_DOCKER_IMAGE = "postgres:14.8";

    private static final String TEST_DB_NAME = "ideas-integration-tests-db";

    private static final String TEST_DB_USERNAME = "sa";

    private static final String TEST_DB_PASSWORD = "sa";

    private static NotificationsPostgreSQLContainer container;

    private NotificationsPostgreSQLContainer() {
        super(DB_DOCKER_IMAGE);
        this.withDatabaseName(TEST_DB_NAME);
        this.withUsername(TEST_DB_USERNAME);
        this.withPassword(TEST_DB_PASSWORD);
    }

    public static NotificationsPostgreSQLContainer getInstance() {
        if (container == null) {
            container = new NotificationsPostgreSQLContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {

    }
}