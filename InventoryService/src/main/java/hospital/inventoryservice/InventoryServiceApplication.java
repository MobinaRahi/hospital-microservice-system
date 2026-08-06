package hospital.inventoryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * InventoryService Application Entry Point
 *
 * Manages:
 * - Drugs and drug categories
 * - Stock management
 * - Suppliers
 * - Purchase orders
 * - Equipment and equipment assignments
 *
 * @author MobinaRahi
 */
@SpringBootApplication
@EnableJpaAuditing
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

}
