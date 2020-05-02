package my.nvinz.isolationrating.datarepository;

import my.nvinz.isolationrating.data.UserData;
import org.springframework.data.repository.CrudRepository;

import java.time.ZonedDateTime;

public interface UserDataRepository extends CrudRepository<UserData, String> {
    Iterable<UserData> findByLastupdatedGreaterThan(ZonedDateTime date);
}
