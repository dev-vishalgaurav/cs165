package edu.cs.dartmouth.cs165.myruns.vishal.backend;

import com.google.appengine.repackaged.com.google.api.client.json.JsonFactory;
import com.google.appengine.repackaged.com.google.api.client.json.JsonParser;
import com.google.appengine.repackaged.com.google.api.client.json.jackson.JacksonFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.cs.dartmouth.cs165.myruns.vishal.backend.data.ExerciseEntry;
import edu.cs.dartmouth.cs165.myruns.vishal.backend.data.ExerciseEntryDataStore;

public class PostDataServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String VALUES_JSON_DATA = "json_data";

    private List<ExerciseEntry> getData(HttpServletRequest req) throws ParseException{
        List<ExerciseEntry> results = new ArrayList<>();
        String data = req.getParameter(VALUES_JSON_DATA);
        if(data!=null && !data.equals("")){
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(data);
            JSONArray array = (JSONArray) jsonObject.get(VALUES_JSON_DATA);
            for(Object obj : array){
                JSONObject element = (JSONObject)obj;
                results.add(new ExerciseEntry(element));
            }
        }
        return  results;
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {


            try {
                String data = req.getParameter(VALUES_JSON_DATA);
                if (data == null || data.equals("")) {
                    resp.getWriter().write("Error");
                    //req.setAttribute("_retStr", "invalid input");
                    //getServletContext().getRequestDispatcher("/query_result.jsp").forward(req, resp);
                    return;
                }
                List<ExerciseEntry> entryList = getData(req);
                for (ExerciseEntry entry : entryList) {
                    ExerciseEntryDataStore.add(entry);
                }
                resp.getWriter().write("Success");
            }catch (ParseException ex) {
                //getServletContext().getRequestDispatcher("/query_result.jsp").forward(req, resp);
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                ExerciseEntryDataStore.mLogger.log(Level.ALL, ex.getMessage(), ex);
            }

    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
            doGet(req, resp);
    }

}
