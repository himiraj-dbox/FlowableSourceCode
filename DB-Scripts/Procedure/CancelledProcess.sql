CREATE DEFINER=`root`@`localhost` PROCEDURE `CancelledProcess`()
BEGIN
select pd.Name_ 'Process'
	, hit.ASSIGNEE_ 'Cancelled By'
    , hit.START_TIME_ 'Process Start Time'
    , hit.END_TIME_ 'Process End Time'
    , hit.Delete_reason_ 'Cancellation Reason'
 from flowable.act_hi_taskinst hit
	join flowable.act_re_procdef pd
    on hit.proc_def_id_=pd.id_
    where hit.Delete_Reason_ is not null;
END