<<<<<<< HEAD:src/main/resources/sql/select/studyaccess/end_users/by-dataset.postgres.sql
-- This is the POSTGRES version of this file.  See also the ORACLE version
=======
-- This is the ORACLE version of this file.  See also the POSTGRES version
>>>>>>> master:src/main/resources/sql/select/studyaccess/end_users/by-dataset_oracle.sql
SELECT
  v.*
, (
    SELECT
      value
    FROM
      useraccounts.account_properties
    WHERE
        user_id = v.user_id
    AND key = 'first_name'
  ) AS first_name
, (
    SELECT
      value
    FROM
      useraccounts.account_properties
    WHERE
        user_id = v.user_id
    AND key = 'last_name'
  ) AS last_name
, (
    SELECT
      value
    FROM
      useraccounts.account_properties
    WHERE
        user_id = v.user_id
    AND key = 'organization'
  ) AS organization
, a.email
FROM
  studyaccess.end_users v
  INNER JOIN useraccounts.accounts a
    ON v.user_id = a.user_id
WHERE
  v.dataset_presenter_id = ?
ORDER BY
  user_id
OFFSET ? LIMIT ?
