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
	  var  editIndex;
	  var flag =false;
	   $(function(){
		   $("#dg").datagrid({
			   
			   url:'data.json',//  ajax 请求 获取数据数据 
			   columns:[[ 

							{field:'code',title:'编号',width:100
								,editor:{
									type:'validatebox',
									options:{
										required:true
									}
								}		
							}, 
							
							{field:'name',title:'名称',width:100
							,editor:{
								type:'validatebox',
								options:{
									required:true
								}
							}	
							}, 
							
							{field:'price',title:'价格',width:100,align:'right'
								,editor:{
									type:'validatebox',
									options:{
										required:true
									}
								}		
							} 
							
							]] ,

			   //  自定义工具栏  handler 按钮事件 
			   toolbar: [{  		
				   iconCls: 'icon-edit',
				   text:'取消编辑',
				   handler: function(){
					   $("#dg").datagrid("cancelEdit",editIndex);
					    if(flag){
					    	 $("#dg").datagrid("deleteRow",editIndex);
					    	 flag = false;
					    }
					   
					   }  	
				   },'-',{  		
				   iconCls: 'icon-help',  	
				   text:'保存编辑',
				   handler: function(){
					  // 获取 开启编辑 哪一行的索引
					   $("#dg").datagrid("endEdit",editIndex);
					  //  保存 数据 获取  ajax 发送 action 
					      // 每一个
					   }  	
				   },'-',{  		
					   iconCls: 'icon-save',  	
					   text:'添加',
					   handler: function(){
						  // 获取 开启编辑 哪一行的索引
						  //  插入一行
						   $('#dg').datagrid('insertRow',{
							  	index: 0,	// index start with 0
							  	row: {
							  		
							  	}
							  });  
						    //  开启编辑
						   $("#dg").datagrid("beginEdit",0);
						   editIndex = 0;
						   flag = true;
						   }  	
					   }] ,
				   //  分页栏
				   pagination:true,
				   // 填满父容器
				   fit:true,
				   // 每页显示数据
				   pageList:[2,4,6,8],
				   // 显示行号
				   rownumbers:true,
				   onDblClickRow:function(rowIndex, rowData){
					   //  打开编辑??
							   // 1: 列属性添加 editor
							   
							   // 2: 双击打开编辑 
							   $("#dg").datagrid("beginEdit",rowIndex);
							   editIndex = rowIndex;
				   },
				   onAfterEdit:function(rowIndex, rowData, changes){
					   alert(rowData.name);
				   }
			   
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
		<table id="dg">
		</table>
	</div>

</body>
</html>