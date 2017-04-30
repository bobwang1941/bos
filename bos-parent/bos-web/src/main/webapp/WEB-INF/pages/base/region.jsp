<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>区域设置</title>
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
	
	<!-- 一键上传脚本导入 -->
	<script src="${pageContext.request.contextPath }/js/oneclick/jquery.ocupload-1.1.2.js"
	type="text/javascript"></script>
	
	
<script type="text/javascript">
var editIndex ;
	function doAdd(){
		$('#addRegionWindow').window("open");
	}
	
	function doSave(){
		$("#grid").datagrid('endEdit',editIndex );
	}
	
	function doDelete(){
		alert("删除...");
	}
	
	function doCancel(){
		if(editIndex!=undefined){
			$("#grid").datagrid('cancelEdit',editIndex);
			if($('#grid').datagrid('getRows')[editIndex].id == undefined){
				$("#grid").datagrid('deleteRow',editIndex);
			}
			editIndex = undefined;
		}
	}
	
	//工具栏
	var toolbar = [
{
	id : 'button-cancel',
	text : '取消编辑',
	iconCls : 'icon-cancel',
	handler : doCancel
}   ,
	               {
		id : 'button-save',	
		text : '保存修改',
		iconCls : 'icon-save',
		handler : doSave
	}, {
		id : 'button-add',
		text : '增加',
		iconCls : 'icon-add',
		handler : doAdd
	}, {
		id : 'button-delete',
		text : '删除',
		iconCls : 'icon-cancel',
		handler : doDelete
	}, {
		id : 'button-import',
		text : '导入',
		iconCls : 'icon-redo'
	}];
	// 定义列
	var columns = [ [ {
		field : 'id',
		checkbox : true,
	},{
		field : 'province',
		title : '省',
		width : 120,
		align : 'center',
		editor :{
			type : 'validatebox',
			options : {
				required: true
			}
		}
	}, {
		field : 'city',
		title : '市',
		width : 120,
		align : 'center',
		editor :{
			type : 'validatebox',
			options : {
				required: true
			}
		}
	}, {
		field : 'district',
		title : '区',
		width : 120,
		align : 'center',
		sortable:true,
		editor :{
			type : 'validatebox',
			options : {
				required: true
			}
		}
	}, {
		field : 'postcode',
		title : '邮编',
		width : 120,
		align : 'center',
		editor :{
			type : 'validatebox',
			options : {
				required: true
			}
		}
	}, {
		field : 'shortcode',
		title : '简码',
		width : 120,
		align : 'center',
		editor :{
			type : 'validatebox',
			options : {
				required: true
			}
		}
	}, {
		field : 'citycode',
		title : '城市编码',
		width : 200,
		align : 'center',
		editor :{
			type : 'validatebox',
			options : {
				required: true
			}
		}
	} ] ];
	
	$(function(){
		// 先将body隐藏，再显示，不会出现页面刷新效果
		$("body").css({visibility:"visible"});
		
		// 收派标准数据表格
		$('#grid').datagrid( {
			iconCls : 'icon-forward',
			fit : true,
			border : false,
			rownumbers : true,
			striped : true,
			pageList: [5,10,15],
			fitColumns:true,
			pagination : true,
			loadMsg:'服务器满头大汗啊...数据正在往这边赶路....客官稍安勿躁...',
			toolbar : toolbar,
			url : "${pageContext.request.contextPath}/bc/regionAction_pageQuery",
			idField : 'id',
			columns : columns,
			onDblClickRow : doDblClickRow,
			onAfterEdit : function(rowIndex, rowData, changes){
				editIndex = undefined;
				// 保存
				$.post("${pageContext.request.contextPath}/bc/regionAction_save",rowData);
			}
		});
		
		// 添加、修改区域窗口
		$('#addRegionWindow').window({
	        title: '添加修改区域',
	        width: 400,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false
	    });
		
		//  一键上传
		$("#button-import").upload({
			    name: 'upload',
		        action: '${pageContext.request.contextPath}/bc/regionAction_oneclickupload',
		        enctype: 'multipart/form-data',
		        onSelect: function() {
		         this.autoSubmit = false;  //  不要立刻提交文件
		         var reg =/^(.+\.xls|.+\.xlsx)$/;   
		         if (reg.test(this.filename())) {  
                     this.submit();  //  上传文件
                 }  
                 else {  
                	 $.messager.alert("警告","请上传一个excel文件","warning");
                 } 
		        },
		        //  服务器完成 回送 200
		        onComplete: function (data, self, element) {
		        	//  data  服务器回送  true/false 伊娃
		        	var da = eval("("+data+")");
		        	 if(da){
		        		 $.messager.alert("恭喜","上传成功","info");
		        	 }else{
		        		 $.messager.alert("错误","上传失败","error");
		        	 }
		        }
		});
		
		// code 简码精确查询对应省市区
		$("#btn").click(function(){
			$("#grid").datagrid("load",{
				shortcode:$("#shortcode").combobox('getText')
			});
		});
		
	});

	function doDblClickRow(rowIndex,rowData){
		// 打开编辑
		if(editIndex == undefined){
			$('#grid').datagrid('beginEdit',rowIndex);
			editIndex = rowIndex;
		}
		
	}
	
</script>	
</head>
<body class="easyui-layout" style="visibility:hidden;">
<div region="north" border="false">
    	<input id="shortcode" class="easyui-combobox" name="shortcode"  
    	data-options="mode:'remote',valueField:'shortcode',textField:'shortcode',url:'${pageContext.request.contextPath }/bc/regionAction_ajaxListShortCode'" />  
     <input type="button" value="查询" id="btn">
  </div>  	
	<div region="center" border="false">
    	<table id="grid"></table>
	</div>
	<div class="easyui-window" title="区域添加修改" id="addRegionWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >保存</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form>
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">区域信息</td>
					</tr>
					<tr>
						<td>省</td>
						<td><input type="text" name="province" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>市</td>
						<td><input type="text" name="city" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>区</td>
						<td><input type="text" name="district" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>邮编</td>
						<td><input type="text" name="postcode" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>简码</td>
						<td><input type="text" name="shortcode" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>城市编码</td>
						<td><input type="text" name="citycode" class="easyui-validatebox" required="true"/></td>
					</tr>
					</table>
			</form>
		</div>
	</div>
</body>
</html>