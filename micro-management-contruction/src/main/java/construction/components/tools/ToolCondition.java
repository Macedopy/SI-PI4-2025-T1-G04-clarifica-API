package construction.components.tools;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ToolCondition {
    EXCELLENT("ótima"),
    GOOD("boa"),
    POOR("ruim"),
    UNAVAILABLE("indisponível");

    private final String displayName;

    ToolCondition(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    public static ToolCondition fromDisplayName(String name) {
        for (ToolCondition c : values()) {
            if (c.displayName.equalsIgnoreCase(name)) {
                return c;
            }
        }
        return GOOD;
    }
}