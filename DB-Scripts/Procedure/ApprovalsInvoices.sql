CREATE DEFINER=`root`@`localhost` PROCEDURE `ApprovalsInvoices`()
BEGIN


Declare taskCount int;

Create temporary table flowable.AgingReport 

select fr.*,convert(fr.resource_bytes_ using latin1) as json_Value
from flowable.act_fo_form_resource fr where ID_ in
(select Form_values_ID_ from flowable.act_fo_form_instance where Submitted_By_='Indexer' and Proc_Inst_Id_ in
		(select Proc_Inst_ID_ from flowable.act_hi_taskinst where Name_='Payment')
);

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
            '$.vendorname')),char) AS 'VendorName',
    convert(trim(BOTH '"' from JSON_EXTRACT(JSON_EXTRACT(fr.json_Value, '$.values'),
            '$.invoiceamount')),char) AS 'InvoiceAmount',
    convert(trim(BOTH '"' from JSON_EXTRACT(JSON_EXTRACT(fr.json_Value, '$.values'),
            '$.invoicedate')),date) AS 'Invoice_Date',
    
    convert(trim(BOTH '"' from JSON_EXTRACT(JSON_EXTRACT(fr.json_Value, '$.values'),
            '$.taskdate')),date) AS 'Task_Date'
 from flowable.AgingReport fr
 where json_extract(json_extract(fr.json_Value,'$.values'),'$.invoicenumber') not like '%null%';
End If;

Create temporary table flowable.ApprovalsInv 
select InvoiceNo,PONo,VendorName,InvoiceAmount,Invoice_Date,Task_Date from flowable.PendingMetrics 
group by InvoiceNo having (count(InvoiceNo)>1 or count(InvoiceNo)=1);

Create temporary table flowable.ApprovalCase
SELECT 
    CASE 
		WHEN InvoiceAmount< 100 Then "AutoApproval"
		WHEN InvoiceAmount>=100 and InvoiceAmount<=500 Then "Single_Approval"
        WHEN InvoiceAmount>500 Then "Multiple_Approval"			
	END AS 'Approvals',

    InvoiceNo
FROM
    flowable.ApprovalsInv;


select Approvals,count(InvoiceNo) 'Total_Invoices' from flowable.ApprovalCase group by Approvals;

drop table flowable.AgingReport;
drop table flowable.PendingMetrics;
drop table flowable.ApprovalsInv;
drop table flowable.ApprovalCase;


End