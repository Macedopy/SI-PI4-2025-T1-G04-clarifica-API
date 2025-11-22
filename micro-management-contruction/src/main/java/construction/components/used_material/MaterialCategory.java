package construction.components.used_material;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MaterialCategory {
    CEMENT("Cimento"),
    SAND("Areia"),
    STEEL("Ferro"),
    BRICK("Tijolo"),
    WOOD("Madeira"),
    ELECTRICAL("Eletrica"),
    PLUMBING("Hidráulica"),
    OTHER("Outros");

    private final String displayName;

    MaterialCategory(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static MaterialCategory fromValue(String value) {
        if (value == null || value.isEmpty()) {
            return OTHER;
        }
        for (MaterialCategory c : values()) {
            if (c.displayName.equalsIgnoreCase(value.trim())) {
                return c;
            }
        }
        return OTHER; // fallback seguro
    }
}
