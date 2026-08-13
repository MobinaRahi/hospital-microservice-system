package hospital.notificationservice.model;

import hospital.notificationservice.model.enums.TemplateType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Represents a notification template for reusable notification content.
 * Templates can be used for both SMS and Email notifications.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Templates have a name for easy identification</li>
 *   <li>type determines whether it's for SMS or Email</li>
 *   <li>subject is used for email templates only</li>
 *   <li>content can contain variables for dynamic replacement</li>
 *   <li>variables define the available placeholders in the content</li>
 *   <li>isActive flag controls whether the template can be used</li>
 * </ul>
 *
 * <p><strong>Variable Syntax:</strong></p>
 * <p>Variables are stored as comma-separated names and used in content
 * with {{variableName}} syntax.</p>
 *
 * <p><strong>Example:</strong></p>
 * <pre>
 * name: "Appointment Reminder SMS"
 * type: SMS
 * content: "Dear {{patientName}}, your appointment with {{doctorName}} is on {{appointmentDate}} at {{appointmentTime}}."
 * variables: "patientName,doctorName,appointmentDate,appointmentTime"
 * </pre>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "notification_templates",
        indexes = {
                @Index(name = "idx_template_type", columnList = "type"),
                @Index(name = "idx_template_name", columnList = "name"),
                @Index(name = "idx_template_is_active", columnList = "is_active")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class NotificationTemplate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Template name for easy identification.
     * Example: "Appointment Reminder SMS", "Lab Result Email"
     */
    @Column(nullable = false, length = 200)
    private String name;

    /**
     * Type of template (SMS or EMAIL).
     * Determines the channel through which notifications using this template will be sent.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TemplateType type;

    /**
     * Email subject line (for EMAIL type templates only).
     * Null for SMS templates.
     * Example: "Your Lab Results Are Ready"
     */
    @Column(length = 500)
    private String subject;

    /**
     * Template content with variable placeholders.
     * Variables are enclosed in {{variableName}} syntax.
     * Example: "Dear {{patientName}}, your result is ready."
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * Comma-separated list of available variables in the content.
     * Example: "patientName,doctorName,appointmentDate"
     */
    @Column(length = 1000)
    private String variables;

    /**
     * Whether this template is currently active and can be used.
     * Inactive templates cannot be used for new notifications.
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Date and time when the template was last updated.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ════════════════════════════════════════════════════════════════════
    // Business Logic Methods
    // ════════════════════════════════════════════════════════════════════

    /**
     * Activates this template.
     * Sets isActive=true.
     */
    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Deactivates this template.
     * Sets isActive=false.
     * Inactive templates cannot be used for new notifications.
     */
    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Checks if the template is active.
     *
     * @return true if the template is active and can be used
     */
    public boolean isActiveTemplate() {
        return Boolean.TRUE.equals(this.isActive);
    }

    /**
     * Checks if this is an SMS template.
     *
     * @return true if type is SMS
     */
    public boolean isSmsTemplate() {
        return this.type == TemplateType.SMS;
    }

    /**
     * Checks if this is an Email template.
     *
     * @return true if type is EMAIL
     */
    public boolean isEmailTemplate() {
        return this.type == TemplateType.EMAIL;
    }

    /**
     * Gets the list of variable names defined in this template.
     *
     * @return array of variable names, or empty array if no variables
     */
    public String[] getVariableList() {
        if (this.variables == null || this.variables.isBlank()) {
            return new String[0];
        }
        return this.variables.split(",");
    }

    /**
     * Replaces variables in the content with provided values.
     *
     * @param replacements map of variable names to their values
     * @return content with variables replaced
     */
    public String renderContent(java.util.Map<String, String> replacements) {
        String rendered = this.content;
        if (replacements != null) {
            for (java.util.Map.Entry<String, String> entry : replacements.entrySet()) {
                rendered = rendered.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
        }
        return rendered;
    }

    /**
     * Updates the template content and variables.
     *
     * @param content   the new content
     * @param variables the new variables
     */
    public void updateContent(String content, String variables) {
        this.content = content;
        this.variables = variables;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates the template name.
     *
     * @param name the new name
     */
    public void updateName(String name) {
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }
}
