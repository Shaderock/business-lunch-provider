ALTER TABLE organization
    ADD is_deleted BOOLEAN DEFAULT FALSE;

ALTER TABLE organization
    ALTER COLUMN is_deleted SET NOT NULL;

ALTER TABLE category
    ADD name VARCHAR(255);
