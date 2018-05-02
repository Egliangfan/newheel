<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>个人信息管理</title>
	<meta name="decorator" content="default"/>
	 <link href="${ctxStatic}/summernote/summernote.css" rel="stylesheet">
	 <link href="${ctxStatic}/summernote/summernote-bs3.css" rel="stylesheet">
	 <script src="${ctxStatic}/summernote/summernote.min.js"></script>
	 <script src="${ctxStatic}/summernote/summernote-zh-CN.js"></script>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#logo").val($("#rich_logo").next().find(".note-editable").html());//取富文本的值
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
			
				//富文本初始化
			$('#rich_logo').summernote({
                lang: 'zh-CN'
            });

			$("#rich_logo").next().find(".note-editable").html(  $("#logo").val());

			$("#rich_logo").next().find(".note-editable").html(  $("#rich_logo").next().find(".note-editable").text());
		});
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="sysUserInfo" action="${ctx}/sysuserinfo/sysUserInfo/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属用户：</label></td>
					<td class="width-35">
						<sys:treeselect id="tuser" name="tuser.id" value="${sysUserInfo.tuser.id}" labelName="tuser.name" labelValue="${sysUserInfo.tuser.name}"
							title="用户" url="/sys/office/treeData?type=3" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>项目分类：</label></td>
					<td class="width-35">
						<form:input path="projectCategory" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>联系电话：</label></td>
					<td class="width-35">
						<form:input path="mobile" htmlEscape="false"    class="form-control required number"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>联系人：</label></td>
					<td class="width-35">
						<form:input path="linkman" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>服务地址：</label></td>
					<td class="width-35">
						<form:input path="serviceAddress" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>服务地区：</label></td>
					<td class="width-35">
						<form:input path="serviceRegion" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>logo：</label></td>
					<td class="width-35">
						<form:hidden path="logo"/>
						<div id="rich_logo">
                           

                        </div>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>简介：</label></td>
					<td class="width-35">
						<form:textarea path="introducion" htmlEscape="false" rows="4"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>价格范围：</label></td>
					<td class="width-35">
						<form:input path="priceRange" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:input path="remarks" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>