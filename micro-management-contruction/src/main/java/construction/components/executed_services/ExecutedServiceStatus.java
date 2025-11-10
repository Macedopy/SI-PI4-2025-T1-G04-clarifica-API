package construction.components.executed_services;


public enum ExecutedServiceStatus {
    PLANNED("planejado"),
    STARTED("iniciado"),
    IN_PROGRESS("andamento"),
    COMPLETED("concluido");

    private final String displayName;

    ExecutedServiceStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ExecutedServiceStatus fromDisplayName(String name) {
        for (ExecutedServiceStatus s : values()) {
            if (s.displayName.equalsIgnoreCase(name)) {
                return s;
            }
        }
        return PLANNED;
    }
}