package io.thiiagoms.ims.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required.")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Description is required.")
    @Column(name = "descritpion", nullable = false)
    private String description;

    @NotBlank(message = "SKU is required.")
    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @NotBlank(message = "Image is required.")
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Positive(message = "Product price must be a positive value.")
    @Column(nullable = false)
    private BigDecimal price;

    @Positive(message = "Product stock quantity must be a positive number.")
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @NotBlank(message = "Expiry Date is required.")
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return """
                {
                    "id": "%d",
                    "name": "%s",
                    "description": "%s",
                    "sku": "%s",
                    "image_url": "%s",
                    "price": "%s",
                    "stock_quantity": "%d",
                    "expiry_date": "%s",
                    "created_at": "%s"
                }
                """.formatted(
                    this.id,
                    this.name,
                    this.description,
                    this.sku,
                    this.imageUrl,
                    this.price.toString(),
                    this.stockQuantity,
                    this.expiryDate.toString(),
                    this.createdAt.toString()
                );
    }
}
