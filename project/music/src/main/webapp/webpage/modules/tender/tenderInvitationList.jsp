<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>投标管理管理</title>
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
		<h5>投标管理列表 </h5>
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
	<form:form id="searchForm" modelAttribute="tenderInvitation" action="${ctx}/tender/tenderInvitation/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>发布人：</span>
				<sys:treeselect id="publisher" name="publisher.id" value="${tenderInvitation.publisher.id}" labelName="publisher.name" labelValue="${tenderInvitation.publisher.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="form-control input-sm" allowClear="true" notAllowSelectParent="true"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="tender:tenderInvitation:add">
				<table:addRow url="${ctx}/tender/tenderInvitation/form" title="投标管理"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="tender:tenderInvitation:edit">
			    <table:editRow url="${ctx}/tender/tenderInvitation/form" title="投标管理" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="tender:tenderInvitation:del">
				<table:delRow url="${ctx}/tender/tenderInvitation/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="tender:tenderInvitation:import">
				<table:importExcel url="${ctx}/tender/tenderInvitation/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="tender:tenderInvitation:export">
	       		<table:exportExcel url="${ctx}/tender/tenderInvitation/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column tender.id">投标项</th>
				<%--<th  class="sort-column publisher.name">发布人</th>--%>
				<%--<th  class="sort-column phone">发布人联系方式</th>--%>
				<th  class="sort-column colum.id">栏目</th>
				<th  class="sort-column quantity">数量</th>
				<th  class="sort-column location">详细地址</th>
				<th  class="sort-column price">预算价格</th>
				<th  class="sort-column status">栏目状态</th>
				<th  class="sort-column remarks">备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tenderInvitation">
			<tr>
				<td> <input type="checkbox" id="${tenderInvitation.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看投标管理', '${ctx}/tender/tenderInvitation/form?id=${tenderInvitation.id}','800px', '500px')">
					${tenderInvitation.tender.demand.name}
				</a></td>
				<%--<td>--%>
					<%--${tenderInvitation.publisher.name}--%>
				<%--</td>--%>
				<%--<td>--%>
					<%--${tenderInvitation.phone}--%>
				<%--</td>--%>
				<td>
					${tenderInvitation.colum.name}
				</td>
				<td>
					${tenderInvitation.quantity}
				</td>
				<td>
					${tenderInvitation.location.detail}
				</td>
				<td>
					${tenderInvitation.price}
				</td>
				<td>
					${fns:getDictLabel(tenderInvitation.status, 'tender_status', '')}
				</td>
				<td>
					${tenderInvitation.remarks}
				</td>
				<td>
					<shiro:hasPermission name="tender:tenderInvitation:view">
						<a href="#" onclick="openDialogView('查看投标管理', '${ctx}/tender/tenderInvitation/form?id=${tenderInvitation.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="tender:tenderInvitation:edit">
    					<a href="#" onclick="openDialog('修改投标管理', '${ctx}/tender/tenderInvitation/form?id=${tenderInvitation.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="tender:tenderInvitation:del">
						<a href="${ctx}/tender/tenderInvitation/delete?id=${tenderInvitation.id}" onclick="return confirmx('确认要删除该投标管理吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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