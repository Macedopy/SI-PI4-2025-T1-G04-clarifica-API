-- Templates de fases por país/jurisdição (ex: Residencial EUA/Canadá, Residencial Brasil)
CREATE TABLE IF NOT EXISTS project_templates (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    country_code VARCHAR(10) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_default BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT NOW()
);

-- Índice para busca rápida por país
CREATE INDEX IF NOT EXISTS idx_project_templates_country ON project_templates(country_code);
