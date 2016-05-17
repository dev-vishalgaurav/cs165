<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.cs.dartmouth.cs165.myruns.vishal.backend.data.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Query Result</title>
</head>
<body>
	<%
		String retStr = (String) request.getAttribute("_retStr");
		if (retStr != null) {
	%>
	<%=retStr%><br>
	<%
		}
	%>
	<center>
		<b>Query Result</b>
		<form name="input" action="/query.do" method="get">
			Name: <input type="text" name="_id"> <input type="submit" value="OK">
		</form>
	</center>
	<b>
		---------------------------------------------------------------------<br>
		<%
			ArrayList<ExerciseEntry> resultList = (ArrayList<ExerciseEntry>) request.getAttribute("result");
			if (resultList != null) {
				for (ExerciseEntry entry : resultList) {
		%> Name:<%=entry.id%>&nbsp; Address:<%=entry.mInputType%>&nbsp;
		PhoneNumber:<%=entry.mActivityType%>&nbsp; &nbsp;&nbsp; <a
		href="/delete.do?name=<%=entry.id%>">delete</a> <br> <%
 	}
 	}
 %>
		---------------------------------------------------------------------<br>
	</b> Add new entry:
	<br>
	<form name="input" action="/add.do" method="post">
		ID: <input type="text" name="_id"> InputType: <input
			type="text" name="input_type"> ActivityType: <input type="text"
			name="activity_type"> <input type="submit" value="Add">
	</form>
	---------------------------------------------------------------------
	<br>
	<form name="input" action="/update.do" method="post">
		Id: <input type="text" name="_id">
		InputType: <input type="text" name="input_type">
		ActivityType: <input type="text" name="activity_type">
		<input type="submit" value="Update">
	</form>
</body>
</html>