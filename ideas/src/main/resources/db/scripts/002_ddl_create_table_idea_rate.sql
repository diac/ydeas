CREATE TABLE idea_rate (
    idea_id INTEGER NOT NULL REFERENCES idea(id),
    user_id INTEGER NOT NULL,
    rate VARCHAR NOT NULL,
    PRIMARY KEY(idea_id, user_id)
);

COMMENT ON TABLE idea_rate IS 'Оценка идеи';
COMMENT ON COLUMN idea_rate.idea_id IS 'Идентификатор идеи';
COMMENT ON COLUMN idea_rate.user_id IS 'Идентификатор пользователя, поставившего оценку';
COMMENT ON COLUMN idea_rate.rate IS 'Оценка';