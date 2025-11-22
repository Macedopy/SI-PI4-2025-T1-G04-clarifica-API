package construction.components.team_present;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MemberStatus {
    PRESENT("presente"),
    ABSENT("ausente"),
    ON_LEAVE("folga");

    private final String displayName;

    MemberStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static MemberStatus fromString(String value) {
        if (value == null) return null;
        for (MemberStatus s : values()) {
            if (s.displayName.equalsIgnoreCase(value)) return s;
        }
        try { return MemberStatus.valueOf(value.toUpperCase()); }
        catch (Exception e) { return PRESENT; }
    }
}
