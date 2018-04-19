package com.gzedu.xlims.third.sms;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;

public enum SmsSenderType {
	

	jpushTestSender(0,"JPush推送测试","test"),
	
	guangzhoushiyanxueyuan(1,"广州实验学院","2f5bfcce71fa462b8e1f65bcd0f4c632"),//国家开放大学（广州）实验学院
	yida(11,"溢达圆梦大学","28112679492d48a1a9a69b51a6566993"),//溢达集团学习中心
	wanbao(12,"万宝圆梦大学","c777800513be46ef96a6b06fb89ef5fd"),//番禺万宝学习中心
	lianjia(13,"链家圆梦大学","eb866db4dc204d128946e4c4e251dfd8"),// 链家招生点
	jinfa(14,"金发圆梦大学","4a204875872347189478fd59be2d886c"),// 金发科技
	baijia(15,"百佳圆梦大学","8c125ef1968e4c7681534a08f7f4caea"),// 百佳超市学习中心
	jiumaojiu(16,"九毛九圆梦大学","77127a4e8291446c899d38434f06597a"),// 九毛九学习中心
	hepai(17,"壳牌圆梦大学","9874937F891E4D3B91E12D5A446A44E8"),// 壳牌学习中心
	
	
	guokaishiyanxueyuan(2,"国开实验学院 ","9b2f42ececf64f38af621554d1ea5b79"),// 国家开放大学实验学院
	maibao(21,"麦胞圆梦大学",""),
	;
	
	public static List<SmsSenderType> gzList = new ArrayList<SmsSenderType>();
	
	public static List<SmsSenderType> gkList = new ArrayList<SmsSenderType>();
	static {
		gzList.add(yida);
		gzList.add(wanbao);
		gzList.add(lianjia);
		gzList.add(jinfa);
		gzList.add(baijia);
		gzList.add(jiumaojiu);
		gzList.add(hepai);
		
		gkList.add(maibao);
	}
	Integer code;
	
	String name;
	
	String orgId;
	
	/*public static List<SmsSenderType> gzList;
	
	public static List<SmsSenderType> gkList;*/
	
	/*static {
		gzList.add(guangzhoushiyanxueyuan);
		gzList.add(yida);
		gzList.add(wanbao);
		gzList.add(lianjia);
		gzList.add(jinfa);
		gzList.add(baijia);
		gzList.add(jiumaojiu);
		gzList.add(hepai);
		
		gkList.add(guokaishiyanxueyuan);
		gkList.add(maibao);
	}*/
	
	private SmsSenderType(Integer code, String name,String orgId) {
		this.code = code;
		this.name = name;
		this.orgId = orgId;
	}
	
	public static SmsSenderType getItemByOrgId(String orgId) {
		for(SmsSenderType t : SmsSenderType.values()) {
			if(ObjectUtils.equals(t.getOrgId(), orgId)) {
				return t;
			}
		}
		return null;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

}
