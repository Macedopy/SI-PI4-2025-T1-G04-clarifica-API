//Responsável: Bruno Macedo

package construction.components.general_information;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "general_information")
public class GeneralInformation extends PanacheEntityBase {

    @Id
    private String id;

    @Column(name = "construction_id", nullable = false, unique = true)
    private String constructionId;

    @NotBlank(message = "Address is required")
    @Column(nullable = false, length = 500)
    private String address;

    @Min(value = 0, message = "Land area must be positive")
    @Column(name = "land_area", nullable = false)
    private double landArea;

    @Pattern(
        regexp = "plana|inclinada|acidentada|com desnível",
        message = "Topography must be one of: plana, inclinada, acidentada, com desnível"
    )
    @Column(nullable = false)
    private String topography;

    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "technical_manager", length = 200)
    private String technicalManager;

    @Column(length = 1000)
    private String observations;

    public GeneralInformation() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConstructionId() {
        return constructionId;
    }

    public void setConstructionId(String constructionId) {
        this.constructionId = constructionId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLandArea() {
        return landArea;
    }

    public void setLandArea(double landArea) {
        this.landArea = landArea;
    }

    public String getTopography() {
        return topography;
    }

    public void setTopography(String topography) {
        this.topography = topography;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getTechnicalManager() {
        return technicalManager;
    }

    public void setTechnicalManager(String technicalManager) {
        this.technicalManager = technicalManager;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}
