<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>素材管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			laydate({
	            elem: '#creatDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>素材列表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-wrench"></i>
			</a>
			<ul class="dropdown-menu dropdown-user">
				<li><a href="#">选项1</a>
				</li>
				<li><a href="#">选项2</a>
				</li>
			</ul>
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="attachment" action="${ctx}/attachment/attachment/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>用户ID：</span>
				<form:input path="userid" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>创建时间：</span>
				<input id="creatDate" name="creatDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${attachment.creatDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			<span>审核状态：</span>
				<form:input path="auditType" htmlEscape="false" maxlength="45"  class=" form-control input-sm"/>
			<span>文件类型：</span>
				<form:input path="fileType" htmlEscape="false" maxlength="32"  class=" form-control input-sm"/>
			<span>文件名：</span>
				<form:input path="filename" htmlEscape="false" maxlength="45"  class=" form-control input-sm"/>
			<span>标题：</span>
				<form:input path="title" htmlEscape="false" maxlength="45"  class=" form-control input-sm"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="attachment:attachment:add">
				<table:addRow url="${ctx}/attachment/attachment/form" title="素材"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="attachment:attachment:edit">
			    <table:editRow url="${ctx}/attachment/attachment/form" title="素材" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="attachment:attachment:del">
				<table:delRow url="${ctx}/attachment/attachment/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="attachment:attachment:import">
				<table:importExcel url="${ctx}/attachment/attachment/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="attachment:attachment:export">
	       		<table:exportExcel url="${ctx}/attachment/attachment/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column userid">用户ID</th>
				<th  class="sort-column creatDate">创建时间</th>
				<th  class="sort-column auditType">审核状态</th>
				<th  class="sort-column fileType">文件类型</th>
				<th  class="sort-column filename">文件名</th>
				<th  class="sort-column title">标题</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="attachment">
			<tr>
				<td> <input type="checkbox" id="${attachment.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看素材', '${ctx}/attachment/attachment/form?id=${attachment.id}','800px', '500px')">
					${attachment.userid}
				</a></td>
				<td>
					<fmt:formatDate value="${attachment.creatDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${attachment.auditType}
				</td>
				<td>
					${attachment.fileType}
				</td>
				<td>
					${attachment.filename}
				</td>
				<td>
					${attachment.title}
				</td>
				<td>
					<shiro:hasPermission name="attachment:attachment:view">
						<a href="#" onclick="openDialogView('查看素材', '${ctx}/attachment/attachment/form?id=${attachment.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="attachment:attachment:edit">
    					<a href="#" onclick="openDialog('修改素材', '${ctx}/attachment/attachment/form?id=${attachment.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="attachment:attachment:del">
						<a href="${ctx}/attachment/attachment/delete?id=${attachment.id}" onclick="return confirmx('确认要删除该素材吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>