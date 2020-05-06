package my.nvinz.isolationrating.controller;

import my.nvinz.isolationrating.data.CityData;
import my.nvinz.isolationrating.dataservice.DataService;
import my.nvinz.isolationrating.data.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;


@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private DataService dataService;

    @RequestMapping(value = "/alluserdata", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<UserData> getAllUserData(){
        return dataService.getAllUserData();
    }

    @RequestMapping(value = "/userdata/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody UserData getUserData(@PathVariable("id") String id){
        return dataService.getUserData(id);
    }

    @RequestMapping(value = "/userdata", method = RequestMethod.POST)
    public @ResponseBody void setUserData(@RequestBody UserData userData) {
        dataService.updateUserData(userData);
    }

    @RequestMapping(value = "/alluserdata/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<UserData> getNewUserData(@PathVariable("date") String dateString) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString);
        return (List<UserData>) dataService.getNewUserDate(zonedDateTime);
    }

    @RequestMapping(value = "/citydata/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CityData getCityData(@PathVariable("id") String name){
        return dataService.getCityData(name);
    }

    @RequestMapping(value = "/citydata", method = RequestMethod.POST)
    public @ResponseBody void setCityData(@RequestBody CityData cityData){
        dataService.updateCitydata(cityData);
    }

}
