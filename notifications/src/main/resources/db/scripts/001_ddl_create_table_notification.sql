CREATE TABLE notification (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR NOT NULL,
    content VARCHAR NOT NULL,
    recipient_uuid UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    status VARCHAR NOT NULL
);

COMMENT ON TABLE notification IS 'Уведомление';
COMMENT ON COLUMN notification.id IS 'Идентификатор уведомления';
COMMENT ON COLUMN notification.title IS 'Заголовок уведомления';
COMMENT ON COLUMN notification.content IS 'Содержание уведомления';
COMMENT ON COLUMN notification.recipient_uuid IS 'UUID получателя уведомления';
COMMENT ON COLUMN notification.created_at IS 'Дата и время создания уведомления';
COMMENT ON COLUMN notification.status IS 'Статус уведомления';