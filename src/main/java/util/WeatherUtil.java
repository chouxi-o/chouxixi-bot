package util;

import com.alibaba.fastjson.JSON;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import simbot.listener.MyNewGroupMemberListen;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherUtil {

    public static void main(String[] args) {
        WeatherUtil.weatherToday("杭州");
    }

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(MyNewGroupMemberListen.class);

    @Value("${api.way-jd}")
    private static String appKey = "4be956c729f4a72be7df27ef815cb6db";

    /**
     * 获取当天天气
     *
     * @return map status-状态;msg-执行信息;result-返回值
     */
    public static Map weatherToday(String city) {
        Map resultMap = new HashMap();

        String url = "https://way.jd.com/jisuapi/weather?city=" + city + "&appkey=" + appKey;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        //获取返回的json
        String r;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                r = response.body().string();
            } else {
                logger.info("weather,网络异常");
                resultMap.put("status", "fail");
                resultMap.put("msg", "网络异常");
                return resultMap;
            }
        } catch (IOException e) {
            logger.info("weather,IO异常");
            resultMap.put("status", "fail");
            resultMap.put("msg", "IO异常");
            return resultMap;
        } finally {
            response.close();
        }
        Map<String, Object> map = JSON.parseObject(r, Map.class);
        map = (Map) map.get("result");
        String status = "" + map.get("status");
        if (status.equals("0")) {

            String resultStart;
            String resultEnd;
            //String result1 = "今天";
            String result2 = "明天";
            //String result3 = "后天";

            //获取地址
            String basicStr = map.get("result").toString();
            Map basicMap = JSON.parseObject(basicStr, Map.class);

            //获取更新日期
            String updateDate = "" + basicMap.get("updatetime");

            resultStart = city;
            resultEnd = "更新时间" + updateDate;

            //获取空气质量状况
            Map aqi = (Map) basicMap.get("aqi");

            //获取实况天气


            String resultNow = "今天:\n" + basicMap.get("weather") + ",空气质量" + aqi.get("quality") + ",温度" + basicMap.get("temp") + "℃,最高温度"
                    + basicMap.get("temphigh") + "℃,最低温度" + basicMap.get("templow") + "℃,"
                    + basicMap.get("winddirect") + ",风力" + basicMap.get("windpower")+"\n";

            //获取7天数据
            List weatherData = (List) map.get("daily");

            /*Map weatherDataTemp;//当天数据,包括当天日期
            Map cond;//获取天气
            Map tmp;//获取最高温度和最低温度
            Map wind;//获取风向和风力*/


            /*//获取生活指数
            Map suggestion = (Map) map.get("suggestion");
            Map air = (Map) suggestion.get("air");
            Map comf = (Map) suggestion.get("comf");
            Map cw = (Map) suggestion.get("cw");
            Map drsg = (Map) suggestion.get("drsg");
            Map flu = (Map) suggestion.get("flu");
            Map sport = (Map) suggestion.get("sport");
            Map trav = (Map) suggestion.get("trav");
            Map uv = (Map) suggestion.get("uv");
            String resultSuggestion = "生活指数:\n"
//                    + "空气指数:" + air.get("brf") + "," + air.get("txt") + "\n"
//                    + "舒适度指数:" + comf.get("brf") + "," + comf.get("txt") + "\n"
                    + "洗车指数:" + cw.get("brf") + "," + cw.get("txt") + "\n"
                    + "穿衣指数:" + drsg.get("brf") + "," + drsg.get("txt") + "\n"
//                    + "感冒指数:" + flu.get("brf") + "," + flu.get("txt") + "\n"
//                    + "运动指数:" + sport.get("brf") + "," + sport.get("txt") + "\n"
//                    + "旅游指数:" + trav.get("brf") + "," + trav.get("txt") + "\n"
                    + "紫外线指数:" + uv.get("brf") + "," + uv.get("txt") + "\n";*/

            String result = resultStart + "天气情况:\n" + resultNow  + resultEnd;

            logger.info("weather,success");
            resultMap.put("status", "success");
            resultMap.put("msg", "success");
            resultMap.put("result", result);
            return resultMap;
        } else {
            logger.info("weather,不存在的地区");
            resultMap.put("status", "fail");
            resultMap.put("msg", "不存在的地区");
            resultMap.put("result", "不存在的地区");
            return resultMap;
        }
    }

}
