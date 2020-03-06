package data;

import data.model.Element;
import data.model.Fixture;
import data.model.Footballer;
import data.model.Team;
import exception.NoFplResponseException;

import java.io.IOException;
import java.util.List;

public interface Repository {

    List<Footballer> getFootballers(int managerId, int gameWeek) throws IOException;

    List<Element> getElements() throws NoFplResponseException;

    List<Fixture> getFixtures(int gameWeek) throws NoFplResponseException;

    List<Team> getTeams() throws NoFplResponseException;

    int getGameWeek() throws NoFplResponseException;
}
