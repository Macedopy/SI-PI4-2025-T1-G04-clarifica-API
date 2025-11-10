package construction.photo;

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

    public static PhotoCategory fromDisplayName(String name) {
        for (PhotoCategory cat : values()) {
            if (cat.displayName.equalsIgnoreCase(name)) {
                return cat;
            }
        }
        return PROGRESS;
    }
}