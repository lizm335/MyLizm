package com.gzedu.xlims.pojo;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the GJT_ACTIVITY_JOIN database table.
 * 
 */
@Embeddable
public class GjtActivityJoinPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ACTIVITY_ID")
	private String activityId;

	@Column(name="STUDENT_ID")
	private String studentId;

	public GjtActivityJoinPK() {
	}
	public String getActivityId() {
		return this.activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getStudentId() {
		return this.studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof GjtActivityJoinPK)) {
			return false;
		}
		GjtActivityJoinPK castOther = (GjtActivityJoinPK)other;
		return 
			this.activityId.equals(castOther.activityId)
			&& this.studentId.equals(castOther.studentId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.activityId.hashCode();
		hash = hash * prime + this.studentId.hashCode();
		
		return hash;
	}
}