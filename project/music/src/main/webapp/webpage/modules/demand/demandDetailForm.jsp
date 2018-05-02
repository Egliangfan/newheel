<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>需求细节管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
		});
		function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
		}
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
			}else if(delFlag.val() == "1"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		}
	</script>
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="demandDetail" action="${ctx}/demand/demandDetail/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>需求项：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/demand/demandDetail/selectdemand" id="demand" name="demand.id"  value="${demandDetail.demand.id}"  title="选择需求项" labelName="demand.name" 
						 labelValue="${demandDetail.demand.name}" cssClass="form-control required" fieldLabels="名称|详述|状态" fieldKeys="name|description|status" searchLabel="名称" searchKey="name" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>角色类型：</label></td>
					<td class="width-35">
						<sys:treeselect id="category" name="category.id" value="${demandDetail.category.id}" labelName="category.name" labelValue="${demandDetail.category.name}"
										title="类型" url="/column/classfy/columnClassfy/treeData" cssClass="form-control required" allowClear="true" notAllowSelectParent="false"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">角色要求详述：</label></td>
					<td class="width-35">
						<form:input path="categoryDetail" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>数量：</label></td>
					<td class="width-35">
						<form:input path="quantity" htmlEscape="false"    class="form-control required digits"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>状态：</label></td>
					<td class="width-35">
						<form:select path="status" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('demand_detail')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
		
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">角色档期：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane active">
			<a class="btn btn-white btn-sm" onclick="addRow('#roleScheduleList', roleScheduleRowIdx, roleScheduleTpl);roleScheduleRowIdx = roleScheduleRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>开始时间</th>
						<th>结束时间</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="roleScheduleList">
				</tbody>
			</table>
			<script type="text/template" id="roleScheduleTpl">//<!--
				<tr id="roleScheduleList{{idx}}">
					<td class="hide">
						<input id="roleScheduleList{{idx}}_id" name="roleScheduleList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="roleScheduleList{{idx}}_delFlag" name="roleScheduleList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="roleScheduleList{{idx}}_startTime" name="roleScheduleList[{{idx}}].startTime" type="text" readonly="readonly" maxlength="20" class="laydate-icon form-control layer-date  required"
							value="{{row.startTime}}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					</td>
					
					
					<td>
						<input id="roleScheduleList{{idx}}_stopTime" name="roleScheduleList[{{idx}}].stopTime" type="text" readonly="readonly" maxlength="20" class="laydate-icon form-control layer-date  required"
							value="{{row.stopTime}}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#roleScheduleList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var roleScheduleRowIdx = 0, roleScheduleTpl = $("#roleScheduleTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(demandDetail.roleScheduleList)};
					for (var i=0; i<data.length; i++){
						addRow('#roleScheduleList', roleScheduleRowIdx, roleScheduleTpl, data[i]);
						roleScheduleRowIdx = roleScheduleRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
	</form:form>
</body>
</html>