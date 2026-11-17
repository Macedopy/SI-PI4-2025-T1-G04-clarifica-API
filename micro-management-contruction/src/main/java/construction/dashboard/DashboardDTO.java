package construction.dashboard;

import java.util.List;
import java.util.Map;

public class DashboardDTO {
    private String constructionId;
    private String constructionName;
    private String constructionStatus;
    private double totalPlannedHours;
    private double totalExecutedHours;
    private int overallProgress;
    private Map<String, Integer> phasesProgress;
    private List<PhaseProgressDTO> phases;
    private int totalServices;
    private int completedServices;
    private int inProgressServices;
    private int plannedServices;

    public DashboardDTO() {}

    public DashboardDTO(String constructionId, double totalPlannedHours, double totalExecutedHours,
                        int overallProgress, Map<String, Integer> phasesProgress) {
        this.constructionId = constructionId;
        this.totalPlannedHours = totalPlannedHours;
        this.totalExecutedHours = totalExecutedHours;
        this.overallProgress = overallProgress;
        this.phasesProgress = phasesProgress;
    }

    public String getConstructionId() { return constructionId; }
    public void setConstructionId(String constructionId) { this.constructionId = constructionId; }

    public String getConstructionName() { return constructionName; }
    public void setConstructionName(String constructionName) { this.constructionName = constructionName; }

    public String getConstructionStatus() { return constructionStatus; }
    public void setConstructionStatus(String constructionStatus) { this.constructionStatus = constructionStatus; }

    public double getTotalPlannedHours() { return totalPlannedHours; }
    public void setTotalPlannedHours(double totalPlannedHours) { this.totalPlannedHours = totalPlannedHours; }

    public double getTotalExecutedHours() { return totalExecutedHours; }
    public void setTotalExecutedHours(double totalExecutedHours) { this.totalExecutedHours = totalExecutedHours; }

    public int getOverallProgress() { return overallProgress; }
    public void setOverallProgress(int overallProgress) { this.overallProgress = overallProgress; }

    public Map<String, Integer> getPhasesProgress() { return phasesProgress; }
    public void setPhasesProgress(Map<String, Integer> phasesProgress) { this.phasesProgress = phasesProgress; }

    public List<PhaseProgressDTO> getPhases() { return phases; }
    public void setPhases(List<PhaseProgressDTO> phases) { this.phases = phases; }

    public int getTotalServices() { return totalServices; }
    public void setTotalServices(int totalServices) { this.totalServices = totalServices; }

    public int getCompletedServices() { return completedServices; }
    public void setCompletedServices(int completedServices) { this.completedServices = completedServices; }

    public int getInProgressServices() { return inProgressServices; }
    public void setInProgressServices(int inProgressServices) { this.inProgressServices = inProgressServices; }

    public int getPlannedServices() { return plannedServices; }
    public void setPlannedServices(int plannedServices) { this.plannedServices = plannedServices; }

    public static class PhaseProgressDTO {
        private String phaseId;
        private String phaseName;
        private String contractor;
        private int progress;
        private int totalServices;
        private int completedServices;
        private double plannedHours;
        private double executedHours;

        public PhaseProgressDTO() {}

        public PhaseProgressDTO(String phaseId, String phaseName, String contractor, int progress,
                                int totalServices, int completedServices, double plannedHours, double executedHours) {
            this.phaseId = phaseId;
            this.phaseName = phaseName;
            this.contractor = contractor;
            this.progress = progress;
            this.totalServices = totalServices;
            this.completedServices = completedServices;
            this.plannedHours = plannedHours;
            this.executedHours = executedHours;
        }

        public String getPhaseId() { return phaseId; }
        public void setPhaseId(String phaseId) { this.phaseId = phaseId; }

        public String getPhaseName() { return phaseName; }
        public void setPhaseName(String phaseName) { this.phaseName = phaseName; }

        public String getContractor() { return contractor; }
        public void setContractor(String contractor) { this.contractor = contractor; }

        public int getProgress() { return progress; }
        public void setProgress(int progress) { this.progress = progress; }

        public int getTotalServices() { return totalServices; }
        public void setTotalServices(int totalServices) { this.totalServices = totalServices; }

        public int getCompletedServices() { return completedServices; }
        public void setCompletedServices(int completedServices) { this.completedServices = completedServices; }

        public double getPlannedHours() { return plannedHours; }
        public void setPlannedHours(double plannedHours) { this.plannedHours = plannedHours; }

        public double getExecutedHours() { return executedHours; }
        public void setExecutedHours(double executedHours) { this.executedHours = executedHours; }
    }
}
