CREATE DEFINER=`root`@`localhost` PROCEDURE `AgingMetrics`()
BEGIN


Declare taskCount int;

Create temporary table flowable.AgingReport 
select fr.*,convert(fr.resource_bytes_ using latin1) as json_Value
from flowable.act_fo_form_resource fr
	 join
	 flowable.act_fo_form_instance fi
     on fr.ID_=fi.form_values_id_
     join flowable.act_fo_form_definition fd 
     on  fi.form_definition_id_=fd.id_
     join flowable.act_hi_taskinst hit
     on hit.id_=fi.task_id_ 
where fi.proc_def_id_ like '%AP%'
and fd.NAME_ like '%frmAP%'
and hit.Name_='Indexer' 
and hit.Proc_inst_Id_ in 
(select Proc_Inst_Id_ from flowable.act_ru_task where name_ like '%Payment');


SELECT 
    COUNT(*)
INTO taskCount FROM
    flowable.act_ru_task
WHERE
    name_ LIKE '%Payment';

if taskCount>0 Then

Create temporary table flowable.PendingMetrics 
select 
	convert(trim(BOTH '"' from JSON_EXTRACT(JSON_EXTRACT(fr.json_Value, '$.values'),
            '$.invoicenumber')),char) AS 'InvoiceNo',
    
	convert(trim(BOTH '"' from JSON_EXTRACT(JSON_EXTRACT(fr.json_Value, '$.values'),
            '$.ponumber')),char) AS 'PONo',
    
	convert(trim(BOTH '"' from JSON_EXTRACT(JSON_EXTRACT(fr.json_Value, '$.values'),
            '$.vendorname')),char) AS 'Vendor Name',
    convert(trim(BOTH '"' from JSON_EXTRACT(JSON_EXTRACT(fr.json_Value, '$.values'),
            '$.invoiceamount')),char) AS 'Invoice Amount',
    convert(trim(BOTH '"' from JSON_EXTRACT(JSON_EXTRACT(fr.json_Value, '$.values'),
            '$.invoicedate')),date) AS 'Invoice_Date',
    
    convert(trim(BOTH '"' from JSON_EXTRACT(JSON_EXTRACT(fr.json_Value, '$.values'),
            '$.taskdate')),date) AS 'Task_Date'
 from flowable.AgingReport fr
 where json_extract(json_extract(fr.json_Value,'$.values'),'$.invoicenumber') not like '%null%';
End If;

Create temporary table flowable.AgingDays 
SELECT 
    CASE 
		WHEN datediff(curdate(),Invoice_Date) > 25 Then ">25"
		WHEN datediff(curdate(),Invoice_Date) > 20 AND datediff(curdate(),Invoice_Date) <= 25 Then "20-25"
        WHEN datediff(curdate(),Invoice_Date) > 15 AND datediff(curdate(),Invoice_Date) <= 20 Then "15-20"
        WHEN datediff(curdate(),Invoice_Date) > 10 AND datediff(curdate(),Invoice_Date) <= 15 Then "10-15"
        WHEN datediff(curdate(),Invoice_Date) > 05 AND datediff(curdate(),Invoice_Date) <= 10 Then "05-10"
        WHEN datediff(curdate(),Invoice_Date) > 00 AND datediff(curdate(),Invoice_Date) <= 05 Then "0-05"			
		
    END AS 'Aging_Days',

    InvoiceNo
FROM
    flowable.PendingMetrics;


select Aging_Days,count(InvoiceNo) from flowable.AgingDays group by Aging_Days;

drop table flowable.AgingReport;
drop table flowable.PendingMetrics;
drop table flowable.AgingDays;


End