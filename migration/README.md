# Migração 2026-10-29 — modelo legado → `phases`

## Resultado

- Migração executada com sucesso no banco `construction` (PostgreSQL 18.1).
- `public` ficou com **14 tabelas** do modelo atual.
- As **45 tabelas legadas** foram movidas para o schema **`legacy`** (não foram apagadas).
- Backup completo em `migration/backups/construction_before_20261029.sql`.
- Cópia de segurança dentro do banco no schema **`backup_20261029_pre_migration`** (59 tabelas).

## Mapeamento dos dados

Os dados legados eram vinculados a **usuários**, não a obras. Para preservar o significado:

| Usuário legado | Papel | Obra criada |
|---|---|---|
| Joao Silva | CLIENT | Obra - Joao Silva |
| João Silva | CLIENT | Obra - João Silva |
| Pedro Mestre | MASTER_BUILDER | Obra - Pedro Mestre |

- Cada obra recebeu **6 fases**: Fundação, Alvenaria, Instalações Hidráulicas, Instalações Elétricas, Acabamentos e Revestimentos.
- O `contractor` de cada fase foi preservado.
- A obra **Residencial Horizonte** permaneceu intacta e sem fases, pois os dados legados pertencem a outros usuários.
- Componentes migrados: **3 team members**, **3 executed services** e **1 machinery** (somente a fundação possuía dados).
- Os IDs legados foram reutilizados nas fases e componentes, facilitando a rastreabilidade.

## Correções de schema aplicadas

- PK composta em `construction_clients`, `construction_master_builders` e `custom_phase_components`.
- `NOT NULL` em `phases.construction_id` e em todos os `phase_id` dos componentes.
- FKs recriadas com `ON DELETE CASCADE`.
- FK em `general_information.phase_id` → `phases.id`.
- Índices nas FKs.
- `executed_services.status` consolidado em 4 valores: `PLANNED`, `STARTED`, `IN_PROGRESS`, `COMPLETED`.
- Checks de progresso (0–100) e de quantidades de ferramentas/máquinas.
- `application.properties` passou de `update` para `validate`, evitando que o Hibernate altere o schema sozinho.

## Rastreabilidade

- `migration.schema_migration` — versão aplicada.
- `migration.legacy_phase_map` — 18 fases legadas → fases novas.
- `migration.legacy_component_map` — componentes legados → componentes novos.

## Verificação

```sql
select count(*) from public.phases;              -- 18
select count(*) from public.team_members;        -- 3
select count(*) from public.executed_services;   -- 3
select count(*) from public.machinery;           -- 1
select count(*) from public.constructions;       -- 4
```

A aplicação foi iniciada com `schema-management.strategy=validate` e os endpoints testados:

- `GET /hello` → 200
- `GET /constructions/user/{ownerId}` → 200
- `GET /constructions/{id}` → 200
- `GET /dashboard/{id}` → 200 (Obra - Joao Silva com 26% de progresso)

## Como restaurar

O backup principal é o dump:

```powershell
$env:PGPASSWORD='postgres'
& 'C:\Program Files\PostgreSQL\18\bin\createdb.exe' -h localhost -p 5432 -U postgres construction_restore
& 'C:\Program Files\PostgreSQL\18\bin\psql.exe' -h localhost -p 5432 -U postgres -d construction_restore -f 'C:\programming\micro-management-contruction\migration\backups\construction_before_20261029.sql'
```

Para voltar as tabelas legadas ao schema `public`:

```sql
ALTER TABLE legacy.foundations SET SCHEMA public;
-- repetir para as demais 44 tabelas
```

Observação: as correções de integridade (PKs, FKs, índices, checks) são melhorias e não precisam ser desfeitas para restaurar os dados. Para um rollback completo do schema, use o dump acima em um banco novo.

## Pendências conhecidas (não fazem parte desta migração)

- `general_information` continua por fase e vazia; o check de `topography` ainda usa valores em inglês, enquanto a UI usa português.
- Ainda existem warnings de CORS no startup (`quarkus.http.cors.allow-credentials` não reconhecido).
- Não há Flyway/Liquibase; o script desta migração é o registro manual da evolução.
