<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>

<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>会员登录</title>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<!-- 引入自定义css文件 style.css -->
<link rel="stylesheet" href="css/style.css" type="text/css" />

<style>
body {
	margin-top: 20px;
	margin: 0 auto;
}

.carousel-inner .item img {
	width: 100%;
	height: 300px;
}
</style>
</head>

<body>


	<!-- 引入header.jsp -->
	<jsp:include page="/header.jsp"></jsp:include>

	<div class="container">
		<div class="row">
			<div style="margin: 0 auto; margin-top: 10px; width: 950px;">
				<strong>我的订单</strong>
				<table class="table table-bordered">
					<c:forEach items="${orderList}" var="order">
						<tbody>
						<tr class="success">
							<th colspan="5">订单编号:${order.oid}&nbsp;&nbsp;${order.state==0?"未付款":"已付款"}</th>
						</tr>
						<tr class="warning">
							<th>图片</th>
							<th>商品</th>
							<th>价格</th>
							<th>数量</th>
							<th>小计</th>
						</tr>
						<c:forEach items="${order.orderItems}" var="orderItem">
							<tr class="active">
								<td width="60" width="40%"><input type="hidden" name="id"
																  value="22"> <img src="${orderItem.product.pimage}" width="70"
																				   height="60"></td>
								<td width="30%"><a target="_blank">${orderItem.product.pname}</a></td>
								<td width="20%">￥${orderItem.product.shop_price}</td>
								<td width="10%">${orderItem.count}</td>
								<td width="15%"><span class="subtotal">￥${orderItem.subtotal}</span></td>
							</tr>
						</c:forEach>
						</tbody>
					</c:forEach>
				</table>
			</div>
		</div>
	</div>

	<!-- 引入footer.jsp -->
	<jsp:include page="/footer.jsp"></jsp:include>
	
</body>

</html>