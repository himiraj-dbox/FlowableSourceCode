package hibernateSignUp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="hire_me_task_status")
public class TileUserTaskStatus {
private long taskId;
private String taskTrekking;
private Date updatedDate;

@Column(name="task_id")
public long getTaskId() {
	return taskId;
}
public void setTaskId(long taskId) {
	this.taskId = taskId;
}

@Column(name="task_trekking")
public String getTaskTrekking() {
	return taskTrekking;
}
public void setTaskTrekking(String taskTrekking) {
	this.taskTrekking = taskTrekking;
}

@Column(name="updated_date")
public Date getUpdatedDate() {
	return updatedDate;
}
public void setUpdatedDate(Date updatedDate) {
	this.updatedDate = updatedDate;
}



}
