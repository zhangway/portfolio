package no.uit.zhangwei.Model.runkeeper;


import java.math.BigDecimal;

public class WGS84 {
    private BigDecimal timestamp;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal altitude;
    private String type;
    
    public WGS84(BigDecimal timestamp, BigDecimal latitude, BigDecimal longitude, 
    			 BigDecimal altitude, String type){
    	this.timestamp = timestamp;
    	this.latitude = latitude;
    	this.longitude = longitude;
    	this.altitude = altitude;
    }

    @Override
    public String toString() {
        return "WGS84{" +
                "timestamp=" + timestamp +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                ", type='" + type + '\'' +
                '}';
    }
}
