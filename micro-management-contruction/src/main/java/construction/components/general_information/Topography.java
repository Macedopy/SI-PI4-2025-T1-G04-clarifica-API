package construction.components.general_information;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Topography {
    FLAT("plana"),
    SLOPED("inclinada"),
    HILLY("colinoso"),
    MOUNTAINOUS("montanhoso");

    private final String displayName;

    Topography(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static Topography fromString(String value) {
        if (value == null) return null;
        for (Topography t : values()) {
            if (t.displayName.equalsIgnoreCase(value)) return t;
        }
        try { return Topography.valueOf(value.toUpperCase()); }
        catch (Exception e) { return FLAT; }
    }
}