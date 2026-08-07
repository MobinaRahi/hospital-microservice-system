package hospital.inventoryservice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration test that loads the full Spring context.
 * Disabled by default because it requires a running database.
 * Enable when running against a test database profile.
 */
@SpringBootTest
@Disabled("Requires running database - enable for integration tests")
class InventoryServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
