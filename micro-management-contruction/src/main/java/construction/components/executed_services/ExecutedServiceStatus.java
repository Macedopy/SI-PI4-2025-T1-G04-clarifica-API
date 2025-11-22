package construction.components.executed_services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ExecutedServiceStatus {
    PLANNED("planejado"),
    STARTED("iniciado"),
    IN_PROGRESS("andamento"),
    COMPLETED("concluido"),
    CONCLUIDO("concluido"),
    EM_ANDAMENTO("andamento"),
    PLANEJADO("planejado"),
    INICIADO("iniciado");

    private final String displayName;

    ExecutedServiceStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ExecutedServiceStatus fromValue(String value) {
        if (value == null) return null;

        // Aceita: "concluido", "CONCLUIDO", "COMPLETED", "concluído", etc
        for (ExecutedServiceStatus s : values()) {
            if (s.displayName.equalsIgnoreCase(value) || 
                s.name().equalsIgnoreCase(value.replace(" ", "_"))) {
                return s;
            }
        }
        return PLANNED; // fallback
    }
}