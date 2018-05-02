<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>投标内容管理</title>
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
	<form:form id="inputForm" modelAttribute="tender" action="${ctx}/tender/tender/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>需求项：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/tender/tender/selectdemand" id="demand" name="demand.id"  value="${tender.demand.id}"  title="选择需求项" labelName="demand.name"
						 labelValue="${tender.demand.name}" cssClass="form-control required" fieldLabels="名称|发布时间" fieldKeys="name|createDate" searchLabel="名称" searchKey="name" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>完成状态：</label></td>
					<td class="width-35">
						<form:select path="status" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('tender_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">审核状态：</label></td>
					<td class="width-35">
						<form:select path="reviewStatus" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('review_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">投标管理：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane active">
			<a class="btn btn-white btn-sm" onclick="addRow('#tenderInvitationList', tenderInvitationRowIdx, tenderInvitationTpl);tenderInvitationRowIdx = tenderInvitationRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>发布人</th>
						<th>发布人联系方式</th>
						<th>栏目</th>
						<th>数量</th>
						<th>详细地址</th>
						<th>预算价格</th>
						<th>栏目状态</th>
						<th>备注信息</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="tenderInvitationList">
				</tbody>
			</table>
			<script type="text/template" id="tenderInvitationTpl">//<!--
				<tr id="tenderInvitationList{{idx}}">
					<td class="hide">
						<input id="tenderInvitationList{{idx}}_id" name="tenderInvitationList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="tenderInvitationList{{idx}}_delFlag" name="tenderInvitationList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					<td  class="max-width-250">
						<sys:treeselect id="tenderInvitationList{{idx}}_publisher" name="tenderInvitationList[{{idx}}].publisher.id" value="{{row.publisher.id}}" labelName="tenderInvitationList{{idx}}.publisher.name" labelValue="{{row.publisher.name}}"
							title="用户" url="/sys/office/treeData?type=3" cssClass="form-control  required" allowClear="true" notAllowSelectParent="true"/>
					</td>


					<td>
						<input id="tenderInvitationList{{idx}}_phone" name="tenderInvitationList[{{idx}}].phone" type="text" value="{{row.phone}}"    class="form-control required"/>
					</td>

					<td class="max-width-35">
						<sys:treeselect id="tenderInvitationList{{idx}}_colum" name="tenderInvitationList[{{idx}}].colum.id" value="{{row.colum.id}}" labelName="tenderInvitationList{{idx}}.colum.name" labelValue="{{row.colum.name}}"
							title="类型" url="/column/classfy/columnClassfy/treeData" cssClass="form-control required" allowClear="true" notAllowSelectParent="false"/>
					</td>


					<td>
						<input id="tenderInvitationList{{idx}}_quantity" name="tenderInvitationList[{{idx}}].quantity" type="text" value="{{row.quantity}}"    class="form-control required digits"/>
					</td>

					<td>
					<sys:gridselect url="${ctx}/tender/tenderInvitation/selectworkplace" id="tenderInvitationList{{idx}}_location" name="tenderInvitationList[{{idx}}].location.id"  value="{{row.location.id}}"  title="选择场地信息" labelName="tenderInvitationList{{idx}}.location.detail"
										labelValue="{{row.location.detail}}" cssClass="form-control required" fieldLabels="详细地址|备注" fieldKeys="detail|remarks" searchLabel="详细地址" searchKey="detail" ></sys:gridselect>
					</td>


					<td>
						<input id="tenderInvitationList{{idx}}_price" name="tenderInvitationList[{{idx}}].price" type="text" value="{{row.price}}"    class="form-control required number"/>
					</td>


					<td>
						<select id="tenderInvitationList{{idx}}_status" name="tenderInvitationList[{{idx}}].status" data-value="{{row.status}}" class="form-control m-b  required">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('tender_status')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>


					<td>
						<textarea id="tenderInvitationList{{idx}}_remarks" name="tenderInvitationList[{{idx}}].remarks" rows="4"    class="form-control ">{{row.remarks}}</textarea>
					</td>

					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#tenderInvitationList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var tenderInvitationRowIdx = 0, tenderInvitationTpl = $("#tenderInvitationTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(tender.tenderInvitationList)};
					for (var i=0; i<data.length; i++){
						addRow('#tenderInvitationList', tenderInvitationRowIdx, tenderInvitationTpl, data[i]);
						tenderInvitationRowIdx = tenderInvitationRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
	</form:form>
</body>
</html>