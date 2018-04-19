package com.gzedu.xlims.pojo.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class AddressBookDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String studentId;
	private String name;
	private String majorName;
	private String headImg;
	private String eeno;
	private BigDecimal ROWNUM_;

	public AddressBookDto(String name, String majorName, String headImg, String eeno, String studentId) {
		super();
		this.name = name;
		this.majorName = majorName;
		this.headImg = headImg;
		this.eeno = eeno;
		this.studentId = studentId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMajorName() {
		return majorName;
	}

	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getEeno() {
		return eeno;
	}

	public void setEeno(String eeno) {
		this.eeno = eeno;
	}

	public BigDecimal getROWNUM_() {
		return ROWNUM_;
	}

	public void setROWNUM_(BigDecimal rOWNUM_) {
		ROWNUM_ = rOWNUM_;
	}

}
