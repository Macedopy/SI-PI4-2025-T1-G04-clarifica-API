--
-- PostgreSQL database dump
--

\restrict hBhXbfTsezu7qN1DGhtsXLbH3L8bOCtW2MfCWaS0jPXz5iXKAzuekVavKM8WX9s

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: coatings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.coatings (
    id character varying(255) NOT NULL,
    contractor character varying(255),
    name character varying(255) NOT NULL,
    user_id character varying(255)
);


ALTER TABLE public.coatings OWNER TO postgres;

--
-- Name: coatings_executed_services; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.coatings_executed_services (
    id character varying(255) NOT NULL,
    executed_hours double precision NOT NULL,
    name character varying(255) NOT NULL,
    notes character varying(1000),
    planned_hours double precision NOT NULL,
    progress integer NOT NULL,
    status character varying(255) NOT NULL,
    team character varying(255) NOT NULL,
    coatings_id character varying(255) NOT NULL,
    CONSTRAINT coatings_executed_services_executed_hours_check CHECK ((executed_hours >= (0)::double precision)),
    CONSTRAINT coatings_executed_services_planned_hours_check CHECK ((planned_hours >= (1)::double precision)),
    CONSTRAINT coatings_executed_services_progress_check CHECK ((progress >= 0)),
    CONSTRAINT coatings_executed_services_status_check CHECK (((status)::text = ANY ((ARRAY['PLANNED'::character varying, 'STARTED'::character varying, 'IN_PROGRESS'::character varying, 'COMPLETED'::character varying, 'CONCLUIDO'::character varying, 'EM_ANDAMENTO'::character varying, 'PLANEJADO'::character varying, 'INICIADO'::character varying])::text[])))
);


ALTER TABLE public.coatings_executed_services OWNER TO postgres;

--
-- Name: coatings_machineries; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.coatings_machineries (
    id character varying(255) NOT NULL,
    category character varying(255) NOT NULL,
    condition character varying(255) NOT NULL,
    fuel_unit character varying(255) NOT NULL,
    fuel_used double precision NOT NULL,
    hours_worked double precision NOT NULL,
    in_maintenance integer NOT NULL,
    in_operation integer NOT NULL,
    name character varying(255) NOT NULL,
    notes character varying(1000),
    total_quantity integer NOT NULL,
    coatings_id character varying(255) NOT NULL,
    CONSTRAINT coatings_machineries_condition_check CHECK (((condition)::text = ANY ((ARRAY['EXCELLENT'::character varying, 'GOOD'::character varying, 'POOR'::character varying, 'UNAVAILABLE'::character varying])::text[]))),
    CONSTRAINT coatings_machineries_fuel_unit_check CHECK (((fuel_unit)::text = ANY ((ARRAY['LITERS'::character varying, 'HOURS'::character varying])::text[]))),
    CONSTRAINT coatings_machineries_fuel_used_check CHECK ((fuel_used >= (0)::double precision)),
    CONSTRAINT coatings_machineries_hours_worked_check CHECK ((hours_worked >= (0)::double precision)),
    CONSTRAINT coatings_machineries_in_maintenance_check CHECK ((in_maintenance >= 0)),
    CONSTRAINT coatings_machineries_in_operation_check CHECK ((in_operation >= 0))
);


ALTER TABLE public.coatings_machineries OWNER TO postgres;

--
-- Name: coatings_materials; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.coatings_materials (
    id character varying(255) NOT NULL,
    category character varying(255) NOT NULL,
    consumed_quantity double precision NOT NULL,
    current_stock double precision NOT NULL,
    minimum_stock double precision NOT NULL,
    name character varying(255) NOT NULL,
    needs_restock boolean NOT NULL,
    unit character varying(255) NOT NULL,
    urgency character varying(255),
    coatings_id character varying(255) NOT NULL,
    CONSTRAINT coatings_materials_category_check CHECK (((category)::text = ANY ((ARRAY['CEMENT'::character varying, 'SAND'::character varying, 'STEEL'::character varying, 'BRICK'::character varying, 'WOOD'::character varying, 'ELECTRICAL'::character varying, 'PLUMBING'::character varying, 'OTHER'::character varying])::text[]))),
    CONSTRAINT coatings_materials_consumed_quantity_check CHECK ((consumed_quantity >= (0)::double precision)),
    CONSTRAINT coatings_materials_current_stock_check CHECK ((current_stock >= (0)::double precision)),
    CONSTRAINT coatings_materials_minimum_stock_check CHECK ((minimum_stock >= (0)::double precision)),
    CONSTRAINT coatings_materials_unit_check CHECK (((unit)::text = ANY ((ARRAY['BAG'::character varying, 'KG'::character varying, 'CUBIC_METER'::character varying, 'UNIT'::character varying, 'LITER'::character varying])::text[])))
);


ALTER TABLE public.coatings_materials OWNER TO postgres;

--
-- Name: coatings_photo_records; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.coatings_photo_records (
    id character varying(255) NOT NULL,
    caption character varying(500),
    category character varying(255) NOT NULL,
    file_path character varying(1000) NOT NULL,
    uploaded_at timestamp(6) without time zone NOT NULL,
    coatings_id character varying(255) NOT NULL,
    CONSTRAINT coatings_photo_records_category_check CHECK (((category)::text = ANY ((ARRAY['PROGRESS'::character varying, 'ISSUE'::character varying, 'BEFORE'::character varying, 'AFTER'::character varying, 'SAFETY'::character varying, 'DIARIO_OBRA'::character varying, 'OUTROS'::character varying])::text[])))
);


ALTER TABLE public.coatings_photo_records OWNER TO postgres;

--
-- Name: coatings_team_members; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.coatings_team_members (
    id character varying(255) NOT NULL,
    cpf character varying(14),
    name character varying(255) NOT NULL,
    role character varying(255) NOT NULL,
    hours_worked double precision NOT NULL,
    notes character varying(500),
    status character varying(255) NOT NULL,
    coatings_id character varying(255) NOT NULL,
    CONSTRAINT coatings_team_members_status_check CHECK (((status)::text = ANY ((ARRAY['PRESENT'::character varying, 'ABSENT'::character varying, 'ON_LEAVE'::character varying])::text[])))
);


ALTER TABLE public.coatings_team_members OWNER TO postgres;

--
-- Name: coatings_tools; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.coatings_tools (
    id character varying(255) NOT NULL,
    category character varying(255) NOT NULL,
    condition character varying(255) NOT NULL,
    in_maintenance integer NOT NULL,
    in_use integer NOT NULL,
    name character varying(255) NOT NULL,
    notes character varying(1000),
    total_quantity integer NOT NULL,
    coatings_id character varying(255) NOT NULL,
    CONSTRAINT coatings_tools_condition_check CHECK (((condition)::text = ANY ((ARRAY['EXCELLENT'::character varying, 'GOOD'::character varying, 'POOR'::character varying, 'UNAVAILABLE'::character varying])::text[]))),
    CONSTRAINT coatings_tools_in_maintenance_check CHECK ((in_maintenance >= 0)),
    CONSTRAINT coatings_tools_in_use_check CHECK ((in_use >= 0)),
    CONSTRAINT coatings_tools_total_quantity_check CHECK ((total_quantity >= 1))
);


ALTER TABLE public.coatings_tools OWNER TO postgres;

--
-- Name: construction_clients; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.construction_clients (
    construction_id character varying(255) NOT NULL,
    user_id character varying(255) NOT NULL
);


ALTER TABLE public.construction_clients OWNER TO postgres;

--
-- Name: construction_master_builders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.construction_master_builders (
    construction_id character varying(255) NOT NULL,
    user_id character varying(255) NOT NULL
);


ALTER TABLE public.construction_master_builders OWNER TO postgres;

--
-- Name: constructions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.constructions (
    id character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    status character varying(255) NOT NULL,
    owner_id character varying(255)
);


ALTER TABLE public.constructions OWNER TO postgres;

--
-- Name: custom_phase_components; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.custom_phase_components (
    custom_phase_id character varying(255) NOT NULL,
    component_name character varying(255)
);


ALTER TABLE public.custom_phase_components OWNER TO postgres;

--
-- Name: custom_phases; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.custom_phases (
    id character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    user_id character varying(255)
);


ALTER TABLE public.custom_phases OWNER TO postgres;

--
-- Name: eletric; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.eletric (
    id character varying(255) NOT NULL,
    contractor character varying(255),
    name character varying(255) NOT NULL,
    user_id character varying(255)
);


ALTER TABLE public.eletric OWNER TO postgres;

--
-- Name: eletric_executed_services; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.eletric_executed_services (
    id character varying(255) NOT NULL,
    executed_hours double precision NOT NULL,
    name character varying(255) NOT NULL,
    notes character varying(1000),
    planned_hours double precision NOT NULL,
    progress integer NOT NULL,
    status character varying(255) NOT NULL,
    team character varying(255) NOT NULL,
    eletric_id character varying(255) NOT NULL,
    CONSTRAINT eletric_executed_services_executed_hours_check CHECK ((executed_hours >= (0)::double precision)),
    CONSTRAINT eletric_executed_services_planned_hours_check CHECK ((planned_hours >= (1)::double precision)),
    CONSTRAINT eletric_executed_services_progress_check CHECK ((progress >= 0)),
    CONSTRAINT eletric_executed_services_status_check CHECK (((status)::text = ANY ((ARRAY['PLANNED'::character varying, 'STARTED'::character varying, 'IN_PROGRESS'::character varying, 'COMPLETED'::character varying, 'CONCLUIDO'::character varying, 'EM_ANDAMENTO'::character varying, 'PLANEJADO'::character varying, 'INICIADO'::character varying])::text[])))
);


ALTER TABLE public.eletric_executed_services OWNER TO postgres;

--
-- Name: eletric_machineries; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.eletric_machineries (
    id character varying(255) NOT NULL,
    category character varying(255) NOT NULL,
    condition character varying(255) NOT NULL,
    fuel_unit character varying(255) NOT NULL,
    fuel_used double precision NOT NULL,
    hours_worked double precision NOT NULL,
    in_maintenance integer NOT NULL,
    in_operation integer NOT NULL,
    name character varying(255) NOT NULL,
    notes character varying(1000),
    total_quantity integer NOT NULL,
    eletric_id character varying(255) NOT NULL,
    CONSTRAINT eletric_machineries_condition_check CHECK (((condition)::text = ANY ((ARRAY['EXCELLENT'::character varying, 'GOOD'::character varying, 'POOR'::character varying, 'UNAVAILABLE'::character varying])::text[]))),
    CONSTRAINT eletric_machineries_fuel_unit_check CHECK (((fuel_unit)::text = ANY ((ARRAY['LITERS'::character varying, 'HOURS'::character varying])::text[]))),
    CONSTRAINT eletric_machineries_fuel_used_check CHECK ((fuel_used >= (0)::double precision)),
    CONSTRAINT eletric_machineries_hours_worked_check CHECK ((hours_worked >= (0)::double precision)),
    CONSTRAINT eletric_machineries_in_maintenance_check CHECK ((in_maintenance >= 0)),
    CONSTRAINT eletric_machineries_in_operation_check CHECK ((in_operation >= 0))
);


ALTER TABLE public.eletric_machineries OWNER TO postgres;

--
-- Name: eletric_materials; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.eletric_materials (
    id character varying(255) NOT NULL,
    category character varying(255) NOT NULL,
    consumed_quantity double precision NOT NULL,
    current_stock double precision NOT NULL,
    minimum_stock double precision NOT NULL,
    name character varying(255) NOT NULL,
    needs_restock boolean NOT NULL,
    unit character varying(255) NOT NULL,
    urgency character varying(255),
    eletric_id character varying(255) NOT NULL,
    CONSTRAINT eletric_materials_category_check CHECK (((category)::text = ANY ((ARRAY['CEMENT'::character varying, 'SAND'::character varying, 'STEEL'::character varying, 'BRICK'::character varying, 'WOOD'::character varying, 'ELECTRICAL'::character varying, 'PLUMBING'::character varying, 'OTHER'::character varying])::text[]))),
    CONSTRAINT eletric_materials_consumed_quantity_check CHECK ((consumed_quantity >= (0)::double precision)),
    CONSTRAINT eletric_materials_current_stock_check CHECK ((current_stock >= (0)::double precision)),
    CONSTRAINT eletric_materials_minimum_stock_check CHECK ((minimum_stock >= (0)::double precision)),
    CONSTRAINT eletric_materials_unit_check CHECK (((unit)::text = ANY ((ARRAY['BAG'::character varying, 'KG'::character varying, 'CUBIC_METER'::character varying, 'UNIT'::character varying, 'LITER'::character varying])::text[])))
);


ALTER TABLE public.eletric_materials OWNER TO postgres;

--
-- Name: eletric_photo_records; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.eletric_photo_records (
    id character varying(255) NOT NULL,
    caption character varying(500),
    category character varying(255) NOT NULL,
    file_path character varying(1000) NOT NULL,
    uploaded_at timestamp(6) without time zone NOT NULL,
    eletric_id character varying(255) NOT NULL,
    CONSTRAINT eletric_photo_records_category_check CHECK (((category)::text = ANY ((ARRAY['PROGRESS'::character varying, 'ISSUE'::character varying, 'BEFORE'::character varying, 'AFTER'::character varying, 'SAFETY'::character varying, 'DIARIO_OBRA'::character varying, 'OUTROS'::character varying])::text[])))
);


ALTER TABLE public.eletric_photo_records OWNER TO postgres;

--
-- Name: eletric_team_members; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.eletric_team_members (
    id character varying(255) NOT NULL,
    cpf character varying(14),
    name character varying(255) NOT NULL,
    role character varying(255) NOT NULL,
    hours_worked double precision NOT NULL,
    notes character varying(500),
    status character varying(255) NOT NULL,
    eletric_id character varying(255) NOT NULL,
    CONSTRAINT eletric_team_members_status_check CHECK (((status)::text = ANY ((ARRAY['PRESENT'::character varying, 'ABSENT'::character varying, 'ON_LEAVE'::character varying])::text[])))
);


ALTER TABLE public.eletric_team_members OWNER TO postgres;

--
-- Name: eletric_tools; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.eletric_tools (
    id character varying(255) NOT NULL,
    category character varying(255) NOT NULL,
    condition character varying(255) NOT NULL,
    in_maintenance integer NOT NULL,
    in_use integer NOT NULL,
    name character varying(255) NOT NULL,
    notes character varying(1000),
    total_quantity integer NOT NULL,
    eletric_id character varying(255) NOT NULL,
    CONSTRAINT eletric_tools_condition_check CHECK (((condition)::text = ANY ((ARRAY['EXCELLENT'::character varying, 'GOOD'::character varying, 'POOR'::character varying, 'UNAVAILABLE'::character varying])::text[]))),
    CONSTRAINT eletric_tools_in_maintenance_check CHECK ((in_maintenance >= 0)),
    CONSTRAINT eletric_tools_in_use_check CHECK ((in_use >= 0)),
    CONSTRAINT eletric_tools_total_quantity_check CHECK ((total_quantity >= 1))
);


ALTER TABLE public.eletric_tools OWNER TO postgres;

--
-- Name: executed_services; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.executed_services (
    id character varying(255) NOT NULL,
    executed_hours double precision NOT NULL,
    name character varying(255) NOT NULL,
    notes character varying(1000),
    planned_hours double precision NOT NULL,
    progress integer NOT NULL,
    status character varying(255) NOT NULL,
    team character varying(255) NOT NULL,
    phase_id character varying(255),
    CONSTRAINT executed_services_executed_hours_check CHECK ((executed_hours >= (0)::double precision)),
    CONSTRAINT executed_services_planned_hours_check CHECK ((planned_hours >= (1)::double precision)),
    CONSTRAINT executed_services_progress_check CHECK ((progress >= 0)),
    CONSTRAINT executed_services_status_check CHECK (((status)::text = ANY ((ARRAY['PLANNED'::character varying, 'STARTED'::character varying, 'IN_PROGRESS'::character varying, 'COMPLETED'::character varying, 'CONCLUIDO'::character varying, 'EM_ANDAMENTO'::character varying, 'PLANEJADO'::character varying, 'INICIADO'::character varying])::text[])))
);


ALTER TABLE public.executed_services OWNER TO postgres;

--
-- Name: finishing_executed_services; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.finishing_executed_services (
    id character varying(255) NOT NULL,
    executed_hours double precision NOT NULL,
    name character varying(255) NOT NULL,
    notes character varying(1000),
    planned_hours double precision NOT NULL,
    progress integer NOT NULL,
    status character varying(255) NOT NULL,
    team character varying(255) NOT NULL,
    finishing_id character varying(255) NOT NULL,
    CONSTRAINT finishing_executed_services_executed_hours_check CHECK ((executed_hours >= (0)::double precision)),
    CONSTRAINT finishing_executed_services_planned_hours_check CHECK ((planned_hours >= (1)::double precision)),
    CONSTRAINT finishing_executed_services_progress_check CHECK ((progress >= 0)),
    CONSTRAINT finishing_executed_services_status_check CHECK (((status)::text = ANY ((ARRAY['PLANNED'::character varying, 'STARTED'::character varying, 'IN_PROGRESS'::character varying, 'COMPLETED'::character varying, 'CONCLUIDO'::character varying, 'EM_ANDAMENTO'::character varying, 'PLANEJADO'::character varying, 'INICIADO'::character varying])::text[])))
);


ALTER TABLE public.finishing_executed_services OWNER TO postgres;

--
-- Name: finishing_machineries; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.finishing_machineries (
    id character varying(255) NOT NULL,
    category character varying(255) NOT NULL,
    condition character varying(255) NOT NULL,
    fuel_unit character varying(255) NOT NULL,
    fuel_used double precision NOT NULL,
    hours_worked double precision NOT NULL,
    in_maintenance integer NOT NULL,
    in_operation integer NOT NULL,
    name character varying(255) NOT NULL,
    notes character varying(1000),
    total_quantity integer NOT NULL,
    finishing_id character varying(255) NOT NULL,
    CONSTRAINT finishing_machineries_condition_check CHECK (((condition)::text = ANY ((ARRAY['EXCELLENT'::character varying, 'GOOD'::character varying, 'POOR'::character varying, 'UNAVAILABLE'::character varying])::text[]))),
    CONSTRAINT finishing_machineries_fuel_unit_check CHECK (((fuel_unit)::text = ANY ((ARRAY['LITERS'::character varying, 'HOURS'::character varying])::text[]))),
    CONSTRAINT finishing_machineries_fuel_used_check CHECK ((fuel_used >= (0)::double precision)),
    CONSTRAINT finishing_machineries_hours_worked_check CHECK ((hours_worked >= (0)::double precision)),
    CONSTRAINT finishing_machineries_in_maintenance_check CHECK ((in_maintenance >= 0)),
    CONSTRAINT finishing_machineries_in_operation_check CHECK ((in_operation >= 0))
);


ALTER TABLE public.finishing_machineries OWNER TO postgres;

--
-- Name: finishing_materials; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.finishing_materials (
    id character varying(255) NOT NULL,
    category character varying(255) NOT NULL,
    consumed_quantity double precision NOT NULL,
    current_stock double precision NOT NULL,
    minimum_stock double precision NOT NULL,
    name character varying(255) NOT NULL,
    needs_restock boolean NOT NULL,
    unit character varying(255) NOT NULL,
    urgency character varying(255),
    finishing_id character varying(255) NOT NULL,
    CONSTRAINT finishing_materials_category_check CHECK (((category)::text = ANY ((ARRAY['CEMENT'::character varying, 'SAND'::character varying, 'STEEL'::character varying, 'BRICK'::character varying, 'WOOD'::character varying, 'ELECTRICAL'::character varying, 'PLUMBING'::character varying, 'OTHER'::character varying])::text[]))),
    CONSTRAINT finishing_materials_consumed_quantity_check CHECK ((consumed_quantity >= (0)::double precision)),
    CONSTRAINT finishing_materials_current_stock_check CHECK ((current_stock >= (0)::double precision)),
    CONSTRAINT finishing_materials_minimum_stock_check CHECK ((minimum_stock >= (0)::double precision)),
    CONSTRAINT finishing_materials_unit_check CHECK (((unit)::text = ANY ((ARRAY['BAG'::character varying, 'KG'::character varying, 'CUBIC_METER'::character varying, 'UNIT'::character varying, 'LITER'::character varying])::text[])))
);


ALTER TABLE public.finishing_materials OWNER TO postgres;

--
-- Name: finishing_photo_records; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.finishing_photo_records (
    id character varying(255) NOT NULL,
    caption character varying(500),
    category character varying(255) NOT NULL,
    file_path character varying(1000) NOT NULL,
    uploaded_at timestamp(6) without time zone NOT NULL,
    finishing_id character varying(255) NOT NULL,
    CONSTRAINT finishing_photo_records_category_check CHECK (((category)::text = ANY ((ARRAY['PROGRESS'::character varying, 'ISSUE'::character varying, 'BEFORE'::character varying, 'AFTER'::character varying, 'SAFETY'::character varying, 'DIARIO_OBRA'::character varying, 'OUTROS'::character varying])::text[])))
);


ALTER TABLE public.finishing_photo_records OWNER TO postgres;

--
-- Name: finishing_team_members; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.finishing_team_members (
    id character varying(255) NOT NULL,
    cpf character varying(14),
    name character varying(255) NOT NULL,
    role character varying(255) NOT NULL,
    hours_worked double precision NOT NULL,
    notes character varying(500),
    status character varying(255) NOT NULL,
    finishing_id character varying(255) NOT NULL,
    CONSTRAINT finishing_team_members_status_check CHECK (((status)::text = ANY ((ARRAY['PRESENT'::character varying, 'ABSENT'::character varying, 'ON_LEAVE'::character varying])::text[])))
);


ALTER TABLE public.finishing_team_members OWNER TO postgres;

--
-- Name: finishing_tools; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.finishing_tools (
    id character varying(255) NOT NULL,
    category character varying(255) NOT NULL,
    condition character varying(255) NOT NULL,
    in_maintenance integer NOT NULL,
    in_use integer NOT NULL,
    name character varying(255) NOT NULL,
    notes character varying(1000),
    total_quantity integer NOT NULL,
    finishing_id character varying(255) NOT NULL,
    CONSTRAINT finishing_tools_condition_check CHECK (((condition)::text = ANY ((ARRAY['EXCELLENT'::character varying, 'GOOD'::character varying, 'POOR'::character varying, 'UNAVAILABLE'::character varying])::text[]))),
    CONSTRAINT finishing_tools_in_maintenance_check CHECK ((in_maintenance >= 0)),
    CONSTRAINT finishing_tools_in_use_check CHECK ((in_use >= 0)),
    CONSTRAINT finishing_tools_total_quantity_check CHECK ((total_quantity >= 1))
);


ALTER TABLE public.finishing_tools OWNER TO postgres;

--
-- Name: finishings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.finishings (
    id character varying(255) NOT NULL,
    contractor character varying(255),
    name character varying(255) NOT NULL,
    user_id character varying(255)
);


ALTER TABLE public.finishings OWNER TO postgres;

--
-- Name: foundation_executed_services; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.foundation_executed_services (
    id character varying(255) NOT NULL,
    executed_hours double precision NOT NULL,
    name character varying(255) NOT NULL,
    notes character varying(1000),
    planned_hours double precision NOT NULL,
    progress integer NOT NULL,
    status character varying(255) NOT NULL,
    team character varying(255) NOT NULL,
    foundation_id character varying(255) NOT NULL,
    CONSTRAINT foundation_executed_services_executed_hours_check CHECK ((executed_hours >= (0)::double precision)),
    CONSTRAINT foundation_executed_services_planned_hours_check CHECK ((planned_hours >= (1)::double precision)),
    CONSTRAINT foundation_executed_services_progress_check CHECK ((progress >= 0)),
    CONSTRAINT foundation_executed_services_status_check CHECK (((status)::text = ANY ((ARRAY['PLANNED'::character varying, 'STARTED'::character varying, 'IN_PROGRESS'::character varying, 'COMPLETED'::character varying, 'CONCLUIDO'::character varying, 'EM_ANDAMENTO'::character varying, 'PLANEJADO'::character varying, 'INICIADO'::character varying])::text[])))
);


ALTER TABLE public.foundation_executed_services OWNER TO postgres;

--
-- Name: foundation_machinery; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.foundation_machinery (
    id character varying(255) NOT NULL,
    category character varying(255) NOT NULL,
    condition character varying(255) NOT NULL,
    fuel_unit character varying(255) NOT NULL,
    fuel_used double precision NOT NULL,
    hours_worked double precision NOT NULL,
    in_maintenance integer NOT NULL,
    in_operation integer NOT NULL,
    name character varying(255) NOT NULL,
    notes character varying(1000),
    total_quantity integer NOT NULL,
    foundation_id character varying(255) NOT NULL,
    CONSTRAINT foundation_machinery_condition_check CHECK (((condition)::text = ANY ((ARRAY['EXCELLENT'::character varying, 'GOOD'::character varying, 'POOR'::character varying, 'UNAVAILABLE'::character varying])::text[]))),
    CONSTRAINT foundation_machinery_fuel_unit_check CHECK (((fuel_unit)::text = ANY ((ARRAY['LITERS'::character varying, 'HOURS'::character varying])::text[]))),
    CONSTRAINT foundation_machinery_fuel_used_check CHECK ((fuel_used >= (0)::double precision)),
    CONSTRAINT foundation_machinery_hours_worked_check CHECK ((hours_worked >= (0)::double precision)),
    CONSTRAINT foundation_machinery_in_maintenance_check CHECK ((in_maintenance >= 0)),
    CONSTRAINT foundation_machinery_in_operation_check CHECK ((in_operation >= 0))
);


ALTER TABLE public.foundation_machinery OWNER TO postgres;

--
-- Name: foundation_materials; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.foundation_materials (
    id character varying(255) NOT NULL,
    category character varying(255) NOT NULL,
    consumed_quantity double precision NOT NULL,
    current_stock double precision NOT NULL,
    minimum_stock double precision NOT NULL,
    name character varying(255) NOT NULL,
    needs_restock boolean NOT NULL,
    unit character varying(255) NOT NULL,
    urgency character varying(255),
    foundation_id character varying(255) NOT NULL,
    CONSTRAINT foundation_materials_category_check CHECK (((category)::text = ANY ((ARRAY['CEMENT'::character varying, 'SAND'::character varying, 'STEEL'::character varying, 'BRICK'::character varying, 'WOOD'::character varying, 'ELECTRICAL'::character varying, 'PLUMBING'::character varying, 'OTHER'::character varying])::text[]))),
    CONSTRAINT foundation_materials_consumed_quantity_check CHECK ((consumed_quantity >= (0)::double precision)),
    CONSTRAINT foundation_materials_current_stock_check CHECK ((current_stock >= (0)::double precision)),
    CONSTRAINT foundation_materials_minimum_stock_check CHECK ((minimum_stock >= (0)::double precision)),
    CONSTRAINT foundation_materials_unit_check CHECK (((unit)::text = ANY ((ARRAY['BAG'::character varying, 'KG'::character varying, 'CUBIC_METER'::character varying, 'UNIT'::character varying, 'LITER'::character varying])::text[])))
);


ALTER TABLE public.foundation_materials OWNER TO postgres;

--
-- Name: foundation_photo_records; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.foundation_photo_records (
    id character varying(255) NOT NULL,
    caption character varying(500),
    category character varying(255) NOT NULL,
    file_path character varying(1000) NOT NULL,
    uploaded_at timestamp(6) without time zone NOT NULL,
    foundation_id character varying(255) NOT NULL,
    CONSTRAINT foundation_photo_records_category_check CHECK (((category)::text = ANY ((ARRAY['PROGRESS'::character varying, 'ISSUE'::character varying, 'BEFORE'::character varying, 'AFTER'::character varying, 'SAFETY'::character varying, 'DIARIO_OBRA'::character varying, 'OUTROS'::character varying])::text[])))
);


ALTER TABLE public.foundation_photo_records OWNER TO postgres;

--
-- Name: foundation_team_members; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.foundation_team_members (
    id character varying(255) NOT NULL,
    cpf character varying(14),
    name character varying(255) NOT NULL,
    role character varying(255) NOT NULL,
    hours_worked double precision NOT NULL,
    notes character varying(500),
    status character varying(255) NOT NULL,
    foundation_id character varying(255) NOT NULL,
    CONSTRAINT foundation_team_members_status_check CHECK (((status)::text = ANY ((ARRAY['PRESENT'::character varying, 'ABSENT'::character varying, 'ON_LEAVE'::character varying])::text[])))
);


ALTER TABLE public.foundation_team_members OWNER TO postgres;

--
-- Name: foundation_tools; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.foundation_tools (
    id character varying(255) NOT NULL,
    category character varying(255) NOT NULL,
    condition character varying(255) NOT NULL,
    in_maintenance integer NOT NULL,
    in_use integer NOT NULL,
    name character varying(255) NOT NULL,
    notes character varying(1000),
    total_quantity integer NOT NULL,
    foundation_id character varying(255) NOT NULL,
    CONSTRAINT foundation_tools_condition_check CHECK (((condition)::text = ANY ((ARRAY['EXCELLENT'::character varying, 'GOOD'::character varying, 'POOR'::character varying, 'UNAVAILABLE'::character varying])::text[]))),
    CONSTRAINT foundation_tools_in_maintenance_check CHECK ((in_maintenance >= 0)),
    CONSTRAINT foundation_tools_in_use_check CHECK ((in_use >= 0)),
    CONSTRAINT foundation_tools_total_quantity_check CHECK ((total_quantity >= 1))
);


ALTER TABLE public.foundation_tools OWNER TO postgres;

--
-- Name: foundations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.foundations (
    id character varying(255) NOT NULL,
    contractor character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    user_id character varying(255)
);


ALTER TABLE public.foundations OWNER TO postgres;

--
-- Name: general_information; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.general_information (
    id character varying(255) NOT NULL,
    address character varying(500) NOT NULL,
    land_area double precision NOT NULL,
    observations character varying(1000),
    phase_id character varying(255) NOT NULL,
    start_date date NOT NULL,
    technical_manager character varying(200),
    topography character varying(255) NOT NULL,
    CONSTRAINT general_information_land_area_check CHECK ((land_area >= (0)::double precision)),
    CONSTRAINT general_information_topography_check CHECK (((topography)::text = ANY ((ARRAY['FLAT'::character varying, 'SLOPED'::character varying, 'HILLY'::character varying, 'MOUNTAINOUS'::character varying])::text[])))
);


ALTER TABLE public.general_information OWNER TO postgres;

--
-- Name: generic_executed_services; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.generic_executed_services (
    id character varying(255) NOT NULL,
    phase_id character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    planned_hours double precision,
    executed_hours double precision,
    status character varying(50),
    progress integer,
    notes text
);


ALTER TABLE public.generic_executed_services OWNER TO postgres;

--
-- Name: generic_general_info; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.generic_general_info (
    id character varying(255) NOT NULL,
    phase_id character varying(255) NOT NULL,
    climate character varying(100),
    work_date date,
    start_time time without time zone,
    end_time time without time zone,
    observations text
);


ALTER TABLE public.generic_general_info OWNER TO postgres;

--
-- Name: generic_machinery; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.generic_machinery (
    id character varying(255) NOT NULL,
    phase_id character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    category character varying(100),
    hours_worked double precision,
    fuel_used double precision,
    fuel_unit character varying(50),
    total_quantity integer,
    in_operation integer,
    in_maintenance integer,
    condition character varying(50),
    notes text
);


ALTER TABLE public.generic_machinery OWNER TO postgres;

--
-- Name: generic_materials; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.generic_materials (
    id character varying(255) NOT NULL,
    phase_id character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    quantity double precision,
    unit character varying(50),
    notes text
);


ALTER TABLE public.generic_materials OWNER TO postgres;

--
-- Name: generic_phases; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.generic_phases (
    id character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    contractor character varying(255),
    user_id character varying(255) NOT NULL
);


ALTER TABLE public.generic_phases OWNER TO postgres;

--
-- Name: generic_photo_records; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.generic_photo_records (
    id character varying(255) NOT NULL,
    phase_id character varying(255) NOT NULL,
    url text NOT NULL,
    description text,
    category character varying(100)
);


ALTER TABLE public.generic_photo_records OWNER TO postgres;

--
-- Name: generic_team_members; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.generic_team_members (
    id character varying(255) NOT NULL,
    phase_id character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    role character varying(100),
    status character varying(50),
    hours_worked double precision DEFAULT 0,
    notes text
);


ALTER TABLE public.generic_team_members OWNER TO postgres;

--
-- Name: generic_tools; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.generic_tools (
    id character varying(255) NOT NULL,
    phase_id character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    condition character varying(50),
    notes text
);


ALTER TABLE public.generic_tools OWNER TO postgres;

--
-- Name: hydraulic_executed_services; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hydraulic_executed_services (
    id character varying(255) NOT NULL,
    executed_hours double precision NOT NULL,
    name character varying(255) NOT NULL,
    notes character varying(1000),
    planned_hours double precision NOT NULL,
    progress integer NOT NULL,
    status character varying(255) NOT NULL,
    team character varying(255) NOT NULL,
    hydraulic_id character varying(255) NOT NULL,
    CONSTRAINT hydraulic_executed_services_executed_hours_check CHECK ((executed_hours >= (0)::double precision)),
    CONSTRAINT hydraulic_executed_services_planned_hours_check CHECK ((planned_hours >= (1)::double precision)),
    CONSTRAINT hydraulic_executed_services_progress_check CHECK ((progress >= 0)),
    CONSTRAINT hydraulic_executed_services_status_check CHECK (((status)::text = ANY ((ARRAY['PLANNED'::character varying, 'STARTED'::character varying, 'IN_PROGRESS'::character varying, 'COMPLETED'::character varying, 'CONCLUIDO'::character varying, 'EM_ANDAMENTO'::character varying, 'PLANEJADO'::character varying, 'INICIADO'::character varying])::text[])))
);


ALTER TABLE public.hydraulic_executed_services OWNER TO postgres;

--
-- Name: hydraulic_machinery; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hydraulic_machinery (
    id character varying(255) NOT NULL,
    category character varying(255) NOT NULL,
    condition character varying(255) NOT NULL,
    fuel_unit character varying(255) NOT NULL,
    fuel_used double precision NOT NULL,
    hours_worked double precision NOT NULL,
    in_maintenance integer NOT NULL,
    in_operation integer NOT NULL,
    name character varying(255) NOT NULL,
    notes character varying(1000),
    total_quantity integer NOT NULL,
    hydraulic_id character varying(255) NOT NULL,
    CONSTRAINT hydraulic_machinery_condition_check CHECK (((condition)::text = ANY ((ARRAY['EXCELLENT'::character varying, 'GOOD'::character varying, 'POOR'::character varying, 'UNAVAILABLE'::character varying])::text[]))),
    CONSTRAINT hydraulic_machinery_fuel_unit_check CHECK (((fuel_unit)::text = ANY ((ARRAY['LITERS'::character varying, 'HOURS'::character varying])::text[]))),
    CONSTRAINT hydraulic_machinery_fuel_used_check CHECK ((fuel_used >= (0)::double precision)),
    CONSTRAINT hydraulic_machinery_hours_worked_check CHECK ((hours_worked >= (0)::double precision)),
    CONSTRAINT hydraulic_machinery_in_maintenance_check CHECK ((in_maintenance >= 0)),
    CONSTRAINT hydraulic_machinery_in_operation_check CHECK ((in_operation >= 0))
);


ALTER TABLE public.hydraulic_machinery OWNER TO postgres;

--
-- Name: hydraulic_materials; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hydraulic_materials (
    id character varying(255) NOT NULL,
    category character varying(255) NOT NULL,
    consumed_quantity double precision NOT NULL,
    current_stock double precision NOT NULL,
    minimum_stock double precision NOT NULL,
    name character varying(255) NOT NULL,
    needs_restock boolean NOT NULL,
    unit character varying(255) NOT NULL,
    urgency character varying(255),
    hydraulic_id character varying(255) NOT NULL,
    CONSTRAINT hydraulic_materials_category_check CHECK (((category)::text = ANY ((ARRAY['CEMENT'::character varying, 'SAND'::character varying, 'STEEL'::character varying, 'BRICK'::character varying, 'WOOD'::character varying, 'ELECTRICAL'::character varying, 'PLUMBING'::character varying, 'OTHER'::character varying])::text[]))),
    CONSTRAINT hydraulic_materials_consumed_quantity_check CHECK ((consumed_quantity >= (0)::double precision)),
    CONSTRAINT hydraulic_materials_current_stock_check CHECK ((current_stock >= (0)::double precision)),
    CONSTRAINT hydraulic_materials_minimum_stock_check CHECK ((minimum_stock >= (0)::double precision)),
    CONSTRAINT hydraulic_materials_unit_check CHECK (((unit)::text = ANY ((ARRAY['BAG'::character varying, 'KG'::character varying, 'CUBIC_METER'::character varying, 'UNIT'::character varying, 'LITER'::character varying])::text[])))
);


ALTER TABLE public.hydraulic_materials OWNER TO postgres;

--
-- Name: hydraulic_photo_records; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hydraulic_photo_records (
    id character varying(255) NOT NULL,
    caption character varying(500),
    category character varying(255) NOT NULL,
    file_path character varying(1000) NOT NULL,
    uploaded_at timestamp(6) without time zone NOT NULL,
    hydraulic_id character varying(255) NOT NULL,
    CONSTRAINT hydraulic_photo_records_category_check CHECK (((category)::text = ANY ((ARRAY['PROGRESS'::character varying, 'ISSUE'::character varying, 'BEFORE'::character varying, 'AFTER'::character varying, 'SAFETY'::character varying, 'DIARIO_OBRA'::character varying, 'OUTROS'::character varying])::text[])))
);


ALTER TABLE public.hydraulic_photo_records OWNER TO postgres;

--
-- Name: hydraulic_team_members; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hydraulic_team_members (
    id character varying(255) NOT NULL,
    cpf character varying(14),
    name character varying(255) NOT NULL,
    role character varying(255) NOT NULL,
    hours_worked double precision NOT NULL,
    notes character varying(500),
    status character varying(255) NOT NULL,
    hydraulic_id character varying(255) NOT NULL,
    CONSTRAINT hydraulic_team_members_status_check CHECK (((status)::text = ANY ((ARRAY['PRESENT'::character varying, 'ABSENT'::character varying, 'ON_LEAVE'::character varying])::text[])))
);


ALTER TABLE public.hydraulic_team_members OWNER TO postgres;

--
-- Name: hydraulic_tools; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hydraulic_tools (
    id character varying(255) NOT NULL,
    category character varying(255) NOT NULL,
    condition character varying(255) NOT NULL,
    in_maintenance integer NOT NULL,
    in_use integer NOT NULL,
    name character varying(255) NOT NULL,
    notes character varying(1000),
    total_quantity integer NOT NULL,
    hydraulic_id character varying(255) NOT NULL,
    CONSTRAINT hydraulic_tools_condition_check CHECK (((condition)::text = ANY ((ARRAY['EXCELLENT'::character varying, 'GOOD'::character varying, 'POOR'::character varying, 'UNAVAILABLE'::character varying])::text[]))),
    CONSTRAINT hydraulic_tools_in_maintenance_check CHECK ((in_maintenance >= 0)),
    CONSTRAINT hydraulic_tools_in_use_check CHECK ((in_use >= 0)),
    CONSTRAINT hydraulic_tools_total_quantity_check CHECK ((total_quantity >= 1))
);


ALTER TABLE public.hydraulic_tools OWNER TO postgres;

--
-- Name: hydraulics; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hydraulics (
    id character varying(255) NOT NULL,
    contractor character varying(255),
    name character varying(255) NOT NULL,
    user_id character varying(255)
);


ALTER TABLE public.hydraulics OWNER TO postgres;

--
-- Name: machinery; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.machinery (
    id character varying(255) NOT NULL,
    category character varying(255) NOT NULL,
    condition character varying(255) NOT NULL,
    fuel_unit character varying(255) NOT NULL,
    fuel_used double precision NOT NULL,
    hours_worked double precision NOT NULL,
    in_maintenance integer NOT NULL,
    in_operation integer NOT NULL,
    name character varying(255) NOT NULL,
    notes character varying(1000),
    phase_id character varying(255) NOT NULL,
    total_quantity integer NOT NULL,
    CONSTRAINT machinery_condition_check CHECK (((condition)::text = ANY ((ARRAY['EXCELLENT'::character varying, 'GOOD'::character varying, 'POOR'::character varying, 'UNAVAILABLE'::character varying])::text[]))),
    CONSTRAINT machinery_fuel_unit_check CHECK (((fuel_unit)::text = ANY ((ARRAY['LITERS'::character varying, 'HOURS'::character varying])::text[]))),
    CONSTRAINT machinery_fuel_used_check CHECK ((fuel_used >= (0)::double precision)),
    CONSTRAINT machinery_hours_worked_check CHECK ((hours_worked >= (0)::double precision)),
    CONSTRAINT machinery_in_maintenance_check CHECK ((in_maintenance >= 0)),
    CONSTRAINT machinery_in_operation_check CHECK ((in_operation >= 0)),
    CONSTRAINT machinery_total_quantity_check CHECK ((total_quantity >= 1))
);


ALTER TABLE public.machinery OWNER TO postgres;

--
-- Name: masonry; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.masonry (
    id character varying(255) NOT NULL,
    contractor character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    user_id character varying(255)
);


ALTER TABLE public.masonry OWNER TO postgres;

--
-- Name: masonry_executed_services; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.masonry_executed_services (
    id character varying(255) NOT NULL,
    executed_hours double precision NOT NULL,
    name character varying(255) NOT NULL,
    notes character varying(1000),
    planned_hours double precision NOT NULL,
    progress integer NOT NULL,
    status character varying(255) NOT NULL,
    team character varying(255) NOT NULL,
    masonry_id character varying(255) NOT NULL,
    CONSTRAINT masonry_executed_services_executed_hours_check CHECK ((executed_hours >= (0)::double precision)),
    CONSTRAINT masonry_executed_services_planned_hours_check CHECK ((planned_hours >= (1)::double precision)),
    CONSTRAINT masonry_executed_services_progress_check CHECK ((progress >= 0)),
    CONSTRAINT masonry_executed_services_status_check CHECK (((status)::text = ANY ((ARRAY['PLANNED'::character varying, 'STARTED'::character varying, 'IN_PROGRESS'::character varying, 'COMPLETED'::character varying, 'CONCLUIDO'::character varying, 'EM_ANDAMENTO'::character varying, 'PLANEJADO'::character varying, 'INICIADO'::character varying])::text[])))
);


ALTER TABLE public.masonry_executed_services OWNER TO postgres;

--
-- Name: materials; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.materials (
    id character varying(255) NOT NULL,
    category character varying(255) NOT NULL,
    consumed_quantity double precision NOT NULL,
    current_stock double precision NOT NULL,
    minimum_stock double precision NOT NULL,
    name character varying(255) NOT NULL,
    needs_restock boolean NOT NULL,
    unit character varying(255) NOT NULL,
    urgency character varying(255),
    phase_id character varying(255),
    CONSTRAINT materials_category_check CHECK (((category)::text = ANY ((ARRAY['CEMENT'::character varying, 'SAND'::character varying, 'STEEL'::character varying, 'BRICK'::character varying, 'WOOD'::character varying, 'ELECTRICAL'::character varying, 'PLUMBING'::character varying, 'OTHER'::character varying])::text[]))),
    CONSTRAINT materials_consumed_quantity_check CHECK ((consumed_quantity >= (0)::double precision)),
    CONSTRAINT materials_current_stock_check CHECK ((current_stock >= (0)::double precision)),
    CONSTRAINT materials_minimum_stock_check CHECK ((minimum_stock >= (0)::double precision)),
    CONSTRAINT materials_unit_check CHECK (((unit)::text = ANY ((ARRAY['BAG'::character varying, 'KG'::character varying, 'CUBIC_METER'::character varying, 'UNIT'::character varying, 'LITER'::character varying])::text[])))
);


ALTER TABLE public.materials OWNER TO postgres;

--
-- Name: phases; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.phases (
    id character varying(255) NOT NULL,
    contractor character varying(255),
    name character varying(255) NOT NULL,
    construction_id character varying(255)
);


ALTER TABLE public.phases OWNER TO postgres;

--
-- Name: photo_records; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.photo_records (
    id character varying(255) NOT NULL,
    caption character varying(500),
    category character varying(255) NOT NULL,
    file_path character varying(1000) NOT NULL,
    uploaded_at timestamp(6) without time zone NOT NULL,
    phase_id character varying(255),
    CONSTRAINT photo_records_category_check CHECK (((category)::text = ANY ((ARRAY['PROGRESS'::character varying, 'ISSUE'::character varying, 'BEFORE'::character varying, 'AFTER'::character varying, 'SAFETY'::character varying, 'DIARIO_OBRA'::character varying, 'OUTROS'::character varying])::text[])))
);


ALTER TABLE public.photo_records OWNER TO postgres;

--
-- Name: team_members; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.team_members (
    id character varying(255) NOT NULL,
    cpf character varying(14),
    hours_worked double precision NOT NULL,
    name character varying(255) NOT NULL,
    notes character varying(500),
    role character varying(255) NOT NULL,
    status character varying(255) NOT NULL,
    phase_id character varying(255),
    CONSTRAINT team_members_status_check CHECK (((status)::text = ANY ((ARRAY['PRESENT'::character varying, 'ABSENT'::character varying, 'ON_LEAVE'::character varying])::text[])))
);


ALTER TABLE public.team_members OWNER TO postgres;

--
-- Name: tools; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tools (
    id character varying(255) NOT NULL,
    available integer NOT NULL,
    category character varying(255) NOT NULL,
    condition character varying(255) NOT NULL,
    in_maintenance integer NOT NULL,
    in_use integer NOT NULL,
    name character varying(255) NOT NULL,
    notes character varying(1000),
    total_quantity integer NOT NULL,
    phase_id character varying(255),
    CONSTRAINT tools_condition_check CHECK (((condition)::text = ANY ((ARRAY['EXCELLENT'::character varying, 'GOOD'::character varying, 'POOR'::character varying, 'UNAVAILABLE'::character varying])::text[]))),
    CONSTRAINT tools_in_maintenance_check CHECK ((in_maintenance >= 0)),
    CONSTRAINT tools_in_use_check CHECK ((in_use >= 0)),
    CONSTRAINT tools_total_quantity_check CHECK ((total_quantity >= 1))
);


ALTER TABLE public.tools OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id character varying(255) NOT NULL,
    cpf character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    first_name character varying(255) NOT NULL,
    last_name character varying(255) NOT NULL,
    phone character varying(255) NOT NULL,
    role character varying(255) NOT NULL,
    CONSTRAINT users_role_check CHECK (((role)::text = ANY ((ARRAY['CLIENT'::character varying, 'MASTER_BUILDER'::character varying, 'OWNER'::character varying])::text[])))
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Data for Name: coatings; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.coatings (id, contractor, name, user_id) FROM stdin;
e02218d0-9135-4d1a-bd51-952b924089b8	Joao Silva	Coatings - Joao Silva	a92f01cb-9302-47bc-b44f-08c384bc9e28
09fed694-a7e6-4bfc-995d-69b0b22bd99e	Pedro Mestre	Coatings - Pedro Mestre	5eb2102d-51d9-4564-9f5a-ef2c7978e8cb
6e5e041c-5f57-4a69-a88c-380eca61c463	João Silva	Coatings - João Silva	7ed9e6c9-1039-4ed2-a287-c56db5ee4dbf
\.


--
-- Data for Name: coatings_executed_services; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.coatings_executed_services (id, executed_hours, name, notes, planned_hours, progress, status, team, coatings_id) FROM stdin;
\.


--
-- Data for Name: coatings_machineries; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.coatings_machineries (id, category, condition, fuel_unit, fuel_used, hours_worked, in_maintenance, in_operation, name, notes, total_quantity, coatings_id) FROM stdin;
\.


--
-- Data for Name: coatings_materials; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.coatings_materials (id, category, consumed_quantity, current_stock, minimum_stock, name, needs_restock, unit, urgency, coatings_id) FROM stdin;
\.


--
-- Data for Name: coatings_photo_records; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.coatings_photo_records (id, caption, category, file_path, uploaded_at, coatings_id) FROM stdin;
\.


--
-- Data for Name: coatings_team_members; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.coatings_team_members (id, cpf, name, role, hours_worked, notes, status, coatings_id) FROM stdin;
\.


--
-- Data for Name: coatings_tools; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.coatings_tools (id, category, condition, in_maintenance, in_use, name, notes, total_quantity, coatings_id) FROM stdin;
\.


--
-- Data for Name: construction_clients; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.construction_clients (construction_id, user_id) FROM stdin;
d3462a0c-30b1-40e2-802a-0ad466de31e6	4366528e-8339-4db9-a252-4366105cc762
\.


--
-- Data for Name: construction_master_builders; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.construction_master_builders (construction_id, user_id) FROM stdin;
d3462a0c-30b1-40e2-802a-0ad466de31e6	1ff4aecb-a40b-4039-806e-485dc63f5da5
\.


--
-- Data for Name: constructions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.constructions (id, name, status, owner_id) FROM stdin;
d3462a0c-30b1-40e2-802a-0ad466de31e6	Residencial Horizonte	ACTIVE	f028caa4-dd19-400e-a365-633c8aa9eacb
\.


--
-- Data for Name: custom_phase_components; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.custom_phase_components (custom_phase_id, component_name) FROM stdin;
\.


--
-- Data for Name: custom_phases; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.custom_phases (id, name, user_id) FROM stdin;
\.


--
-- Data for Name: eletric; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.eletric (id, contractor, name, user_id) FROM stdin;
4deb60a5-5965-4dc6-9906-f8d51083c5fd	Joao Silva	Eletric - Joao Silva	a92f01cb-9302-47bc-b44f-08c384bc9e28
4c008627-e1e1-494c-a7dc-04c96e9555c2	Pedro Mestre	Eletric - Pedro Mestre	5eb2102d-51d9-4564-9f5a-ef2c7978e8cb
a25d2fcb-2ea4-4fcb-aa2c-09a69773e536	João Silva	Eletric - João Silva	7ed9e6c9-1039-4ed2-a287-c56db5ee4dbf
\.


--
-- Data for Name: eletric_executed_services; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.eletric_executed_services (id, executed_hours, name, notes, planned_hours, progress, status, team, eletric_id) FROM stdin;
\.


--
-- Data for Name: eletric_machineries; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.eletric_machineries (id, category, condition, fuel_unit, fuel_used, hours_worked, in_maintenance, in_operation, name, notes, total_quantity, eletric_id) FROM stdin;
\.


--
-- Data for Name: eletric_materials; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.eletric_materials (id, category, consumed_quantity, current_stock, minimum_stock, name, needs_restock, unit, urgency, eletric_id) FROM stdin;
\.


--
-- Data for Name: eletric_photo_records; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.eletric_photo_records (id, caption, category, file_path, uploaded_at, eletric_id) FROM stdin;
\.


--
-- Data for Name: eletric_team_members; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.eletric_team_members (id, cpf, name, role, hours_worked, notes, status, eletric_id) FROM stdin;
\.


--
-- Data for Name: eletric_tools; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.eletric_tools (id, category, condition, in_maintenance, in_use, name, notes, total_quantity, eletric_id) FROM stdin;
\.


--
-- Data for Name: executed_services; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.executed_services (id, executed_hours, name, notes, planned_hours, progress, status, team, phase_id) FROM stdin;
\.


--
-- Data for Name: finishing_executed_services; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.finishing_executed_services (id, executed_hours, name, notes, planned_hours, progress, status, team, finishing_id) FROM stdin;
\.


--
-- Data for Name: finishing_machineries; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.finishing_machineries (id, category, condition, fuel_unit, fuel_used, hours_worked, in_maintenance, in_operation, name, notes, total_quantity, finishing_id) FROM stdin;
\.


--
-- Data for Name: finishing_materials; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.finishing_materials (id, category, consumed_quantity, current_stock, minimum_stock, name, needs_restock, unit, urgency, finishing_id) FROM stdin;
\.


--
-- Data for Name: finishing_photo_records; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.finishing_photo_records (id, caption, category, file_path, uploaded_at, finishing_id) FROM stdin;
\.


--
-- Data for Name: finishing_team_members; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.finishing_team_members (id, cpf, name, role, hours_worked, notes, status, finishing_id) FROM stdin;
\.


--
-- Data for Name: finishing_tools; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.finishing_tools (id, category, condition, in_maintenance, in_use, name, notes, total_quantity, finishing_id) FROM stdin;
\.


--
-- Data for Name: finishings; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.finishings (id, contractor, name, user_id) FROM stdin;
e4932863-5ae4-4461-91b4-619b95b1ecbd	Joao Silva	Finishing - Joao Silva	a92f01cb-9302-47bc-b44f-08c384bc9e28
3170e6b8-14f5-4abd-8cfb-a4359ddc2edf	Pedro Mestre	Finishing - Pedro Mestre	5eb2102d-51d9-4564-9f5a-ef2c7978e8cb
12dfd6b8-3c18-4466-a6e9-889d66a8c59e	João Silva	Finishing - João Silva	7ed9e6c9-1039-4ed2-a287-c56db5ee4dbf
\.


--
-- Data for Name: foundation_executed_services; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.foundation_executed_services (id, executed_hours, name, notes, planned_hours, progress, status, team, foundation_id) FROM stdin;
e58a967d-2e62-47a2-bafa-c8b90cde559b	19.5	Assentar tijolo	Assentar tijolos na Fundação da construção\n	80	24	PLANEJADO	Equipe Principal	98990f51-d793-4424-897f-f353478c22c5
48b4c5af-5587-45d1-ac52-95624c321a1e	3	Furar paredes da sala de estar	Os operacionais vão furar a parede.	20	15	PLANEJADO	Operacionais B	98990f51-d793-4424-897f-f353478c22c5
5da5b008-b83b-47a0-99ef-e161189b8963	8.5	Concretar vigas das colunas		18	47	PLANEJADO	Equipe A	98990f51-d793-4424-897f-f353478c22c5
\.


--
-- Data for Name: foundation_machinery; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.foundation_machinery (id, category, condition, fuel_unit, fuel_used, hours_worked, in_maintenance, in_operation, name, notes, total_quantity, foundation_id) FROM stdin;
79a563f8-d784-4f0e-8d37-0275746a7e2f	Pesado	GOOD	LITERS	59.9	5	2	3	Retroescavadeira John Deere		5	98990f51-d793-4424-897f-f353478c22c5
\.


--
-- Data for Name: foundation_materials; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.foundation_materials (id, category, consumed_quantity, current_stock, minimum_stock, name, needs_restock, unit, urgency, foundation_id) FROM stdin;
\.


--
-- Data for Name: foundation_photo_records; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.foundation_photo_records (id, caption, category, file_path, uploaded_at, foundation_id) FROM stdin;
\.


--
-- Data for Name: foundation_team_members; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.foundation_team_members (id, cpf, name, role, hours_worked, notes, status, foundation_id) FROM stdin;
3c6610b9-4335-4b38-80bb-33be2b5108ab	55544477788	Bruno Macedo	Pedreiro	50	Pedreiro responsável pela Fundação inicial.	PRESENT	98990f51-d793-4424-897f-f353478c22c5
ff10051d-e88f-45a8-9569-f131e3954f37		Roger	Ajudante de Pedreiro	28	Ajudante da fundação do Bruno	PRESENT	98990f51-d793-4424-897f-f353478c22c5
87b51d2a-268c-42bc-91b1-bc864a2303dc	77788899929	Ricardo	Ajudante de Pedreiro	20		PRESENT	98990f51-d793-4424-897f-f353478c22c5
\.


--
-- Data for Name: foundation_tools; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.foundation_tools (id, category, condition, in_maintenance, in_use, name, notes, total_quantity, foundation_id) FROM stdin;
\.


--
-- Data for Name: foundations; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.foundations (id, contractor, name, user_id) FROM stdin;
98990f51-d793-4424-897f-f353478c22c5	Joao Silva	Foundation - Joao Silva	a92f01cb-9302-47bc-b44f-08c384bc9e28
7fac42d9-da54-4844-bf5f-9e9cc6ccc84a	Pedro Mestre	Foundation - Pedro Mestre	5eb2102d-51d9-4564-9f5a-ef2c7978e8cb
b8078eb9-b524-4546-9f1a-b3e5680f437c	João Silva	Foundation - João Silva	7ed9e6c9-1039-4ed2-a287-c56db5ee4dbf
\.


--
-- Data for Name: general_information; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.general_information (id, address, land_area, observations, phase_id, start_date, technical_manager, topography) FROM stdin;
\.


--
-- Data for Name: generic_executed_services; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.generic_executed_services (id, phase_id, name, planned_hours, executed_hours, status, progress, notes) FROM stdin;
\.


--
-- Data for Name: generic_general_info; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.generic_general_info (id, phase_id, climate, work_date, start_time, end_time, observations) FROM stdin;
\.


--
-- Data for Name: generic_machinery; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.generic_machinery (id, phase_id, name, category, hours_worked, fuel_used, fuel_unit, total_quantity, in_operation, in_maintenance, condition, notes) FROM stdin;
\.


--
-- Data for Name: generic_materials; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.generic_materials (id, phase_id, name, quantity, unit, notes) FROM stdin;
\.


--
-- Data for Name: generic_phases; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.generic_phases (id, name, contractor, user_id) FROM stdin;
\.


--
-- Data for Name: generic_photo_records; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.generic_photo_records (id, phase_id, url, description, category) FROM stdin;
\.


--
-- Data for Name: generic_team_members; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.generic_team_members (id, phase_id, name, role, status, hours_worked, notes) FROM stdin;
\.


--
-- Data for Name: generic_tools; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.generic_tools (id, phase_id, name, condition, notes) FROM stdin;
\.


--
-- Data for Name: hydraulic_executed_services; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hydraulic_executed_services (id, executed_hours, name, notes, planned_hours, progress, status, team, hydraulic_id) FROM stdin;
\.


--
-- Data for Name: hydraulic_machinery; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hydraulic_machinery (id, category, condition, fuel_unit, fuel_used, hours_worked, in_maintenance, in_operation, name, notes, total_quantity, hydraulic_id) FROM stdin;
\.


--
-- Data for Name: hydraulic_materials; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hydraulic_materials (id, category, consumed_quantity, current_stock, minimum_stock, name, needs_restock, unit, urgency, hydraulic_id) FROM stdin;
\.


--
-- Data for Name: hydraulic_photo_records; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hydraulic_photo_records (id, caption, category, file_path, uploaded_at, hydraulic_id) FROM stdin;
\.


--
-- Data for Name: hydraulic_team_members; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hydraulic_team_members (id, cpf, name, role, hours_worked, notes, status, hydraulic_id) FROM stdin;
\.


--
-- Data for Name: hydraulic_tools; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hydraulic_tools (id, category, condition, in_maintenance, in_use, name, notes, total_quantity, hydraulic_id) FROM stdin;
\.


--
-- Data for Name: hydraulics; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hydraulics (id, contractor, name, user_id) FROM stdin;
3bd29c8b-2279-4600-88e4-a3972c17e5ce	Joao Silva	Hydraulic - Joao Silva	a92f01cb-9302-47bc-b44f-08c384bc9e28
6ad49b9c-8a9e-424d-a025-fe5c52ce31bf	Pedro Mestre	Hydraulic - Pedro Mestre	5eb2102d-51d9-4564-9f5a-ef2c7978e8cb
0cd4d7aa-9dce-4ad7-905c-23f9d4473dc9	João Silva	Hydraulic - João Silva	7ed9e6c9-1039-4ed2-a287-c56db5ee4dbf
\.


--
-- Data for Name: machinery; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.machinery (id, category, condition, fuel_unit, fuel_used, hours_worked, in_maintenance, in_operation, name, notes, phase_id, total_quantity) FROM stdin;
\.


--
-- Data for Name: masonry; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.masonry (id, contractor, name, user_id) FROM stdin;
a602f070-c7f9-4546-a6e8-8a89f9bb8c61	Joao Silva	Masonry - Joao Silva	a92f01cb-9302-47bc-b44f-08c384bc9e28
efe0011e-a7fa-47bd-80a5-cd67f1e89060	Pedro Mestre	Masonry - Pedro Mestre	5eb2102d-51d9-4564-9f5a-ef2c7978e8cb
c1a8ddab-c41e-4db0-8c57-c155c14f2a0c	João Silva	Masonry - João Silva	7ed9e6c9-1039-4ed2-a287-c56db5ee4dbf
\.


--
-- Data for Name: masonry_executed_services; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.masonry_executed_services (id, executed_hours, name, notes, planned_hours, progress, status, team, masonry_id) FROM stdin;
\.


--
-- Data for Name: materials; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.materials (id, category, consumed_quantity, current_stock, minimum_stock, name, needs_restock, unit, urgency, phase_id) FROM stdin;
\.


--
-- Data for Name: phases; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.phases (id, contractor, name, construction_id) FROM stdin;
\.


--
-- Data for Name: photo_records; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.photo_records (id, caption, category, file_path, uploaded_at, phase_id) FROM stdin;
\.


--
-- Data for Name: team_members; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.team_members (id, cpf, hours_worked, name, notes, role, status, phase_id) FROM stdin;
\.


--
-- Data for Name: tools; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tools (id, available, category, condition, in_maintenance, in_use, name, notes, total_quantity, phase_id) FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, cpf, email, first_name, last_name, phone, role) FROM stdin;
a92f01cb-9302-47bc-b44f-08c384bc9e28	434.734.228-20	ricardo.silva@example.com	Joao	Silva	55-11-98765-4321	CLIENT
5eb2102d-51d9-4564-9f5a-ef2c7978e8cb	987.654.321-11	pedro.obras@email.com	Pedro	Mestre	11888888888	MASTER_BUILDER
7ed9e6c9-1039-4ed2-a287-c56db5ee4dbf	123.456.789-00	joao.silva@email.com	João	Silva	11999999999	CLIENT
f028caa4-dd19-400e-a365-633c8aa9eacb	000.000.000-01	contato@premium.com	Construtora B&M	Premium	11999999999	OWNER
1ff4aecb-a40b-4039-806e-485dc63f5da5	000.000.000-02	geraldo@obras.com	Mestre	Geraldo	11988888888	MASTER_BUILDER
4366528e-8339-4db9-a252-4366105cc762	000.000.000-03	carlos@cliente.com	Carlos	Dono	11977777777	CLIENT
\.


--
-- Name: coatings_executed_services coatings_executed_services_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coatings_executed_services
    ADD CONSTRAINT coatings_executed_services_pkey PRIMARY KEY (id);


--
-- Name: coatings_machineries coatings_machineries_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coatings_machineries
    ADD CONSTRAINT coatings_machineries_pkey PRIMARY KEY (id);


--
-- Name: coatings_materials coatings_materials_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coatings_materials
    ADD CONSTRAINT coatings_materials_pkey PRIMARY KEY (id);


--
-- Name: coatings_photo_records coatings_photo_records_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coatings_photo_records
    ADD CONSTRAINT coatings_photo_records_pkey PRIMARY KEY (id);


--
-- Name: coatings coatings_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coatings
    ADD CONSTRAINT coatings_pkey PRIMARY KEY (id);


--
-- Name: coatings_team_members coatings_team_members_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coatings_team_members
    ADD CONSTRAINT coatings_team_members_pkey PRIMARY KEY (id);


--
-- Name: coatings_tools coatings_tools_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coatings_tools
    ADD CONSTRAINT coatings_tools_pkey PRIMARY KEY (id);


--
-- Name: constructions constructions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.constructions
    ADD CONSTRAINT constructions_pkey PRIMARY KEY (id);


--
-- Name: custom_phases custom_phases_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.custom_phases
    ADD CONSTRAINT custom_phases_pkey PRIMARY KEY (id);


--
-- Name: eletric_executed_services eletric_executed_services_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.eletric_executed_services
    ADD CONSTRAINT eletric_executed_services_pkey PRIMARY KEY (id);


--
-- Name: eletric_machineries eletric_machineries_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.eletric_machineries
    ADD CONSTRAINT eletric_machineries_pkey PRIMARY KEY (id);


--
-- Name: eletric_materials eletric_materials_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.eletric_materials
    ADD CONSTRAINT eletric_materials_pkey PRIMARY KEY (id);


--
-- Name: eletric_photo_records eletric_photo_records_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.eletric_photo_records
    ADD CONSTRAINT eletric_photo_records_pkey PRIMARY KEY (id);


--
-- Name: eletric eletric_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.eletric
    ADD CONSTRAINT eletric_pkey PRIMARY KEY (id);


--
-- Name: eletric_team_members eletric_team_members_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.eletric_team_members
    ADD CONSTRAINT eletric_team_members_pkey PRIMARY KEY (id);


--
-- Name: eletric_tools eletric_tools_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.eletric_tools
    ADD CONSTRAINT eletric_tools_pkey PRIMARY KEY (id);


--
-- Name: executed_services executed_services_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.executed_services
    ADD CONSTRAINT executed_services_pkey PRIMARY KEY (id);


--
-- Name: finishing_executed_services finishing_executed_services_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.finishing_executed_services
    ADD CONSTRAINT finishing_executed_services_pkey PRIMARY KEY (id);


--
-- Name: finishing_machineries finishing_machineries_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.finishing_machineries
    ADD CONSTRAINT finishing_machineries_pkey PRIMARY KEY (id);


--
-- Name: finishing_materials finishing_materials_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.finishing_materials
    ADD CONSTRAINT finishing_materials_pkey PRIMARY KEY (id);


--
-- Name: finishing_photo_records finishing_photo_records_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.finishing_photo_records
    ADD CONSTRAINT finishing_photo_records_pkey PRIMARY KEY (id);


--
-- Name: finishing_team_members finishing_team_members_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.finishing_team_members
    ADD CONSTRAINT finishing_team_members_pkey PRIMARY KEY (id);


--
-- Name: finishing_tools finishing_tools_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.finishing_tools
    ADD CONSTRAINT finishing_tools_pkey PRIMARY KEY (id);


--
-- Name: finishings finishings_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.finishings
    ADD CONSTRAINT finishings_pkey PRIMARY KEY (id);


--
-- Name: foundation_executed_services foundation_executed_services_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.foundation_executed_services
    ADD CONSTRAINT foundation_executed_services_pkey PRIMARY KEY (id);


--
-- Name: foundation_machinery foundation_machinery_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.foundation_machinery
    ADD CONSTRAINT foundation_machinery_pkey PRIMARY KEY (id);


--
-- Name: foundation_materials foundation_materials_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.foundation_materials
    ADD CONSTRAINT foundation_materials_pkey PRIMARY KEY (id);


--
-- Name: foundation_photo_records foundation_photo_records_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.foundation_photo_records
    ADD CONSTRAINT foundation_photo_records_pkey PRIMARY KEY (id);


--
-- Name: foundation_team_members foundation_team_members_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.foundation_team_members
    ADD CONSTRAINT foundation_team_members_pkey PRIMARY KEY (id);


--
-- Name: foundation_tools foundation_tools_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.foundation_tools
    ADD CONSTRAINT foundation_tools_pkey PRIMARY KEY (id);


--
-- Name: foundations foundations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.foundations
    ADD CONSTRAINT foundations_pkey PRIMARY KEY (id);


--
-- Name: general_information general_information_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.general_information
    ADD CONSTRAINT general_information_pkey PRIMARY KEY (id);


--
-- Name: generic_executed_services generic_executed_services_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.generic_executed_services
    ADD CONSTRAINT generic_executed_services_pkey PRIMARY KEY (id);


--
-- Name: generic_general_info generic_general_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.generic_general_info
    ADD CONSTRAINT generic_general_info_pkey PRIMARY KEY (id);


--
-- Name: generic_machinery generic_machinery_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.generic_machinery
    ADD CONSTRAINT generic_machinery_pkey PRIMARY KEY (id);


--
-- Name: generic_materials generic_materials_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.generic_materials
    ADD CONSTRAINT generic_materials_pkey PRIMARY KEY (id);


--
-- Name: generic_phases generic_phases_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.generic_phases
    ADD CONSTRAINT generic_phases_pkey PRIMARY KEY (id);


--
-- Name: generic_photo_records generic_photo_records_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.generic_photo_records
    ADD CONSTRAINT generic_photo_records_pkey PRIMARY KEY (id);


--
-- Name: generic_team_members generic_team_members_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.generic_team_members
    ADD CONSTRAINT generic_team_members_pkey PRIMARY KEY (id);


--
-- Name: generic_tools generic_tools_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.generic_tools
    ADD CONSTRAINT generic_tools_pkey PRIMARY KEY (id);


--
-- Name: hydraulic_executed_services hydraulic_executed_services_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hydraulic_executed_services
    ADD CONSTRAINT hydraulic_executed_services_pkey PRIMARY KEY (id);


--
-- Name: hydraulic_machinery hydraulic_machinery_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hydraulic_machinery
    ADD CONSTRAINT hydraulic_machinery_pkey PRIMARY KEY (id);


--
-- Name: hydraulic_materials hydraulic_materials_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hydraulic_materials
    ADD CONSTRAINT hydraulic_materials_pkey PRIMARY KEY (id);


--
-- Name: hydraulic_photo_records hydraulic_photo_records_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hydraulic_photo_records
    ADD CONSTRAINT hydraulic_photo_records_pkey PRIMARY KEY (id);


--
-- Name: hydraulic_team_members hydraulic_team_members_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hydraulic_team_members
    ADD CONSTRAINT hydraulic_team_members_pkey PRIMARY KEY (id);


--
-- Name: hydraulic_tools hydraulic_tools_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hydraulic_tools
    ADD CONSTRAINT hydraulic_tools_pkey PRIMARY KEY (id);


--
-- Name: hydraulics hydraulics_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hydraulics
    ADD CONSTRAINT hydraulics_pkey PRIMARY KEY (id);


--
-- Name: machinery machinery_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.machinery
    ADD CONSTRAINT machinery_pkey PRIMARY KEY (id);


--
-- Name: masonry_executed_services masonry_executed_services_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.masonry_executed_services
    ADD CONSTRAINT masonry_executed_services_pkey PRIMARY KEY (id);


--
-- Name: masonry masonry_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.masonry
    ADD CONSTRAINT masonry_pkey PRIMARY KEY (id);


--
-- Name: materials materials_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.materials
    ADD CONSTRAINT materials_pkey PRIMARY KEY (id);


--
-- Name: phases phases_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.phases
    ADD CONSTRAINT phases_pkey PRIMARY KEY (id);


--
-- Name: photo_records photo_records_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.photo_records
    ADD CONSTRAINT photo_records_pkey PRIMARY KEY (id);


--
-- Name: team_members team_members_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.team_members
    ADD CONSTRAINT team_members_pkey PRIMARY KEY (id);


--
-- Name: tools tools_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tools
    ADD CONSTRAINT tools_pkey PRIMARY KEY (id);


--
-- Name: foundations uk3392xydt53askvals4j2vvk0h; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.foundations
    ADD CONSTRAINT uk3392xydt53askvals4j2vvk0h UNIQUE (user_id);


--
-- Name: users uk6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: hydraulics uk7k5m7ryunb3bq6ix0aqlji0q1; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hydraulics
    ADD CONSTRAINT uk7k5m7ryunb3bq6ix0aqlji0q1 UNIQUE (user_id);


--
-- Name: users uk7kqluf7wl0oxs7n90fpya03ss; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk7kqluf7wl0oxs7n90fpya03ss UNIQUE (cpf);


--
-- Name: finishings ukfmfrxt31liecymcdc3nlhxpm2; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.finishings
    ADD CONSTRAINT ukfmfrxt31liecymcdc3nlhxpm2 UNIQUE (user_id);


--
-- Name: masonry ukmb81o32mhv82ciy607n0ubc1y; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.masonry
    ADD CONSTRAINT ukmb81o32mhv82ciy607n0ubc1y UNIQUE (user_id);


--
-- Name: eletric ukmcodft214ajm7meh6ekcb1bcm; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.eletric
    ADD CONSTRAINT ukmcodft214ajm7meh6ekcb1bcm UNIQUE (user_id);


--
-- Name: coatings uks3d82w79so2jv9rr64hf16tqe; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coatings
    ADD CONSTRAINT uks3d82w79so2jv9rr64hf16tqe UNIQUE (user_id);


--
-- Name: general_information uks904aq4rg2l2a5nsgl4795kss; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.general_information
    ADD CONSTRAINT uks904aq4rg2l2a5nsgl4795kss UNIQUE (phase_id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: masonry_executed_services fk1d386g22kmkot0mlvlaflv6t2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.masonry_executed_services
    ADD CONSTRAINT fk1d386g22kmkot0mlvlaflv6t2 FOREIGN KEY (masonry_id) REFERENCES public.masonry(id);


--
-- Name: coatings_materials fk1jff0url4ll4sv8fbxarklouf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coatings_materials
    ADD CONSTRAINT fk1jff0url4ll4sv8fbxarklouf FOREIGN KEY (coatings_id) REFERENCES public.coatings(id);


--
-- Name: coatings_tools fk1po4dpowl7nyf7v3ebln33h2f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coatings_tools
    ADD CONSTRAINT fk1po4dpowl7nyf7v3ebln33h2f FOREIGN KEY (coatings_id) REFERENCES public.coatings(id);


--
-- Name: materials fk26204qfglx3gdhhqmhaicytr5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.materials
    ADD CONSTRAINT fk26204qfglx3gdhhqmhaicytr5 FOREIGN KEY (phase_id) REFERENCES public.phases(id);


--
-- Name: construction_master_builders fk2dke3cnd7ouqoylvjw8ldm37t; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.construction_master_builders
    ADD CONSTRAINT fk2dke3cnd7ouqoylvjw8ldm37t FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: coatings_photo_records fk2f391fhkufcbx83oflp3ds310; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coatings_photo_records
    ADD CONSTRAINT fk2f391fhkufcbx83oflp3ds310 FOREIGN KEY (coatings_id) REFERENCES public.coatings(id);


--
-- Name: eletric_executed_services fk2hfqai738nb49siayougmtavd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.eletric_executed_services
    ADD CONSTRAINT fk2hfqai738nb49siayougmtavd FOREIGN KEY (eletric_id) REFERENCES public.eletric(id);


--
-- Name: finishing_executed_services fk3361vhsrjcuxw760wv9l5eewn; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.finishing_executed_services
    ADD CONSTRAINT fk3361vhsrjcuxw760wv9l5eewn FOREIGN KEY (finishing_id) REFERENCES public.finishings(id);


--
-- Name: custom_phase_components fk33roxuwy8u9pwggm2r9rieidk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.custom_phase_components
    ADD CONSTRAINT fk33roxuwy8u9pwggm2r9rieidk FOREIGN KEY (custom_phase_id) REFERENCES public.custom_phases(id);


--
-- Name: coatings_team_members fk4e0j5burm72eo1vnv3us9mcnl; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coatings_team_members
    ADD CONSTRAINT fk4e0j5burm72eo1vnv3us9mcnl FOREIGN KEY (coatings_id) REFERENCES public.coatings(id);


--
-- Name: construction_master_builders fk4ncci5oy0ddg82qy89rgtittf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.construction_master_builders
    ADD CONSTRAINT fk4ncci5oy0ddg82qy89rgtittf FOREIGN KEY (construction_id) REFERENCES public.constructions(id);


--
-- Name: eletric_materials fk4s7tji2ies5v3ltvuipckq8tc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.eletric_materials
    ADD CONSTRAINT fk4s7tji2ies5v3ltvuipckq8tc FOREIGN KEY (eletric_id) REFERENCES public.eletric(id);


--
-- Name: finishing_photo_records fk68jjjq3s74smcuv0ce646qm9c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.finishing_photo_records
    ADD CONSTRAINT fk68jjjq3s74smcuv0ce646qm9c FOREIGN KEY (finishing_id) REFERENCES public.finishings(id);


--
-- Name: executed_services fk75sa2fps9r4ec17kmr21mk3v8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.executed_services
    ADD CONSTRAINT fk75sa2fps9r4ec17kmr21mk3v8 FOREIGN KEY (phase_id) REFERENCES public.phases(id);


--
-- Name: hydraulics fk80hidx8a37aft5f0werm5r79v; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hydraulics
    ADD CONSTRAINT fk80hidx8a37aft5f0werm5r79v FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: masonry fk9p0ha9mxf6ctwcihg2oolt9hi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.masonry
    ADD CONSTRAINT fk9p0ha9mxf6ctwcihg2oolt9hi FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: coatings_executed_services fk9s3dg0qmnn1c390olgc7aacan; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coatings_executed_services
    ADD CONSTRAINT fk9s3dg0qmnn1c390olgc7aacan FOREIGN KEY (coatings_id) REFERENCES public.coatings(id);


--
-- Name: generic_general_info fk_phase_info; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.generic_general_info
    ADD CONSTRAINT fk_phase_info FOREIGN KEY (phase_id) REFERENCES public.generic_phases(id) ON DELETE CASCADE;


--
-- Name: generic_machinery fk_phase_machinery; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.generic_machinery
    ADD CONSTRAINT fk_phase_machinery FOREIGN KEY (phase_id) REFERENCES public.generic_phases(id) ON DELETE CASCADE;


--
-- Name: generic_materials fk_phase_material; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.generic_materials
    ADD CONSTRAINT fk_phase_material FOREIGN KEY (phase_id) REFERENCES public.generic_phases(id) ON DELETE CASCADE;


--
-- Name: generic_photo_records fk_phase_photo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.generic_photo_records
    ADD CONSTRAINT fk_phase_photo FOREIGN KEY (phase_id) REFERENCES public.generic_phases(id) ON DELETE CASCADE;


--
-- Name: generic_executed_services fk_phase_service; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.generic_executed_services
    ADD CONSTRAINT fk_phase_service FOREIGN KEY (phase_id) REFERENCES public.generic_phases(id) ON DELETE CASCADE;


--
-- Name: generic_team_members fk_phase_team; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.generic_team_members
    ADD CONSTRAINT fk_phase_team FOREIGN KEY (phase_id) REFERENCES public.generic_phases(id) ON DELETE CASCADE;


--
-- Name: generic_tools fk_phase_tool; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.generic_tools
    ADD CONSTRAINT fk_phase_tool FOREIGN KEY (phase_id) REFERENCES public.generic_phases(id) ON DELETE CASCADE;


--
-- Name: generic_phases fk_user_phase; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.generic_phases
    ADD CONSTRAINT fk_user_phase FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: finishing_team_members fkceltgjl4wv8edh9x1x5ym26xb; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.finishing_team_members
    ADD CONSTRAINT fkceltgjl4wv8edh9x1x5ym26xb FOREIGN KEY (finishing_id) REFERENCES public.finishings(id);


--
-- Name: finishing_materials fkcvml32dv429luoqdmsq5f5llw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.finishing_materials
    ADD CONSTRAINT fkcvml32dv429luoqdmsq5f5llw FOREIGN KEY (finishing_id) REFERENCES public.finishings(id);


--
-- Name: hydraulic_team_members fkdebvppus3d63hnelb9wx6pwsh; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hydraulic_team_members
    ADD CONSTRAINT fkdebvppus3d63hnelb9wx6pwsh FOREIGN KEY (hydraulic_id) REFERENCES public.hydraulics(id);


--
-- Name: hydraulic_materials fkeh1484o6qebjgvo6owjy2tfyb; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hydraulic_materials
    ADD CONSTRAINT fkeh1484o6qebjgvo6owjy2tfyb FOREIGN KEY (hydraulic_id) REFERENCES public.hydraulics(id);


--
-- Name: coatings fkesm7tiwdofga85cddp2mekpjy; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coatings
    ADD CONSTRAINT fkesm7tiwdofga85cddp2mekpjy FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: constructions fkgcad2hauqwds16cnpyulk287o; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.constructions
    ADD CONSTRAINT fkgcad2hauqwds16cnpyulk287o FOREIGN KEY (owner_id) REFERENCES public.users(id);


--
-- Name: hydraulic_executed_services fkghi9p47ndqnajlk9r1w77t57b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hydraulic_executed_services
    ADD CONSTRAINT fkghi9p47ndqnajlk9r1w77t57b FOREIGN KEY (hydraulic_id) REFERENCES public.hydraulics(id);


--
-- Name: eletric_machineries fkh0eucul6seiegfhvhs6dyw5jc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.eletric_machineries
    ADD CONSTRAINT fkh0eucul6seiegfhvhs6dyw5jc FOREIGN KEY (eletric_id) REFERENCES public.eletric(id);


--
-- Name: finishing_tools fkhiufaf5s26wkyfeja7xv11j2w; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.finishing_tools
    ADD CONSTRAINT fkhiufaf5s26wkyfeja7xv11j2w FOREIGN KEY (finishing_id) REFERENCES public.finishings(id);


--
-- Name: coatings_machineries fkhke71i1vucqsqgr2xx22w47jv; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coatings_machineries
    ADD CONSTRAINT fkhke71i1vucqsqgr2xx22w47jv FOREIGN KEY (coatings_id) REFERENCES public.coatings(id);


--
-- Name: eletric fkhuvh2gmjlr0lhhyb8arit4g83; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.eletric
    ADD CONSTRAINT fkhuvh2gmjlr0lhhyb8arit4g83 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: foundation_team_members fkhy6ppffxg57sjno87v9a2ffwp; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.foundation_team_members
    ADD CONSTRAINT fkhy6ppffxg57sjno87v9a2ffwp FOREIGN KEY (foundation_id) REFERENCES public.foundations(id);


--
-- Name: team_members fkiv0e30ph6dnhfy6dg9rewefy5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.team_members
    ADD CONSTRAINT fkiv0e30ph6dnhfy6dg9rewefy5 FOREIGN KEY (phase_id) REFERENCES public.phases(id);


--
-- Name: eletric_photo_records fkj9n3ex57qvv5p0pn1xsrfwt3g; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.eletric_photo_records
    ADD CONSTRAINT fkj9n3ex57qvv5p0pn1xsrfwt3g FOREIGN KEY (eletric_id) REFERENCES public.eletric(id);


--
-- Name: photo_records fkjqwfuggn61067tax0mdiewrj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.photo_records
    ADD CONSTRAINT fkjqwfuggn61067tax0mdiewrj FOREIGN KEY (phase_id) REFERENCES public.phases(id);


--
-- Name: foundation_executed_services fkjworu9pysvwa4clnt32stuprs; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.foundation_executed_services
    ADD CONSTRAINT fkjworu9pysvwa4clnt32stuprs FOREIGN KEY (foundation_id) REFERENCES public.foundations(id);


--
-- Name: machinery fkk8bk2dhj23f34iogd26rrf8lr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.machinery
    ADD CONSTRAINT fkk8bk2dhj23f34iogd26rrf8lr FOREIGN KEY (phase_id) REFERENCES public.phases(id);


--
-- Name: hydraulic_machinery fkkk4nggk547tdlj3tic0q2qcb3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hydraulic_machinery
    ADD CONSTRAINT fkkk4nggk547tdlj3tic0q2qcb3 FOREIGN KEY (hydraulic_id) REFERENCES public.hydraulics(id);


--
-- Name: hydraulic_tools fklfmu4e8si9k3ribqvux7dnghw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hydraulic_tools
    ADD CONSTRAINT fklfmu4e8si9k3ribqvux7dnghw FOREIGN KEY (hydraulic_id) REFERENCES public.hydraulics(id);


--
-- Name: finishings fkm5wnb6w9eyrc3wp41a1j4tn5t; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.finishings
    ADD CONSTRAINT fkm5wnb6w9eyrc3wp41a1j4tn5t FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: foundation_machinery fkmkevqi0dnkvug0j86cp56y676; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.foundation_machinery
    ADD CONSTRAINT fkmkevqi0dnkvug0j86cp56y676 FOREIGN KEY (foundation_id) REFERENCES public.foundations(id);


--
-- Name: custom_phases fkmp2antnrw3iqhgxl9plqy32c7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.custom_phases
    ADD CONSTRAINT fkmp2antnrw3iqhgxl9plqy32c7 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: eletric_team_members fkna34b13dg7vrrgo8gjehhcjs9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.eletric_team_members
    ADD CONSTRAINT fkna34b13dg7vrrgo8gjehhcjs9 FOREIGN KEY (eletric_id) REFERENCES public.eletric(id);


--
-- Name: phases fkng07f4ts5bfr8vy9bpyv4x63s; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.phases
    ADD CONSTRAINT fkng07f4ts5bfr8vy9bpyv4x63s FOREIGN KEY (construction_id) REFERENCES public.constructions(id);


--
-- Name: construction_clients fko3m0s8sijqpyw58llov6gai5d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.construction_clients
    ADD CONSTRAINT fko3m0s8sijqpyw58llov6gai5d FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: finishing_machineries fkoa8p5hy25ujtyl9mek3pvmgp4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.finishing_machineries
    ADD CONSTRAINT fkoa8p5hy25ujtyl9mek3pvmgp4 FOREIGN KEY (finishing_id) REFERENCES public.finishings(id);


--
-- Name: tools fkolkkltipfv9dp0grrjabosutd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tools
    ADD CONSTRAINT fkolkkltipfv9dp0grrjabosutd FOREIGN KEY (phase_id) REFERENCES public.phases(id);


--
-- Name: foundation_materials fkpmvcner98xmlihffyoxbn9y0f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.foundation_materials
    ADD CONSTRAINT fkpmvcner98xmlihffyoxbn9y0f FOREIGN KEY (foundation_id) REFERENCES public.foundations(id);


--
-- Name: construction_clients fkq46i5as2fkf9jwot1m6ij8vvt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.construction_clients
    ADD CONSTRAINT fkq46i5as2fkf9jwot1m6ij8vvt FOREIGN KEY (construction_id) REFERENCES public.constructions(id);


--
-- Name: hydraulic_photo_records fkqn4rhilye01tx89xbkpmwt6va; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hydraulic_photo_records
    ADD CONSTRAINT fkqn4rhilye01tx89xbkpmwt6va FOREIGN KEY (hydraulic_id) REFERENCES public.hydraulics(id);


--
-- Name: eletric_tools fkr1lgaql26dylj73942cvl8rf0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.eletric_tools
    ADD CONSTRAINT fkr1lgaql26dylj73942cvl8rf0 FOREIGN KEY (eletric_id) REFERENCES public.eletric(id);


--
-- Name: foundation_tools fkru6wxh9qa8k75eojeuma43lrv; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.foundation_tools
    ADD CONSTRAINT fkru6wxh9qa8k75eojeuma43lrv FOREIGN KEY (foundation_id) REFERENCES public.foundations(id);


--
-- Name: foundations fkwy3oablo7goqksw6p0msa2rn; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.foundations
    ADD CONSTRAINT fkwy3oablo7goqksw6p0msa2rn FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: foundation_photo_records fkx78ur449dg8bcl981gs1jbon; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.foundation_photo_records
    ADD CONSTRAINT fkx78ur449dg8bcl981gs1jbon FOREIGN KEY (foundation_id) REFERENCES public.foundations(id);


--
-- PostgreSQL database dump complete
--

\unrestrict hBhXbfTsezu7qN1DGhtsXLbH3L8bOCtW2MfCWaS0jPXz5iXKAzuekVavKM8WX9s

