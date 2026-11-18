-- Adiciona campos de internacionalização e template na tabela constructions
ALTER TABLE constructions ADD COLUMN IF NOT EXISTS country_code VARCHAR(10) DEFAULT 'BR';
ALTER TABLE constructions ADD COLUMN IF NOT EXISTS currency_code VARCHAR(3) DEFAULT 'BRL';
ALTER TABLE constructions ADD COLUMN IF NOT EXISTS measurement_system VARCHAR(20) DEFAULT 'metric';
ALTER TABLE constructions ADD COLUMN IF NOT EXISTS state_province_code VARCHAR(20);
ALTER TABLE constructions ADD COLUMN IF NOT EXISTS municipality VARCHAR(100);
ALTER TABLE constructions ADD COLUMN IF NOT EXISTS construction_method VARCHAR(50);
ALTER TABLE constructions ADD COLUMN IF NOT EXISTS template_id VARCHAR(36);
ALTER TABLE constructions ADD COLUMN IF NOT EXISTS language VARCHAR(5) DEFAULT 'pt';

-- Adiciona campos de controle financeiro e status na tabela phases
ALTER TABLE phases ADD COLUMN IF NOT EXISTS template_phase_id VARCHAR(50);
ALTER TABLE phases ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'not_started';
ALTER TABLE phases ADD COLUMN IF NOT EXISTS planned_start_date DATE;
ALTER TABLE phases ADD COLUMN IF NOT EXISTS planned_end_date DATE;
ALTER TABLE phases ADD COLUMN IF NOT EXISTS actual_start_date DATE;
ALTER TABLE phases ADD COLUMN IF NOT EXISTS actual_end_date DATE;
ALTER TABLE phases ADD COLUMN IF NOT EXISTS budget_amount DECIMAL(15,2) DEFAULT 0;
ALTER TABLE phases ADD COLUMN IF NOT EXISTS currency_code VARCHAR(3);
