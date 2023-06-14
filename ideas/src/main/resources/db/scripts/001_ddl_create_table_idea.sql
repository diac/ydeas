CREATE TABLE idea (
    id SERIAL PRIMARY KEY,
    title VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    author_user_id INTEGER NOT NULL,
    created_at TIMESTAMP
);

COMMENT ON TABLE idea IS 'Идея';
COMMENT ON COLUMN idea.id IS 'Идентификатор идеи';
COMMENT ON COLUMN idea.title IS 'Заголовок идеи';
COMMENT ON COLUMN idea.description IS 'Описание идеи';
COMMENT ON COLUMN idea.author_user_id IS 'Идентификатор автора идеи';
COMMENT ON COLUMN idea.created_at IS 'Дата и время создания идеи';