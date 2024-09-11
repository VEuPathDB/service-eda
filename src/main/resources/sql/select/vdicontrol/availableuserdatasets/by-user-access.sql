SELECT
	(case d.owner when ? then 1 else 0 end) is_owner,
	user_id,
	dataset_stable_id,
	name,
	description,
	study_stable_id,
	d.owner AS owner
FROM
	%s.availableuserdatasets a
JOIN
	%s.userstudydatasetid u
ON
	concat('EDAUD_', a.USER_DATASET_ID) = u.dataset_stable_id
JOIN
	%s.dataset d
ON
	a.user_dataset_id = d.dataset_id
WHERE
	a.user_id = ? or a.is_public = '1'
