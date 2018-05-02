<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>需求模板管理</title>
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
	<form:form id="inputForm" modelAttribute="demandTemplate" action="${ctx}/demand/template/demandTemplate/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>版本标识：</label></td>
					<td class="width-35">
						<form:input path="version" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>业务类型：</label></td>
					<td class="width-35">
						<sys:treeselect id="category" name="category.id" value="${demandTemplate.category.id}" labelName="category.name" labelValue="${demandTemplate.category.name}"
										title="类型" url="/column/classfy/columnClassfy/treeData" cssClass="form-control required" allowClear="true" notAllowSelectParent="false"/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>状态：</label></td>
					<td class="width-35">
						<form:select path="status" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
		
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">需求模板细节：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane active">
			<a class="btn btn-white btn-sm" onclick="addRow('#demandTemplateDetailList', demandTemplateDetailRowIdx, demandTemplateDetailTpl);demandTemplateDetailRowIdx = demandTemplateDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>角色类型</th>
						<th>角色需求详述</th>
						<th>数量</th>
						<th>备注信息</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="demandTemplateDetailList">
				</tbody>
			</table>
			<script type="text/template" id="demandTemplateDetailTpl">//<!--
				<tr id="demandTemplateDetailList{{idx}}">
					<td class="hide">
						<input id="demandTemplateDetailList{{idx}}_id" name="demandTemplateDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="demandTemplateDetailList{{idx}}_delFlag" name="demandTemplateDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					<td class="max-width-35">
						<sys:treeselect id="demandTemplateDetailList{{idx}}_roleCategory" name="demandTemplateDetailList[{{idx}}].roleCategory.id" value="{{row.roleCategory.id}}" labelName="demandTemplateDetailList{{idx}}.roleCategory.name" labelValue="{{row.roleCategory.name}}"
							title="类型" url="/column/classfy/columnClassfy/treeData" cssClass="form-control required" allowClear="true" notAllowSelectParent="false"/>
					</td>

					<td>
						<input id="demandTemplateDetailList{{idx}}_categoryDetail" name="demandTemplateDetailList[{{idx}}].categoryDetail" type="text" value="{{row.categoryDetail}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="demandTemplateDetailList{{idx}}_quantity" name="demandTemplateDetailList[{{idx}}].quantity" type="text" value="{{row.quantity}}"    class="form-control required digits"/>
					</td>
					
					
					<td>
						<textarea id="demandTemplateDetailList{{idx}}_remarks" name="demandTemplateDetailList[{{idx}}].remarks" rows="4"    class="form-control ">{{row.remarks}}</textarea>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#demandTemplateDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var demandTemplateDetailRowIdx = 0, demandTemplateDetailTpl = $("#demandTemplateDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(demandTemplate.demandTemplateDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#demandTemplateDetailList', demandTemplateDetailRowIdx, demandTemplateDetailTpl, data[i]);
						demandTemplateDetailRowIdx = demandTemplateDetailRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
	</form:form>
</body>
</html>