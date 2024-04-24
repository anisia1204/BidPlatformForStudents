package com.licenta.web;

import com.licenta.domain.vo.AnnouncementVO;
import com.licenta.domain.vo.ChartDataVO;
import com.licenta.service.AnnouncementFilterService;
import com.licenta.service.AnnouncementService;
import com.licenta.service.FavoriteAnnouncementService;
import com.licenta.service.dto.FavoriteAnnouncementDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {
    private final AnnouncementService announcementService;
    private final AnnouncementFilterService announcementFilterService;
    private final FavoriteAnnouncementService favoriteAnnouncementService;

    public AnnouncementController(AnnouncementService announcementService, AnnouncementFilterService announcementFilterService, FavoriteAnnouncementService favoriteAnnouncementService) {
        this.announcementService = announcementService;
        this.announcementFilterService = announcementFilterService;
        this.favoriteAnnouncementService = favoriteAnnouncementService;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Page<AnnouncementVO>> getMyAnnouncements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) String announcementTitle,
            @RequestParam(required = false) String announcementType,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        Pageable pageable = PageRequest.of(page, size,
                sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
        return ResponseEntity.ok(announcementFilterService.getMyAnnouncements(pageable, announcementTitle, announcementType, status, min, max, from, to));
    }

    @GetMapping(value = "/dashboard")
    @ResponseBody
    public ResponseEntity<Page<AnnouncementVO>> getDashboardAnnouncements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "status") String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String announcementTitle,
            @RequestParam(required = false) String announcementType,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        Pageable pageable = PageRequest.of(page, size,
                sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
        return ResponseEntity.ok(announcementFilterService.getDashboardAnnouncements(pageable, announcementTitle, announcementType, status, min, max, from, to));
    }

    @GetMapping(value = "/favorite")
    @ResponseBody
    public ResponseEntity<Page<AnnouncementVO>> getFavoriteAnnouncements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "status") String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String announcementTitle,
            @RequestParam(required = false) String announcementType,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        Pageable pageable = PageRequest.of(page, size,
                sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
        return ResponseEntity.ok(announcementFilterService.getFavoriteAnnouncements(pageable, announcementTitle, announcementType, status, min, max, from, to));
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

    @PostMapping(value = "/favorite")
    @ResponseBody
    public ResponseEntity<?> addToFavorite(@RequestBody FavoriteAnnouncementDTO favoriteAnnouncementDTO) {
        return ResponseEntity.ok(favoriteAnnouncementService.addToFavorites(favoriteAnnouncementDTO));
    }

    @DeleteMapping (value = "/favorite/{favoriteAnnouncementId}")
    @ResponseBody
    public ResponseEntity<?> removeFromFavorite(@PathVariable Long favoriteAnnouncementId) {
        return ResponseEntity.ok(favoriteAnnouncementService.removeFromFavorites(favoriteAnnouncementId));
    }

    @GetMapping("chart-data")
    @ResponseBody
    public ResponseEntity<ChartDataVO> getChartData() {
        return ResponseEntity.ok(announcementService.getChartData());
    }
}
