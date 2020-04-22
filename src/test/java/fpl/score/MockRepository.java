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
 * Team 2 = footballers 1 & 2
 * Team 3 = footballer 3
 * Team 4 = footballer 6
 * Team 5 = footballer 4
 * Team 6 = footballer 7
 * Team 20 = footballer 5
 *
 * ---------
 * Fixtures
 * Team 2 plays 3,
 * Team 2 plays 6,
 * Team 20 plays 5,
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

        Footballer seven = new Footballer();
        seven.setId(7);
        seven.setPosition(7);
        footballers.add(seven);

        //Fixme: Logic copied from transient repo should be tested separately
        List<Element> elements = getElements();
        for (Element e : elements) {
            for (Footballer f : footballers) {
                if (e != null && e.getId() == f.getId()) {
                    f.setTeamId(e.getTeam());
                    f.setWebName(e.getWeb_name());
                }
            }
        }
        return footballers;
    }

    @Override
    public List<Element> getElements() {
        List<Element> elements = new ArrayList<>();
        elements.add(new Element(1, 2, "Element One"));
        elements.add(new Element(2, 2, "Element Two"));
        elements.add(new Element(3, 3, "Element Three"));
        elements.add(new Element(4, 5, "Element Four"));
        elements.add(new Element(5, 20, "Element Five"));
        elements.add(new Element(6, 4, "Element Six"));
        elements.add(new Element(7, 6, "Element Seven"));
        return elements;
    }

    @Override
    public List<Fixture> getFixtures(int gameWeek) {
        List<Fixture> fixtures = new ArrayList<>();
        fixtures.add(new Fixture(2, 5, 3, 3));
        fixtures.add(new Fixture(2, 1, 6, 2));
        fixtures.add(new Fixture(20, 2, 5, 4));
        return fixtures;
    }

    @Override
    public List<Team> getTeams() {
        List<Team> teams = new ArrayList<>();
        teams.add(new Team(2, "Two"));
        teams.add(new Team(3,"Three"));
        teams.add(new Team(4,"Four"));
        teams.add(new Team(5,"Five"));
        teams.add(new Team(6,"Six"));
        teams.add(new Team(20, "Twenty"));
        return teams;
    }

    @Override
    public int getGameWeek() {
        return 29;
    }
}
