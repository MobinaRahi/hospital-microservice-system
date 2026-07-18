-- ============================================================
-- Oracle init script — creates application schemas
--
-- NOTE: gvenzl/oracle-xe runs init scripts against XEPDB1 (PDB)
-- automatically. Do NOT use ALTER SESSION SET CONTAINER here.
-- ============================================================

-- Create auth user
CREATE USER auth IDENTIFIED BY auth123;
GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE, CREATE VIEW,
    CREATE PROCEDURE, CREATE TRIGGER TO auth;
ALTER USER auth QUOTA UNLIMITED ON USERS;

-- Create core user
CREATE USER core IDENTIFIED BY core123;
GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE, CREATE VIEW,
    CREATE PROCEDURE, CREATE TRIGGER TO core;
ALTER USER core QUOTA UNLIMITED ON USERS;

-- Create clinical user
CREATE USER clinical IDENTIFIED BY clinical123;
GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE, CREATE VIEW,
    CREATE PROCEDURE, CREATE TRIGGER TO clinical;
ALTER USER clinical QUOTA UNLIMITED ON USERS;