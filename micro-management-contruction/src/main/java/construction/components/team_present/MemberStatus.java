package construction.components.team_present;

public enum MemberStatus {
    PRESENT("presente"),
    ABSENT("ausente"),
    ON_LEAVE("folga");

    private final String displayName;

    MemberStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static MemberStatus fromDisplayName(String name) {
        for (MemberStatus s : values()) {
            if (s.displayName.equalsIgnoreCase(name)) {
                return s;
            }
        }
        return PRESENT;
    }
}
