package fpl.url;

public enum FantasyEndpoint {

    PICKS_PREFIX("https://fantasy.premierleague.com/api/entry/2029893/event/"),
    PICKS_SUFFIX("/picks/"),
    FIXTURES("https://fantasy.premierleague.com/api/fixtures/?event="),
    STATIC("https://fantasy.premierleague.com/api/bootstrap-static/");

    public final String url;

    FantasyEndpoint(String url) {
        this.url = url;
    }
}