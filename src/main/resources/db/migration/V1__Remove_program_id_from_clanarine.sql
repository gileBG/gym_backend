-- Remove program_id foreign key constraint and column from clanarine table
ALTER TABLE clanarine DROP FOREIGN KEY clanarine_ibfk_1;
ALTER TABLE clanarine DROP COLUMN program_id;
