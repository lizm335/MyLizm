/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 缓存服务接口<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月7日
 * @version 2.5
 * @since JDK 1.7
 */
public interface CacheService {

	String HKEY_DICTIONARY = "Dictionary";

	class Value implements Serializable {
		private String code;
		private String name;

		public Value(String code, String name) {
			this.code = code;
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}
	}

	/**
	 * 原学科联动数据
	 */
	class ExsubjectValue extends Value {

		public ExsubjectValue(String code, String name) {
			super(code, name);
		}

		// 原学科门类字典
		private List<Value> dicExsubjectkindList;

		public List<Value> getDicExsubjectkindList() {
			return dicExsubjectkindList;
		}

		public void setDicExsubjectkindList(List<Value> dicExsubjectkindList) {
			this.dicExsubjectkindList = dicExsubjectkindList;
		}

	}

	/**
	 * 
	 * 功能说明：学籍管理使用
	 * 
	 * @author 张伟文 zhangeweiwen@eenet.com
	 * @Date 2017年3月15日
	 * @version 2.5
	 *
	 */
	final class Dictionary {
		/**
		 * 根据字典数据List的code获取name
		 * 
		 * @param dicList
		 * @param code
		 * @return
		 */
		public static String getName(List<Value> dicList, String code) {
			for (Iterator<Value> iterator = dicList.iterator(); iterator.hasNext();) {
				Value v = iterator.next();
				if (v.getCode().equals(code)) {
					return v.getName();
				}
			}
			return null;
		}

		// 政治面貌字典
		public static List<Value> dicPoliticsstatuList;
		// 原学历层次字典
		public static List<Value> dicExedulevelList;
		// 原学科字典
		public static List<ExsubjectValue> dicExsubjectList;
		// 原学历学习类型字典
		public static List<Value> dicExedubaktypeList;
		// 原学历证明材料字典
		public static List<Value> dicExcertificateproveList;
		// 原学历证件类型字典
		public static List<Value> dicExeduCertificateList;
		// 是否电大毕业字典
		public static List<Value> dicIsgraduatebytvList;

		static {
			dicPoliticsstatuList = new ArrayList<Value>();
			dicPoliticsstatuList.add(new Value("群众", "群众"));
			dicPoliticsstatuList.add(new Value("共青团员", "共青团员"));
			dicPoliticsstatuList.add(new Value("中共党员", "中共党员"));
			dicPoliticsstatuList.add(new Value("民主党派", "民主党派"));

			dicExedulevelList = new ArrayList<Value>();
			dicExedulevelList.add(new Value("高中", "高中"));
			dicExedulevelList.add(new Value("职高中专技校等同等学历", "职高中专技校等同等学历"));
			dicExedulevelList.add(new Value("专科", "专科"));
			dicExedulevelList.add(new Value("本科", "本科"));
			dicExedulevelList.add(new Value("硕士", "硕士"));
			dicExedulevelList.add(new Value("博士", "博士"));

			dicExsubjectList = new ArrayList<ExsubjectValue>();
			ExsubjectValue value = new ExsubjectValue("哲学", "哲学");
			List<Value> dicExsubjectkindList = new ArrayList<Value>();
			dicExsubjectkindList.add(new Value("哲学类", "哲学类"));
			value.setDicExsubjectkindList(dicExsubjectkindList);
			dicExsubjectList.add(value);
			ExsubjectValue value2 = new ExsubjectValue("医学", "医学");
			List<Value> dicExsubjectkindList2 = new ArrayList<Value>();
			dicExsubjectkindList2.add(new Value("基础医学类", "基础医学类"));
			dicExsubjectkindList2.add(new Value("预防医学类", "预防医学类"));
			dicExsubjectkindList2.add(new Value("临床医学与医学技术类", "临床医学与医学技术类"));
			dicExsubjectkindList2.add(new Value("口腔医学类", "口腔医学类"));
			dicExsubjectkindList2.add(new Value("中医学类", "中医学类"));
			dicExsubjectkindList2.add(new Value("法医学类", "法医学类"));
			dicExsubjectkindList2.add(new Value("护理学类", "护理学类"));
			dicExsubjectkindList2.add(new Value("药学类", "药学类"));
			value2.setDicExsubjectkindList(dicExsubjectkindList2);
			dicExsubjectList.add(value2);
			ExsubjectValue value3 = new ExsubjectValue("管理学", "管理学");
			List<Value> dicExsubjectkindList3 = new ArrayList<Value>();
			dicExsubjectkindList3.add(new Value("管理科学与工程类", "管理科学与工程类"));
			dicExsubjectkindList3.add(new Value("工商管理类", "工商管理类"));
			dicExsubjectkindList3.add(new Value("公共管理类", "公共管理类"));
			dicExsubjectkindList3.add(new Value("农业经济管理类", "农业经济管理类"));
			dicExsubjectkindList3.add(new Value("图书档案学类", "图书档案学类"));
			value3.setDicExsubjectkindList(dicExsubjectkindList3);
			dicExsubjectList.add(value3);
			ExsubjectValue value4 = new ExsubjectValue("农林牧渔类", "农林牧渔类");
			List<Value> dicExsubjectkindList4 = new ArrayList<Value>();
			dicExsubjectkindList4.add(new Value("农业技术类", "农业技术类"));
			dicExsubjectkindList4.add(new Value("林业技术类", "林业技术类"));
			dicExsubjectkindList4.add(new Value("畜牧兽医类", "畜牧兽医类"));
			dicExsubjectkindList4.add(new Value("水产养殖类", "水产养殖类"));
			dicExsubjectkindList4.add(new Value("农林管理类", "农林管理类"));
			value4.setDicExsubjectkindList(dicExsubjectkindList4);
			dicExsubjectList.add(value4);
			ExsubjectValue value5 = new ExsubjectValue("交通运输大类", "交通运输大类");
			List<Value> dicExsubjectkindList5 = new ArrayList<Value>();
			dicExsubjectkindList5.add(new Value("城市轨道运输类", "城市轨道运输类"));
			value5.setDicExsubjectkindList(dicExsubjectkindList5);
			dicExsubjectList.add(value5);
			ExsubjectValue value6 = new ExsubjectValue("生化与药品大类", "生化与药品大类");
			List<Value> dicExsubjectkindList6 = new ArrayList<Value>();
			dicExsubjectkindList6.add(new Value("化工技术类", "化工技术类"));
			dicExsubjectkindList6.add(new Value("食品药品管理类", "食品药品管理类"));
			value6.setDicExsubjectkindList(dicExsubjectkindList6);
			dicExsubjectList.add(value6);
			ExsubjectValue value7 = new ExsubjectValue("制造大类", "制造大类");
			List<Value> dicExsubjectkindList7 = new ArrayList<Value>();
			dicExsubjectkindList7.add(new Value("机械设计制造类", "机械设计制造类"));
			value7.setDicExsubjectkindList(dicExsubjectkindList7);
			dicExsubjectList.add(value7);
			ExsubjectValue value8 = new ExsubjectValue("轻纺食品大类", "轻纺食品大类");
			List<Value> dicExsubjectkindList8 = new ArrayList<Value>();
			dicExsubjectkindList8.add(new Value("食品类", "食品类"));
			value8.setDicExsubjectkindList(dicExsubjectkindList8);
			dicExsubjectList.add(value8);
			ExsubjectValue value9 = new ExsubjectValue("旅游大类", "旅游大类");
			List<Value> dicExsubjectkindList9 = new ArrayList<Value>();
			dicExsubjectkindList9.add(new Value("旅游管理类", "旅游管理类"));
			value9.setDicExsubjectkindList(dicExsubjectkindList9);
			dicExsubjectList.add(value9);
			ExsubjectValue value10 = new ExsubjectValue("公共事业大类", "公共事业大类");
			List<Value> dicExsubjectkindList10 = new ArrayList<Value>();
			dicExsubjectkindList10.add(new Value("公共管理类", "公共管理类"));
			dicExsubjectkindList10.add(new Value("公共服务类", "公共服务类"));
			value10.setDicExsubjectkindList(dicExsubjectkindList10);
			dicExsubjectList.add(value10);
			ExsubjectValue value11 = new ExsubjectValue("经济学", "经济学");
			List<Value> dicExsubjectkindList11 = new ArrayList<Value>();
			dicExsubjectkindList11.add(new Value("经济学类", "经济学类"));
			value11.setDicExsubjectkindList(dicExsubjectkindList11);
			dicExsubjectList.add(value11);
			ExsubjectValue value12 = new ExsubjectValue("文化教育大类", "文化教育大类");
			List<Value> dicExsubjectkindList12 = new ArrayList<Value>();
			dicExsubjectkindList12.add(new Value("语言文化类", "语言文化类"));
			value12.setDicExsubjectkindList(dicExsubjectkindList12);
			dicExsubjectList.add(value12);
			ExsubjectValue value13 = new ExsubjectValue("艺术设计传媒大类", "艺术设计传媒大类");
			List<Value> dicExsubjectkindList13 = new ArrayList<Value>();
			dicExsubjectkindList13.add(new Value("艺术设计类", "艺术设计类"));
			value13.setDicExsubjectkindList(dicExsubjectkindList13);
			dicExsubjectList.add(value13);
			ExsubjectValue value14 = new ExsubjectValue("法学", "法学");
			List<Value> dicExsubjectkindList14 = new ArrayList<Value>();
			dicExsubjectkindList14.add(new Value("法学类", "法学类"));
			dicExsubjectkindList14.add(new Value("马克思主义理论类", "马克思主义理论类"));
			dicExsubjectkindList14.add(new Value("社会学类", "社会学类"));
			dicExsubjectkindList14.add(new Value("政治学类", "政治学类"));
			dicExsubjectkindList14.add(new Value("公安学类", "公安学类"));
			value14.setDicExsubjectkindList(dicExsubjectkindList14);
			dicExsubjectList.add(value14);
			ExsubjectValue value15 = new ExsubjectValue("教育学", "教育学");
			List<Value> dicExsubjectkindList15 = new ArrayList<Value>();
			dicExsubjectkindList15.add(new Value("教育学类", "教育学类"));
			dicExsubjectkindList15.add(new Value("体育学类", "体育学类"));
			value15.setDicExsubjectkindList(dicExsubjectkindList15);
			dicExsubjectList.add(value15);
			ExsubjectValue value16 = new ExsubjectValue("文学", "文学");
			List<Value> dicExsubjectkindList16 = new ArrayList<Value>();
			dicExsubjectkindList16.add(new Value("中国语言文学类", "中国语言文学类"));
			dicExsubjectkindList16.add(new Value("外国语言文学类", "外国语言文学类"));
			dicExsubjectkindList16.add(new Value("新闻传播学类", "新闻传播学类"));
			dicExsubjectkindList16.add(new Value("艺术类", "艺术类"));
			value16.setDicExsubjectkindList(dicExsubjectkindList16);
			dicExsubjectList.add(value16);
			ExsubjectValue value17 = new ExsubjectValue("历史学", "历史学");
			List<Value> dicExsubjectkindList17 = new ArrayList<Value>();
			dicExsubjectkindList17.add(new Value("历史类", "历史类"));
			value17.setDicExsubjectkindList(dicExsubjectkindList17);
			dicExsubjectList.add(value17);
			ExsubjectValue value18 = new ExsubjectValue("理学", "理学");
			List<Value> dicExsubjectkindList18 = new ArrayList<Value>();
			dicExsubjectkindList18.add(new Value("数学类", "数学类"));
			dicExsubjectkindList18.add(new Value("物理学类", "物理学类"));
			dicExsubjectkindList18.add(new Value("化学类", "化学类"));
			dicExsubjectkindList18.add(new Value("生物学类", "生物学类"));
			dicExsubjectkindList18.add(new Value("天文学类", "天文学类"));
			dicExsubjectkindList18.add(new Value("地质学类", "地质学类"));
			dicExsubjectkindList18.add(new Value("地理科学类", "地理科学类"));
			dicExsubjectkindList18.add(new Value("地球物理学类", "地球物理学类"));
			dicExsubjectkindList18.add(new Value("大气物理学类", "大气物理学类"));
			dicExsubjectkindList18.add(new Value("海洋科学类", "海洋科学类"));
			dicExsubjectkindList18.add(new Value("力学类", "力学类"));
			dicExsubjectkindList18.add(new Value("电子信息科学类", "电子信息科学类"));
			dicExsubjectkindList18.add(new Value("材料科学类", "材料科学类"));
			dicExsubjectkindList18.add(new Value("环境科学类", "环境科学类"));
			dicExsubjectkindList18.add(new Value("心理学类", "心理学类"));
			dicExsubjectkindList18.add(new Value("统计学类", "统计学类"));
			value18.setDicExsubjectkindList(dicExsubjectkindList18);
			dicExsubjectList.add(value18);
			ExsubjectValue value19 = new ExsubjectValue("工学", "工学");
			List<Value> dicExsubjectkindList19 = new ArrayList<Value>();
			dicExsubjectkindList19.add(new Value("地矿类", "地矿类"));
			dicExsubjectkindList19.add(new Value("材料类", "材料类"));
			dicExsubjectkindList19.add(new Value("机械类", "机械类"));
			dicExsubjectkindList19.add(new Value("仪器仪表类", "仪器仪表类"));
			dicExsubjectkindList19.add(new Value("能源动力类", "能源动力类"));
			dicExsubjectkindList19.add(new Value("电气信息类", "电气信息类"));
			dicExsubjectkindList19.add(new Value("土建类", "土建类"));
			dicExsubjectkindList19.add(new Value("水利类", "水利类"));
			dicExsubjectkindList19.add(new Value("测绘类", "测绘类"));
			dicExsubjectkindList19.add(new Value("环境安全类", "环境安全类"));
			dicExsubjectkindList19.add(new Value("化工与制药类", "化工与制药类"));
			dicExsubjectkindList19.add(new Value("交通运输类", "交通运输类"));
			dicExsubjectkindList19.add(new Value("海洋工程类", "海洋工程类"));
			dicExsubjectkindList19.add(new Value("轻工纺织食品类", "轻工纺织食品类"));
			dicExsubjectkindList19.add(new Value("航空航天类", "航空航天类"));
			dicExsubjectkindList19.add(new Value("武器类", "武器类"));
			dicExsubjectkindList19.add(new Value("工程力学类", "工程力学类"));
			dicExsubjectkindList19.add(new Value("生物工程类", "生物工程类"));
			dicExsubjectkindList19.add(new Value("农业工程类", "农业工程类"));
			dicExsubjectkindList19.add(new Value("林业工程类", "林业工程类"));
			dicExsubjectkindList19.add(new Value("公安技术类", "公安技术类"));
			value19.setDicExsubjectkindList(dicExsubjectkindList19);
			dicExsubjectList.add(value19);
			ExsubjectValue value20 = new ExsubjectValue("农学", "农学");
			List<Value> dicExsubjectkindList20 = new ArrayList<Value>();
			dicExsubjectkindList20.add(new Value("植物生产类", "植物生产类"));
			dicExsubjectkindList20.add(new Value("草业科学类", "草业科学类"));
			dicExsubjectkindList20.add(new Value("森林资源类", "森林资源类"));
			dicExsubjectkindList20.add(new Value("环境生态类", "环境生态类"));
			dicExsubjectkindList20.add(new Value("动物生产类", "动物生产类"));
			dicExsubjectkindList20.add(new Value("动物医学类", "动物医学类"));
			dicExsubjectkindList20.add(new Value("水产用", "水产用"));
			value20.setDicExsubjectkindList(dicExsubjectkindList20);
			dicExsubjectList.add(value20);
			ExsubjectValue value21 = new ExsubjectValue("其他", "其他");
			List<Value> dicExsubjectkindList21 = new ArrayList<Value>();
			dicExsubjectkindList21.add(new Value("其他", "其他"));
			value21.setDicExsubjectkindList(dicExsubjectkindList21);
			dicExsubjectList.add(value21);

			dicExedubaktypeList = new ArrayList<Value>();
			dicExedubaktypeList.add(new Value("自考", "自考"));
			dicExedubaktypeList.add(new Value("非自考", "非自考"));

			dicExcertificateproveList = new ArrayList<Value>();
			dicExcertificateproveList.add(new Value("学历网查", "学历网查"));
			dicExcertificateproveList.add(new Value("学历认证报告", "学历认证报告"));
			dicExcertificateproveList.add(new Value("留学认证报告", "留学认证报告"));

			dicExeduCertificateList = new ArrayList<Value>();
			dicExeduCertificateList.add(new Value("身份证", "身份证"));

			dicIsgraduatebytvList = new ArrayList<Value>();
			dicIsgraduatebytvList.add(new Value("是", "是"));
			dicIsgraduatebytvList.add(new Value("否", "否"));
		}
	}

	final class DictionaryKey {
		/**
		 * 所有区域的key
		 */
		public static final String ALLAREA = "ALLAREA";
		/**
		 * 区域省份的key
		 */
		public static final String PROVINCE = "Province";

		/**
		 * 区域市的key
		 */
		public static String CITY = "${Province}_City";

		/**
		 * 区域区/县的key
		 */
		public static String AREA = "${Province}_${City}_Area";
		/**
		 * 性别
		 */
		public static final String SEX = "Sex";
		/**
		 * 文化程度码（最后学历,学历码）
		 */
		public static final String CULTURALLEVELCODE = "CulturalLevelCode";
		/**
		 * 学籍状态
		 */
		public static final String STUDENTNUMBERSTATUS = "StudentNumberStatus";
		/**
		 * 教职工类型
		 */
		public static final String EMPLOYEETYPE = "EmployeeType";
		/**
		 * 培养层次
		 */
		public static final String TRAININGLEVEL = "TrainingLevel";

		/**
		 * 考试类型
		 */
		public static final String EXAMINATIONMODE = "ExaminationMode";
		/**
		 * 课程类型
		 */
		public static final String COURSETYPE = "CourseType";
		/**
		 * 用户类型
		 */
		public static final String USERTYPE = "UserType";
		/**
		 * 学员类型
		 */
		public static final String USER_STUDENT_TYPE = "USER_TYPE";
		/**
		 * 服务方式
		 */
		public static final String SERVICEMETHOD = "ServiceMethod";
		/**
		 * 考试状态
		 */
		public static final String EXAM_STATE = "EXAM_STATE";
		/**
		 * 民族
		 */
		public static final String NATIONALITYCODE = "NationalityCode";
		/**
		 * 籍贯码
		 */
		public static final String NATIVECODE = "NativeCode";
		/**
		 * 婚姻状况
		 */
		public static final String MARITALSTATUSCODE = "MaritalStatusCode";
		/**
		 * 户口性质
		 */
		public static final String ACCOUNTSNATURECODE = "AccountsNatureCode";
		/**
		 * 职业状态
		 */
		public static final String OCCUPATIONSTATUS = "OccupationStatus";
		
		/**
		 * 适用行业
		 */
		public static final String TRADE_CODE = "TRADE_CODE";

		/**
		 * 课程性质
		 */
		public static final String COURSENATURE = "CourseNature";

		/**
		 * 角色对应平台字典
		 */
		public static final String PLATFORM_TYPE = "PLATFORM_TYPE";
		
		/**
		 * 成绩状态字典
		 */
		public static final String EXAM_SCORE = "EXAM_SCORE";
		
		/**
		 * 课程类型字典
		 */
		public static final String COURSE_CATEGORY_INFO = "CourseCategoryInfo";
		
	}

	/**
	 * 获取缓存
	 * 
	 * @param key
	 * @return
	 */
	Object getCached(String key);
	
	Map<String, String> getMapCachedCode(String key);

	/**
	 * 获取缓存的数据字典
	 * 
	 * @param key
	 * @return
	 */
	List<Value> getCachedDictionary(String key);

	/**
	 * 获取缓存的数据字典key=id,value=name
	 * 
	 * @param key
	 * @return
	 */
	Map<String, String> getMapCachedDictionary(String key);

	/**
	 * 
	 * @param key
	 * @return
	 */
	List<Map<String, Object>> getListCachedDictionary(String key);

	/**
	 * 设置缓存的数据字典
	 * 
	 * @param key
	 * @param vlist
	 * @return
	 */
	void setCachedDictionary(String key, List<Value> vlist);

	/**
	 * 获取缓存的数据字典
	 * 
	 * @param key
	 * @return
	 */
	Map<String, String> getCachedDictionaryMap(String key);

	/**
	 * 获取缓存数据字典对应的NAME
	 * 
	 * @param key
	 * @param code
	 * @return
	 */
	String getCachedDictionaryName(String key, String code);

	/**
	 * 判断缓存数据字典中是否存在对应的KEY
	 * 
	 * @param key
	 * @return
	 */
	boolean hasCachedDictionary(String key);

	/**
	 * 更新缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	boolean updateCached(String key, Object value);

	/**
	 * 删除缓存
	 * 
	 * @param key
	 * @return
	 */
	boolean deleteCached(String key);

}
