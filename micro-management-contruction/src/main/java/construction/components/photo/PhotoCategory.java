package construction.components.photo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PhotoCategory {
    PROGRESS("progresso"),
    ISSUE("problema"),
    BEFORE("antes"),
    AFTER("depois"),
    SAFETY("segurança");

    private final String displayName;

    PhotoCategory(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static PhotoCategory fromString(String value) {
        if (value == null) return null;
        for (PhotoCategory c : values()) {
            if (c.displayName.equalsIgnoreCase(value)) return c;
        }
        try { return PhotoCategory.valueOf(value.toUpperCase()); }
        catch (Exception e) { return PROGRESS; }
    }
}
