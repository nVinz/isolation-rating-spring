package my.nvinz.isolationrating.datarepository;

import my.nvinz.isolationrating.data.CityData;
import org.springframework.data.repository.CrudRepository;

public interface CityDataRepository extends CrudRepository<CityData, String> {
}
