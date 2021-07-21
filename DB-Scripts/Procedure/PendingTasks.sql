CREATE DEFINER=`root`@`localhost` PROCEDURE `PendingTasks`()
Begin
select 
    var.Text_ 'Invoice Number'
    ,Task.Name_ 'AP Stage'
    ,Task.ASSIGNEE_ 'Pending With'
    , Task.CREATE_TIME_ 'Task Started at' 
    
	from flowable.act_ru_task Task 
    join flowable.act_ru_execution Execution
	on Task.Execution_ID_=Execution.ID_
    join flowable.act_ru_variable var
    on var.Proc_Inst_Id_=Execution.Proc_Inst_ID_ and var.Name_='invoicenumber'
    where Task.Proc_Def_ID_ like '%AP%';
End