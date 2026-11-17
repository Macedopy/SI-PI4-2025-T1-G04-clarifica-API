-- Migração: Controle de Custos e Estoque da Obra
-- Tabelas para rastrear materiais, ferramentas, maquinários, equipe e custos financeiros

-- 1. Compras de materiais
CREATE TABLE IF NOT EXISTS public.material_purchases (
    id character varying(255) PRIMARY KEY,
    material_id character varying(255),
    phase_id character varying(255) NOT NULL,
    quantity double precision NOT NULL DEFAULT 0,
    unit_price double precision NOT NULL DEFAULT 0,
    total_price double precision NOT NULL DEFAULT 0,
    purchase_date date NOT NULL DEFAULT CURRENT_DATE,
    purchased_by character varying(255) NOT NULL,
    supplier character varying(255),
    notes text,
    created_at timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_material_purchases_phase FOREIGN KEY (phase_id) REFERENCES public.phases(id) ON DELETE CASCADE,
    CONSTRAINT fk_material_purchases_material FOREIGN KEY (material_id) REFERENCES public.materials(id) ON DELETE SET NULL,
    CONSTRAINT fk_material_purchases_user FOREIGN KEY (purchased_by) REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_material_purchases_phase ON public.material_purchases(phase_id);
CREATE INDEX IF NOT EXISTS idx_material_purchases_date ON public.material_purchases(purchase_date);
CREATE INDEX IF NOT EXISTS idx_material_purchases_user ON public.material_purchases(purchased_by);

-- 2. Compras de ferramentas
CREATE TABLE IF NOT EXISTS public.tool_purchases (
    id character varying(255) PRIMARY KEY,
    tool_id character varying(255),
    phase_id character varying(255) NOT NULL,
    quantity integer NOT NULL DEFAULT 1,
    unit_price double precision NOT NULL DEFAULT 0,
    total_price double precision NOT NULL DEFAULT 0,
    purchase_date date NOT NULL DEFAULT CURRENT_DATE,
    purchased_by character varying(255) NOT NULL,
    supplier character varying(255),
    notes text,
    created_at timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_tool_purchases_phase FOREIGN KEY (phase_id) REFERENCES public.phases(id) ON DELETE CASCADE,
    CONSTRAINT fk_tool_purchases_tool FOREIGN KEY (tool_id) REFERENCES public.tools(id) ON DELETE SET NULL,
    CONSTRAINT fk_tool_purchases_user FOREIGN KEY (purchased_by) REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_tool_purchases_phase ON public.tool_purchases(phase_id);
CREATE INDEX IF NOT EXISTS idx_tool_purchases_date ON public.tool_purchases(purchase_date);

-- 3. Registros de maquinário (próprio ou alugado)
CREATE TABLE IF NOT EXISTS public.machinery_records (
    id character varying(255) PRIMARY KEY,
    machinery_id character varying(255),
    phase_id character varying(255) NOT NULL,
    is_owned boolean NOT NULL DEFAULT true,
    rental_type character varying(20) DEFAULT 'none', -- none, per_hour, per_day, per_month, per_year
    rental_price double precision DEFAULT 0,
    status character varying(30) NOT NULL DEFAULT 'active', -- active, maintenance, stopped
    stop_reason text,
    stopped_by character varying(255),
    stopped_at timestamp without time zone,
    current_value double precision DEFAULT 0,
    total_fuel_cost double precision DEFAULT 0,
    total_maintenance_cost double precision DEFAULT 0,
    created_at timestamp without time zone NOT NULL DEFAULT NOW(),
    updated_at timestamp without time zone,
    CONSTRAINT fk_machinery_records_phase FOREIGN KEY (phase_id) REFERENCES public.phases(id) ON DELETE CASCADE,
    CONSTRAINT fk_machinery_records_machinery FOREIGN KEY (machinery_id) REFERENCES public.machinery(id) ON DELETE SET NULL,
    CONSTRAINT fk_machinery_records_stopped_by FOREIGN KEY (stopped_by) REFERENCES public.users(id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_machinery_records_phase ON public.machinery_records(phase_id);
CREATE INDEX IF NOT EXISTS idx_machinery_records_status ON public.machinery_records(status);

-- 4. Logs de combustível do maquinário
CREATE TABLE IF NOT EXISTS public.machinery_fuel_logs (
    id character varying(255) PRIMARY KEY,
    machinery_id character varying(255) NOT NULL,
    phase_id character varying(255) NOT NULL,
    log_date date NOT NULL DEFAULT CURRENT_DATE,
    liters double precision NOT NULL DEFAULT 0,
    price_per_liter double precision NOT NULL DEFAULT 0,
    total_cost double precision NOT NULL DEFAULT 0,
    recorded_by character varying(255) NOT NULL,
    notes text,
    created_at timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_machinery_fuel_logs_machinery FOREIGN KEY (machinery_id) REFERENCES public.machinery(id) ON DELETE CASCADE,
    CONSTRAINT fk_machinery_fuel_logs_phase FOREIGN KEY (phase_id) REFERENCES public.phases(id) ON DELETE CASCADE,
    CONSTRAINT fk_machinery_fuel_logs_user FOREIGN KEY (recorded_by) REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_machinery_fuel_logs_machinery ON public.machinery_fuel_logs(machinery_id);
CREATE INDEX IF NOT EXISTS idx_machinery_fuel_logs_date ON public.machinery_fuel_logs(log_date);

-- 5. Logs de manutenção do maquinário
CREATE TABLE IF NOT EXISTS public.machinery_maintenance_logs (
    id character varying(255) PRIMARY KEY,
    machinery_id character varying(255) NOT NULL,
    phase_id character varying(255) NOT NULL,
    maintenance_date date NOT NULL DEFAULT CURRENT_DATE,
    description text NOT NULL,
    cost double precision NOT NULL DEFAULT 0,
    performed_by character varying(255) NOT NULL,
    notes text,
    created_at timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_machinery_maintenance_logs_machinery FOREIGN KEY (machinery_id) REFERENCES public.machinery(id) ON DELETE CASCADE,
    CONSTRAINT fk_machinery_maintenance_logs_phase FOREIGN KEY (phase_id) REFERENCES public.phases(id) ON DELETE CASCADE,
    CONSTRAINT fk_machinery_maintenance_logs_user FOREIGN KEY (performed_by) REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_machinery_maintenance_logs_machinery ON public.machinery_maintenance_logs(machinery_id);
CREATE INDEX IF NOT EXISTS idx_machinery_maintenance_logs_date ON public.machinery_maintenance_logs(maintenance_date);

-- 6. Logs de trabalho da equipe (mão de obra)
CREATE TABLE IF NOT EXISTS public.team_member_work_logs (
    id character varying(255) PRIMARY KEY,
    team_member_id character varying(255) NOT NULL,
    phase_id character varying(255) NOT NULL,
    work_date date NOT NULL DEFAULT CURRENT_DATE,
    hours_worked double precision NOT NULL DEFAULT 0,
    hourly_rate double precision NOT NULL DEFAULT 0,
    total_cost double precision NOT NULL DEFAULT 0,
    added_by character varying(255) NOT NULL,
    notes text,
    created_at timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_team_work_logs_member FOREIGN KEY (team_member_id) REFERENCES public.team_members(id) ON DELETE CASCADE,
    CONSTRAINT fk_team_work_logs_phase FOREIGN KEY (phase_id) REFERENCES public.phases(id) ON DELETE CASCADE,
    CONSTRAINT fk_team_work_logs_user FOREIGN KEY (added_by) REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_team_work_logs_member ON public.team_member_work_logs(team_member_id);
CREATE INDEX IF NOT EXISTS idx_team_work_logs_phase ON public.team_member_work_logs(phase_id);
CREATE INDEX IF NOT EXISTS idx_team_work_logs_date ON public.team_member_work_logs(work_date);

-- 7. Categorias de custo para relatórios financeiros
CREATE TABLE IF NOT EXISTS public.cost_categories (
    id character varying(255) PRIMARY KEY,
    name character varying(100) NOT NULL,
    description text,
    color character varying(20) DEFAULT '#3b82f6',
    created_at timestamp without time zone NOT NULL DEFAULT NOW()
);

INSERT INTO public.cost_categories (id, name, description, color) VALUES
    ('cat-materials', 'Materiais', 'Custo com materiais de construção', '#10b981'),
    ('cat-tools', 'Ferramentas', 'Custo com ferramentas e equipamentos', '#3b82f6'),
    ('cat-machinery', 'Maquinário', 'Custo com aluguel, combustível e manutenção de máquinas', '#f59e0b'),
    ('cat-labor', 'Mão de Obra', 'Custo com trabalhadores e equipe', '#8b5cf6'),
    ('cat-other', 'Outros', 'Outros custos da obra', '#6b7280')
ON CONFLICT (id) DO NOTHING;

SELECT 'Tabelas de controle de custos criadas com sucesso' as status;
