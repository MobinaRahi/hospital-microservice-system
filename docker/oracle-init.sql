-- ============================================================
-- Oracle init script — creates the two application schemas
-- Run automatically on first container start by the Oracle image.
-- ============================================================

-- ---- AuthService schema ----
CREATE USER auth IDENTIFIED BY auth123;
GRANT CONNECT, RESOURCE, DBA TO auth;
ALTER USER auth QUOTA UNLIMITED ON USERS;

-- ---- CoreService schema ----
CREATE USER core IDENTIFIED BY core123;
GRANT CONNECT, RESOURCE, DBA TO core;
ALTER USER core QUOTA UNLIMITED ON USERS;
