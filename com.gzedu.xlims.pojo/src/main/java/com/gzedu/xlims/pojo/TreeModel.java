/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.pojo;

import java.util.List;

/**
 * 功能说明：树结构模型
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月14日
 * @version 2.5
 *
 */
public class TreeModel {
	String id;
	String text;
	String href = "#";  //用于标记是模块还是操作
	List<TreeModel> nodes;

	TreeState state = new TreeState();

	public TreeState getState() {
		return state;
	}

	public void setState(TreeState state) {
		this.state = state;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public List<TreeModel> getNodes() {
		return nodes;
	}

	public void setNodes(List<TreeModel> nodes) {
		this.nodes = nodes;
	}

}
