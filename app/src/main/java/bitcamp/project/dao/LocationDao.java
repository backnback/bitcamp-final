package bitcamp.project.dao;

import bitcamp.project.vo.Location;
import bitcamp.project.vo.Story;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LocationDao {

  List<Location> findAllByFirstName(String firstName) throws Exception;

  List<String> findAllFirstNames() throws Exception;
}
