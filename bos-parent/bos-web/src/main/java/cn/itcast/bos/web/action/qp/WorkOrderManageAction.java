package cn.itcast.bos.web.action.qp;

import java.awt.Color;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.dom4j.util.ProxyDocumentFactory;
import org.hibernate.search.spi.BuildContext;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

import cn.itcast.bos.attachment.MyFileUtils;
import cn.itcast.bos.domain.qp.WorkOrderManage;
import cn.itcast.bos.web.action.base.BaseAction;

@SuppressWarnings("all")
@Controller("workOrderManageAction")
@Scope("prototype")
@Namespace("/qp")
@ParentPackage("bos")
public class WorkOrderManageAction extends BaseAction<WorkOrderManage> {

	//取派报表下载
	@Action(value = "wordOrderManagerAction_export")
	public String export() {
		
		try {
			List<WorkOrderManage> list = serviceFacade.getWorkOrderManageService().findAllWorkOrderManager();
			
			//实现下载功能
			HttpServletResponse response = ServletActionContext.getResponse();
			
			//com.lowagie.text.Document
			Document document = new Document();
			PdfWriter.getInstance(document, response.getOutputStream());
			
			//两个头
			String filename = new Date(System.currentTimeMillis()).toLocaleString()+"工作报表.pdf";
			response.setContentType(ServletActionContext.getServletContext().getMimeType(filename));
			response.setHeader("Content-Disposition","attachment;filename="
			+MyFileUtils.encodeDownloadFilename(filename, 
					ServletActionContext.getRequest().getHeader("user-agent")));
			
			document.open();
			
			//表写一个table，添加数据
			Table table = new Table(6, list.size()+1);
			table.setWidth(80);
			
			//水品对其方式
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			//竖直对齐方式
			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
			
			
			//设置表格字体
			BaseFont cn = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
			Font font  = new Font(cn,10,Font.NORMAL,Color.BLUE);
			
			//象表格写数据
			table.addCell(buildCell("工作单编号",font));
			table.addCell(buildCell("到达地",font));
			table.addCell(buildCell("货物",font));
			table.addCell(buildCell("数量",font));
			table.addCell(buildCell("重量",font));
			table.addCell(buildCell("配载要求",font));
			
			//表格数据
			for(WorkOrderManage workOrderManage : list){
				table.addCell(buildCell(workOrderManage.getId(), font));
				table.addCell(buildCell(workOrderManage.getArrivecity(), font));
				table.addCell(buildCell(workOrderManage.getProduct(), font));
				table.addCell(buildCell(workOrderManage.getNum().toString(), font));
				table.addCell(buildCell(workOrderManage.getWeight().toString(), font));
				table.addCell(buildCell(workOrderManage.getFloadreqr(), font));
				
				
			}
			
			document.add(table);
			document.close();
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	//w封装数据Phrase 对象方法
	private Cell buildCell(String content, Font font) throws BadElementException {
			Phrase phrase = new Phrase(content, font);
			return new Cell(phrase);
		}

	
	
	@Action(value = "workOrderManageAction_save", results = { @Result(name = "save", type = "json") })
	public String save() {
		try {
			serviceFacade.getWorkOrderManageService().save(model);
			push(true);
		} catch (Exception e) {
			e.printStackTrace();
			push(false);
		}
		return "save";
	}

	//
	@Action(value = "workOrderManageAction_pageQuery")
	public String pageQuery() {
		String name = getParameter("conditionName");// product /arrivecity
		String value = getParameter("conditionValue");// 用户输入数据 鼠标
		Page<WorkOrderManage> pageData = null;
		// 用户没有输入条件参数 无条件数据库查询 jpa
		if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(value)) {
			// 索引查询 hibernate-seach 自定义 dao
			// 用户输入参数 ---> 数据封装 --->service---> 自定义 dao impl 实现类 使用hibernate-search api
			pageData = serviceFacade.getWorkOrderManageService().pageQuery(getPageRequest(), name, value);// hibernate-search
		} else {
			pageData = serviceFacade.getWorkOrderManageService().pageQuery(getPageRequest());// jpa 查询
		}
		setPageData(pageData);
		return "pageQuery";
	}

}
