CREATE TABLE media_object (
    id SERIAL PRIMARY KEY,
    url VARCHAR UNIQUE NOT NULL,
    file_name VARCHAR NOT NULL,
    original_file_name VARCHAR NOT NULL,
    media_type VARCHAR NOT NULL,
    content_length BIGINT NOT NULL,
    title VARCHAR NOT NULL,
    description VARCHAR NOT NULL
);

COMMENT ON TABLE media_object IS 'Медиа-объект';
COMMENT ON COLUMN media_object.id IS 'Идентификатор медиа-объекта';
COMMENT ON COLUMN media_object.url IS 'URL медиа-объекта';
COMMENT ON COLUMN media_object.file_name IS 'Имя файла медиа-объекта';
COMMENT ON COLUMN media_object.original_file_name IS 'Оригинальное имя файла медиа-объекта';
COMMENT ON COLUMN media_object.media_type IS 'Тип медиа медиа-объекта';
COMMENT ON COLUMN media_object.content_length IS 'Длина контента медиа-объекта';
COMMENT ON COLUMN media_object.title IS 'Заголовок медиа-объекта';
COMMENT ON COLUMN media_object.description IS 'Описание медиа-объекта';