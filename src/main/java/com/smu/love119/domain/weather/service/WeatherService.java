package com.smu.love119.domain.weather.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;

    public WeatherService() {
        this.restTemplate = new RestTemplate();
    }

    public String getWeatherInfo() {
        String baseDate = getBaseDate();  // 가장 적절한 날짜 가져오기
        String baseTime = getBaseTime();  // 가장 최신 예보 시간 가져오기

        String apiUrl = String.format(
                "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=kLMkCvGNBkIz1GiBsNGu26Qrt5%%2BZxubCAemeFey1y6FD7oENDOscHQyPx2wGKoKz%%2BjX6tjUDUT4C2cnQQxhScA%%3D%%3D&pageNo=1&numOfRows=1000&dataType=XML&base_date=%s&base_time=%s&nx=55&ny=127",
                baseDate, baseTime
        );

        try {
            URI uri = new URI(apiUrl);
            String responseXml = restTemplate.getForObject(uri, String.class);

            System.out.println("API 응답: " + responseXml);

            JSONObject responseJson = XML.toJSONObject(responseXml);

            // API 오류 응답 처리
            if (responseJson.has("OpenAPI_ServiceResponse")) {
                JSONObject serviceResponse = responseJson.getJSONObject("OpenAPI_ServiceResponse");
                if (serviceResponse.has("cmmMsgHeader")) {
                    String errMsg = serviceResponse.getJSONObject("cmmMsgHeader").getString("errMsg");
                    System.out.println("API 에러: " + errMsg);
                    return "API 에러 발생: " + errMsg;
                }
            }

            // 정상 응답 처리
            JSONObject response = responseJson.getJSONObject("response");
            JSONObject body = response.getJSONObject("body");
            JSONArray items = body.getJSONObject("items").getJSONArray("item");

            List<String> weatherDetails = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String category = item.getString("category");
                String fcstValue = item.get("fcstValue").toString();

                switch (category) {
                    case "TMP":
                        weatherDetails.add("온도: " + fcstValue + "°C");
                        break;
                    case "SKY":
                        String skyCondition = switch (fcstValue) {
                            case "1" -> "맑음";
                            case "3" -> "구름많음";
                            case "4" -> "흐림";
                            default -> "알 수 없음";
                        };
                        weatherDetails.add("하늘 상태: " + skyCondition);
                        break;
                    case "PCP":
                        String precipitation = "강수 없음".equals(fcstValue) ? "강수 없음" : "강수량: " + fcstValue;
                        weatherDetails.add(precipitation);
                        break;
                    case "REH":
                        weatherDetails.add("습도: " + fcstValue + "%");
                        break;
                    case "SNO":
                        String snow = "적설 없음".equals(fcstValue) ? "적설 없음" : "적설량: " + fcstValue;
                        weatherDetails.add(snow);
                        break;
                }
            }

            return String.join(", ", weatherDetails);

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return "URL 구문 오류가 발생했습니다.";
        } catch (Exception e) {
            System.err.println("예기치 못한 오류: " + e.getMessage());
            return "데이터 처리 중 오류가 발생했습니다.";
        }
    }

    private String getBaseDate() {
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();

        if (now.isBefore(LocalTime.of(5, 0))) {
            return today.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        } else {
            return today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
    }

    private String getBaseTime() {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();

        if (hour < 2) return "2300";
        else if (hour < 5) return "0200";
        else if (hour < 8) return "0500";
        else if (hour < 11) return "0800";
        else if (hour < 14) return "1100";
        else if (hour < 17) return "1400";
        else if (hour < 20) return "1700";
        else if (hour < 23) return "2000";
        else return "2300";
    }
}
