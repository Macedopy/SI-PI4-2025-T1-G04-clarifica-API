package construction.components.machinery;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Condition {
    EXCELLENT("otima"),
    GOOD("boa"),
    POOR("ruim"),
    UNAVAILABLE("indisponivel");

    private final String displayName;

    Condition(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static Condition fromString(String value) {
        if (value == null) return null;
        for (Condition c : values()) {
            if (c.displayName.equalsIgnoreCase(value)) return c;
        }
        try { return Condition.valueOf(value.toUpperCase()); }
        catch (Exception e) { return GOOD; }
    }
}
