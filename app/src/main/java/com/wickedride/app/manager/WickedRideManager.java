package com.wickedride.app.manager;

import android.content.Context;

import com.wickedride.app.models.all_bike_models.Datum;
import com.wickedride.app.utils.Constants;

import java.util.List;

/**
 * Created by Madhumita on 14-05-2015.
 */
public class WickedRideManager {
    public static String HostName = "52.10.33.225";
    public static String PortNumber = "9090";
    public static String ServiceName = "localhost";
    public static String ConferenceAddress = "@conference.52.10.33.225";
    public static String BroardCastAddress = "broadcast.52.10.33.225";
    public static WickedRideManager INSTANCE;
    private Context mContext;

    private String pickupDate;
    private String pickupTime;
    private String dropDate;
    private String dropTime;
    private String bookingPrice;
    private String noOfDaysHrs;
    private String bikeId;
    private List<Datum> brandsSaved;
    private int checkedPosition;
    private String selectedBrands;
    private String selectedArea;
    private String areaId;

    public List<Datum> getBrandsSaved() {
        return brandsSaved;
    }

    public void setBrandsSaved(List<Datum> brandsSaved) {
        this.brandsSaved = brandsSaved;
    }

    public int getCheckedPosition() {
        return checkedPosition;
    }

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
    }

    /*
         * Constructor
         */
    public WickedRideManager(Context context) {
        this.mContext = context;
    }

    /*
     * Singleton Initialization
	 */
    public static WickedRideManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new WickedRideManager(context);
        }
        return INSTANCE;
    }


    public void setPickUpDate(String pickupDateStr){
        this.pickupDate = pickupDateStr;
    }

    public String getPickupDate(){
        return pickupDate;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getDropDate() {
        return dropDate;
    }

    public void setDropDate(String dropDate) {
        this.dropDate = dropDate;
    }

    public String getDropTime() {
        return dropTime;
    }

    public void setDropTime(String dropTime) {
        this.dropTime = dropTime;
    }

    public void setBookingPrice(String bookingPrice) {
        this.bookingPrice = bookingPrice;
    }

    public String getPriceInclusiveOfVat(){
        if(!bookingPrice.isEmpty()) {
            double totalBookingPrice = Integer.parseInt(bookingPrice);
            totalBookingPrice = ((totalBookingPrice / 100) * Constants.VAT_PERCENTAGE) + totalBookingPrice;
            totalBookingPrice = (double) Math.round(totalBookingPrice * 100) / 100;
            return totalBookingPrice+"";
        }else{
            return "";
        }
    }

    public String getBookingPrice(){
        return bookingPrice;
    }

    public void setNoOfDaysHrs(String noOfDaysHrs) {
        this.noOfDaysHrs = noOfDaysHrs;
    }

    public String getNoOfDaysHrs(){
        return noOfDaysHrs;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public String getBikeId(){
        return  bikeId;
    }

    public void setSelectedBrands(String selectedBrands) {
        this.selectedBrands = selectedBrands;
    }

    public String getSelectedBrands(){
        return selectedBrands;
    }

    public void setSelectedArea(String selectedArea) {
        this.selectedArea = selectedArea;
    }

    public String getSelectedArea(){
        return selectedArea;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaId(){
        return areaId;
    }
}
