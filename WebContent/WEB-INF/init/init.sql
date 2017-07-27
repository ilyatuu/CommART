DROP VIEW IF EXISTS view_table2;
create view view_table2 as
SELECT
   `baseline_v2_core`.`_URI` AS `_URI`,
   `baseline_v2_core`.`_CREATION_DATE` AS `_CREATION_DATE`,
   `baseline_v2_core`.`_SUBMISSION_DATE` AS `_SUBMISSION_DATE`,'BASELINE_V2_CORE' AS `TABLE_NAME`,
   `baseline_v2_core`.`REGION` AS `REGION`,
   `baseline_v2_core`.`DISTRICT` AS `DISTRICT`,
   `baseline_v2_core`.`DEVICEID` AS `DEVICE_ID`,
   `baseline_v2_core`.`Q003_HF` AS `FACILITY`,
   `baseline_v2_core`.`Q005` AS `PARTICIPANT_ID`,
   `baseline_v2_core`.`VIRAL_ID` AS `VIRAL_ID`,'<a href=\'#\' data-toggle=\'modal\' data-target=\'#frmViroLoad\'>Edit</a>' AS `EDIT`,
   `baseline_v2_core`.`VIRAL_RESULTS` AS `VIRAL_RESULTS`,
   `baseline_v2_core`.`VIRAL_COMMENTS` AS `VIRAL_COMMENTS`,
   `baseline_v2_core`.`VIRAL_TYPE` AS `VIRAL_TYPE`,
   `baseline_v2_core`.`VIRAL_QUALITY` AS `VIRAL_QUALITY`,
   `baseline_v2_core`.`CTC_ID` AS `CTC_NO`,
   `baseline_v2_core`.`DECEASED` AS `DECEASED`
FROM `baseline_v2_core`;

DROP VIEW IF EXISTS view_summary2;
create view view_summary2 as
SELECT
   count(0) AS `COUNT_REC`,
   sum((`baseline_v2_core`.`VIRAL_RESULTS` is not null)) AS `COUNT_VIRAL`,
   sum((`baseline_v2_core`.`CTC_ID` is not null)) AS `COUNT_CTC`
FROM `baseline_v2_core`;

