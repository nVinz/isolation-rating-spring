package my.nvinz.isolationrating.dataservice;

import my.nvinz.isolationrating.data.CityData;
import my.nvinz.isolationrating.data.UserColor;
import my.nvinz.isolationrating.datarepository.CityDataRepository;
import my.nvinz.isolationrating.datarepository.UserDataRepository;
import my.nvinz.isolationrating.data.UserData;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;

@org.springframework.stereotype.Service
public class DataService implements Service {

    @Autowired
    private UserDataRepository userDataRepository;
    @Autowired
    private CityDataRepository cityDataRepository;

    public List<UserData> getAllUserData() {
        List<UserData> userDataList = new LinkedList<>();
        userDataRepository.findAll().forEach(userDataList::add);
        return userDataList;
    }

    public UserData getUserData(String id) {
        return userDataRepository.findById(id).orElse(null);
    }

    public Iterable<UserData> getNewUserDate(ZonedDateTime date) {
        return userDataRepository.findByLastupdatedGreaterThan(date);
    }

    public void updateUserData(UserData userData) {
        // not in MSK
        if (userData.getLatitude() > 56.055552 ||
                userData.getLatitude() < 55.491693 ||
                userData.getLongtitude() > 38.087612 ||
                userData.getLongtitude() < 37.048991) {
            return;
        }
        if (userDataRepository.findById(userData.getIp()).isPresent()) {
            UserData user = userDataRepository.findById(userData.getIp()).get();

            // -1 for rating update
            if (userData.getRating() == -1) {
                userData.setRating(user.getRating());
            }

            // Value of 3 digits after dot for old & new coordinates: nnn.NNNnnn
            String userLatitude = String.valueOf(user.getLatitude());
            userLatitude = userLatitude.substring(userLatitude.indexOf(".") + 1);
            userLatitude = userLatitude.substring(0, 3);

            String userLongtitude = String.valueOf(user.getLongtitude());
            userLongtitude = userLongtitude.substring(userLongtitude.indexOf(".") + 1);
            userLongtitude = userLongtitude.substring(0, 3);

            String newLatitude = String.valueOf(userData.getLatitude());
            newLatitude = newLatitude.substring(newLatitude.indexOf(".") + 1);
            newLatitude = newLatitude.substring(0, 3);

            String newLongtitude = String.valueOf(userData.getLongtitude());
            newLongtitude = newLongtitude.substring(newLongtitude.indexOf(".") + 1);
            newLongtitude = newLongtitude.substring(0, 3);

            long timeDiff = MINUTES.between(user.getLastupdated().toInstant(), userData.getLastupdated().toInstant());

            if (userLongtitude.equals(newLongtitude) && userLatitude.equals(newLatitude)) {
                userData.setRating(userData.getRating() + timeDiff / 5.0  * 0.05);
                if (userData.getRating() > 10) userData.setRating(10);
            }
            else {
                userData.setRating(userData.getRating() - timeDiff / 5.0 * 0.05);
                if (userData.getRating() < 0) userData.setRating(0);
            }
        }
        else {
            userData.setRating(5);
        }
        userData.setRating( BigDecimal.valueOf(userData.getRating())
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue());

        // Map icon color
        UserColor ratingColor;
        if (userData.getRating() > 8.0) ratingColor = UserColor.GREEN;
        else if (userData.getRating() > 4.0) ratingColor = UserColor.ORANGE;
        else ratingColor = UserColor.RED;

        userData.setColor(ratingColor.getColor());

        userDataRepository.save(userData);
    }

    public CityData getCityData(String name) {
        CityData cityData = cityDataRepository.findById(name).orElse(null);
        if (cityData != null) {
            ArrayList<UserData> userData = (ArrayList<UserData>) getNewUserDate(cityData.getLastupdated());

            double usersCount = userData.stream()
                    .mapToDouble(UserData::getRating)
                    .sum();

            cityData.setCount(userData.size());
            cityData.setRating(usersCount / userData.size());
            cityData.setRating( BigDecimal.valueOf(cityData.getRating())
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue());

            // Update DB data
            updateCitydata(cityData);
        }
        return cityData;
    }

    public void updateCitydata(CityData cityData) {

        if (cityDataRepository.findById(cityData.getName()).isPresent()) {

            CityData city = cityDataRepository.findById(cityData.getName()).get();

            // count difference or -1 for rating update
            if (city.getCount() != cityData.getCount() || cityData.getCount() == -1) {

                double rating = 0;
                List<UserData> userDataList = new LinkedList<>();

                userDataRepository.findAll().forEach(userDataList::add);
                for (UserData data : userDataList) {
                    // TODO 24h statistic
                    //if (DAYS.between(city.getLastupdated().toInstant(), cityData.getLastupdated().toInstant()) > 1)
                    rating += data.getRating();
                }

                rating /= userDataRepository.count();
                cityData.setRating(rating);
            }
        }

        cityDataRepository.save(cityData);
    }

}
