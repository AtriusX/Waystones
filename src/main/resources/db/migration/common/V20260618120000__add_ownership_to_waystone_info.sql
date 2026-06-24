-- Add ownership columns to waystone_info
ALTER TABLE waystone_info ADD COLUMN waystone_uuid CHAR(36);
ALTER TABLE waystone_info ADD COLUMN primary_owner_uuid CHAR(36);
ALTER TABLE waystone_info ADD COLUMN is_locked BOOLEAN NOT NULL DEFAULT FALSE;

-- Create unique index for waystone_uuid
CREATE UNIQUE INDEX idx_waystone_info_waystone_uuid ON waystone_info(waystone_uuid);

-- Create the authorized users table
CREATE TABLE IF NOT EXISTS waystone_authorized_users (
    waystone_uuid CHAR(36) NOT NULL,
    owner_uuid CHAR(36) NOT NULL,
    PRIMARY KEY (waystone_uuid, owner_uuid),
    FOREIGN KEY (waystone_uuid) REFERENCES waystone_info(waystone_uuid) ON DELETE CASCADE
);