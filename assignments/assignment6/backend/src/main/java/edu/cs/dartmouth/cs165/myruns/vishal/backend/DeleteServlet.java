package edu.cs.dartmouth.cs165.myruns.vishal.backend;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.cs.dartmouth.cs165.myruns.vishal.backend.data.ExerciseEntry;
import edu.cs.dartmouth.cs165.myruns.vishal.backend.data.ExerciseEntryDataStore;

public class DeleteServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String name = req.getParameter(ExerciseEntry.ExerciseEntryColumns._ID);
		ExerciseEntryDataStore.delete(name);
		MessagingEndpoint.sendDeleteBroadcast(name);
		resp.sendRedirect("/query.do");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		doGet(req, resp);
	}
}
