package com.gzedu.xlims.pojo;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.math.BigDecimal;
import java.util.List;

/**
 * The persistent class for the GJT_MENU database table.
 * 
 */
@Entity
@Table(name="GJT_MENU")
@NamedQuery(name="GjtMenu.findAll", query="SELECT g FROM GjtMenu g")
public class GjtMenu implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;

	private String name;//菜单名称

	@Column(name="NAME_EN")
	private String nameEn;

	@Column(name="ORDER_NO")
	private BigDecimal orderNo;//排序

	@OneToMany(cascade = { CascadeType.PERSIST })
	@JoinColumn(name = "pid")
	@OrderBy("orderNo asc")
	@NotFound(action = NotFoundAction.IGNORE)
	List<GjtMenu> childMenueList;// 子菜单列表
	
	@ManyToOne
	@JoinColumn(name = "pid")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtMenu gjtMenu;//父菜单

	private String url;//菜单地址

	private String visible;

	@Column(name="XX_ID")
	private String xxid;//学院
	//菜单级数
	@Transient
	private BigDecimal menu_level;
	@Transient
	private String parentId;//父id

	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	/**
	 * @return the menu_level
	 */
	public BigDecimal getMenu_level() {
		return menu_level;
	}

	/**
	 * @param menu_level the menu_level to set
	 */
	public void setMenu_level(BigDecimal menu_level) {
		this.menu_level = menu_level;
	}

	/**
	 * @return the xxid
	 */
	public String getXxid() {
		return xxid;
	}

	/**
	 * @param xxid the xxid to set
	 */
	public void setXxid(String xxid) {
		this.xxid = xxid;
	}

	/**
	 * @return the childMenueList
	 */
	public List<GjtMenu> getChildMenueList() {
		return childMenueList;
	}

	/**
	 * @param childMenueList the childMenueList to set
	 */
	public void setChildMenueList(List<GjtMenu> childMenueList) {
		this.childMenueList = childMenueList;
	}

	/**
	 * @return the gjtMenu
	 */
	public GjtMenu getGjtMenu() {
		return gjtMenu;
	}

	/**
	 * @param gjtMenu the gjtMenu to set
	 */
	public void setGjtMenu(GjtMenu gjtMenu) {
		this.gjtMenu = gjtMenu;
	}

	public GjtMenu() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameEn() {
		return this.nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public BigDecimal getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(BigDecimal orderNo) {
		this.orderNo = orderNo;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVisible() {
		return this.visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}


}