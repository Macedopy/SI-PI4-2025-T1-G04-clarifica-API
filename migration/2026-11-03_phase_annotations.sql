-- Migração: Tabela de anotações de fases (phase_annotations)
-- Permite que Mestre de Obras e Proprietário adicionem anotações por fase e componente
-- com data, título, conteúdo, visibilidade pública/privada e registro de quem criou.

CREATE TABLE IF NOT EXISTS public.phase_annotations (
    id character varying(255) PRIMARY KEY,
    phase_id character varying(255) NOT NULL,
    component_type character varying(50),       -- SERVICE, MACHINERY, MATERIAL, TOOL, TEAM_MEMBER, PHOTO, PHASE
    component_id character varying(255),       -- ID do componente específico (opcional)
    title character varying(255) NOT NULL,
    content text NOT NULL,
    is_public boolean NOT NULL DEFAULT false,  -- true = cliente também vê
    created_by character varying(255) NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT NOW(),
    updated_at timestamp without time zone,
    CONSTRAINT fk_phase_annotations_phase FOREIGN KEY (phase_id) REFERENCES public.phases(id) ON DELETE CASCADE,
    CONSTRAINT fk_phase_annotations_created_by FOREIGN KEY (created_by) REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_phase_annotations_phase ON public.phase_annotations(phase_id);
CREATE INDEX IF NOT EXISTS idx_phase_annotations_created_by ON public.phase_annotations(created_by);
CREATE INDEX IF NOT EXISTS idx_phase_annotations_component ON public.phase_annotations(component_type, component_id);
