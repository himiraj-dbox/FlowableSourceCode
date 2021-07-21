CREATE DEFINER=`root`@`localhost` PROCEDURE `PaymentComplete`()
BEGIN
Create temporary table flowable.PaymentReport 
select 
fr.*

,convert(fr.resource_bytes_ using latin1) as invoice_Value
,convert(A.resource_bytes_ using latin1) as payment_Value
,fi.PROC_INST_ID_
from 

(select fri.Resource_Bytes_,fii.proc_inst_ID_
from flowable.act_fo_form_resource fri
	 join
	 flowable.act_fo_form_instance fii
     on fri.ID_=fii.form_values_id_
     join flowable.act_fo_form_definition fdi
     on  fii.form_definition_id_=fdi.id_
     
     join flowable.act_hi_taskinst hpti
     on hpti.proc_inst_ID_=fii.proc_inst_id_
     and hpti.id_=fii.task_id_ and hpti.Name_ in ('Payment')
         
where fii.proc_def_id_ like '%AP%'
and fdi.NAME_ in ('frmAP','frmPayment')
/*and fii.Proc_INST_ID_=fi.PROC_INST_ID_*/
and hpti.Proc_inst_Id_ not in 
(select Proc_Inst_Id_ from flowable.act_ru_task where name_ like '%Payment')
) A

join
flowable.act_fo_form_instance fi
     on A.proc_inst_id_=fi.proc_inst_id_ 
join
flowable.act_fo_form_resource fr
	 on fr.ID_=fi.form_values_id_
     
	join flowable.act_fo_form_definition fd 
     on  fi.form_definition_id_=fd.id_
         join flowable.act_hi_taskinst hpt
     on hpt.proc_inst_ID_=fi.proc_inst_id_
     and hpt.id_=fi.task_id_ and hpt.Name_ in ('Indexer')
         
where fi.proc_def_id_ like '%AP%'
and fd.NAME_ in ('frmAP','frmPayment')

and hpt.Proc_inst_Id_ not in 
(select Proc_Inst_Id_ from flowable.act_ru_task where name_ like '%Payment');

 
CREATE temporary table flowable.finalPayReport
  
SELECT 
    convert(trim(BOTH '"' from JSON_EXTRACT(JSON_EXTRACT(invoice_Value, '$.values'),
            '$.invoicenumber')),char) AS 'InvoiceNo',
    
	convert(trim(BOTH '"' from JSON_EXTRACT(JSON_EXTRACT(invoice_Value, '$.values'),
            '$.ponumber')),char) AS 'PONo',
    
	convert(trim(BOTH '"' from JSON_EXTRACT(JSON_EXTRACT(invoice_Value, '$.values'),
            '$.vendorname')),char) AS 'Vendor Name',
    convert(trim(BOTH '"' from JSON_EXTRACT(JSON_EXTRACT(invoice_Value, '$.values'),
            '$.invoiceamount')),char) AS 'Invoice Amount',
    convert(trim(BOTH '"' from JSON_EXTRACT(JSON_EXTRACT(invoice_Value, '$.values'),
            '$.invoicedate')),date) AS 'Invoice_Date',
    
    convert(trim(BOTH '"' from JSON_EXTRACT(JSON_EXTRACT(invoice_Value, '$.values'),
            '$.taskdate')),date) AS 'Task_Date',

    convert(trim(BOTH '"' from JSON_EXTRACT(JSON_EXTRACT(payment_Value, '$.values'),
            '$.approvalamount')),unsigned integer) AS 'Approval Amount',
    
	convert(trim(BOTH '"' from JSON_EXTRACT(JSON_EXTRACT(payment_Value, '$.values'),
            '$.paymentreferencenumber')),char) AS 'Payment Reference',

	convert(trim(BOTH '"' from JSON_EXTRACT(JSON_EXTRACT(payment_Value, '$.values'),
            '$.paymentcomplete')),char) AS 'Payment Complete'
FROM
    flowable.PaymentReport;

select * from flowable.finalPayReport;

drop table flowable.finalPayReport;
drop table flowable.PaymentReport;

END