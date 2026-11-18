-- Atividades dentro de cada fase de uma obra (instância do template)
CREATE TABLE IF NOT EXISTS phase_activities (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    phase_id VARCHAR(36) NOT NULL REFERENCES phases(id) ON DELETE CASCADE,
    activity_id VARCHAR(50) NOT NULL,         -- ID estável: 'foundation_concrete'
    name_key VARCHAR(100) NOT NULL,           -- chave de tradução
    cost_category_id VARCHAR(50),             -- concreto, mão de obra, etc.
    status VARCHAR(20) DEFAULT 'not_started', -- not_started, scheduled, in_progress, blocked, pending_inspection, inspection_failed, completed, verified, not_applicable
    sequence INTEGER,
    is_optional BOOLEAN DEFAULT false,
    budget_amount DECIMAL(15,2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_phase_activities_phase ON phase_activities(phase_id);
CREATE INDEX IF NOT EXISTS idx_phase_activities_status ON phase_activities(status);
