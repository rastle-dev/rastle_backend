package rastle.dev.rastle_backend.global.util;

public class KeyUtil {
    public static String toRedisKey(String username, String agent, String ip) {
        return username + "_" + agent + "_" + ip;
    }

}
