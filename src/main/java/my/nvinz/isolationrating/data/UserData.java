package my.nvinz.isolationrating.data;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Table(name = "IsolationUsers")
public class UserData {

    @Id
    private String ip;
    private double rating;
    private double latitude;
    private double longtitude;
    private ZonedDateTime lastupdated;
    private String color;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public ZonedDateTime getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(ZonedDateTime lastupdated) {
        this.lastupdated = lastupdated;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
