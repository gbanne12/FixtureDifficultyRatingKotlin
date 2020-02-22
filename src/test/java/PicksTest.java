import org.junit.Test;

public class PicksTest {

    String json = "{\"active_chip\":null,\"automatic_subs\":[],\"entry_history\":" +
            "{\"event\":2,\"points\":75,\"total_points\":111,\"rank\":26969,\"rank_sort\":27244,\"overall_rank\":2190922," +
            "\"bank\":0,\"value\":1002,\"event_transfers\":2,\"event_transfers_cost\":4,\"points_on_bench\":6},\"" +
            "pick\":[{\"element\":111,\"position\":1,\"multiplier\":1,\"is_captain\":false,\"is_vice_captain\":true}," +
            "{\"element\":122,\"position\":2,\"multiplier\":1,\"is_captain\":false,\"is_vice_captain\":false}," +
            "{\"element\":141,\"position\":3,\"multiplier\":1,\"is_captain\":false,\"is_vice_captain\":false}," +
            "{\"element\":181,\"position\":4,\"multiplier\":1,\"is_captain\":false,\"is_vice_captain\":false}," +
            "{\"element\":239,\"position\":5,\"multiplier\":1,\"is_captain\":false,\"is_vice_captain\":false}," +
            "{\"element\":431,\"position\":6,\"multiplier\":1,\"is_captain\":false,\"is_vice_captain\":false}," +
            "{\"element\":218,\"position\":7,\"multiplier\":1,\"is_captain\":false,\"is_vice_captain\":false}," +
            "{\"element\":192,\"position\":8,\"multiplier\":1,\"is_captain\":false,\"is_vice_captain\":false}," +
            "{\"element\":278,\"position\":9,\"multiplier\":1,\"is_captain\":false,\"is_vice_captain\":false}," +
            "{\"element\":409,\"position\":10,\"multiplier\":1,\"is_captain\":false,\"is_vice_captain\":false}," +
            "{\"element\":187,\"position\":11,\"multiplier\":2,\"is_captain\":true,\"is_vice_captain\":false}," +
            "{\"element\":48,\"position\":12,\"multiplier\":0,\"is_captain\":false,\"is_vice_captain\":false}," +
            "{\"element\":160,\"position\":13,\"multiplier\":0,\"is_captain\":false,\"is_vice_captain\":false}," +
            "{\"element\":420,\"position\":14,\"multiplier\":0,\"is_captain\":false,\"is_vice_captain\":false}," +
            "{\"element\":128,\"position\":15,\"multiplier\":0,\"is_captain\":false,\"is_vice_captain\":false}]}";

    @Test
    public void canGetElementIdForCurrentSelctions() {
      /*  JSONArray jsonArray = data.getJSONArray("pick");
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Pick> jsonAdapter = moshi.adapter(Pick.class);*/
    }


}
