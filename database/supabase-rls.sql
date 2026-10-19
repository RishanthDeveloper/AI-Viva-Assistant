-- Run ONCE in Supabase: Dashboard -> SQL Editor, AFTER the backend has started
-- at least once (Hibernate creates the tables on first start).
--
-- Turns on Row Level Security for every table in "public".
-- The Spring Boot backend connects as the "postgres" role, which bypasses RLS,
-- so the app keeps working. Without this, anyone holding your project's public
-- anon key could read these tables (including password hashes) through
-- Supabase's auto-generated REST API.
DO $$
DECLARE t record;
BEGIN
  FOR t IN SELECT tablename FROM pg_tables WHERE schemaname = 'public' LOOP
    EXECUTE format('ALTER TABLE public.%I ENABLE ROW LEVEL SECURITY', t.tablename);
  END LOOP;
END $$;

-- Check: every row should show rowsecurity = true
SELECT tablename, rowsecurity FROM pg_tables WHERE schemaname = 'public' ORDER BY tablename;
