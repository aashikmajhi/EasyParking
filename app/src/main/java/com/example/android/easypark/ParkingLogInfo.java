package com.example.android.easypark;

public class ParkingLogInfo
{
    String status;
    String time;
    String place;
    String cost;

    public ParkingLogInfo()
    {}

    public ParkingLogInfo(String status, String time, String place, String cost) {
        this.status = status;
        this.time = time;
        this.place = place;
        this.cost = cost;
    }

    public static String calculateCost(String inTime, String outTime){

            String[] entryTime = inTime.split(":");
            String[] exitTime = outTime.split(":");

            int seconds;
            int minutes;
            int hours;
            double parkingFare;

            if(Integer.parseInt(exitTime[2].trim()) < Integer.parseInt(entryTime[2].trim())){
                seconds = (Integer.parseInt(exitTime[0].trim())-Integer.parseInt(entryTime[0].trim()))%60;

                if(Integer.parseInt(exitTime[1].trim()) < Integer.parseInt(entryTime[1].trim())){
                    minutes = (Integer.parseInt(exitTime[1].trim())-Integer.parseInt(entryTime[1].trim()))%60 - 1;

                    if(Integer.parseInt(exitTime[0].trim()) < Integer.parseInt(entryTime[0].trim())){
                        hours = (Integer.parseInt(exitTime[0].trim())-Integer.parseInt(entryTime[0].trim()))%24 - 1;
                    }
                    else
                    {
                        hours = Integer.parseInt(exitTime[0].trim())-Integer.parseInt(entryTime[0].trim()) - 1;
                    }
                }
                else{
                    minutes = Integer.parseInt(exitTime[1].trim())-Integer.parseInt(entryTime[1].trim()) - 1;

                    if(Integer.parseInt(exitTime[0].trim()) < Integer.parseInt(entryTime[0].trim())){
                        hours = (Integer.parseInt(exitTime[0].trim())-Integer.parseInt(entryTime[0].trim()))%24 ;
                    }
                    else
                    {
                        hours = Integer.parseInt(exitTime[0].trim())-Integer.parseInt(entryTime[0].trim());
                    }
                }
            }
            else {
                seconds = Integer.parseInt(exitTime[0].trim())-Integer.parseInt(entryTime[0].trim());

                if(Integer.parseInt(exitTime[1].trim()) < Integer.parseInt(entryTime[1].trim())){
                    minutes = (Integer.parseInt(exitTime[1].trim())-Integer.parseInt(entryTime[1].trim()))%60 ;

                    if(Integer.parseInt(exitTime[0].trim()) < Integer.parseInt(entryTime[0].trim())){
                        hours = (Integer.parseInt(exitTime[0].trim())-Integer.parseInt(entryTime[0].trim()))%24 - 1;
                    }
                    else
                    {
                        hours = Integer.parseInt(exitTime[0].trim())-Integer.parseInt(entryTime[0].trim()) - 1;
                    }
                }
                else{
                    minutes = Integer.parseInt(exitTime[1].trim())-Integer.parseInt(entryTime[1].trim()) ;

                    if(Integer.parseInt(exitTime[0].trim()) < Integer.parseInt(entryTime[0].trim())){
                        hours = (Integer.parseInt(exitTime[0].trim())-Integer.parseInt(entryTime[0].trim()))%24 ;
                    }
                    else
                    {
                        hours = Integer.parseInt(exitTime[0].trim())-Integer.parseInt(entryTime[0].trim());
                    }
                }
            }

            parkingFare = hours*18*60 + minutes*18 + seconds*(0.3);

            return Double.toString(parkingFare);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
