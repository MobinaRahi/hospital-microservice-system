package hospital.billingservice.repository;

import hospital.billingservice.model.InvoiceItem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for InvoiceItem entity.
 *
 * @author MobinaRahi
 */
@Repository
public interface InvoiceItemRepository extends BaseEntityRepository<InvoiceItem, Long> {

    /**
     * Finds all items for a specific invoice.
     *
     * @param invoiceId the invoice ID
     * @return list of invoice items
     */
    List<InvoiceItem> findByInvoiceId(Long invoiceId);

    /**
     * Finds items by service code.
     * Useful for reporting: "how many times was service X billed?"
     *
     * @param serviceCode the service code
     * @return list of invoice items with the service code
     */
    List<InvoiceItem> findByServiceCode(String serviceCode);

    /**
     * Finds a specific item within an invoice.
     *
     * @param invoiceId the invoice ID
     * @param id        the item ID
     * @return invoice item if found
     */
    Optional<InvoiceItem> findByInvoiceIdAndId(Long invoiceId, Long id);
}
