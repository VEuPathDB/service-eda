-- This is the POSTGRES version of this file.  See also the ORACLE version
WITH
  dataset(id)  AS (SELECT ?)
, status (val) AS (SELECT ?)
SELECT
  count(1)
FROM
  (
    SELECT
      row_number() OVER (ORDER BY user_id, dataset_presenter_id) AS rn
    FROM
      studyaccess.end_users u
      INNER JOIN dataset d
        ON d.id IS NULL
        OR d.id = u.dataset_presenter_id
      INNER JOIN status s
        ON s.val IS NULL
        OR s.val = u.approval_status_id
  ) t
