<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
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
	function doAdd(){
		
		$('#addStaffWindow').window("open");
		$('#tel').validatebox('reduce'); 
	}
	
	function doView(){
		alert("查看...");
	}
	
	function doDelete(){
		//  方法  1  判断用户是否 选中一行或者多行...
		var arr = $("#grid").datagrid("getSelections");
		if(arr==null||arr.length==0){
			$.messager.alert("警告!","至少选择一行","warning");
			return ;
		}else{
			//  选中 ....  push  数组最后添加一个元素    join 将数组直接转换字符串 采用执行分隔符
			var  ids = new Array();//  数组 存放所有选中 id
			for(var i =0;i<arr.length;i++){
				ids.push(arr[i].id);//  所有id  遍历  添加一个新的数组中
			}
			// 数组 转换 带有分隔符的字符串 
			var idsString = ids.join(",");
			// 2:  ajax 请求 
			$.post("${pageContext.request.contextPath}/bc/staffAction_delBatch",{"delIds":idsString},function(data){
				// data true /false 
				  if(data){
					  $.messager.alert("恭喜!","更新成功","info");
				  }else{
					  $.messager.alert("抱歉!","批量更新失败","error");
				  }
				//  重新加载datagrid   自动 url 地址重新发一次请求 
				$("#grid").datagrid("reload");
			});
		}
	}
	
	function doRestore(){
		alert("将取派员还原...");
	}
	//工具栏
	var toolbar = [ {
		id : 'button-view',	
		text : '查询',
		iconCls : 'icon-search',
		handler : doView
	}, {
		id : 'button-add',
		text : '增加',
		iconCls : 'icon-add',
		handler : doAdd
	},
	<shiro:hasRole name="base">
	{
		id : 'button-delete',
		text : '作废',
		iconCls : 'icon-cancel',
		handler : doDelete
	},
	</shiro:hasRole>
	{
		id : 'button-save',
		text : '还原',
		iconCls : 'icon-save',
		handler : doRestore
	}];
	// 定义列
	var columns = [ [ {
		field : 'id',
		checkbox : true
	},{
		field : 'name',
		title : '姓名',
		width : 120,
		align : 'center',
		sortable:true
	}, {
		field : 'telephone',
		title : '手机号',
		width : 120,
		align : 'center',
		styler: function(value,row,index){
				if (value.match(9)){
					return 'background-color:#FFF8DC;color:green;';
				}  			
      } 
	}, {
		field : 'haspda',
		title : '是否有PDA',
		width : 120,
		align : 'center',
		formatter : function(data,row, index){
			//  data json  字符串 对应的   haspda  值
			if(row.haspda=="1"){
				return "有";
			}else{
				return "无";
			}
		}
	}, {
		field : 'deltag',
		title : '是否作废',
		width : 120,
		align : 'center',
		formatter : function(data,row, index){
			if(data=="0"){
				return "正常使用"
			}else{
				return "已作废";
			}
		}
	}, {
		field : 'standard',
		title : '业绩总览',
		width : 120,
		align : 'center'
	}, {
		field : 'station',
		title : '所属单位',
		width : 200,
		align : 'center'
	} ] ];
	
	$(function(){
		// 先将body隐藏，再显示，不会出现页面刷新效果
		$("body").css({visibility:"visible"});
		// 取派员信息表格
		$('#grid').datagrid( {
			iconCls : 'icon-forward',
			fit : true,
			border : false,
			rownumbers : true,
			striped : true,
			queryParams: {  		
				    name: 'easyui',  		
				    subject: 'datagrid'  	
				  },
			pageList: [5,10],
			fitColumns:true,
			pagination : true,
			loadMsg:'...服务器满头大汗啊...数据正在往这边赶路....客官 稍安勿躁.......',
			toolbar : toolbar,
			sortName : 'name',// 定义哪列可以排序 
			sortOrder : 'desc',// 定义列的排列顺序，只能是'asc'或'desc'。默认asc 
			remoteSort : false,
			url : "${pageContext.request.contextPath}/bc/staffAction_pageQuery",
			idField : 'id',
			columns : columns,
			onDblClickRow : function(rowIndex, rowData){
				//  双击弹窗  ....将rowData 数据回显 form 里面{id:'',name:'刘德华',telephone:'137....'}
				$('#addStaffWindow').window("open");
				$('#tel').validatebox('remove');//解除校验机制 
				//  回显要求 : 表单的name属性值 = json对象 key 一致 可以回显数据
				$("#addStaffForm").form("load",rowData);//  form表单的回显 功能 ...
				// $('#id').validatebox('reduce');
			}
		});
		
		// 添加取派员窗口
		$('#addStaffWindow').window({
	        title: '取派员操作',
	        width: 400,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false,
	        onBeforeClose:function(){
	        	//   清除form 表单数据 尤其  隐藏id 一定要清除  reset   jquery --->Dom
	        	  $("#addStaffForm")[0].reset();//  text  
	        	 $("#tel").removeClass('validatebox-invalid');  
	             $("#id").val("");  //  一定将隐藏id 值清除 // hidden
	        }
	    });
		
		//  页面加载完成  保存 提交表单
		$("#save").click(function(){
			//  条件判断  校验成功  提交表单   查看form表单提交 手册文档
			//  获取所有选项 是否合法   flag 
			 var flag = $("#addStaffForm").form("validate");
			if(flag){
			     $("#addStaffForm").submit();
			     $('#addStaffWindow').window("close");
			}
		});
	});
	
	//  easyui 校验器扩展  
	$.extend($.fn.validatebox.defaults.rules, { 
			telephone: { 
			validator: function(value, param){ 
			// value 用户输入框数据  param 传递参数
			  //  定义手机号 正则表达式  匹配  value  
			       var reg = /^1[3|4|5|7|8]\d{9}$/;
			       return reg.test(value); 
			}, 
			message: '手机号必须以1|3|4|5|7|8开头的11位数字' 
			} ,
			//  ajax 校验手机号唯一
		    uniquePhone:{
		        validator: function (value) {  
		            var flag;
		            $.ajax({  
	                    url : '${pageContext.request.contextPath}/bc/staffAction_validTelephone',  
	                    type : 'POST',                    
	                    timeout : 60000,  
	                    data:{"telephone" : value},  
	                    async: false,    
	                    success : function(data, textStatus, jqXHR) {     
	                        if (data.flag) {  
	                            flag = true;      
	                        }else{  
	                            flag = false;  
	                        }  
	                    }  
	                });  
		            if(flag){  
		            	// 样式效果
	                    $("#tel").removeClass('validatebox-invalid');  
	                }  
	                return flag;  
		        },  
		        message: '手机号已经存在'  
		    }  
}); 

	$.extend($.fn.validatebox.methods, {
	    remove: function (jq, newposition) {
// 	        return jq.each(function () {
// 	            $(this).removeClass("validatebox-text validatebox-invalid").unbind('focus').unbind('blur');
// 	        });
              return $(jq).removeClass("validatebox-text validatebox-invalid");
	    },

	    reduce: function (jq, newposition) {
	        return jq.each(function () {
	            var opt = $(this).data().validatebox.options;
	            $(this).addClass("validatebox-text").validatebox(opt);
	        });
	    }
	}); 
</script>	
</head>
<shiro:hasRole name="base"></shiro:hasRole>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="center" border="false">
    	<table id="grid"></table>
	</div>
	<!-- 添加取派员窗体  -->
	<div class="easyui-window" title="对收派员进行添加或者修改" id="addStaffWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >保存</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form id="addStaffForm" method="post" action="${pageContext.request.contextPath }/bc/staffAction_save">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">收派员信息</td>
					</tr>
					<tr>
						<td>姓名</td>
						<td>
						<input type="hidden" name="id" id="id"/>
						<input type="text" name="name" class="easyui-validatebox" data-options="required:true"/></td>
					</tr>
					<tr>
						<td>手机</td>
						<td><input id="tel" type="text" name="telephone" class="easyui-validatebox" 
						   data-options="required:true,validType:['telephone','uniquePhone']"/>
						   <span id="tel_sp"></span>
						   </td>
					</tr>
					<tr>
						<td>单位</td>
						<td><input type="text" name="station" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td colspan="2">
						<input type="checkbox" name="haspda" value="1" />
						是否有PDA</td>
					</tr>
					<tr>
						<td>业绩总览</td>
						<td>
							<input type="text" name="standard" class="easyui-validatebox" required="true"/>  
						</td>
					</tr>
					</table>
			</form>
		</div>
	</div>
</body>
</html>	