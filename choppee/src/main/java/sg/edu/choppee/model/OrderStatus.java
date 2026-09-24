package sg.edu.choppee.model;

/**
 * Lifecycle states of a purchase order.
 * Stored as a VARCHAR string via @Enumerated(EnumType.STRING).
 */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
