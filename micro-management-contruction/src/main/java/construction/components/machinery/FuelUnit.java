package construction.machinery;

public enum FuelUnit {
    LITERS("litros"),
    HOURS("horas");

    private final String displayName;

    FuelUnit(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    public static FuelUnit fromDisplayName(String name) {
        for (FuelUnit unit : values()) {
            if (unit.displayName.equalsIgnoreCase(name)) {
                return unit;
            }
        }
        return LITERS;
    }
}