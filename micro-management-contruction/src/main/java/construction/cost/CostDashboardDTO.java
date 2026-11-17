package construction.cost;

import java.util.List;

public class CostDashboardDTO {
    private String constructionId;
    private String constructionName;
    private double grandTotal;
    private double totalMaterials;
    private double totalTools;
    private double totalMachineryRental;
    private double totalFuel;
    private double totalMaintenance;
    private double totalLabor;
    private double totalMachineryValue;
    private List<CostDashboardService.PhaseCostDTO> phaseCosts;

    public CostDashboardDTO() {}

    // Getters and Setters
    public String getConstructionId() { return constructionId; }
    public void setConstructionId(String constructionId) { this.constructionId = constructionId; }

    public String getConstructionName() { return constructionName; }
    public void setConstructionName(String constructionName) { this.constructionName = constructionName; }

    public double getGrandTotal() { return grandTotal; }
    public void setGrandTotal(double grandTotal) { this.grandTotal = grandTotal; }

    public double getTotalMaterials() { return totalMaterials; }
    public void setTotalMaterials(double totalMaterials) { this.totalMaterials = totalMaterials; }

    public double getTotalTools() { return totalTools; }
    public void setTotalTools(double totalTools) { this.totalTools = totalTools; }

    public double getTotalMachineryRental() { return totalMachineryRental; }
    public void setTotalMachineryRental(double totalMachineryRental) { this.totalMachineryRental = totalMachineryRental; }

    public double getTotalFuel() { return totalFuel; }
    public void setTotalFuel(double totalFuel) { this.totalFuel = totalFuel; }

    public double getTotalMaintenance() { return totalMaintenance; }
    public void setTotalMaintenance(double totalMaintenance) { this.totalMaintenance = totalMaintenance; }

    public double getTotalLabor() { return totalLabor; }
    public void setTotalLabor(double totalLabor) { this.totalLabor = totalLabor; }

    public double getTotalMachineryValue() { return totalMachineryValue; }
    public void setTotalMachineryValue(double totalMachineryValue) { this.totalMachineryValue = totalMachineryValue; }

    public List<CostDashboardService.PhaseCostDTO> getPhaseCosts() { return phaseCosts; }
    public void setPhaseCosts(List<CostDashboardService.PhaseCostDTO> phaseCosts) { this.phaseCosts = phaseCosts; }
}
