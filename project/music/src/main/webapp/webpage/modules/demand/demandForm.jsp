<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>需求管理管理</title>
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
			
					laydate({
			            elem: '#startDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        });
					laydate({
			            elem: '#stopDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
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
	<form:form id="inputForm" modelAttribute="demand" action="${ctx}/demand/demand/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>需求/主题名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>类型：</label></td>
					<td class="width-35">
						<sys:treeselect id="category" name="category.id" value="${demand.category.id}" labelName="category.name" labelValue="${demand.category.name}"
										title="类型" url="/column/classfy/columnClassfy/treeData" cssClass="form-control required" allowClear="true" notAllowSelectParent="false"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>需求详述：</label></td>
					<td class="width-35">
						<form:textarea path="description" htmlEscape="false" rows="4"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>联系方式：</label></td>
					<td class="width-35">
						<form:input path="phone" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>场地信息：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/demand/demand/selectworkplace" id="workplace" name="workplace.id"  value="${demand.workplace.id}"  title="选择场地信息" labelName="workplace.detail" 
						 labelValue="${demand.workplace.detail}" cssClass="form-control required" fieldLabels="详细地址|备注" fieldKeys="detail|remarks" searchLabel="详细地址" searchKey="detail" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>正式发布日期：</label></td>
					<td class="width-35">
						<input id="startDate" name="startDate" type="text" maxlength="20" class="laydate-icon form-control layer-date required"
							value="<fmt:formatDate value="${demand.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>截止日期：</label></td>
					<td class="width-35">
						<input id="stopDate" name="stopDate" type="text" maxlength="20" class="laydate-icon form-control layer-date required"
							value="<fmt:formatDate value="${demand.stopDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>状态：</label></td>
					<td class="width-35">
						<form:select path="status" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('demand_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">需求细节：</a>
                </li>
				<li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">档期：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane active">
			<a class="btn btn-white btn-sm" onclick="addRow('#demandDetailList', demandDetailRowIdx, demandDetailTpl);demandDetailRowIdx = demandDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>角色类型</th>
						<th>角色要求详述</th>
						<th>数量</th>
						<th>状态</th>
						<th>备注信息</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="demandDetailList">
				</tbody>
			</table>
			<script type="text/template" id="demandDetailTpl">//<!--
				<tr id="demandDetailList{{idx}}">
					<td class="hide">
						<input id="demandDetailList{{idx}}_id" name="demandDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="demandDetailList{{idx}}_delFlag" name="demandDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					<td class="width-35">
						<sys:treeselect id="demandDetailList{{idx}}_category" name="demandDetailList[{{idx}}].category.id" value="{{row.category.id}}" labelName="demandDetailList[{{idx}}].category.name" labelValue="{{row.category.name}}"
							title="类型" url="/column/classfy/columnClassfy/treeData" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
					</td>
					
					
					<td>
						<input id="demandDetailList{{idx}}_categoryDetail" name="demandDetailList[{{idx}}].categoryDetail" type="text" value="{{row.categoryDetail}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="demandDetailList{{idx}}_quantity" name="demandDetailList[{{idx}}].quantity" type="text" value="{{row.quantity}}"    class="form-control required digits"/>
					</td>


					
					
					<td>
						<select id="demandDetailList{{idx}}_status" name="demandDetailList[{{idx}}].status" data-value="{{row.status}}" class="form-control m-b  required">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('demand_detail')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<textarea id="demandDetailList{{idx}}_remarks" name="demandDetailList[{{idx}}].remarks" rows="4"    class="form-control ">{{row.remarks}}</textarea>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#demandDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var demandDetailRowIdx = 0, demandDetailTpl = $("#demandDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(demand.demandDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#demandDetailList', demandDetailRowIdx, demandDetailTpl, data[i]);
						demandDetailRowIdx = demandDetailRowIdx + 1;
					}
				});
			</script>
			</div>
				<div id="tab-2" class="tab-pane">
			<a class="btn btn-white btn-sm" onclick="addRow('#demandScheduleList', demandScheduleRowIdx, demandScheduleTpl);demandScheduleRowIdx = demandScheduleRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>开始时间</th>
						<th>结束时间</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="demandScheduleList">
				</tbody>
			</table>
			<script type="text/template" id="demandScheduleTpl">//<!--
				<tr id="demandScheduleList{{idx}}">
					<td class="hide">
						<input id="demandScheduleList{{idx}}_id" name="demandScheduleList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="demandScheduleList{{idx}}_delFlag" name="demandScheduleList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="demandScheduleList{{idx}}_startTime" name="demandScheduleList[{{idx}}].startTime" type="text" readonly="readonly" maxlength="20" class="laydate-icon form-control layer-date  required"
							value="{{row.startTime}}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					</td>
					
					
					<td>
						<input id="demandScheduleList{{idx}}_stopTime" name="demandScheduleList[{{idx}}].stopTime" type="text" readonly="readonly" maxlength="20" class="laydate-icon form-control layer-date  required"
							value="{{row.stopTime}}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#demandScheduleList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var demandScheduleRowIdx = 0, demandScheduleTpl = $("#demandScheduleTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(demand.demandScheduleList)};
					for (var i=0; i<data.length; i++){
						addRow('#demandScheduleList', demandScheduleRowIdx, demandScheduleTpl, data[i]);
						demandScheduleRowIdx = demandScheduleRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
	</form:form>
</body>
</html>