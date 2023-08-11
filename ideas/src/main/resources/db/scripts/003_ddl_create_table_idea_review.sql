CREATE TABLE idea_review (
    idea_id INTEGER NOT NULL PRIMARY KEY REFERENCES idea(id),
    reviewer_user_uuid UUID NOT NULL,
    idea_status VARCHAR NOT NULL
);

COMMENT ON TABLE idea_review IS 'Рассмотрение идеи';
COMMENT ON COLUMN idea_review.idea_id IS 'Идентификатор идеи (PK + FK)';
COMMENT ON COLUMN idea_review.reviewer_user_uuid IS 'UUID пользователя-эксперта, рассмотревшего идею';
COMMENT ON COLUMN idea_review.idea_status IS 'Статус идеи после рассмотрения';