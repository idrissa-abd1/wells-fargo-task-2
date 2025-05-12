package com.wellsfargo.counselor.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import jakarta.persistence.*; // Ensure all imports are from jakarta.persistence
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Financial Advisor in the system.
 * Maps to the 'FinancialAdvisors' table.
 */
@Entity
@Table(name = "FinancialAdvisors") // Explicitly map to the SQL table name
public class Advisor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use IDENTITY strategy for MySQL AUTO_INCREMENT
    private Long advisorId; // Use Long instead of int

    // Removed firstName and lastName, using a single 'name' field as per DDL
    @Column(nullable = false, length = 255)
    private String name;

    // Added fields from DDL
    @Column(unique = true, length = 100) // Added unique constraint
    private String employeeId;

    @Column(length = 255) // Max length from DDL
    private String contactInformation;

    // Removed address, phone, email as they are covered by contactInformation or not in final DDL

    // Added timestamp fields from DDL
    // These are typically managed by the database (DEFAULT CURRENT_TIMESTAMP),
    // but mapping them allows reading the values.
    // Use insertable=false, updatable=false if JPA should not set them.
    @Column(nullable = false, updatable = false) // Cannot be null, not updated after creation
    private LocalDateTime createdAt;

    @Column(nullable = false) // Cannot be null, updated on modification
    private LocalDateTime updatedAt;

    // --- Relationships ---

    // One Advisor has Many Clients
    // mappedBy="advisor" indicates that the 'advisor' field in the Client entity owns the relationship.
    // FetchType.LAZY means the relationship is lazy-loaded, which means it is only loaded when needed.
    // CascadeType.ALL means operations (persist, merge, remove, etc.) on Advisor cascade to associated Clients.
    // orphanRemoval=true means if a Client is removed from this list, it should be deleted from the database.
    @OneToMany(mappedBy = "advisor", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Client> clients;

    // --- Constructors ---

    // JPA requires a no-arg constructor
    protected Advisor() {
    }

    // Constructor for creating new Advisors (without ID, createdAt, updatedAt, clients)
    public Advisor(String name, String employeeId, String contactInformation) {
        this.name = name;
        this.employeeId = employeeId;
        this.contactInformation = contactInformation;
    }

    // --- Getters and Setters ---
    // No setter for advisorId as it's auto-generated

    public Long getAdvisorId() {
        return advisorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // No setter for createdAt if managed by DB/JPA lifecycle

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // No setter for updatedAt if managed by DB/JPA lifecycle

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    // --- equals(), hashCode(), toString() ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Advisor advisor = (Advisor) o;
        // Use ID for equality check if available, otherwise rely on unique fields like employeeId
        return advisorId != null ? advisorId.equals(advisor.advisorId) : employeeId != null && employeeId.equals(advisor.employeeId);
    }

    @Override
    public int hashCode() {
        // Use ID for hash code if available, otherwise rely on unique fields
        return advisorId != null ? Objects.hash(advisorId) : Objects.hash(employeeId);
    }

    @Override
    public String toString() {
        return "Advisor{" +
                "advisorId=" + advisorId +
                ", name='" + name + '\'' +
                ", employeeId='" + employeeId + '\'' +
                '}';
    }
}
