package com.example.carson.yjenglish.discover.model;

import java.util.List;

/**
 * Created by 84594 on 2018/10/3.
 */

public class DiscoverInfo {
    private String status;
    private String msg;
    private DiscoverItem data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DiscoverItem getData() {
        return data;
    }

    public void setData(DiscoverItem data) {
        this.data = data;
    }

    public class DiscoverItem {
        private List<DailyCard> daily_pic;
        //福利社
        private List<WelfareService> welfare_service;

        public List<DailyCard> getDaily_pic() {
            return daily_pic;
        }

        public void setDaily_pic(List<DailyCard> daily_pic) {
            this.daily_pic = daily_pic;
        }

        public List<WelfareService> getWelfare_service() {
            return welfare_service;
        }

        public void setWelfare_service(List<WelfareService> welfare_service) {
            this.welfare_service = welfare_service;
        }

        public class DailyCard {
            private String is_favour;
            private String daily_pic;
            private String id;

            public String getIs_favour() {
                return is_favour;
            }

            public void setIs_favour(String is_favour) {
                this.is_favour = is_favour;
            }

            public String getDaily_pic() {
                return daily_pic;
            }

            public void setDaily_pic(String daily_pic) {
                this.daily_pic = daily_pic;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        public class WelfareService {
            private String st;
            private String et;
            private String id;
            private String url;
            private String pic;

            public String getSt() {
                return st;
            }

            public void setSt(String st) {
                this.st = st;
            }

            public String getEt() {
                return et;
            }

            public void setEt(String et) {
                this.et = et;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }
        }
    }
}
