CREATE TABLE idea_attachment (
    idea_id INTEGER NOT NULL REFERENCES idea(id),
    media_object_id INTEGER NOT NULL REFERENCES media_object(id),
    PRIMARY KEY(idea_id, media_object_id)
);

COMMENT ON TABLE idea_attachment IS 'Связь между идеями и прикрепленными медиа-объектами';
COMMENT ON COLUMN idea_attachment.idea_id IS 'Идентификатор идеи';
COMMENT ON COLUMN idea_attachment.media_object_id IS 'Идентификатор медиа объекта';