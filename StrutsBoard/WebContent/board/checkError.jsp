<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="s" uri="/struts-tags" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>비밀번호 오류</title>
<link type="text/css" rel="stylesheet" href="/StrutsBoard/board/common/css/css.css"/>
<script type="text/javascript">
function ErrorMessage(){
	alert("비밀번호가 틀립니다.");
	history.back(-1);
}
</script>
</head>
<body>
	<script>ErrorMessage()</script>
</body>
</html>