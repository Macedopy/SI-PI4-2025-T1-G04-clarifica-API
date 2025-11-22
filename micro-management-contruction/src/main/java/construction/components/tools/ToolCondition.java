package construction.components.tools;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ToolCondition {
    EXCELLENT("otima"),
    GOOD("boa"),
    POOR("ruim"),
    UNAVAILABLE("indisponivel");

    private final String displayName;

    ToolCondition(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static ToolCondition fromString(String value) {
        if (value == null) return null;
        for (ToolCondition c : values()) {
            if (c.displayName.equalsIgnoreCase(value)) return c;
        }
        try { return ToolCondition.valueOf(value.toUpperCase()); }
        catch (Exception e) { return GOOD; }
    }
}
