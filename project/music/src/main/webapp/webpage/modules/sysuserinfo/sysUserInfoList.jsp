<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>个人信息管理</title>
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
		<h5>个人信息列表 </h5>
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
	<form:form id="searchForm" modelAttribute="sysUserInfo" action="${ctx}/sysuserinfo/sysUserInfo/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>所属用户：</span>
				<sys:treeselect id="tuser" name="tuser.id" value="${sysUserInfo.tuser.id}" labelName="tuser.name" labelValue="${sysUserInfo.tuser.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="form-control input-sm" allowClear="true" notAllowSelectParent="true"/>
			<span>项目分类：</span>
				<form:input path="projectCategory" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>联系人：</span>
				<form:input path="linkman" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>服务地区：</span>
				<form:input path="serviceRegion" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>价格范围：</span>
				<form:input path="priceRange" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="sysuserinfo:sysUserInfo:add">
				<table:addRow url="${ctx}/sysuserinfo/sysUserInfo/form" title="个人信息"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sysuserinfo:sysUserInfo:edit">
			    <table:editRow url="${ctx}/sysuserinfo/sysUserInfo/form" title="个人信息" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sysuserinfo:sysUserInfo:del">
				<table:delRow url="${ctx}/sysuserinfo/sysUserInfo/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sysuserinfo:sysUserInfo:import">
				<table:importExcel url="${ctx}/sysuserinfo/sysUserInfo/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sysuserinfo:sysUserInfo:export">
	       		<table:exportExcel url="${ctx}/sysuserinfo/sysUserInfo/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column tuser.name">所属用户</th>
				<th  class="sort-column projectCategory">项目分类</th>
				<th  class="sort-column mobile">联系电话</th>
				<th  class="sort-column linkman">联系人</th>
				<th  class="sort-column serviceAddress">服务地址</th>
				<th  class="sort-column serviceRegion">服务地区</th>
				<th  class="sort-column logo">logo</th>
				<th  class="sort-column introducion">简介</th>
				<th  class="sort-column priceRange">价格范围</th>
				<th  class="sort-column remarks">备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sysUserInfo">
			<tr>
				<td> <input type="checkbox" id="${sysUserInfo.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看个人信息', '${ctx}/sysuserinfo/sysUserInfo/form?id=${sysUserInfo.id}','800px', '500px')">
					${sysUserInfo.tuser.name}
				</a></td>
				<td>
					${sysUserInfo.projectCategory}
				</td>
				<td>
					${sysUserInfo.mobile}
				</td>
				<td>
					${sysUserInfo.linkman}
				</td>
				<td>
					${sysUserInfo.serviceAddress}
				</td>
				<td>
					${sysUserInfo.serviceRegion}
				</td>
				<td>
					${fns:unescapeHtml(sysUserInfo.logo)}
				</td>
				<td>
					${sysUserInfo.introducion}
				</td>
				<td>
					${sysUserInfo.priceRange}
				</td>
				<td>
					${sysUserInfo.remarks}
				</td>
				<td>
					<shiro:hasPermission name="sysuserinfo:sysUserInfo:view">
						<a href="#" onclick="openDialogView('查看个人信息', '${ctx}/sysuserinfo/sysUserInfo/form?id=${sysUserInfo.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="sysuserinfo:sysUserInfo:edit">
    					<a href="#" onclick="openDialog('修改个人信息', '${ctx}/sysuserinfo/sysUserInfo/form?id=${sysUserInfo.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="sysuserinfo:sysUserInfo:del">
						<a href="${ctx}/sysuserinfo/sysUserInfo/delete?id=${sysUserInfo.id}" onclick="return confirmx('确认要删除该个人信息吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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