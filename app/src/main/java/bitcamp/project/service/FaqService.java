package bitcamp.project.service;

import bitcamp.project.vo.Faq;

import java.util.List;

public interface FaqService {
    void add(Faq faq) throws Exception;

    List<Faq> list() throws Exception;

    Faq get(int id) throws Exception;

    boolean update(Faq faq) throws Exception;

    void delete(int id) throws Exception;
}
