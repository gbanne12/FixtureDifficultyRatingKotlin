package data.model;

import java.io.IOException;
import java.util.List;

public interface FootballerDao {

    List<Footballer> getAll(int managerId, int gameWeek) throws IOException;

    void update(List<Footballer> footballers, int id, Opponent opponent);
}
