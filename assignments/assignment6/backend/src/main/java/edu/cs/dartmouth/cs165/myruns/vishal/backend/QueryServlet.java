package edu.cs.dartmouth.cs165.myruns.vishal.backend;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.cs.dartmouth.cs165.myruns.vishal.backend.data.ExerciseEntry;
import edu.cs.dartmouth.cs165.myruns.vishal.backend.data.ExerciseEntryDataStore;

public class QueryServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		String id = req.getParameter(ExerciseEntry.ExerciseEntryColumns._ID);
		ArrayList<ExerciseEntry> result = ExerciseEntryDataStore.query(id);
		req.setAttribute("result", result);
		getServletContext().getRequestDispatcher("/query_result.jsp").forward(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doGet(req, resp);
	}
}
