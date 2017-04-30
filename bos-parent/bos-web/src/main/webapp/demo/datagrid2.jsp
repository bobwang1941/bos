<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="../js/jquery-1.8.3.js"></script>
<script type="text/javascript" src="../js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="../js/easyui/locale/easyui-lang-zh_CN.js"></script>
<link type="text/css" rel="stylesheet"
	href="../js/easyui/themes/default/easyui.css" />
<link type="text/css" rel="stylesheet"
	href="../js/easyui/themes/icon.css" />
	
	<script type="text/javascript">
	
	   $(function(){
		   $("#dg").datagrid({
			   //  自定义工具栏  handler 按钮事件 
			   toolbar: [{  		
				   iconCls: 'icon-edit',
				   text:'编辑',
				   handler: function(){
					   alert('edit');
					   }  	
				   },'-',{  		
				   iconCls: 'icon-help',  	
				   text:'帮助',
				   handler: function(){
					   alert('help');
					   }  	
				   }] ,
				   //  分页栏
				   pagination:true,
				   // 填满父容器
				   fit:true,
				   // 每页显示数据
				   pageList:[2,4,6,8],
				   // 显示行号
				   rownumbers:true
			   
		   });
	   })
	</script>
	
</head>
<body class="easyui-layout">

	<div
		data-options="region:'north',title:'北部',split:false,iconCls:'icon-ok'"
		style="height: 100px;"></div>

	<div data-options="region:'south',title:'南部',split:false"
		style="height: 100px;"></div>

	<div
		data-options="region:'east',iconCls:'icon-reload',title:'East',split:true"
		style="width: 100px;"></div>

	<div data-options="region:'west',title:'菜单导航',split:false"
		style="width: 180px;">
		<!-- 折叠菜单 -->
		<div id="aa" class="easyui-accordion" data-options="fit:true">

			<div title="基本功能">菜单1</div>

			<div title="系统管理">菜单2</div>

			<div title="联系我们">菜单三</div>

		</div>

	</div>

	<div data-options="region:'center',title:'中部'">
		<!-- 显示数据表格  -->
		<table id="dg" class="easyui-datagrid" data-options="url:'data.json'">
			<thead>
				<tr>

					<th data-options="field:'code'">编号</th>

					<th data-options="field:'name'">名称</th>

					<th data-options="field:'price'">价格</th>

				</tr>
			</thead>
		</table>
	</div>

</body>
</html>