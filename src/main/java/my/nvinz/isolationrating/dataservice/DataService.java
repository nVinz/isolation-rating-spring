package my.nvinz.isolationrating.dataservice;

import my.nvinz.isolationrating.data.CityData;
import my.nvinz.isolationrating.datarepository.CityDataRepository;
import my.nvinz.isolationrating.datarepository.UserDataRepository;
import my.nvinz.isolationrating.data.UserData;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;

@org.springframework.stereotype.Service
public class DataService implements Service {

    @Autowired
    private UserDataRepository userDataRepository;
    @Autowired
    private CityDataRepository cityDataRepository;

    public List<UserData> getAllUserData() {
        List<UserData> userDataList = new ArrayList<>();
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

        if (userDataRepository.findById(userData.getIp()).isPresent()) {
            UserData user = userDataRepository.findById(userData.getIp()).get();

            // -1 for rating update
            if (userData.getRating() == -1) {
                userData.setRating(user.getRating());
            }

            DecimalFormat decimalFormat = new DecimalFormat("#.##");

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

            //userData.setRating(decimalFormat.format(userData.getRating()));
        }
        else {
            userData.setRating(5);
        }
        userDataRepository.save(userData);
    }

    public CityData getCityData(String name) {
        return cityDataRepository.findById(name).orElse(null);
    }

    public void updateCitydata(CityData cityData) {

        if (cityDataRepository.findById(cityData.getName()).isPresent()) {

            CityData city = cityDataRepository.findById(cityData.getName()).get();

            // count difference or -1 for rating update
            if (city.getCount() != cityData.getCount() || cityData.getCount() == -1) {

                double rating = 0;
                List<UserData> userDataList = new ArrayList<>();

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
