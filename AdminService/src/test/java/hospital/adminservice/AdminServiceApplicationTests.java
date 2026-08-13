package hospital.adminservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration test that loads the full Spring context.
 * Uses 'test' profile to avoid loading real database configuration.
 *
 * @author MobinaRahi
 */
@SpringBootTest
@ActiveProfiles("test")
class AdminServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
