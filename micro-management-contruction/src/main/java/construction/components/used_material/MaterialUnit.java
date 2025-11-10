package construction.components.used_material;

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

    public static MaterialUnit fromSymbol(String symbol) {
        for (MaterialUnit unit : values()) {
            if (unit.symbol.equalsIgnoreCase(symbol)) {
                return unit;
            }
        }
        return UNIT; // fallback
    }
}
