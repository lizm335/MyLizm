package com.ouchgzee.headTeacher.id;

import java.io.Serializable;

/**
 * GJT_STUDENT_EXAM_POINT表的复合主键<br>
 * Created by Administrator on 2016/6/16.
 */
@Deprecated public class BzrGjtStudentExamPointId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String studentId;

    private String termId;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BzrGjtStudentExamPointId other = (BzrGjtStudentExamPointId) obj;
        if ((this.studentId == null) ? (other.studentId != null) : !this.studentId.equals(other.studentId)) {
            return false;
        }
        if ((this.termId == null) ? (other.termId != null) : !this.termId.equals(other.termId)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (this.studentId != null ? this.studentId.hashCode() : 0);
        hash = 41 * hash + (this.termId != null ? this.termId.hashCode() : 0);
        return hash;
    }

}
