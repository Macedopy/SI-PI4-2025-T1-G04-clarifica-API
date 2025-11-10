public package construction.machinery;

public enum Condition {
    EXCELLENT("ótima"),
    GOOD("boa"),
    POOR("ruim"),
    UNAVAILABLE("indisponível");

    private final String displayName;

    Condition(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    public static Condition fromDisplayName(String name) {
        for (Condition c : values()) {
            if (c.displayName.equalsIgnoreCase(name)) {
                return c;
            }
        }
        return GOOD;
    }
} {
    
}
