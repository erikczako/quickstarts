package dev.notioniq.quickstarts;

import io.quarkiverse.amazon.dynamodb.enhanced.runtime.NamedDynamoDbTable;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.logging.Log;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;

@QuarkusMain
@IfBuildProfile("dev")
public class Application {

    public static void main(String... args) {
        Quarkus.run(DynamoDbLocal.class, args);
    }

    public static class DynamoDbLocal implements QuarkusApplication {

        @Inject
        @NamedDynamoDbTable(ShoppingCart.TABLE_NAME)
        private DynamoDbTable<ShoppingCart> dynamoDbTable;

        @Override
        public int run(String... args) {
            try {
                dynamoDbTable.createTable();
            } catch (ResourceInUseException exception) {
                Log.info("Table already created");
            }

            Quarkus.waitForExit();
            return 0;
        }
    }
}
