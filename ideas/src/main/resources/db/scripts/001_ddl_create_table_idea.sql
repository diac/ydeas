CREATE TABLE idea (
    id SERIAL PRIMARY KEY,
    title VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    author_uuid UUID NOT NULL,
    created_at TIMESTAMP NOT NULL
);

COMMENT ON TABLE idea IS 'Идея';
COMMENT ON COLUMN idea.id IS 'Идентификатор идеи';
COMMENT ON COLUMN idea.title IS 'Заголовок идеи';
COMMENT ON COLUMN idea.description IS 'Описание идеи';
COMMENT ON COLUMN idea.author_uuid IS 'Идентификатор автора идеи';
COMMENT ON COLUMN idea.created_at IS 'Дата и время создания идеи';