package construction.components.used_material;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MaterialUnit {
    BAG("saco"),
    KG("kg"),
    CUBIC_METER("m³"),
    UNIT("unidade"),
    LITER("litro");

    private final String symbol;

    MaterialUnit(String symbol) {
        this.symbol = symbol;
    }

    @JsonValue
    public String getSymbol() {
        return symbol;
    }

    @JsonCreator
    public static MaterialUnit fromString(String value) {
        if (value == null) return null;
        for (MaterialUnit u : values()) {
            if (u.symbol.equalsIgnoreCase(value)) return u;
        }
        try { return MaterialUnit.valueOf(value.toUpperCase().replace("³", "IC_METER")); }
        catch (Exception e) { return UNIT; }
    }
}
