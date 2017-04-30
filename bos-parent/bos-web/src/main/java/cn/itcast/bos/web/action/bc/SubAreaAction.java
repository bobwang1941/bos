package cn.itcast.bos.web.action.bc;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.attachment.MyFileUtils;
import cn.itcast.bos.domain.bc.DecidedZone;
import cn.itcast.bos.domain.bc.Region;
import cn.itcast.bos.domain.bc.Subarea;
import cn.itcast.bos.web.action.base.BaseAction;

@SuppressWarnings("all")
@Controller("subAreaAction")
@Scope("prototype")
@Namespace("/bc")
@ParentPackage("bos")
public class SubAreaAction extends BaseAction<Subarea> {
	// 定区添加之分区狐仙
	@Action(value = "subAreaAction_findnoassociation", results = { @Result(name = "findnoassociation", type = "json") })
	public String findnoassociation() {
		try {
			List<Subarea> subs = serviceFacade.getSubAreaService().findnoassociation();
			push(subs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "findnoassociation";
	}

	@Action(value = "subAreaAction_save", results = { @Result(name = "save", location = "/WEB-INF/pages/base/subarea.jsp") })
	public String save() {
		try {
			serviceFacade.getSubAreaService().save(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "save";
	}

	// 分区分页查询 无条件分页 +无条件分页查询
	// Subarea 封装所有的请求参数条件 mysql limit oracle rownum 动态 sql
	@Action(value = "subAreaAction_pageQuery")
	public String pageQuery() {
		// 将条件 从Model取出来 封装 Sepcification接口实现方法中
		// 1:
		Specification<Subarea> spec = new Specification<Subarea>() {
			@Override
			public Predicate toPredicate(Root<Subarea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 条件封装 实现 root 查询主表对象实体类 cb 连接条件对象 类似 hibernate Criteria Restrictions.
				// 1: 关键字查询 查询自己表 subarea即可 不需要多表连接的 单表查询
				List<Predicate> list = new ArrayList<Predicate>(); // 存放多个条件对象 Predicate
				if (StringUtils.isNotBlank(model.getAddresskey())) {
					// 用户要进行关键字查询
					Predicate p1 = cb.like(root.get("addresskey").as(String.class), "%" + model.getAddresskey() + "%");
					list.add(p1);
				}

				// 2: 多表查询 分区连接 region 查询 root--Subarea --->连接 Region
				if (model.getRegion() != null) {
					// 连接区域表 表关系 root n : 1 region 多方连接一方
					Join<Subarea, Region> regionJoin = root.join(root.getModel().getSingularAttribute("region", Region.class), JoinType.LEFT);
					// regionJoin 操作 区域表 Region
					if (StringUtils.isNotBlank(model.getRegion().getProvince())) {
						Predicate p2 = cb.like(regionJoin.get("province").as(String.class), "%" + model.getRegion().getProvince() + "%");
						list.add(p2);
					}
					if (StringUtils.isNotBlank(model.getRegion().getCity())) {
						Predicate p3 = cb.like(regionJoin.get("city").as(String.class), "%" + model.getRegion().getCity() + "%");
						list.add(p3);
					}
					if (StringUtils.isNotBlank(model.getRegion().getDistrict())) {
						Predicate p4 = cb.like(regionJoin.get("district").as(String.class), "%" + model.getRegion().getDistrict() + "%");
						list.add(p4);
					}
				}
				// 3: 定区编号查询 root subarea ---decidedzone
				if (model.getDecidedZone() != null && StringUtils.isNotBlank(model.getDecidedZone().getId())) {
					Predicate p5 = cb.equal(root.get("decidedZone").as(DecidedZone.class), model.getDecidedZone());
					list.add(p5);
				}
				// 集合转换成数组
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));
			}
		};

		// 2:
		Page<Subarea> pageData = serviceFacade.getSubAreaService().pageQuery(getPageRequest(), spec);
		setPageData(pageData);
		return "pageQuery";
	}

	// subAreaAction_download 接受表单提交参数信息 多条件查询
	@Action(value = "subAreaAction_download")
	public String download() {
		// 将条件 从Model取出来 封装 Sepcification接口实现方法中
		// 1:
		Specification<Subarea> spec = new Specification<Subarea>() {
			@Override
			public Predicate toPredicate(Root<Subarea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 条件封装 实现 root 查询主表对象实体类 cb 连接条件对象 类似 hibernate Criteria Restrictions.
				// 1: 关键字查询 查询自己表 subarea即可 不需要多表连接的 单表查询
				List<Predicate> list = new ArrayList<Predicate>(); // 存放多个条件对象 Predicate
				if (StringUtils.isNotBlank(model.getAddresskey())) {
					// 用户要进行关键字查询
					Predicate p1 = cb.like(root.get("addresskey").as(String.class), "%" + model.getAddresskey() + "%");
					list.add(p1);
				}

				// 2: 多表查询 分区连接 region 查询 root--Subarea --->连接 Region
				if (model.getRegion() != null) {
					// 连接区域表 表关系 root n : 1 region 多方连接一方
					Join<Subarea, Region> regionJoin = root.join(root.getModel().getSingularAttribute("region", Region.class), JoinType.LEFT);
					// regionJoin 操作 区域表 Region
					if (StringUtils.isNotBlank(model.getRegion().getProvince())) {
						Predicate p2 = cb.like(regionJoin.get("province").as(String.class), "%" + model.getRegion().getProvince() + "%");
						list.add(p2);
					}
					if (StringUtils.isNotBlank(model.getRegion().getCity())) {
						Predicate p3 = cb.like(regionJoin.get("city").as(String.class), "%" + model.getRegion().getCity() + "%");
						list.add(p3);
					}
					if (StringUtils.isNotBlank(model.getRegion().getDistrict())) {
						Predicate p4 = cb.like(regionJoin.get("district").as(String.class), "%" + model.getRegion().getDistrict() + "%");
						list.add(p4);
					}
				}
				// 3: 定区编号查询 root subarea ---decidedzone
				if (model.getDecidedZone() != null && StringUtils.isNotBlank(model.getDecidedZone().getId())) {
					Predicate p5 = cb.equal(root.get("decidedZone").as(DecidedZone.class), model.getDecidedZone());
					list.add(p5);
				}
				// 集合转换成数组
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));
			}
		};

		// 2: dao 已经实现了多条件查询 27记录
		List<Subarea> subs = serviceFacade.getSubAreaService().findSubareasBySpecifications(spec);
		// 3: 集合数据 写入 工作簿对象中 POI+下载知识点
		// 创建新的Excel 工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 在Excel工作簿中建一工作表，其名为缺省值
		HSSFSheet sheet = workbook.createSheet("上海传智");
		HSSFRow row = sheet.createRow(0);
		// 标题 分区编号 区域编码 关键字 起始号 结束号 位置信息
		row.createCell(0).setCellValue("分区编号");
		row.createCell(1).setCellValue("区域编号");
		row.createCell(2).setCellValue("关键字");
		row.createCell(3).setCellValue("起始号");
		row.createCell(4).setCellValue("结束号");
		row.createCell(5).setCellValue("位置信息");
		if (subs != null && subs.size() != 0) {
			for (Subarea s : subs) {
				int lastRowNum = sheet.getLastRowNum();// 当前sheet 最后一行的 角标 0
				HSSFRow row1 = sheet.createRow(lastRowNum + 1);
				// 标题 分区编号 区域编码 关键字 起始号 结束号 位置信息
				row1.createCell(0).setCellValue(s.getId());
				row1.createCell(1).setCellValue(s.getRegion().getId());
				row1.createCell(2).setCellValue(s.getAddresskey());
				row1.createCell(3).setCellValue(s.getStartnum());
				row1.createCell(4).setCellValue(s.getEndnum());
				row1.createCell(5).setCellValue(s.getPosition());
			}
			// 工作簿完成
			// 下载 两个头 文件类型头 mime 类型 附件头 一个流
			String filename = new Date(System.currentTimeMillis()).toLocaleString() + "_传智播客itcast37期.xls";
			HttpServletResponse response = getResponse();
			// ServletContext.getMimeType 根据文件扩展名 获取对应Mime 类型
			response.setContentType(getServletContext().getMimeType(filename));// 类型头
			// 2附件头
			response.setHeader("Content-Disposition", "attachment;filename=" + MyFileUtils.encodeDownloadFilename(filename, getRequest().getHeader("user-agent")));
			// 3输出流
			try {
				workbook.write(response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 4": 下载
		return NONE;
	}
}
