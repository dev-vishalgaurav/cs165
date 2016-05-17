package edu.cs.dartmouth.cs165.myruns.vishal.backend;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.cs.dartmouth.cs165.myruns.vishal.backend.data.ExerciseEntry;
import edu.cs.dartmouth.cs165.myruns.vishal.backend.data.ExerciseEntryDataStore;

public class UpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        /**
         public String id;
         public int mInputType;        // Manual, GPS or automatic
         public int mActivityType;     // Running, cycling etc.
         public long mDateTime;    // When does this entry happen
         public int mDuration;         // Exercise duration in seconds
         public double mDistance;      // Distance traveled. Either in meters or feet.
         public double mAvgPace;       // Average pace
         public double mAvgSpeed;      // Average speed
         public int mCalorie;          // Calories burnt
         public double mClimb;         // Climb. Either in meters or feet.
         public int mHeartRate;        // Heart rate
         public String mComment;       // Comments
         */
        try {
            String id = req.getParameter(ExerciseEntry.ExerciseEntryColumns._ID);
            int inputType = Integer.parseInt(req.getParameter(ExerciseEntry.ExerciseEntryColumns.INPUT_TYPE));
            int activityType = Integer.parseInt(req.getParameter(ExerciseEntry.ExerciseEntryColumns.ACTIVITY_TYPE));
            long dateTime = Long.parseLong(req.getParameter(ExerciseEntry.ExerciseEntryColumns.DATE_TIME));
            int duration = Integer.parseInt(req.getParameter(ExerciseEntry.ExerciseEntryColumns.DURATION));
            double distance = Double.parseDouble(req.getParameter(ExerciseEntry.ExerciseEntryColumns.DISTANCE));
            double avgPace = Double.parseDouble(req.getParameter(ExerciseEntry.ExerciseEntryColumns.AVG_PACE));
            double avgSpeed = Double.parseDouble(req.getParameter(ExerciseEntry.ExerciseEntryColumns.AVG_SPEED));
            int calorie = Integer.parseInt(req.getParameter(ExerciseEntry.ExerciseEntryColumns.CALORIE));
            double climb = Double.parseDouble(req.getParameter(ExerciseEntry.ExerciseEntryColumns.CLIMB));
            int heartRate = Integer.parseInt(req.getParameter(ExerciseEntry.ExerciseEntryColumns.HEART_RATE));
            String comment = req.getParameter(ExerciseEntry.ExerciseEntryColumns.COMMENT);
            if (id != null && !id.equals("")) {
                ExerciseEntry entry = new ExerciseEntry(id,inputType,activityType,dateTime,duration,distance,avgPace,avgSpeed,calorie,climb,heartRate,comment);
                ExerciseEntryDataStore.update(entry);
            }
            resp.sendRedirect("/query.do");
        } catch (Exception ex) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            ExerciseEntryDataStore.mLogger.log(Level.ALL, ex.getMessage(), ex);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        doGet(req, resp);
    }

}
