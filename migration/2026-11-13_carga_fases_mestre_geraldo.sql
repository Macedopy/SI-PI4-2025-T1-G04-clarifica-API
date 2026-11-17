-- Carga de fases para o Mestre Geraldo (MASTER_BUILDER)
-- user_id  : 1ff4aecb-a40b-4039-806e-485dc63f5da5
-- obra     : Residencial Horizonte (d3462a0c-30b1-40e2-802a-0ad466de31e6)
-- Conteudo : 8 fases com materiais, ferramentas, maquinarios, equipe, servicos e fotos
-- Idempotente: remove as fases geradas por este script antes de recriar.

BEGIN;

SET client_encoding = 'UTF8';

-- Limpeza (idempotencia): as FKs usam ON DELETE CASCADE
DELETE FROM phases
WHERE construction_id = 'd3462a0c-30b1-40e2-802a-0ad466de31e6'
  AND name IN (
    'Preparacao do Terreno', 'Fundacao', 'Estrutura', 'Alvenaria',
    'Instalacoes Hidraulicas', 'Instalacoes Eletricas', 'Revestimentos', 'Acabamentos',
    'Preparação do Terreno', 'Fundação', 'Instalações Hidráulicas', 'Instalações Elétricas'
  );

-- ---------------------------------------------------------------- fases
INSERT INTO phases (id, construction_id, name, contractor)
SELECT gen_random_uuid()::text,
       'd3462a0c-30b1-40e2-802a-0ad466de31e6',
       v.name,
       'Mestre Geraldo'
FROM (VALUES
    ('Preparação do Terreno'),
    ('Fundação'),
    ('Estrutura'),
    ('Alvenaria'),
    ('Instalações Hidráulicas'),
    ('Instalações Elétricas'),
    ('Revestimentos'),
    ('Acabamentos')
) AS v(name);

-- ------------------------------------------------------------ materiais
INSERT INTO materials (id, phase_id, name, category, unit, consumed_quantity,
                       current_stock, minimum_stock, needs_restock, urgency)
SELECT gen_random_uuid()::text, p.id, v.name, v.category, v.unit, v.consumed,
       v.stock, v.minimum, v.needs, v.urgency
FROM phases p
JOIN (VALUES
    -- Preparação do Terreno
    ('Preparação do Terreno','Terra de nivelamento','OTHER','CUBIC_METER',120,0,0,false,'BAIXA'),
    ('Preparação do Terreno','Sacos de cimento para base','CEMENT','BAG',40,150,50,false,'BAIXA'),
    ('Preparação do Terreno','Areia lavada','SAND','CUBIC_METER',18,25,10,false,'MEDIA'),
    ('Preparação do Terreno','Rachão compactado','OTHER','CUBIC_METER',35,40,15,false,'BAIXA'),
    ('Preparação do Terreno','Broca para concreto','OTHER','UNIT',6,12,4,false,'BAIXA'),
    ('Preparação do Terreno','Blocos cerâmicos','BRICK','UNIT',140,300,100,false,'BAIXA'),
    -- Fundação
    ('Fundação','Cimento CP II','CEMENT','BAG',320,800,300,false,'MEDIA'),
    ('Fundação','Areia média','SAND','CUBIC_METER',22,40,15,false,'BAIXA'),
    ('Fundação','Brita graduada','OTHER','CUBIC_METER',14,30,10,false,'MEDIA'),
    ('Fundação','Armadura CA-50','STEEL','KG',1250,3000,800,false,'ALTA'),
    ('Fundação','Impermeabilização','OTHER','LITER',45,120,40,false,'BAIXA'),
    ('Fundação','Fita de PVC','PLUMBING','UNIT',120,400,100,false,'BAIXA'),
    -- Estrutura
    ('Estrutura','Concreto usinado FCK 25','OTHER','CUBIC_METER',38,60,20,false,'ALTA'),
    ('Estrutura','Armadura CA-50','STEEL','KG',1840,3200,800,false,'ALTA'),
    ('Estrutura','Aço CA-100','STEEL','KG',760,1500,400,false,'MEDIA'),
    ('Estrutura','Fôrma de madeira','WOOD','UNIT',120,300,80,false,'BAIXA'),
    ('Estrutura','Madeira serrada','WOOD','UNIT',95,240,60,false,'MEDIA'),
    ('Estrutura','Escoras metálicas','OTHER','UNIT',48,80,30,false,'MEDIA'),
    -- Alvenaria
    ('Alvenaria','Bloco cerâmico 14x19x39','BRICK','UNIT',980,2200,600,false,'MEDIA'),
    ('Alvenaria','Cimento CP II','CEMENT','BAG',210,700,250,false,'BAIXA'),
    ('Alvenaria','Areia','SAND','CUBIC_METER',18,35,12,false,'BAIXA'),
    ('Alvenaria','Cal Portland','OTHER','BAG',60,180,50,false,'BAIXA'),
    ('Alvenaria','Reboco pronto','OTHER','KG',900,2500,700,false,'BAIXA'),
    ('Alvenaria','Tijolo baiano','BRICK','UNIT',420,900,300,false,'MEDIA'),
    -- Instalações Hidráulicas
    ('Instalações Hidráulicas','Tubo PVC soldável 25mm','PLUMBING','UNIT',220,500,150,false,'MEDIA'),
    ('Instalações Hidráulicas','Tubo PVC soldável 50mm','PLUMBING','UNIT',110,260,80,false,'MEDIA'),
    ('Instalações Hidráulicas','Joelho PVC 90°','PLUMBING','UNIT',95,240,60,false,'BAIXA'),
    ('Instalações Hidráulicas','Torneira cromada','PLUMBING','UNIT',28,60,15,false,'BAIXA'),
    ('Instalações Hidráulicas','Vaso sanitário','PLUMBING','UNIT',12,20,6,false,'MEDIA'),
    ('Instalações Hidráulicas','Caixa de água 1000 L','PLUMBING','UNIT',2,3,1,false,'ALTA'),
    -- Instalações Elétricas
    ('Instalações Elétricas','Cabo flexível 2,5mm','ELECTRICAL','KG',480,1200,350,false,'MEDIA'),
    ('Instalações Elétricas','Cabo flexível 6mm','ELECTRICAL','KG',210,500,150,false,'MEDIA'),
    ('Instalações Elétricas','Eletrocalha galvanizada','ELECTRICAL','UNIT',320,700,200,false,'BAIXA'),
    ('Instalações Elétricas','Disjuntor monopolar 20A','ELECTRICAL','UNIT',34,60,20,false,'MEDIA'),
    ('Instalações Elétricas','Quadro de distribuição','ELECTRICAL','UNIT',6,10,3,false,'ALTA'),
    ('Instalações Elétricas','Luminária LED','ELECTRICAL','UNIT',48,90,30,false,'BAIXA'),
    -- Revestimentos
    ('Revestimentos','Piso porcelanato','OTHER','CUBIC_METER',62,110,40,false,'MEDIA'),
    ('Revestimentos','Argamassa AC-II','CEMENT','BAG',180,420,120,false,'MEDIA'),
    ('Revestimentos','Rejunte','OTHER','KG',85,200,60,false,'BAIXA'),
    ('Revestimentos','Tinta acrílica','OTHER','LITER',140,300,100,false,'BAIXA'),
    ('Revestimentos','Massa corrida','OTHER','KG',120,260,80,false,'BAIXA'),
    ('Revestimentos','Porcelanato decorativo','OTHER','CUBIC_METER',18,36,12,false,'MEDIA'),
    -- Acabamentos
    ('Acabamentos','Madeira maciça para porta','WOOD','UNIT',34,60,20,false,'MEDIA'),
    ('Acabamentos','Madeira laminada para piso','WOOD','CUBIC_METER',22,40,12,false,'MEDIA'),
    ('Acabamentos','Vidro temperado','OTHER','CUBIC_METER',16,28,10,false,'ALTA'),
    ('Acabamentos','Ferragens para porta','OTHER','UNIT',48,90,30,false,'BAIXA'),
    ('Acabamentos','Silicone','OTHER','UNIT',36,80,25,false,'BAIXA'),
    ('Acabamentos','Bucha e parafuso','OTHER','UNIT',600,1200,400,false,'BAIXA')
) AS v(phase_name, name, category, unit, consumed, stock, minimum, needs, urgency)
  ON v.phase_name = p.name
WHERE p.construction_id = 'd3462a0c-30b1-40e2-802a-0ad466de31e6';

-- ----------------------------------------------------------- ferramentas
INSERT INTO tools (id, phase_id, name, category, total_quantity, in_use, in_maintenance,
                   available, condition, notes)
SELECT gen_random_uuid()::text, p.id, v.name, v.category, v.total, v.in_use, v.in_maintenance,
       v.total - v.in_use - v.in_maintenance, v.condition, v.notes
FROM phases p
JOIN (VALUES
    ('Preparação do Terreno','Retroescavadeira','Máquinas pesadas',1,1,0,'GOOD','Alugada para a semana'),
    ('Preparação do Terreno','Compactador de solo','Máquinas pesadas',1,1,0,'GOOD','Em uso na área leste'),
    ('Preparação do Terreno','Nível a laser','Instrumentos',2,1,0,'EXCELLENT','Calibrado em março/2026'),
    ('Preparação do Terreno','Pás e picaretas','Ferramentas manuais',6,4,0,'GOOD',''),
    ('Fundação','Concreteira','Máquinas pesadas',1,1,0,'GOOD','Para lançamento de concreto'),
    ('Fundação','Vibrador de imersão','Máquinas pesadas',2,1,0,'GOOD',''),
    ('Fundação','Pá e picareta','Ferramentas manuais',8,5,0,'GOOD',''),
    ('Fundação','Escoras metálicas','Ferramentas manuais',12,0,0,'EXCELLENT',''),
    ('Estrutura','Guincho de obra','Máquinas pesadas',1,1,0,'GOOD','Operado por Carlos'),
    ('Estrutura','Máquina de corte de aço','Máquinas pesadas',1,1,0,'GOOD',''),
    ('Estrutura','Carrinho de obra','Ferramentas manuais',5,3,0,'GOOD',''),
    ('Estrutura','Chave de torque','Instrumentos',3,2,0,'EXCELLENT',''),
    ('Alvenaria','Betoneira 400L','Máquinas pesadas',2,1,0,'GOOD','Uma em manutenção'),
    ('Alvenaria','Prensa de tijolo','Ferramentas manuais',4,2,0,'GOOD',''),
    ('Alvenaria','Nível de bolha','Instrumentos',8,5,0,'GOOD',''),
    ('Alvenaria','Andaime modular','Máquinas pesadas',20,14,0,'GOOD',''),
    ('Instalações Hidráulicas','Cortador de tubo','Ferramentas manuais',4,2,0,'GOOD',''),
    ('Instalações Hidráulicas','Solda elétrica PVC','Instrumentos',2,1,0,'GOOD',''),
    ('Instalações Hidráulicas','Balde de massa','Ferramentas manuais',6,3,0,'GOOD',''),
    ('Instalações Hidráulicas','Prensa hidráulica','Instrumentos',1,1,0,'EXCELLENT',''),
    ('Instalações Elétricas','Multímetro digital','Instrumentos',4,2,0,'EXCELLENT',''),
    ('Instalações Elétricas','Alicate de corte','Ferramentas manuais',8,4,0,'GOOD',''),
    ('Instalações Elétricas','Furadeira de impacto','Ferramentas manuais',6,3,0,'GOOD',''),
    ('Instalações Elétricas','Escada de fibra','Máquinas pesadas',3,2,0,'GOOD',''),
    ('Revestimentos','Cortador de porcelanato','Máquinas pesadas',2,1,0,'GOOD','Lâmina um pouco gasta'),
    ('Revestimentos','Desempenadeira','Ferramentas manuais',4,2,0,'GOOD',''),
    ('Revestimentos','Balde de argamassa','Ferramentas manuais',10,6,0,'GOOD',''),
    ('Revestimentos','Rolo de pintura','Ferramentas manuais',12,7,0,'GOOD',''),
    ('Acabamentos','Lixadeira orbital','Ferramentas manuais',3,2,0,'GOOD',''),
    ('Acabamentos','Tupia de acabamento','Ferramentas manuais',2,1,0,'GOOD',''),
    ('Acabamentos','Jogo de chaves Allen','Instrumentos',4,2,0,'EXCELLENT',''),
    ('Acabamentos','Pistola de pintura','Máquinas pesadas',3,1,0,'GOOD','')
) AS v(phase_name, name, category, total, in_use, in_maintenance, condition, notes)
  ON v.phase_name = p.name
WHERE p.construction_id = 'd3462a0c-30b1-40e2-802a-0ad466de31e6';

-- ---------------------------------------------------------- maquinários
INSERT INTO machinery (id, phase_id, name, category, hours_worked, fuel_used, fuel_unit,
                       total_quantity, in_operation, in_maintenance, condition, notes)
SELECT gen_random_uuid()::text, p.id, v.name, v.category, v.hours, v.fuel, v.fuel_unit,
       v.total, v.in_operation, v.in_maintenance, v.condition, v.notes
FROM phases p
JOIN (VALUES
    ('Preparação do Terreno','Retroescavadeira Caterpillar','Máquinas pesadas',86,320,'LITERS',1,1,0,'GOOD','Alugada - R$ 180/dia'),
    ('Preparação do Terreno','Rolo compactador','Máquinas pesadas',64,210,'LITERS',1,1,0,'GOOD',''),
    ('Preparação do Terreno','Picareta hidráulica','Máquinas pesadas',38,0,'HOURS',1,0,1,'POOR','Em manutenção - troca de óleo'),
    ('Fundação','Bomba de concreto','Máquinas pesadas',42,180,'LITERS',1,1,0,'GOOD',''),
    ('Fundação','Escavadeira','Máquinas pesadas',110,390,'LITERS',1,1,0,'GOOD',''),
    ('Fundação','Gerador','Máquinas pesadas',95,260,'LITERS',1,0,0,'GOOD','Reserva para obra'),
    ('Estrutura','Guincho de torre','Máquinas pesadas',145,480,'LITERS',1,1,0,'GOOD','Operador Bruno'),
    ('Estrutura','Máquina de dobrar armadura','Máquinas pesadas',72,0,'HOURS',1,1,0,'EXCELLENT',''),
    ('Estrutura','Bomba de lançamento','Máquinas pesadas',58,190,'LITERS',1,0,1,'GOOD','Revisão agendada'),
    ('Alvenaria','Betoneira','Máquinas pesadas',120,0,'HOURS',2,1,0,'GOOD',''),
    ('Alvenaria','Elevador de obra','Máquinas pesadas',88,220,'LITERS',1,1,0,'GOOD',''),
    ('Alvenaria','Carregador frontal','Máquinas pesadas',102,340,'LITERS',1,1,0,'GOOD',''),
    ('Instalações Hidráulicas','Bomba de recalque','Máquinas pesadas',46,140,'LITERS',1,0,0,'GOOD',''),
    ('Instalações Hidráulicas','Compressor','Máquinas pesadas',78,240,'LITERS',1,1,0,'GOOD',''),
    ('Instalações Elétricas','Plataforma articulada','Máquinas pesadas',64,180,'LITERS',1,1,0,'GOOD','Acesso à fachada'),
    ('Instalações Elétricas','Gerador 15 kVA','Máquinas pesadas',130,410,'LITERS',1,1,0,'GOOD',''),
    ('Revestimentos','Cortadora de piso','Máquinas pesadas',52,0,'HOURS',1,1,0,'GOOD',''),
    ('Revestimentos','Aspiradora industrial','Máquinas pesadas',34,0,'HOURS',1,0,0,'GOOD',''),
    ('Acabamentos','Compressor de pintura','Máquinas pesadas',28,90,'LITERS',1,1,0,'GOOD',''),
    ('Acabamentos','Plataforma de pintura','Máquinas pesadas',22,0,'HOURS',1,1,0,'EXCELLENT','')
) AS v(phase_name, name, category, hours, fuel, fuel_unit, total, in_operation, in_maintenance, condition, notes)
  ON v.phase_name = p.name
WHERE p.construction_id = 'd3462a0c-30b1-40e2-802a-0ad466de31e6';

-- --------------------------------------------------------------- equipe
INSERT INTO team_members (id, phase_id, name, role, cpf, hours_worked, status, notes)
SELECT gen_random_uuid()::text, p.id, v.name, v.role, v.cpf, v.hours, v.status, v.notes
FROM phases p
JOIN (VALUES
    ('Preparação do Terreno','João Pedro Alves','Encarregado','111.222.333-44',44,'PRESENT','Responsável pela área externa'),
    ('Preparação do Terreno','Carlos Eduardo Lima','Mestre de obras','222.333.444-55',40,'PRESENT',''),
    ('Preparação do Terreno','Rafael Souza','Operador de máquina','333.444.555-66',38,'PRESENT',''),
    ('Preparação do Terreno','Marcos Pereira','Servente','444.555.666-77',36,'ON_LEAVE','Férias até o fim do mês'),
    ('Fundação','João Pedro Alves','Encarregado','111.222.333-44',46,'PRESENT',''),
    ('Fundação','Sérgio Ramos','Armador','555.666.777-88',44,'PRESENT',''),
    ('Fundação','Adriana Melo','Pedreiro','666.777.888-99',42,'PRESENT',''),
    ('Fundação','Jorge Santos','Carpinteiro','777.888.999-00',40,'ABSENT','Atestado médico'),
    ('Estrutura','Carlos Eduardo Lima','Mestre de obras','222.333.444-55',48,'PRESENT',''),
    ('Estrutura','Sérgio Ramos','Armador','555.666.777-88',47,'PRESENT',''),
    ('Estrutura','Adriana Melo','Pedreiro','666.777.888-99',46,'PRESENT',''),
    ('Estrutura','Paulo Henrique Dias','Carpinteiro de formas','888.999.000-11',45,'PRESENT',''),
    ('Estrutura','Fernando Costa','Sinaleiro','999.000.111-22',43,'PRESENT',''),
    ('Alvenaria','João Pedro Alves','Encarregado','111.222.333-44',45,'PRESENT',''),
    ('Alvenaria','Adriana Melo','Pedreiro','666.777.888-99',44,'PRESENT',''),
    ('Alvenaria','Paulo Henrique Dias','Carpinteiro de formas','888.999.000-11',43,'PRESENT',''),
    ('Alvenaria','Luana Freitas','Pedreiro','000.111.222-33',42,'PRESENT',''),
    ('Alvenaria','Bruno Tavares','Servente','111.222.333-44',41,'ON_LEAVE',''),
    ('Instalações Hidráulicas','Paulo Ribeiro','Encanador','222.333.444-55',40,'PRESENT','Responsável pela água fria'),
    ('Instalações Hidráulicas','Rogério Lima','Auxiliar de instalação','333.444.555-66',39,'PRESENT',''),
    ('Instalações Hidráulicas','Sílvia Rocha','Auxiliar de instalação','444.555.666-77',38,'PRESENT',''),
    ('Instalações Elétricas','Rogério Lima','Eletricista','333.444.555-66',42,'PRESENT','Responsável pela distribuição'),
    ('Instalações Elétricas','Ricardo Nunes','Eletricista','555.666.777-88',41,'PRESENT',''),
    ('Instalações Elétricas','Sílvia Rocha','Auxiliar de instalação','444.555.666-77',40,'PRESENT',''),
    ('Instalações Elétricas','Tiago Pires','Encarregado elétrico','666.777.888-99',39,'PRESENT',''),
    ('Revestimentos','João Pedro Alves','Encarregado','111.222.333-44',44,'PRESENT',''),
    ('Revestimentos','Luana Freitas','Pedreiro','000.111.222-33',43,'PRESENT',''),
    ('Revestimentos','Vítor Hugo Reis','Pintor','111.333.444-55',42,'PRESENT',''),
    ('Revestimentos','Camila Reis','Pintora','222.444.555-66',41,'PRESENT',''),
    ('Acabamentos','Vítor Hugo Reis','Pintor','111.333.444-55',45,'PRESENT',''),
    ('Acabamentos','Camila Reis','Pintora','222.444.555-66',44,'PRESENT',''),
    ('Acabamentos','Dênis Prado','Marceneiro','333.555.666-77',43,'PRESENT',''),
    ('Acabamentos','João Pedro Alves','Encarregado','111.222.333-44',42,'PRESENT',''),
    ('Acabamentos','Rita Alves','Auxiliar de limpeza','444.666.777-88',40,'ON_LEAVE','')
) AS v(phase_name, name, role, cpf, hours, status, notes)
  ON v.phase_name = p.name
WHERE p.construction_id = 'd3462a0c-30b1-40e2-802a-0ad466de31e6';

-- ------------------------------------------------------------- serviços
INSERT INTO executed_services (id, phase_id, name, team, planned_hours, executed_hours,
                               status, progress, notes)
SELECT gen_random_uuid()::text, p.id, v.name, v.team, v.planned, v.executed,
       v.status, v.progress, v.notes
FROM phases p
JOIN (VALUES
    ('Preparação do Terreno','Limpeza do terreno','Equipe de terraplenagem',16,16,'COMPLETED',100,'Terreno limpo e isolado'),
    ('Preparação do Terreno','Escavação do lote','Equipe de terraplenagem',24,24,'COMPLETED',100,'900 m³ escavados'),
    ('Preparação do Terreno','Compactação do solo','Equipe de terraplenagem',20,18,'IN_PROGRESS',90,'Falta a última camada'),
    ('Preparação do Terreno','Implantação de marcos','Equipe de topografia',12,0,'PLANNED',0,'Agendado para a próxima semana'),
    ('Fundação','Escavação de sapatas','Equipe de fundação',32,32,'COMPLETED',100,''),
    ('Fundação','Armação das sapatas','Equipe de armação',28,28,'COMPLETED',100,'1250 kg de aço CA-50'),
    ('Fundação','Concreto das sapatas','Equipe de concretagem',20,20,'COMPLETED',100,'FCK 25 MPa'),
    ('Fundação','Impermeabilização e formas','Equipe de fundação',16,10,'IN_PROGRESS',60,'Metade das formas prontas'),
    ('Fundação','Cura do concreto','Equipe de concretagem',12,0,'STARTED',15,'Iniciada nesta semana'),
    ('Estrutura','Montagem de formas','Equipe de formas',40,36,'IN_PROGRESS',85,'2 pavimentos concluídos'),
    ('Estrutura','Armação de pilares e vigas','Equipe de armação',36,30,'IN_PROGRESS',75,''),
    ('Estrutura','Concreto da laje','Equipe de concretagem',18,18,'COMPLETED',100,''),
    ('Estrutura','Desmoldagem','Equipe de formas',12,4,'IN_PROGRESS',30,'Aguardando 28 dias'),
    ('Alvenaria','Marcação e nivelamento','Equipe de alvenaria',16,16,'COMPLETED',100,''),
    ('Alvenaria','Execução de paredes','Equipe de alvenaria',60,52,'IN_PROGRESS',80,'980 blocos assentes'),
    ('Alvenaria','Vergas e contrapiso','Equipe de alvenaria',24,12,'IN_PROGRESS',50,''),
    ('Alvenaria','Reboco interno','Equipe de reboco',36,0,'PLANNED',0,''),
    ('Instalações Hidráulicas','Rede de água fria','Equipe de encanadores',28,24,'IN_PROGRESS',80,'220 m de tubo PVC 25mm'),
    ('Instalações Hidráulicas','Rede de água quente','Equipe de encanadores',20,10,'IN_PROGRESS',45,''),
    ('Instalações Hidráulicas','Instalação de louças e metais','Equipe de encanadores',16,4,'IN_PROGRESS',25,''),
    ('Instalações Hidráulicas','Teste de estanqueidade','Equipe de encanadores',8,0,'PLANNED',0,''),
    ('Instalações Elétricas','Passagem de eletrodutos','Equipe de eletricistas',32,28,'IN_PROGRESS',80,''),
    ('Instalações Elétricas','Fiação e cabeamento','Equipe de eletricistas',36,24,'IN_PROGRESS',60,'480 kg de cabo 2,5mm'),
    ('Instalações Elétricas','Instalação de quadro e disjuntores','Equipe de eletricistas',16,8,'IN_PROGRESS',45,''),
    ('Instalações Elétricas','Instalação de luminárias','Equipe de eletricistas',12,0,'PLANNED',0,''),
    ('Revestimentos','Assentamento de piso','Equipe de revestimento',40,32,'IN_PROGRESS',75,'62 m² de porcelanato'),
    ('Revestimentos','Preparação de paredes','Equipe de revestimento',24,18,'IN_PROGRESS',60,''),
    ('Revestimentos','Pintura de paredes','Equipe de pintura',32,16,'IN_PROGRESS',45,'Duas salas concluídas'),
    ('Revestimentos','Aplicação de rejunte','Equipe de revestimento',12,6,'IN_PROGRESS',40,''),
    ('Acabamentos','Instalação de portas','Equipe de marceneiros',20,12,'IN_PROGRESS',55,''),
    ('Acabamentos','Instalação de vidros','Equipe de vidraceiros',12,6,'IN_PROGRESS',40,'16 m² de vidro temperado'),
    ('Acabamentos','Pintura final e retoques','Equipe de pintura',24,8,'IN_PROGRESS',30,''),
    ('Acabamentos','Limpeza final da obra','Equipe de limpeza',16,0,'PLANNED',0,'')
) AS v(phase_name, name, team, planned, executed, status, progress, notes)
  ON v.phase_name = p.name
WHERE p.construction_id = 'd3462a0c-30b1-40e2-802a-0ad466de31e6';

-- ---------------------------------------------------------------- fotos
INSERT INTO photo_records (id, phase_id, file_path, caption, category, uploaded_at)
SELECT gen_random_uuid()::text, p.id, v.path, v.caption, v.category, NOW() - (v.hours || ' hours')::interval
FROM phases p
JOIN (VALUES
    ('Preparação do Terreno','/fotos/terreno/antes-01.jpg','Terreno antes da preparação','BEFORE',72),
    ('Preparação do Terreno','/fotos/terreno/diario-01.jpg','Após a escavação do lote','DIARIO_OBRA',48),
    ('Preparação do Terreno','/fotos/terreno/progresso-01.jpg','Compactação concluída','PROGRESS',12),
    ('Fundação','/fotos/fundacao/armacao-01.jpg','Armadura das sapatas pronta','PROGRESS',60),
    ('Fundação','/fotos/fundacao/concreto-01.jpg','Concreto das sapatas','PROGRESS',36),
    ('Fundação','/fotos/fundacao/agua-01.jpg','Água acumulada na escavação','ISSUE',24),
    ('Estrutura','/fotos/estrutura/formas-01.jpg','Fôrmas do segundo pavimento','PROGRESS',30),
    ('Estrutura','/fotos/estrutura/seguranca-01.jpg','EPI em uso - NR-35','SAFETY',18),
    ('Estrutura','/fotos/estrutura/laje-01.jpg','Concreto da laje','PROGRESS',6),
    ('Alvenaria','/fotos/alvenaria/paredes-01.jpg','Paredes de bloco em execução','PROGRESS',26),
    ('Alvenaria','/fotos/alvenaria/reboco-01.jpg','Reboco do primeiro pavimento','PROGRESS',10),
    ('Alvenaria','/fotos/alvenaria/antes-01.jpg','Área antes do lançamento','BEFORE',80),
    ('Instalações Hidráulicas','/fotos/hidraulica/tubos-01.jpg','Rede de água fria em execução','PROGRESS',20),
    ('Instalações Hidráulicas','/fotos/hidraulica/vazamento-01.jpg','Ponto de vedação com vazamento','ISSUE',8),
    ('Instalações Elétricas','/fotos/eletrica/eletrodutos-01.jpg','Passagem de eletrodutos','PROGRESS',22),
    ('Instalações Elétricas','/fotos/eletrica/quadro-01.jpg','Quadro de distribuição instalado','PROGRESS',9),
    ('Revestimentos','/fotos/revestimento/piso-01.jpg','Piso da sala principal','PROGRESS',16),
    ('Revestimentos','/fotos/revestimento/pintura-01.jpg','Primeira parede pintada','PROGRESS',7),
    ('Revestimentos','/fotos/revestimento/processo-01.jpg','Preparação de paredes','DIARIO_OBRA',40),
    ('Acabamentos','/fotos/acabamento/portas-01.jpg','Primeiras portas instaladas','PROGRESS',14),
    ('Acabamentos','/fotos/acabamento/vidro-01.jpg','Montagem dos vidros','PROGRESS',6),
    ('Acabamentos','/fotos/acabamento/final-01.jpg','Detalhe do acabamento da cozinha','AFTER',2)
) AS v(phase_name, path, caption, category, hours)
  ON v.phase_name = p.name
WHERE p.construction_id = 'd3462a0c-30b1-40e2-802a-0ad466de31e6';

COMMIT;

-- Verificação
SELECT p.name AS fase,
       (SELECT count(*) FROM materials m WHERE m.phase_id = p.id) AS materiais,
       (SELECT count(*) FROM tools t WHERE t.phase_id = p.id) AS ferramentas,
       (SELECT count(*) FROM machinery mc WHERE mc.phase_id = p.id) AS maquinarios,
       (SELECT count(*) FROM team_members tm WHERE tm.phase_id = p.id) AS equipe,
       (SELECT count(*) FROM executed_services es WHERE es.phase_id = p.id) AS servicos,
       (SELECT count(*) FROM photo_records pr WHERE pr.phase_id = p.id) AS fotos
FROM phases p
WHERE p.construction_id = 'd3462a0c-30b1-40e2-802a-0ad466de31e6'
ORDER BY p.name;
