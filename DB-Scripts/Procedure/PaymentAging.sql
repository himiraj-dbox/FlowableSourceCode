CREATE DEFINER=`root`@`localhost` PROCEDURE `PaymentAging`()
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


select count(*) into taskCount from flowable.act_ru_task where name_ like '%Payment';

select * from flowable.AgingReport;

if taskCount>0 Then

select 
	json_extract(json_extract(fr.json_Value,'$.values'),'$.invoicenumber') as 'Invoice#'
	,json_extract(json_extract(fr.json_Value,'$.values'),'$.ponumber') as 'PO#'
	,json_extract(json_extract(fr.json_Value,'$.values'),'$.vendorname') as 'Vendor Name'
	,json_extract(json_extract(fr.json_Value,'$.values'),'$.invoiceamount') as 'Invoice Amount'
    ,json_extract(json_extract(fr.json_Value,'$.values'),'$.invoicedate') as 'Invoice_Date'
    ,json_extract(json_extract(fr.json_Value,'$.values'),'$.taskdate') as 'Task_Date'
 from flowable.AgingReport fr
 where json_extract(json_extract(fr.json_Value,'$.values'),'$.invoicenumber') not like '%null%';

End If;

drop table flowable.AgingReport;

End