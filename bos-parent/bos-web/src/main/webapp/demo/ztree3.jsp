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
	var setting = {
			//  菜单添加点击事件 
			callback: {
				onClick: function(event, treeId, treeNode, clickFlag){
					//  treeNode 每一个菜单json 对象
					//  添加tabs 选项卡  如果选项卡存在 选中 ....  如果选项卡不存在  添加  page 存在值  才可以添加选项卡
					if(treeNode.page!=null){
						//  存在 选项卡 切换 选中 
						 var flag = $('#tt').tabs('exists',treeNode.name);
						  if(flag){
							  //  true  存在 切换
							  $('#tt').tabs('select',treeNode.name); 
						  }else{
							  $("#tt").tabs("add",{
									 //  该对象就是 tabs选项卡  当前页面 内嵌一个页面布局  iframe
									 title:treeNode.name,
									 content:'<div style="width:100%;height:100%;overflow:hidden;">'
											+ '<iframe src="'
											+ treeNode.page
											+ '" scrolling="auto" style="width:100%;height:100%;border:0;" ></iframe></div>',
									 closable:true
								 });
						  }
						
					}
					
				}
			}
			,
			data: {
				simpleData: {
					enable: true
				}
			}
		};
	
	 $(function(){
		 //  发送ajax  才可以访问  xx.json 文件
		 $.post("${pageContext.request.contextPath}/json/menu.json",function(data){
		      $.fn.zTree.init($("#treeDemo"), setting, data);
		 },"json");
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

	<div id="tt" data-options="region:'center',title:'中部'" class="easyui-tabs">
	    
	</div>

</body>
</html>