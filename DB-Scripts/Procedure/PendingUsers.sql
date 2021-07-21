select Assignee_ 'User', count(Assignee_) 'Pending_Tasks' from flowable.act_ru_task 
	where Proc_Def_Id_ like 'final-AP%' and Assignee_ is not null group by Assignee_;