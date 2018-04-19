package com.gzedu.xlims.pojo.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class GjtGradeDto {

	private static final long serialVersionUID = 1L;

	private String GRADE_ID;

	private String XXMC;

	private String CREATED_BY;

	private Timestamp CREATED_DT;

	private Date ENTER_DT;

	private String GRADE_CODE;

	private String GRADE_NAME;

	private String IS_DELETED;

	private String IS_ENABLED;

	private BigDecimal SPACE_MONTH;

	private String UPDATED_BY;

	private Timestamp UPDATED_DT;

	private BigDecimal VERSION;// 版本好

	private BigDecimal NUM;

	private String XX_ID;

	private BigDecimal ROWNUM_;

	private BigDecimal SPECNUM;

	/**
	 * @return the sPECNUM
	 */
	public BigDecimal getSPECNUM() {
		return SPECNUM;
	}

	/**
	 * @param sPECNUM
	 *            the sPECNUM to set
	 */
	public void setSPECNUM(BigDecimal sPECNUM) {
		SPECNUM = sPECNUM;
	}

	/**
	 * @return the rOWNUM_
	 */

	public BigDecimal getROWNUM_() {
		return ROWNUM_;
	}

	/**
	 * @return the xXMC
	 */
	public String getXXMC() {
		return XXMC;
	}

	/**
	 * @param xXMC
	 *            the xXMC to set
	 */
	public void setXXMC(String xXMC) {
		XXMC = xXMC;
	}

	/**
	 * @param rOWNUM_
	 *            the rOWNUM_ to set
	 */
	public void setROWNUM_(BigDecimal rOWNUM_) {
		ROWNUM_ = rOWNUM_;
	}

	/**
	 * @return the gRADE_ID
	 */
	public String getGRADE_ID() {
		return GRADE_ID;
	}

	/**
	 * @param gRADE_ID
	 *            the gRADE_ID to set
	 */
	public void setGRADE_ID(String gRADE_ID) {
		GRADE_ID = gRADE_ID;
	}

	/**
	 * @return the cREATED_BY
	 */
	public String getCREATED_BY() {
		return CREATED_BY;
	}

	/**
	 * @param cREATED_BY
	 *            the cREATED_BY to set
	 */
	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}

	/**
	 * @return the cREATED_DT
	 */
	public Timestamp getCREATED_DT() {
		return CREATED_DT;
	}

	/**
	 * @param cREATED_DT
	 *            the cREATED_DT to set
	 */
	public void setCREATED_DT(Timestamp cREATED_DT) {
		CREATED_DT = cREATED_DT;
	}

	/**
	 * @return the eNTER_DT
	 */
	public Date getENTER_DT() {
		return ENTER_DT;
	}

	/**
	 * @param eNTER_DT
	 *            the eNTER_DT to set
	 */
	public void setENTER_DT(Date eNTER_DT) {
		ENTER_DT = eNTER_DT;
	}

	/**
	 * @return the gRADE_CODE
	 */
	public String getGRADE_CODE() {
		return GRADE_CODE;
	}

	/**
	 * @param gRADE_CODE
	 *            the gRADE_CODE to set
	 */
	public void setGRADE_CODE(String gRADE_CODE) {
		GRADE_CODE = gRADE_CODE;
	}

	/**
	 * @return the gRADE_NAME
	 */
	public String getGRADE_NAME() {
		return GRADE_NAME;
	}

	/**
	 * @param gRADE_NAME
	 *            the gRADE_NAME to set
	 */
	public void setGRADE_NAME(String gRADE_NAME) {
		GRADE_NAME = gRADE_NAME;
	}

	/**
	 * @return the iS_DELETED
	 */
	public String getIS_DELETED() {
		return IS_DELETED;
	}

	/**
	 * @param iS_DELETED
	 *            the iS_DELETED to set
	 */
	public void setIS_DELETED(String iS_DELETED) {
		IS_DELETED = iS_DELETED;
	}

	/**
	 * @return the iS_ENABLED
	 */
	public String getIS_ENABLED() {
		return IS_ENABLED;
	}

	/**
	 * @param iS_ENABLED
	 *            the iS_ENABLED to set
	 */
	public void setIS_ENABLED(String iS_ENABLED) {
		IS_ENABLED = iS_ENABLED;
	}

	/**
	 * @return the uPDATED_BY
	 */
	public String getUPDATED_BY() {
		return UPDATED_BY;
	}

	/**
	 * @param uPDATED_BY
	 *            the uPDATED_BY to set
	 */
	public void setUPDATED_BY(String uPDATED_BY) {
		UPDATED_BY = uPDATED_BY;
	}

	/**
	 * @return the uPDATED_DT
	 */
	public Timestamp getUPDATED_DT() {
		return UPDATED_DT;
	}

	/**
	 * @param uPDATED_DT
	 *            the uPDATED_DT to set
	 */
	public void setUPDATED_DT(Timestamp uPDATED_DT) {
		UPDATED_DT = uPDATED_DT;
	}

	/**
	 * @return the sPACE_MONTH
	 */
	public BigDecimal getSPACE_MONTH() {
		return SPACE_MONTH;
	}

	/**
	 * @param sPACE_MONTH
	 *            the sPACE_MONTH to set
	 */
	public void setSPACE_MONTH(BigDecimal sPACE_MONTH) {
		SPACE_MONTH = sPACE_MONTH;
	}

	/**
	 * @return the vERSION
	 */
	public BigDecimal getVERSION() {
		return VERSION;
	}

	/**
	 * @param vERSION
	 *            the vERSION to set
	 */
	public void setVERSION(BigDecimal vERSION) {
		VERSION = vERSION;
	}

	/**
	 * @return the xX_ID
	 */
	public String getXX_ID() {
		return XX_ID;
	}

	/**
	 * @param xX_ID
	 *            the xX_ID to set
	 */
	public void setXX_ID(String xX_ID) {
		XX_ID = xX_ID;
	}

	/**
	 * @return the nUM
	 */
	public BigDecimal getNUM() {
		return NUM;
	}

	/**
	 * @param nUM
	 *            the nUM to set
	 */
	public void setNUM(BigDecimal nUM) {
		NUM = nUM;
	}

}