<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>需求细节管理</title>
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
		<h5>需求细节列表 </h5>
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
	<form:form id="searchForm" modelAttribute="demandDetail" action="${ctx}/demand/demandDetail/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>需求项：</span>
				<sys:gridselect url="${ctx}/demand/demandDetail/selectdemand" id="demand" name="demand"  value="${demandDetail.demand.id}"  title="选择需求项" labelName="demand.name" 
					labelValue="${demandDetail.demand.name}" cssClass="form-control required" fieldLabels="名称|详述|状态" fieldKeys="name|description|status" searchLabel="名称" searchKey="name" ></sys:gridselect>
			<span>角色类型：</span>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="demand:demandDetail:add">
				<table:addRow url="${ctx}/demand/demandDetail/form" title="需求细节"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="demand:demandDetail:edit">
			    <table:editRow url="${ctx}/demand/demandDetail/form" title="需求细节" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="demand:demandDetail:del">
				<table:delRow url="${ctx}/demand/demandDetail/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="demand:demandDetail:import">
				<table:importExcel url="${ctx}/demand/demandDetail/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="demand:demandDetail:export">
	       		<table:exportExcel url="${ctx}/demand/demandDetail/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column demand.id">需求项</th>
				<th  class="sort-column category.id">角色类型</th>
				<th  class="sort-column categoryDetail">角色要求详述</th>
				<th  class="sort-column quantity">数量</th>
				<th  class="sort-column status">状态</th>
				<th  class="sort-column remarks">备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="demandDetail">
			<tr>
				<td> <input type="checkbox" id="${demandDetail.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看需求细节', '${ctx}/demand/demandDetail/form?id=${demandDetail.id}','800px', '500px')">
					${demandDetail.demand.name}
				</a></td>
				<td>
					${demandDetail.category.name}
				</td>
				<td>
					${demandDetail.categoryDetail}
				</td>
				<td>
					${demandDetail.quantity}
				</td>
				<td>
					${fns:getDictLabel(demandDetail.status, 'demand_detail', '')}
				</td>
				<td>
					${demandDetail.remarks}
				</td>
				<td>
					<shiro:hasPermission name="demand:demandDetail:view">
						<a href="#" onclick="openDialogView('查看需求细节', '${ctx}/demand/demandDetail/form?id=${demandDetail.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="demand:demandDetail:edit">
    					<a href="#" onclick="openDialog('修改需求细节', '${ctx}/demand/demandDetail/form?id=${demandDetail.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="demand:demandDetail:del">
						<a href="${ctx}/demand/demandDetail/delete?id=${demandDetail.id}" onclick="return confirmx('确认要删除该需求细节吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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