package bitcamp.project.service.impl;

import bitcamp.project.dao.FaqDao;
import bitcamp.project.service.FaqService;
import bitcamp.project.vo.Faq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FaqServiceImpl implements FaqService {
    @Autowired
    FaqDao faqDao;

    @Transactional
    @Override
    public void add(Faq faq) throws Exception {
        faqDao.add(faq);
    }

    @Override
    public List<Faq> list() throws Exception {
        return faqDao.findAll();
    }

    @Override
    public Faq get(int id) throws Exception {
        return faqDao.findBy(id);
    }

    @Transactional
    @Override
    public void delete(int id) throws Exception {
        faqDao.delete(id);
    }
}
