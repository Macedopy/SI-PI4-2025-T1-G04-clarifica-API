-- Itens de custo detalhados por fase/atividade
CREATE TABLE IF NOT EXISTS cost_items (
    id VARCHAR(36) PRIMARY KEY,
    phase_id VARCHAR(36) NOT NULL REFERENCES phases(id) ON DELETE CASCADE,
    activity_id VARCHAR(36) REFERENCES phase_activities(id) ON DELETE SET NULL,
    description VARCHAR(200) NOT NULL,
    vendor_id VARCHAR(36),                           -- fornecedor/subcontratado (usuário)
    budget_amount DECIMAL(15,2) DEFAULT 0,
    committed_amount DECIMAL(15,2) DEFAULT 0,
    invoiced_amount DECIMAL(15,2) DEFAULT 0,
    actual_cost_amount DECIMAL(15,2) DEFAULT 0,
    paid_amount DECIMAL(15,2) DEFAULT 0,
    retained_amount DECIMAL(15,2) DEFAULT 0,
    currency_code VARCHAR(3),
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_cost_items_phase ON cost_items(phase_id);
CREATE INDEX IF NOT EXISTS idx_cost_items_activity ON cost_items(activity_id);

-- Faturas
CREATE TABLE IF NOT EXISTS invoices (
    id VARCHAR(36) PRIMARY KEY,
    cost_item_id VARCHAR(36) NOT NULL REFERENCES cost_items(id) ON DELETE CASCADE,
    invoice_number VARCHAR(100),
    amount DECIMAL(15,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'draft',       -- draft, received, under_review, approved, partially_paid, paid, disputed, void
    due_date DATE,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_invoices_cost_item ON invoices(cost_item_id);
CREATE INDEX IF NOT EXISTS idx_invoices_status ON invoices(status);

-- Pagamentos
CREATE TABLE IF NOT EXISTS payments (
    id VARCHAR(36) PRIMARY KEY,
    invoice_id VARCHAR(36) NOT NULL REFERENCES invoices(id) ON DELETE CASCADE,
    amount DECIMAL(15,2) NOT NULL,
    paid_at TIMESTAMP,
    proof_url TEXT,                           -- comprovante
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_payments_invoice ON payments(invoice_id);

-- Change Orders (alterações de escopo/preço/prazo)
CREATE TABLE IF NOT EXISTS change_orders (
    id VARCHAR(36) PRIMARY KEY,
    construction_id VARCHAR(36) NOT NULL REFERENCES constructions(id) ON DELETE CASCADE,
    phase_id VARCHAR(36) REFERENCES phases(id) ON DELETE SET NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    price_change DECIMAL(15,2) DEFAULT 0,
    days_change INTEGER DEFAULT 0,
    status VARCHAR(20) DEFAULT 'pending',     -- pending, approved, rejected
    approved_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_change_orders_construction ON change_orders(construction_id);
CREATE INDEX IF NOT EXISTS idx_change_orders_phase ON change_orders(phase_id);
CREATE INDEX IF NOT EXISTS idx_change_orders_status ON change_orders(status);
