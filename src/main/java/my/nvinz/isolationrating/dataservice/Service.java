package my.nvinz.isolationrating.dataservice;

import my.nvinz.isolationrating.data.UserData;

import java.util.List;

public interface Service {

    List<UserData> getAllUserData();

    UserData getUserData(String id);

    void updateUserData(UserData userData);
}
