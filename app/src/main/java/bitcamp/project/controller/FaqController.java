package bitcamp.project.controller;

import bitcamp.project.service.FaqService;
import bitcamp.project.vo.Faq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/faqs")
public class FaqController {

    @Autowired
    FaqService faqService;

    @GetMapping
    public ResponseEntity<List<Faq>> list() {
        try {
            List<Faq> faqs = faqService.list();
            return ResponseEntity.ok(faqs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> add(@RequestBody Faq faq) {
        try {
            faqService.add(faq);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "FAQ 추가 성공");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "FAQ 추가 중 오류 발생");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable int id, @RequestBody Faq faq) {
        Map<String, Object> response = new HashMap<>();
        try {
            faq.setId(id);
            boolean isUpdated = faqService.update(faq);

            if (isUpdated) {
                response.put("message", "FAQ 업데이트 성공");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "FAQ를 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("message", "FAQ 업데이트 중 오류 발생");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            faqService.delete(id);
            response.put("message", "FAQ 삭제 성공");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "FAQ 삭제 중 오류 발생");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Faq> view(@PathVariable int id) {
        try {
            Faq faq = faqService.get(id);
            if (faq != null) {
                return ResponseEntity.ok(faq);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
