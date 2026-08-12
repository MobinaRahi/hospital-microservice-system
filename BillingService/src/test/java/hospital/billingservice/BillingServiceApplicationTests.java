package hospital.billingservice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration test that loads the full Spring context.
 * Disabled by default because it requires a running database.
 */
@SpringBootTest
@Disabled("Requires running database - enable for integration tests")
class BillingServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
