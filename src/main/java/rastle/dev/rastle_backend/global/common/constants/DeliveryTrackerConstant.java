package rastle.dev.rastle_backend.global.common.constants;

public class DeliveryTrackerConstant {

    public static final String BASE_URL = "https://apis.tracker.delivery";
    public static final String TOKEN_URL = "https://auth.tracker.delivery/oauth2/token";
    public static final String GRANT_TYPE = "client_credentials";
    public static final String GRAPH_QL = "/graphql";
    public static final String CALLBACK_URL = "https://api.recordyslow.com/delivery/webhook";
    public static final String CARRIER_ID = "kr.cjlogistics";
    public static final String REGISTER_WEB_HOOK_QUERY = "mutation RegisterTrackWebhook(\n  $input: RegisterTrackWebhookInput!\n) {\n  registerTrackWebhook(input: $input)\n}";
    public static final String SEARCH_TRACKING_NUMBER_QUERY = "query Track(\n  $carrierId: ID!,\n  $trackingNumber: String!\n) {\n  track(\n    carrierId: $carrierId,\n    trackingNumber: $trackingNumber\n  ) {\n    lastEvent {\n      time\n      status {\n        code\n      }\n    }\n  }\n}";
}
