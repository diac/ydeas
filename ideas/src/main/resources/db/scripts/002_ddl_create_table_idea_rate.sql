CREATE TABLE idea_rate (
    idea_id INTEGER NOT NULL REFERENCES idea(id),
    user_uuid UUID NOT NULL,
    rate VARCHAR NOT NULL,
    PRIMARY KEY(idea_id, user_uuid)
);

COMMENT ON TABLE idea_rate IS 'Оценка идеи';
COMMENT ON COLUMN idea_rate.idea_id IS 'Идентификатор идеи';
COMMENT ON COLUMN idea_rate.user_uuid IS 'UUID пользователя, поставившего оценку';
COMMENT ON COLUMN idea_rate.rate IS 'Оценка';