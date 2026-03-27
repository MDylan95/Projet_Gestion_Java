SET PAGESIZE 100
SET LINESIZE 200
COLUMN column_name FORMAT A30
COLUMN data_type FORMAT A20

SELECT column_name, data_type 
FROM user_tab_columns 
WHERE table_name = 'CLIENT'
ORDER BY column_id;

SELECT column_name, data_type 
FROM user_tab_columns 
WHERE table_name = 'ARTICLE'
ORDER BY column_id;

EXIT;
