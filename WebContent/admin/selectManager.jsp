<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript">
	function dormManagerMove(dormManagerId, dormBuildId) {
		if(confirm("您确定要移除这个宿管吗？")) {
			window.location="dormBuild?action=move&dormBuildId="+dormBuildId+"&dormManagerId="+dormManagerId;
		}
	}
	
	$(document).ready(function(){
		$("ul li:eq(3)").addClass("active");
	});
</script>

<div class="data_list">
		<div class="data_list_title">
			宿舍管理员设置
		</div>
		<form class="select_form" name="myForm" method="post" action="dormBuild?action=addManager">
				<button class="btn btn-success" type="button"  onclick="javascript:history.back()">返回</button>
				<span class="data_select">
					<input type="hidden" id="dormBuildId" name="dormBuildId" value="${dormBuildId }"/>
					<select id="dormManagerId" name="dormManagerId" style="width: 150px;">
					<option value="">请选择管理员...</option>
					<c:forEach var="managerToSelect" items="${managerListToSelect }">
						<option value="${managerToSelect.dormManagerId }">${managerToSelect.name }</option>
					</c:forEach>
					</select>
					<button type="submit" class="btn btn-info" onkeydown="if(event.keyCode==13) myForm.submit()" style="margin-bottom: 10px">添加</button>
				</span>
		</form>
		<div>
			<table class="table table-hover table-striped table-bordered">
				<tr>
					<th>姓名</th>
					<th>性别</th>
					<th>电话</th>
					<th>用户名</th>
					<th>操作</th>
				</tr>
				<c:forEach  varStatus="i" var="managerWithId" items="${managerListWithId }">
					<tr>
						<td>${managerWithId.name }</td>
						<td>${managerWithId.sex }</td>
						<td>${managerWithId.tel }</td>
						<td>${managerWithId.userName }</td>
						<td><button class="btn btn-mini btn-danger" type="button" onclick="dormManagerMove(${managerWithId.dormManagerId},${managerWithId.dormBuildId})">移除</button></td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<div align="center"><font color="red">${error }</font></div>
		<div class="pagination pagination-centered">
			<ul>
				${pageCode }
			</ul>
		</div>
</div>