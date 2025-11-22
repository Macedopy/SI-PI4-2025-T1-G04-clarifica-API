package construction.components.machinery;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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

    @JsonCreator
    public static FuelUnit fromString(String value) {
        if (value == null) return null;
        for (FuelUnit u : values()) {
            if (u.displayName.equalsIgnoreCase(value)) return u;
        }
        try { return FuelUnit.valueOf(value.toUpperCase()); }
        catch (Exception e) { return LITERS; }
    }
}
