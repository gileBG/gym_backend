SET @fk_name = (
    SELECT kcu.CONSTRAINT_NAME
    FROM information_schema.KEY_COLUMN_USAGE kcu
    WHERE kcu.TABLE_SCHEMA = DATABASE()
      AND kcu.TABLE_NAME = 'clanarine'
      AND kcu.COLUMN_NAME = 'program_id'
      AND kcu.REFERENCED_TABLE_NAME IS NOT NULL
    LIMIT 1
);

SET @drop_fk_sql = IF(
    @fk_name IS NOT NULL,
    CONCAT('ALTER TABLE clanarine DROP FOREIGN KEY ', @fk_name),
    'SELECT 1'
);
PREPARE stmt_fk FROM @drop_fk_sql;
EXECUTE stmt_fk;
DEALLOCATE PREPARE stmt_fk;

SET @column_exists = (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS c
    WHERE c.TABLE_SCHEMA = DATABASE()
      AND c.TABLE_NAME = 'clanarine'
      AND c.COLUMN_NAME = 'program_id'
);

SET @drop_column_sql = IF(
    @column_exists > 0,
    'ALTER TABLE clanarine DROP COLUMN program_id',
    'SELECT 1'
);
PREPARE stmt_col FROM @drop_column_sql;
EXECUTE stmt_col;
DEALLOCATE PREPARE stmt_col;
