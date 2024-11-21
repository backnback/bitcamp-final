package bitcamp.project.dao;

import bitcamp.project.vo.Location;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LocationDao {

  List<Location> findAllByFirstName(String firstName) throws Exception;

  List<String> findAllFirstNames() throws Exception;

  Location findByFullName(@Param("firstName") String firstName, @Param("secondName") String secondName) throws Exception;

  Location findById(int id) throws Exception;

  List<Location> getListAndId() throws Exception;
}
