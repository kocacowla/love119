package com.smu.love119.domain.weather.dto;

import com.smu.love119.domain.weather.dto.WeatherItem;

import java.util.List;

public class WeatherResponse {

    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    // 내부 Response 클래스 정의
    public static class Response {
        private Body body;

        public Body getBody() {
            return body;
        }

        public void setBody(Body body) {
            this.body = body;
        }
    }

    // 내부 Body 클래스 정의
    public static class Body {
        private Items items;

        public Items getItems() {
            return items;
        }

        public void setItems(Items items) {
            this.items = items;
        }
    }

    // 내부 Items 클래스 정의
    public static class Items {
        private List<WeatherItem> item;

        public List<WeatherItem> getItem() {
            return item;
        }

        public void setItem(List<WeatherItem> item) {
            this.item = item;
        }
    }
}
