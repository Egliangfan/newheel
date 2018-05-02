<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>需求模板细节管理</title>
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
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="demandTemplateDetail" action="${ctx}/demand/template/demandTemplateDetail/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>需求模板：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/demand/template/demandTemplateDetail/selectdemandTemplate" id="demandTemplate" name="demandTemplate.id"  value="${demandTemplateDetail.demandTemplate.id}"  title="选择需求模板" labelName="demandTemplate.name" 
						 labelValue="${demandTemplateDetail.demandTemplate.name}" cssClass="form-control required" fieldLabels="名称|备注" fieldKeys="name|remarks" searchLabel="名称" searchKey="name" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>角色类型：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/demand/template/demandTemplateDetail/selectroleCategory" id="roleCategory" name="roleCategory.id"  value="${demandTemplateDetail.roleCategory.id}"  title="选择角色类型" labelName="roleCategory.name" 
						 labelValue="${demandTemplateDetail.roleCategory.name}" cssClass="form-control required" fieldLabels="名称|详述" fieldKeys="name|description" searchLabel="名称" searchKey="name" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">角色需求详述：</label></td>
					<td class="width-35">
						<form:input path="categoryDetail" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>数量：</label></td>
					<td class="width-35">
						<form:input path="quantity" htmlEscape="false"    class="form-control required digits"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>