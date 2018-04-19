/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.pojo.dto;

import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;

/**
 * 
 * 功能说明：班级活动参与人员信息
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年2月28日
 * @version 2.5
 *
 */
public class GjtActivityDto {
	private String ID;

	private String ACTIVITY_ADDRESS;

	private String ACTIVITY_INTRODUCE;

	private String ACTIVITY_PICTURE;

	private String ACTIVITY_TITLE;

	private BigDecimal AUDIT_STATUS;

	private Date BEGIN_TIME;

	private BigDecimal CEILING_NUM;

	private BigDecimal CHARGE_MONEY;

	private BigDecimal COMMENT_NUM;

	private String CREATED_BY;

	private Date CREATED_DT;

	private Date END_TIME;

	private BigDecimal IS_FREE;

	private BigDecimal JOIN_NUM;

	private String PUBLICITY_PICTURE;

	private BigDecimal ROWNUM_;

	public BigDecimal getROWNUM_() {
		return ROWNUM_;
	}

	public void setROWNUM_(BigDecimal rOWNUM_) {
		ROWNUM_ = rOWNUM_;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getACTIVITY_ADDRESS() {
		return ACTIVITY_ADDRESS;
	}

	public void setACTIVITY_ADDRESS(String aCTIVITY_ADDRESS) {
		ACTIVITY_ADDRESS = aCTIVITY_ADDRESS;
	}

	public String getACTIVITY_INTRODUCE() {
		return ACTIVITY_INTRODUCE;
	}

	public void setACTIVITY_INTRODUCE(Clob aCTIVITY_INTRODUCE) {
		try {
			ACTIVITY_INTRODUCE = aCTIVITY_INTRODUCE != null
					? aCTIVITY_INTRODUCE.getSubString(1, (int) aCTIVITY_INTRODUCE.length()) : null;
		} catch (SQLException e) {

		}
	}

	public String getACTIVITY_PICTURE() {
		return ACTIVITY_PICTURE;
	}

	public void setACTIVITY_PICTURE(Clob aCTIVITY_PICTURE) {
		try {
			ACTIVITY_PICTURE = aCTIVITY_PICTURE != null
					? aCTIVITY_PICTURE.getSubString(1, (int) aCTIVITY_PICTURE.length()) : null;
		} catch (SQLException e) {

		}
	}

	public String getACTIVITY_TITLE() {
		return ACTIVITY_TITLE;
	}

	public void setACTIVITY_TITLE(String aCTIVITY_TITLE) {
		ACTIVITY_TITLE = aCTIVITY_TITLE;
	}

	public BigDecimal getAUDIT_STATUS() {
		return AUDIT_STATUS;
	}

	public void setAUDIT_STATUS(BigDecimal aUDIT_STATUS) {
		AUDIT_STATUS = aUDIT_STATUS;
	}

	public Date getBEGIN_TIME() {
		return BEGIN_TIME;
	}

	public void setBEGIN_TIME(Date bEGIN_TIME) {
		BEGIN_TIME = bEGIN_TIME;
	}

	public BigDecimal getCEILING_NUM() {
		return CEILING_NUM;
	}

	public void setCEILING_NUM(BigDecimal cEILING_NUM) {
		CEILING_NUM = cEILING_NUM;
	}

	public BigDecimal getCHARGE_MONEY() {
		return CHARGE_MONEY;
	}

	public void setCHARGE_MONEY(BigDecimal cHARGE_MONEY) {
		CHARGE_MONEY = cHARGE_MONEY;
	}

	public BigDecimal getCOMMENT_NUM() {
		return COMMENT_NUM;
	}

	public void setCOMMENT_NUM(BigDecimal cOMMENT_NUM) {
		COMMENT_NUM = cOMMENT_NUM;
	}

	public String getCREATED_BY() {
		return CREATED_BY;
	}

	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}

	public Date getCREATED_DT() {
		return CREATED_DT;
	}

	public void setCREATED_DT(Date cREATED_DT) {
		CREATED_DT = cREATED_DT;
	}

	public Date getEND_TIME() {
		return END_TIME;
	}

	public void setEND_TIME(Date eND_TIME) {
		END_TIME = eND_TIME;
	}

	public BigDecimal getIS_FREE() {
		return IS_FREE;
	}

	public void setIS_FREE(BigDecimal iS_FREE) {
		IS_FREE = iS_FREE;
	}

	public BigDecimal getJOIN_NUM() {
		return JOIN_NUM;
	}

	public void setJOIN_NUM(BigDecimal jOIN_NUM) {
		JOIN_NUM = jOIN_NUM;
	}

	public String getPUBLICITY_PICTURE() {
		return PUBLICITY_PICTURE;
	}

	public void setPUBLICITY_PICTURE(String pUBLICITY_PICTURE) {
		PUBLICITY_PICTURE = pUBLICITY_PICTURE;
	}

}
