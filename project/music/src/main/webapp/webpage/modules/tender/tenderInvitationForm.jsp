<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>投标管理管理</title>
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
	<form:form id="inputForm" modelAttribute="tenderInvitation" action="${ctx}/tender/tenderInvitation/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">投标项：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/tender/tenderInvitation/selecttender" id="tender" name="tender.id"  value="${tenderInvitation.tender.id}"  title="选择投标项" labelName="a"
						 labelValue="${tenderInvitation.tender.demand.name}" cssClass="form-control required" fieldLabels="ID|发布时间" fieldKeys="id|createDate" searchLabel="ID" searchKey="id" ></sys:gridselect>
					</td>
					<%--<td class="width-15 active"><label class="pull-right"><font color="red">*</font>发布人：</label></td>--%>
					<%--<td class="width-35">--%>
						<%--<sys:treeselect id="publisher" name="publisher.id" value="${tenderInvitation.publisher.id}" labelName="publisher.name" labelValue="${tenderInvitation.publisher.name}"--%>
							<%--title="用户" url="/sys/office/treeData?type=3" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>--%>
					<%--</td>--%>
				<%--</tr>--%>
				<%--<tr>--%>
					<%--<td class="width-15 active"><label class="pull-right"><font color="red">*</font>发布人联系方式：</label></td>--%>
					<%--<td class="width-35">--%>
						<%--<form:input path="phone" htmlEscape="false"    class="form-control required"/>--%>
					<%--</td>--%>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>栏目：</label></td>
					<td class="width-35">
						<sys:treeselect id="classfy" name="colum.id" value="${tenderInvitation.colum.id}" labelName="colum.name" labelValue="${tenderInvitation.colum.name}"
										title="类型" url="/column/classfy/columnClassfy/treeData" cssClass="form-control required" allowClear="true" notAllowSelectParent="false"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>数量：</label></td>
					<td class="width-35">
						<form:input path="quantity" htmlEscape="false"    class="form-control required digits"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>详细地址：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/demand/demand/selectworkplace" id="location" name="location.id"  value="${tenderInvitation.location.id}"  title="选择场地信息" labelName="location.detail"
										labelValue="${tenderInvitation.location.detail}" cssClass="form-control required" fieldLabels="详细地址|备注" fieldKeys="detail|remarks" searchLabel="详细地址" searchKey="detail" ></sys:gridselect>

					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>预算价格：</label></td>
					<td class="width-35">
						<form:input path="price" htmlEscape="false"    class="form-control required number"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>栏目状态：</label></td>
					<td class="width-35">
						<form:select path="status" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('tender_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
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
		
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">投标档期：</a>
                </li>
				<li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">投标者：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane active">
			<a class="btn btn-white btn-sm" onclick="addRow('#tenderScheduleList', tenderScheduleRowIdx, tenderScheduleTpl);tenderScheduleRowIdx = tenderScheduleRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>档期起始时间</th>
						<th>档期结束时间</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="tenderScheduleList">
				</tbody>
			</table>
			<script type="text/template" id="tenderScheduleTpl">//<!--
				<tr id="tenderScheduleList{{idx}}">
					<td class="hide">
						<input id="tenderScheduleList{{idx}}_id" name="tenderScheduleList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="tenderScheduleList{{idx}}_delFlag" name="tenderScheduleList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="tenderScheduleList{{idx}}_startDate" name="tenderScheduleList[{{idx}}].startDate" type="text" readonly="readonly" maxlength="20" class="laydate-icon form-control layer-date  required"
							value="{{row.startDate}}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					</td>
					
					
					<td>
						<input id="tenderScheduleList{{idx}}_stopDate" name="tenderScheduleList[{{idx}}].stopDate" type="text" readonly="readonly" maxlength="20" class="laydate-icon form-control layer-date  required"
							value="{{row.stopDate}}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#tenderScheduleList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var tenderScheduleRowIdx = 0, tenderScheduleTpl = $("#tenderScheduleTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(tenderInvitation.tenderScheduleList)};
					for (var i=0; i<data.length; i++){
						addRow('#tenderScheduleList', tenderScheduleRowIdx, tenderScheduleTpl, data[i]);
						tenderScheduleRowIdx = tenderScheduleRowIdx + 1;
					}
				});
			</script>
			</div>
				<div id="tab-2" class="tab-pane">
			<a class="btn btn-white btn-sm" onclick="addRow('#tenderSubmissionList', tenderSubmissionRowIdx, tenderSubmissionTpl);tenderSubmissionRowIdx = tenderSubmissionRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>投标者</th>
						<th>应标状态</th>
						<th>投标时间</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="tenderSubmissionList">
				</tbody>
			</table>
			<script type="text/template" id="tenderSubmissionTpl">//<!--
				<tr id="tenderSubmissionList{{idx}}">
					<td class="hide">
						<input id="tenderSubmissionList{{idx}}_id" name="tenderSubmissionList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="tenderSubmissionList{{idx}}_delFlag" name="tenderSubmissionList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td  class="max-width-250">
						<sys:treeselect id="tenderSubmissionList{{idx}}_user" name="tenderSubmissionList[{{idx}}].user.id" value="{{row.user.id}}" labelName="tenderSubmissionList{{idx}}.user.name" labelValue="{{row.user.name}}"
							title="用户" url="/sys/office/treeData?type=3" cssClass="form-control  required" allowClear="true" notAllowSelectParent="true"/>
					</td>
					<td>
						<select id="tenderSubmissionList{{idx}}_status" name="tenderSubmissionList[{{idx}}].status" data-value="{{row.status}}" class="form-control m-b  required">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('bid_status')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>

					<td>
					<input id="tenderSubmissionList{{idx}}_createDate" name="tenderSubmissionList[{{idx}}].createDate" type="text" readonly="readonly" maxlength="20" class="laydate-icon form-control layer-date  required"
							value="{{row.createDate}}" "/>
							</td>
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#tenderSubmissionList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var tenderSubmissionRowIdx = 0, tenderSubmissionTpl = $("#tenderSubmissionTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(tenderInvitation.tenderSubmissionList)};
					for (var i=0; i<data.length; i++){
						addRow('#tenderSubmissionList', tenderSubmissionRowIdx, tenderSubmissionTpl, data[i]);
						tenderSubmissionRowIdx = tenderSubmissionRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
	</form:form>
</body>
</html>