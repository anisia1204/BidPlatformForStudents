package com.licenta.web;

import com.licenta.domain.vo.AnnouncementVO;
import com.licenta.service.AnnouncementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {
    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Page<AnnouncementVO>> getMyAnnouncements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String[] sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity.ok(announcementService.getMyAnnouncements(pageable));
    }

    @GetMapping(value = "/dashboard")
    @ResponseBody
    public ResponseEntity<Page<AnnouncementVO>> getDashboardAnnouncements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "status") String[] sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity.ok(announcementService.getDashboardAnnouncements(pageable));
    }

    @GetMapping(value = {"/{id}"})
    @ResponseBody
    public ResponseEntity<AnnouncementVO> getDetails(@PathVariable Long id) {
        return ResponseEntity.ok(announcementService.getDetails(id));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<?> delete(@PathVariable Long id) {
        announcementService.delete(id);
        return ResponseEntity.ok(true);
    }
}
