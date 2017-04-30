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
<script type="text/javascript"
	src="../js/ztree/jquery.ztree.all-3.5.js"></script>
<link type="text/css" rel="stylesheet"
	href="../js/easyui/themes/default/easyui.css" />
<link type="text/css" rel="stylesheet"
	href="../js/easyui/themes/icon.css" />
<link type="text/css" rel="stylesheet"
	href="../js/ztree/zTreeStyle.css" />
	
	<script type="text/javascript">
	var setting = {	};
	
	var zNodes =[
	             {name:"传智集团",open:true,children:[{name:"上海传智"},{name:"北京传智"},{name:"深圳传智"}]},
	             {name:"搜索引擎公司",children:[{name:"百度公司",children:[{name:"百度音乐"},{name:"百度贴吧"},{name:"百度百科"}]},{name:"搜狐公司"},{name:"雅虎公司"}]},
	             {name:"交友平台",children:[{name:"陌陌"},{name:"约吧"},{name:"约爱"}]}
	             ];
	 $(function(){
		 $.fn.zTree.init($("#treeDemo"), setting, zNodes);
	 });
	
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

			<div title="基本功能">
				<!-- 菜单树  simpleData.html  standardData.html -->
					<ul id="treeDemo" class="ztree"></ul>
			</div>

			<div title="系统管理">菜单2</div>

			<div title="联系我们">菜单三</div>

		</div>


	</div>

	<div data-options="region:'center',title:'中部'">呵呵呵呵</div>

</body>
</html>