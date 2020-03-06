package fpl.score;

import data.Repository;
import data.model.Element;
import data.model.Fixture;
import data.model.Footballer;
import data.model.Team;
import exception.NoFplResponseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Set up test data for 6 footballers.
 * Footballers in team 2: 1 & 2
 * Footballers in team 3: 3
 * Footballers in team 4: 6
 * Footballers in team 5: 4
 * Footballers in team 20: 5
 * <p>
 * Fixtures 2 plays 3, 20 plays 5
 * Team 4 does not play.
 */
public class MockRepository implements Repository {

    @Override
    public List<Footballer> getFootballers(int managerId, int gameWeek) throws NoFplResponseException {
        List<Footballer> footballers = new ArrayList<>();
        Footballer one = new Footballer();
        one.setId(1);
        one.setPosition(1);
        footballers.add(one);

        Footballer two = new Footballer();
        two.setId(2);
        two.setPosition(2);
        footballers.add(two);

        Footballer three = new Footballer();
        three.setId(3);
        three.setPosition(3);
        footballers.add(three);

        Footballer four = new Footballer();
        four.setId(4);
        four.setPosition(4);
        footballers.add(four);

        Footballer five = new Footballer();
        five.setId(5);
        five.setPosition(5);
        footballers.add(five);

        Footballer six = new Footballer();
        six.setId(6);
        six.setPosition(6);
        footballers.add(six);

        List<Element> elements = getElements();
        for (Element e : elements) {
            for (Footballer f : footballers) {
                if (e != null && e.id == f.getId()) {
                    f.setTeamId(e.team);
                    f.setWebName(e.web_name);
                }
            }
        }
        return footballers;
    }

    @Override
    public List<Element> getElements() throws NoFplResponseException {
        List<Element> elements = new ArrayList<>();
        elements.add(new Element(1, 2, "Element One"));
        elements.add(new Element(2, 2, "Element Two"));
        elements.add(new Element(3, 3, "Element Three"));
        elements.add(new Element(4, 5, "Element Four"));
        elements.add(new Element(6, 4, "Element Six"));
        elements.add(new Element(5, 20, "Element Twenty"));
        return elements;
    }

    @Override
    public List<Fixture> getFixtures(int gameWeek) {
        List<Fixture> fixtures = new ArrayList<>();
        fixtures.add(new Fixture(2, 5, 3, 3));
        fixtures.add(new Fixture(20, 2, 5, 4));
        return fixtures;
    }

    @Override
    public List<Team> getTeams() {
        List<Team> teams = new ArrayList<>();
        teams.add(new Team(2, "TWO", "Two"));
        teams.add(new Team(3, "THR", "Three"));
        teams.add(new Team(4, "FOU", "Four"));
        teams.add(new Team(5, "FIV", "Five"));
        teams.add(new Team(20, "TWE", "Twenty"));
        return teams;
    }

    @Override
    public int getGameWeek() {
        return 29;
    }
}
