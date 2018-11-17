package lib;

import com.mysql.cj.util.StringUtils;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import javax.ws.rs.core.Response;

/**
 * Created by Long
 * Date: 11/15/2018
 * Time: 7:34 PM
 */
public class MyLib {

    /**
     * start: [0 - ~]
     * limit: [1 - 100]
     */
    public static String isValidPaginationInput(String start, String limit) {
        if (!StringUtils.isStrictlyNumeric(start)) return "Invalid start param";
        else if (Integer.valueOf(start) < 0) return "Invalid start param (min value is 0)";

        if (!StringUtils.isStrictlyNumeric(limit)) return "Invalid limit param";
        else if (Integer.valueOf(limit) < 1)
            return "Invalid limit param (min value is 1)";
        else if (Integer.valueOf(limit) > 100)
            return "Invalid limit param (max value is 100)";

        return "OK";
    }

    public static Response echoErrorMessage(String message) {
        return echoMessage(message, Response.Status.BAD_REQUEST);
    }

    public static Response echoErrorMessage(String message, Response.Status httpCode) {
        return echoMessage(message, httpCode);
    }

    public static Response echoSuccessMessage(String message) {
        return echoMessage(message, Response.Status.OK);
    }

    public static Response echoSuccessMessage(JSONArray result) {
        return echoMessage(Response.Status.OK.toString(), result, Response.Status.OK);
    }

    public static Response echoSuccessMessage(JSONObject result) {
        return echoMessage(Response.Status.OK.toString(), result, Response.Status.OK);
    }

    public static Response echoMessage(String message, Response.Status httpCode) {
        return Response.status(httpCode)
                .entity((new JSONObject()
                        .appendField("status", httpCode.getStatusCode())
                        .appendField("message", message)).toJSONString())
                .build();
    }

    public static Response echoMessage(String message, JSONObject result, Response.Status httpCode) {
        return Response.status(httpCode)
                .entity((new JSONObject()
                        .appendField("status", httpCode.getStatusCode())
                        .appendField("message", message)
                        .appendField("result", result)).toJSONString())
                .build();
    }

    public static Response echoMessage(String message, JSONArray result, Response.Status httpCode) {
        return Response.status(httpCode)
                .entity((new JSONObject()
                        .appendField("status", httpCode.getStatusCode())
                        .appendField("message", message)
                        .appendField("result", result)).toJSONString())
                .build();
    }
}
