package my.nvinz.isolationrating.datarepository;

import my.nvinz.isolationrating.data.UserData;
import org.springframework.data.repository.CrudRepository;

public interface UserDataRepository extends CrudRepository<UserData, String> {
}
