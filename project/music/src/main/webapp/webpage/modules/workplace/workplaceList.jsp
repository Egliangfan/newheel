<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>场地位置管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>场地位置列表 </h5>
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
	<form:form id="searchForm" modelAttribute="workplace" action="${ctx}/workplace/workplace/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>地理位置：</span>
				<sys:treeselect id="area" name="area.id" value="${workplace.area.id}" labelName="area.name" labelValue="${workplace.area.name}"
					title="区域" url="/sys/area/treeData" cssClass="form-control input-sm" allowClear="true" notAllowSelectParent="true"/>
			<span>详细位置：</span>
				<form:input path="detail" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>状态：</span>
				<form:select path="status"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="workplace:workplace:add">
				<table:addRow url="${ctx}/workplace/workplace/form" title="场地位置"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="workplace:workplace:edit">
			    <table:editRow url="${ctx}/workplace/workplace/form" title="场地位置" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="workplace:workplace:del">
				<table:delRow url="${ctx}/workplace/workplace/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="workplace:workplace:import">
				<table:importExcel url="${ctx}/workplace/workplace/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="workplace:workplace:export">
	       		<table:exportExcel url="${ctx}/workplace/workplace/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column createBy.id">创建者</th>
				<th  class="sort-column area.name">地理位置</th>
				<th  class="sort-column detail">详细位置</th>
				<th  class="sort-column remarks">备注信息</th>
				<th  class="sort-column status">状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="workplace">
			<tr>
				<td> <input type="checkbox" id="${workplace.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看场地位置', '${ctx}/workplace/workplace/form?id=${workplace.id}','800px', '500px')">
					${workplace.createBy.id}
				</a></td>
				<td>
					${workplace.area.name}
				</td>
				<td>
					${workplace.detail}
				</td>
				<td>
					${workplace.remarks}
				</td>
				<td>
					${fns:getDictLabel(workplace.status, 'status', '')}
				</td>
				<td>
					<shiro:hasPermission name="workplace:workplace:view">
						<a href="#" onclick="openDialogView('查看场地位置', '${ctx}/workplace/workplace/form?id=${workplace.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="workplace:workplace:edit">
    					<a href="#" onclick="openDialog('修改场地位置', '${ctx}/workplace/workplace/form?id=${workplace.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="workplace:workplace:del">
						<a href="${ctx}/workplace/workplace/delete?id=${workplace.id}" onclick="return confirmx('确认要删除该场地位置吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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