package com.gzedu.xlims.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 同步数据出错日志表<br/>
 *
 * @author huangyifei
 * @email huangyifei@eenet.com
 * @date 2017年08月22日
 */
@Entity
@Table(name = "GJT_SYNC_LOG")
@NamedQuery(name = "GjtSyncLog.findAll", query = "SELECT s FROM GjtSyncLog s")
public class GjtSyncLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private String id;
	// 姓名
	private String xm;
	// 学号
	private String xh;
	//业务代码
	@Column(name = "RSBIZ_CODE")
	private String rsbizCode;
	// 备注/参数
	private String memo;
	//业务明细/返回结果
	@Column(name = "RSBIZ_CODE_INFO")
	private String rsbizCodeInfo;
	//创建时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createDate;

	public GjtSyncLog() {
	}

	public GjtSyncLog(String xm, String xh) {
		this.xm = xm;
		this.xh = xh;
	}

	public GjtSyncLog(String xm, String xh, String rsbizCode, String memo, String rsbizCodeInfo) {
		this.xm = xm;
		this.xh = xh;
		this.rsbizCode = rsbizCode;
		this.memo = memo;
		this.rsbizCodeInfo = rsbizCodeInfo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getRsbizCode() {
		return rsbizCode;
	}

	public void setRsbizCode(String rsbizCode) {
		this.rsbizCode = rsbizCode;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getRsbizCodeInfo() {
		return rsbizCodeInfo;
	}

	public void setRsbizCodeInfo(String rsbizCodeInfo) {
		this.rsbizCodeInfo = rsbizCodeInfo;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
