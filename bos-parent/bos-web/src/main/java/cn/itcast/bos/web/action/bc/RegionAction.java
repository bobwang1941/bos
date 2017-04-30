package cn.itcast.bos.web.action.bc;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.bc.Region;
import cn.itcast.bos.pinyin4j.PinYin4jUtils;
import cn.itcast.bos.web.action.base.BaseAction;

@SuppressWarnings("all")
@Controller("regionAction")
@Scope("prototype")
@Namespace("/bc")
@ParentPackage("bos")
public class RegionAction extends BaseAction<Region> {
	// struts2 如何接受上传文件? 当前的action获取上传文件了 FileUploadIntercepter
	private File upload;

	// private String uploadFileName;

	public void setUpload(File upload) {
		this.upload = upload;
	}

	@Action(value = "regionAction_oneclickupload", results = { @Result(name = "oneclickupload", type = "json") })
	public String oneclickupload() {
		try {
			// 如何接受 上传文件
			// 创建对Excel工作簿文件的引用
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(upload));
			// 创建对工作表的引用。
			// 本例是按名引用（让我们假定那张表有着缺省名"Sheet1"）
			// HSSFSheet sheet = workbook.getSheet("Sheet1");
			HSSFSheet sheet = workbook.getSheetAt(0);
			// 也可用getSheetAt(int index)按索引引用，
			// 在Excel文档中，第一张工作表的缺省索引是0，
			// 其语句为：HSSFSheet sheet = workbook.getSheetAt(0);
			List<Region> regions = new ArrayList<Region>();
			for (Row row : sheet) {
				// 跳过第一行
				if (row.getRowNum() == 0) {
					continue;
				}
				Region r = new Region();
				r.setId(row.getCell(0).getStringCellValue());
				String province = row.getCell(1).getStringCellValue();// 安徽省
				r.setProvince(province);
				String city = row.getCell(2).getStringCellValue();
				r.setCity(city);
				String district = row.getCell(3).getStringCellValue();
				r.setDistrict(district);
				r.setPostcode(row.getCell(4).getStringCellValue());
				// 准备 简码
				province = province.substring(0, province.length() - 1);
				city = city.substring(0, city.length() - 1);
				district = district.substring(0, district.length() - 1);
				String[] values;
				if (province.equals(city)) {
					// 直辖市 北京市 北京市 朝阳区
					values = PinYin4jUtils.getHeadByString(province + district);
				} else {
					// 江苏省 南京市 玄武区
					values = PinYin4jUtils.getHeadByString(province + city + district);
				}
				StringBuffer sb = new StringBuffer();
				for (String s : values) {
					sb.append(s);
				}
				// 将省市区 首字母 大写 拼接
				r.setShortcode(sb.toString());// 简码 省市区 首字母 大写组成
				// 市的信息 转换 全拼
				r.setCitycode(PinYin4jUtils.hanziToPinyin(city, ""));// 市 全拼
				regions.add(r);
			}
			// 2 业务层
			serviceFacade.getRegionService().batchImport(regions);
			push(true);
		} catch (Exception e) {
			push(false);
			e.printStackTrace();
		}
		return "oneclickupload";
	}

	//
	// // 区域的分页查询
	@Action(value = "regionAction_pageQuery")
	public String pageQuery() {
		String column = getParameter("sort");// 字段名称
		String order = getParameter("order");// asc desc
		final String shortcode = model.getShortcode();

		Specification<Region> spec = new Specification<Region>() {
			@Override
			public Predicate toPredicate(Root<Region> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (StringUtils.isNotBlank(shortcode)) {
					Predicate p1 = cb.equal(root.get("shortcode").as(String.class), shortcode);
					list.add(p1);
				}
				Predicate[] p = new Predicate[list.size()];
				Predicate[] predicates = list.toArray(p);
				return cb.and(predicates);
			}
		};
		Page<Region> pageData = null;
		if (StringUtils.isNotBlank(column) && StringUtils.isNotBlank(order)) {
			// 排序查询
			pageData = serviceFacade.getRegionService().pageQuery(getPageRequest(), column, order, spec);
		} else {
			pageData = serviceFacade.getRegionService().pageQuery(getPageRequest(), spec);
		}
		setPageData(pageData);// 提供父类
		return "pageQuery";// 全局结果集
	}

	@Action(value = "regionAction_save", results = { @Result(name = "save", type = "json") })
	public String save() {
		try {
			serviceFacade.getRegionService().save(model);
			push(true);
		} catch (Exception e) {
			push(false);
			e.printStackTrace();
		}
		return "save";
	}

	@Action(value = "regionAction_ajaxListShortCode", results = { @Result(name = "ajaxListShortCode", type = "json") })
	public String ajaxListShortCode() {
		try {
			String q = getParameter("q");
			List<Region> regions = serviceFacade.getRegionService().ajaxListShortCode(q);
			push(regions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ajaxListShortCode";
	}

	// 分区添加业务 区域全部查询 ajaxList 形式返回
	@Action(value = "regionAction_ajaxList", results = { @Result(name = "ajaxList", type = "json") })
	public String ajaxList() {
		try {
			String q = getParameter("q");
			// q 没有值 所有的 q 存在值 模糊查询 省市区 模糊查询
			List<Region> regions = serviceFacade.getRegionService().ajaxList(q);
			push(regions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ajaxList";
	}

	// 省
	@Action(value = "regionAction_ajaxListProvinces", results = { @Result(name = "ajaxListProvinces", type = "json") })
	public String ajaxListProvinces() {
		try {
			List<String> regions = serviceFacade.getRegionService().ajaxListProvinces();
			push(regions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ajaxListProvinces";
	}

	// 市
	@Action(value = "regionAction_ajaxListCitys", results = { @Result(name = "ajaxListCitys", type = "json") })
	public String ajaxListCitys() {
		try {
			List<String> citys = serviceFacade.getRegionService().ajaxListCitys(model.getProvince());
			push(citys);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ajaxListCitys";
	}

	// 市
	@Action(value = "regionAction_ajaxListDistricts", results = { @Result(name = "ajaxListDistricts", type = "json") })
	public String ajaxListDistricts() {
		try {
			List<String> districts = serviceFacade.getRegionService().ajaxListDistricts(model.getCity());
			push(districts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ajaxListDistricts";
	}

}
