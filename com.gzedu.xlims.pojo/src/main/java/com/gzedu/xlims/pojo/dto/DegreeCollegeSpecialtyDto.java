/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.dto;

/**
 * 学位专业信息DTO<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年11月22日
 * @version 2.5
 * @since JDK 1.7
 */
public class DegreeCollegeSpecialtyDto {

    /**
     * 构造函数
     * @param collegeId
     * @param specialtyId
     * @param collegeName
     * @param specialtyName
     * @param degreeReq
     */
    public DegreeCollegeSpecialtyDto(String collegeSpecialtyId, String collegeId, String specialtyId, String collegeName, String specialtyName, boolean degreeReq) {
        this.collegeSpecialtyId = collegeSpecialtyId;
        this.collegeId = collegeId;
        this.specialtyId = specialtyId;
        this.collegeName = collegeName;
        this.specialtyName = specialtyName;
        this.degreeReq = degreeReq;
    }

    /**
     * ID
     */
    private String collegeSpecialtyId;

    /**
	 * 院校ID
	 */
	private String collegeId;

	/**
	 * 专业ID
	 */
	private String specialtyId;

	/**
	 * 院校名称
	 */
	private String collegeName;

	/**
	 * 专业名称
	 */
	private String specialtyName;

	/**
	 * 是否配置学位条件
	 */
	private boolean degreeReq;

    public String getCollegeSpecialtyId() {
        return collegeSpecialtyId;
    }

    public void setCollegeSpecialtyId(String collegeSpecialtyId) {
        this.collegeSpecialtyId = collegeSpecialtyId;
    }

    public String getCollegeId() {
		return collegeId;
	}

	public void setCollegeId(String collegeId) {
		this.collegeId = collegeId;
	}

	public String getSpecialtyId() {
		return specialtyId;
	}

	public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
	}

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public String getSpecialtyName() {
		return specialtyName;
	}

	public void setSpecialtyName(String specialtyName) {
		this.specialtyName = specialtyName;
	}

	public boolean isDegreeReq() {
		return degreeReq;
	}

	public void setDegreeReq(boolean degreeReq) {
		this.degreeReq = degreeReq;
	}

}
