package construction.components.used_material;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "materials")
public class Material extends PanacheEntityBase {

    @Id
    private String id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaterialCategory category;

    @Column(name = "consumed_quantity", nullable = false)
    @Min(value = 0, message = "Consumed quantity cannot be negative")
    private double consumedQuantity = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaterialUnit unit;

    @Column(name = "current_stock", nullable = false)
    @Min(value = 0, message = "Current stock cannot be negative")
    private double currentStock = 0.0;

    @Column(name = "minimum_stock", nullable = false)
    @Min(value = 0, message = "Minimum stock cannot be negative")
    private double minimumStock = 10.0;

    @Column(name = "needs_restock", nullable = false)
    private boolean needsRestock = false;

    @Column(name = "urgency")
    private String urgency;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MaterialCategory getCategory() {
        return category;
    }

    public void setCategory(MaterialCategory category) {
        this.category = category;
    }

    public double getConsumedQuantity() {
        return consumedQuantity;
    }

    public void setConsumedQuantity(double consumedQuantity) {
        this.consumedQuantity = consumedQuantity;
    }

    public MaterialUnit getUnit() {
        return unit;
    }

    public void setUnit(MaterialUnit unit) {
        this.unit = unit;
    }

    public double getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(double currentStock) {
        this.currentStock = currentStock;
    }

    public double getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(double minimumStock) {
        this.minimumStock = minimumStock;
    }

    public boolean isNeedsRestock() {
        return needsRestock;
    }

    public void setNeedsRestock(boolean needsRestock) {
        this.needsRestock = needsRestock;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public void consume(double amount) {
        if (amount <= 0) return;
        this.consumedQuantity += amount;
        this.currentStock = Math.max(0, this.currentStock - amount);
        updateRestockStatus();
    }

    public void addStock(double amount) {
        if (amount <= 0) return;
        this.currentStock += amount;
        updateRestockStatus();
    }

    public void updateRestockStatus() {
        this.needsRestock = this.currentStock <= this.minimumStock;
    }

    public String getStockDisplay() {
        return String.format("%.2f %s", currentStock, unit.getSymbol());
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - Stock: %.2f %s", name, category.getDisplayName(), currentStock, unit.getSymbol());
    }
}
