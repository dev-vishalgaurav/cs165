<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.cs.dartmouth.cs165.myruns.vishal.backend.data.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Query Result</title>
<style>
table {
    width:100%;
}
table, th, td {
    border: 1px solid black;
    border-collapse: collapse;
}
th, td {
    padding: 5px;
    text-align: left;
}
table#t01 tr:nth-child(even) {
    background-color: #eee;
}
table#t01 tr:nth-child(odd) {
   background-color:#fff;
}
table#t01 th {
    background-color: black;
    color: white;
}
</style>
</head>
<body>
<table>
          <tr>
            <th>ID</th>
            <th>InputType</th>
            <th>ActivityType</th>
            <th>Date</th>
            <th>Duration</th>
            <th>Distance</th>
            <th>Avg Pace</th>
            <th>Avg Speed</th>
            <th>Calorie</th>
            <th>Climb</th>
            <th>Heart Rate</th>
            <th>Comment</th>
            <th>Action Delete</th>
          </tr>
	<%
		String retStr = (String) request.getAttribute("_retStr");
		if (retStr != null) {
	%>
	<%=retStr%><br>
	<%
		}
	%>
	<center>
		<b>My Runs 6 Exercise Entries</b>
	</center>
	<b>
		<%
			ArrayList<ExerciseEntry> resultList = (ArrayList<ExerciseEntry>) request.getAttribute("result");
			if (resultList != null) {
				for (ExerciseEntry entry : resultList) {
				         %> <tr><%
		%> <th><%=entry.id%></th>
		<th><%=entry.getInputType()%></th>
		<th><%=entry.getActivityType()%></th>
		<th><%=new Date(entry.mDateTime).toString()%></th>
		<th><%=entry.mDuration + " secs"%></th>
		<th><%=entry.mDistance + " miles"%></th>
		<th><%=entry.mAvgPace + " miles/h"%></th>
		<th><%=entry.mAvgSpeed + " miles/h" %></th>
		<th><%=entry.mCalorie + " cals"%></th>
		<th><%=entry.mClimb + " miles"%></th>
		<th><%=entry.mHeartRate + " bpm"%></th>
		<th><%=entry.mComment %></th>
		<th><a href="/delete.do?<%=ExerciseEntry.ExerciseEntryColumns._ID%>=<%=entry.id%>">delete</a></th> <%
 	}
 	}
 %></tr>

</body>
</html>