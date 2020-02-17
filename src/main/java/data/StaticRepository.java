package data;

import data.model.EventDao;

import java.io.IOException;

/**
 * Repo reflecting the static FPL endpoint made up of Elements, Event and Teams
 */
public class StaticRepository {

    EventDao eventDao = new EventDao();

    /**
     * Get the currently active game week number
     * Will return the first unfinished game week, so an in-progress but unfinished week will be returned
     *
     * @return number of the first unfinished game week
     * @throws IOException
     */
    public int getCurrentWeek() throws IOException {
        int week;
        if (eventDao.getWeek() == null) {
            week = eventDao.getCurrentWeek();
        } else {
            week = eventDao.getWeek();
        }
        return week;
    }
}
