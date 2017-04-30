<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加业务受理单</title>
<!-- 导入jquery核心类库 -->
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
<!-- 导入easyui类库 -->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/ext/portal.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/default.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/ext/jquery.portal.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/ext/jquery.cookie.js"></script>
<script
	src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"
	type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		$("body").css({visibility:"visible"});
		
		// 对save按钮条件 点击事件
		$('#save').click(function(){
			// 对form 进行校验
				$('#noticebillForm').submit();
			if($('#noticebillForm').form('validate')){
			}
		});
		//  加载省 
		  $.post("${pageContext.request.contextPath}/bc/regionAction_ajaxListProvinces",function(data){
			  //  List<String> province  ["江苏省","浙江省",""]
			    $(data).each(function(){
			    	$("#province").append("<option value='"+this+"'>"+this+"</option>");
			    });
		  });
		
		//  用户选择省  发送ajax  获取 对应市 
		$("#province").change(function(){
			//  优化 
		      $("#city")[0].length=1;
		      $("#district")[0].length=1;
			  $.post("${pageContext.request.contextPath}/bc/regionAction_ajaxListCitys",{"province":this.value},function(data){
				  //  List<String> province  ["南京市","苏州市",""]
				    $(data).each(function(){
				    	$("#city").append("<option value='"+this+"'>"+this+"</option>");
				    });
			  });
		});
		//  用户选择省  发送ajax  获取 对应市 
		$("#city").change(function(){
			//  优化 
		      $("#district")[0].length=1;
			  $.post("${pageContext.request.contextPath}/bc/regionAction_ajaxListDistricts",{"city":this.value},function(data){
				  //  List<String> province  ["南京市","苏州市",""]
				    $(data).each(function(){
				    	$("#district").append("<option value='"+this+"'>"+this+"</option>");
				    });
			  });
		});
		
		
		
	});
	
	function findCustomerByTelephone(obj){
		$.post("${pageContext.request.contextPath}/qp/noticeBillAction_findCustomerByTelephone",{"telephone":obj.value},function(data){
			 //  Customer == data   回显客户基本信息  
			 if(data==null){
				 //  crm系统没有客户信息  提示信息清除
				 // 显示客户信息 
				 $("#customerId").val("");
				 $("#customerName").val("");
				 $("#customerId").removeAttr("readonly");
			 }else{
				 // 显示客户信息 
				 $("#customerId").val(data.id);
				 $("#customerId").attr("readonly","readonly");//  禁止客户id修改
				 $("#customerName").val(data.name);
			 }
			 
		});
		
	}
	
	
</script>
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="north" style="height:31px;overflow:hidden;" split="false"
		border="false">
		<div class="datagrid-toolbar">
			<a id="save" icon="icon-save" href="#" class="easyui-linkbutton"
				plain="true">新单</a>
			<a id="edit" icon="icon-edit" href="${pageContext.request.contextPath }/page_qupai_noticebill.action" class="easyui-linkbutton"
				plain="true">工单操作</a>	
		</div>
	</div>
	<div region="center" style="overflow:auto;padding:5px;" border="false">
		<form id="noticebillForm" action="${pageContext.request.contextPath }/qp/noticeBillAction_save" method="post">
			<table class="table-edit" width="95%" align="center">
				<tr class="title">
					<td colspan="4">客户信息</td>
				</tr>
				<tr>
					<td>来电号码:</td>
					<td><input type="text" class="easyui-validatebox" name="telephone"
						required="true" onblur="findCustomerByTelephone(this);"/></td>
					<td>客户编号:</td>
					<td><input id="customerId" type="text" class="easyui-validatebox"  name="customerId"
						required="true" /></td>
				</tr>
				<tr>
					<td>客户姓名:</td>
					<td><input id="customerName" type="text" class="easyui-validatebox" name="customerName"
						required="true" /></td>
					<td>联系人:</td>
					<td><input type="text" class="easyui-validatebox" name="delegater"
						required="true" /></td>
				</tr>
				<tr class="title">
					<td colspan="4">商品信息</td>
				</tr>
				<tr>
					<td>品名:</td>
					<td><input type="text" class="easyui-validatebox" name="product"
						required="true" /></td>
					<td>件数:</td>
					<td><input type="text" class="easyui-numberbox" name="num"
						required="true" /></td>
				</tr>
				<tr>
					<td>重量:</td>
					<td><input type="text" class="easyui-numberbox" name="weight"
						required="true" /></td>
					<td>体积:</td>
					<td><input type="text" class="easyui-validatebox" name="volume"
						required="true" /></td>
				</tr>
				<tr>
				<td>取件地址</td>
					<td colspan="3">
					省&nbsp;
					<select id="province" name="province">
					         <option>--请选择--</option>
					</select>&nbsp;
					市&nbsp;
					<select id="city" name="city">
					         <option>--请选择--</option>
					</select>&nbsp;
					区&nbsp;
					<select id="district" name="district">
					         <option>--请选择--</option>
					</select>
					&nbsp;详细地址:
					
					<input type="text" class="easyui-validatebox" name="pickaddress"
						required="true" size="61"/>
						</td>
				</tr>
				<tr>
					<td>到达城市:</td>
					<td><input type="text" class="easyui-validatebox" name="arrivecity"
						required="true" /></td>
					<td>预约取件时间:</td>
					<td><input type="text" class="easyui-datebox" name="pickdate"
						data-options="required:true, editable:false" /></td>
				</tr>
				<tr>
					<td>备注:</td>
					<td colspan="3"><textarea rows="5" cols="80" type="text" class="easyui-validatebox" name="remark"
						required="true" ></textarea></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>