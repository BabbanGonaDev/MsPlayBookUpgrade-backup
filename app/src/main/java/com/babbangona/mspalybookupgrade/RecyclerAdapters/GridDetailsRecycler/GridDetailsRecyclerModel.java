package com.babbangona.mspalybookupgrade.RecyclerAdapters.GridDetailsRecycler;

import java.util.ArrayList;
import java.util.List;

public class GridDetailsRecyclerModel {

    private double max_lat;

    private double min_lat;

    private double max_lng;

    private double min_lng;

    public GridDetailsRecyclerModel() {
    }

    public GridDetailsRecyclerModel(double max_lat, double min_lat, double max_lng, double min_lng) {
        this.max_lat = max_lat;
        this.min_lat = min_lat;
        this.max_lng = max_lng;
        this.min_lng = min_lng;
    }

    public double getMax_lat() {
        return max_lat;
    }

    public void setMax_lat(double max_lat) {
        this.max_lat = max_lat;
    }

    public double getMin_lat() {
        return min_lat;
    }

    public void setMin_lat(double min_lat) {
        this.min_lat = min_lat;
    }

    public double getMax_lng() {
        return max_lng;
    }

    public void setMax_lng(double max_lng) {
        this.max_lng = max_lng;
    }

    public double getMin_lng() {
        return min_lng;
    }

    public void setMin_lng(double min_lng) {
        this.min_lng = min_lng;
    }

    public List<FirstGridModel> getAlmostVerticalGridModel(InitialGridDetailsModel initialGridDetailsModel){
        List<FirstGridModel> firstGridModelList = new ArrayList<>();
        double max_lat = initialGridDetailsModel.getMax_lat();
        double lat_interval = (initialGridDetailsModel.getMax_lat() - initialGridDetailsModel.getMin_lat())/4.9;
        double lng_interval = (initialGridDetailsModel.getMax_lng() - initialGridDetailsModel.getMin_lng())/4.9;
        double difference = (int)(Math.round((initialGridDetailsModel.getMax_lat() - initialGridDetailsModel.getMin_lat()) / lat_interval));
        difference = Math.max(difference, 1);
        for(int i = 0; i < difference; i++) {
            double min_lat = max_lat-lat_interval;
            if (i == 0){
                max_lat = max_lat + 0.1;
            }
            if (i == (difference-1)){
                min_lat = min_lat - 0.1;
            }
            firstGridModelList.add(new FirstGridModel(max_lat, min_lat, initialGridDetailsModel.getMax_lng(),
                    initialGridDetailsModel.getMin_lng(),lng_interval));
            max_lat = min_lat;
        }
        return firstGridModelList;
    }

    public static class FirstGridModel{

        private double max_lat;

        private double min_lat;

        private double max_lng;

        private double min_lng;

        private double lng_interval;

        public FirstGridModel(double max_lat, double min_lat, double max_lng, double min_lng,
                              double lng_interval) {
            this.max_lat = max_lat;
            this.min_lat = min_lat;
            this.max_lng = max_lng;
            this.min_lng = min_lng;
            this.lng_interval = lng_interval;
        }

        public FirstGridModel(double max_lat, double min_lat, double max_lng, double min_lng) {
            this.max_lat = max_lat;
            this.min_lat = min_lat;
            this.max_lng = max_lng;
            this.min_lng = min_lng;
        }

        public double getMax_lat() {
            return max_lat;
        }

        public void setMax_lat(double max_lat) {
            this.max_lat = max_lat;
        }

        public double getMin_lat() {
            return min_lat;
        }

        public void setMin_lat(double min_lat) {
            this.min_lat = min_lat;
        }

        public double getMax_lng() {
            return max_lng;
        }

        public void setMax_lng(double max_lng) {
            this.max_lng = max_lng;
        }

        public double getMin_lng() {
            return min_lng;
        }

        public void setMin_lng(double min_lng) {
            this.min_lng = min_lng;
        }

        public double getLng_interval() {
            return lng_interval;
        }

        public void setLng_interval(double lng_interval) {
            this.lng_interval = lng_interval;
        }
    }

    public static class InitialGridDetailsModel{

        private double max_lat;

        private double min_lat;

        private double max_lng;

        private double min_lng;

        public InitialGridDetailsModel(double max_lat, double min_lat, double max_lng, double min_lng) {
            this.max_lat = max_lat;
            this.min_lat = min_lat;
            this.max_lng = max_lng;
            this.min_lng = min_lng;
        }

        public double getMax_lat() {
            return max_lat;
        }

        public void setMax_lat(double max_lat) {
            this.max_lat = max_lat;
        }

        public double getMin_lat() {
            return min_lat;
        }

        public void setMin_lat(double min_lat) {
            this.min_lat = min_lat;
        }

        public double getMax_lng() {
            return max_lng;
        }

        public void setMax_lng(double max_lng) {
            this.max_lng = max_lng;
        }

        public double getMin_lng() {
            return min_lng;
        }

        public void setMin_lng(double min_lng) {
            this.min_lng = min_lng;
        }
    }

}
