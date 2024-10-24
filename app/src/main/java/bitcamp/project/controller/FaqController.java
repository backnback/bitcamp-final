package bitcamp.project.controller;

import bitcamp.project.service.FaqService;
import bitcamp.project.vo.Faq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faqs")
public class FaqController {

    @Autowired
    FaqService faqService;

    @GetMapping("list")
    public List<Faq> list() throws Exception {
        return faqService.list();
    }

    @PostMapping("add")
    public void add(@RequestBody Faq faq) throws Exception {
        faqService.add(faq);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") int id) throws Exception {
        faqService.delete(id);
    }

    @GetMapping("view/{id}")
    public Faq view(@PathVariable("id") int id) throws Exception {
        return faqService.get(id);
    }


}
