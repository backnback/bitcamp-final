package bitcamp.project.dao;

import bitcamp.project.vo.Faq;
import bitcamp.project.vo.Story;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FaqDao {
    boolean add(Faq faq) throws Exception;

    List<Faq> findAll() throws Exception;

    Faq findBy(int id) throws Exception;

    boolean update(Faq faq) throws Exception;

    boolean delete(int id) throws Exception;
}
