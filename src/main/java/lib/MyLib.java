package lib;

import com.mysql.cj.util.StringUtils;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import javax.ws.rs.core.MediaType;
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


    public static Response echoSuccessMessage(String message) {
        return Response.ok((new JSONObject()
                .appendField("error", false)
                .appendField("message", message)).toJSONString(), MediaType.APPLICATION_JSON).build();
    }
    public static Response echoSuccessMessage(JSONObject message) {
        return Response.ok(message.toJSONString(), MediaType.APPLICATION_JSON).build();
    }
    public static Response echoSuccessMessage(JSONArray message) {
        return Response.ok(message.toJSONString(), MediaType.APPLICATION_JSON).build();
    }

    public static Response echoErrorMessage(String message) {
        return echoErrorMessage(message, Response.Status.BAD_REQUEST);
    }

    public static Response echoErrorMessage(String message, Response.Status httpCode) {
        return Response.status(httpCode)
                .entity((new JSONObject()
                        .appendField("error", true)
                        .appendField("message", message)).toJSONString())
                .build();
    }
}
