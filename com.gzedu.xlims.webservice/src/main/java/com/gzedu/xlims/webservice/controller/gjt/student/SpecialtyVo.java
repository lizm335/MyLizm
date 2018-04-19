package com.gzedu.xlims.webservice.controller.gjt.student;

/**
 * 专业信息VO
 */
public class SpecialtyVo {
	private String specialtyId;// 专业ID
	private String zymc;// 专业名称

    public String getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(String specialtyId) {
        this.specialtyId = specialtyId;
    }

    public String getZymc() {
        return zymc;
    }

    public void setZymc(String zymc) {
        this.zymc = zymc;
    }
}