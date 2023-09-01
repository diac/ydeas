CREATE VIEW
    idea_rating
AS
SELECT
    Idea.idea_id AS idea_id,
    Idea.idea_title AS idea_title,
    Idea.idea_author_uuid AS idea_author_uuid,
    Idea.idea_created_at AS idea_created_at,
    Idea.likes AS likes,
    Idea.dislikes AS dislikes,
    Idea.rating AS rating,
    IdeaReview.idea_status AS idea_status
FROM
    (
    SELECT
        Idea.id AS idea_id,
        Idea.title AS idea_title,
        Idea.author_uuid AS idea_author_uuid,
        Idea.created_at AS idea_created_at,
        SUM(
            CASE WHEN IdeaRate.rate = 'LIKE' THEN 1 ELSE 0 END
        ) AS likes,
        SUM(
            CASE WHEN IdeaRate.rate = 'DISLIKE' THEN 1 ELSE 0 END
        ) AS dislikes,
        SUM(
            CASE
            WHEN IdeaRate.rate = 'LIKE' THEN 1
            WHEN IdeaRate.rate = 'DISLIKE' THEN -1
            ELSE 0
            END
        ) AS rating
    FROM
        idea AS Idea
    LEFT JOIN
        idea_rate AS IdeaRate ON IdeaRate.idea_id = Idea.id
    GROUP BY
        Idea.id
    ) AS Idea
LEFT JOIN
    idea_review AS IdeaReview ON IdeaReview.idea_id = Idea.idea_id;

COMMENT ON VIEW idea_rating IS 'Рейтинг идей';