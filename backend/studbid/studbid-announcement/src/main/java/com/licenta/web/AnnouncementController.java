package com.licenta.web;

import com.licenta.domain.Announcement;
import com.licenta.domain.vo.AnnouncementVO;
import com.licenta.domain.vo.ChartDataVO;
import com.licenta.service.AnnouncementService;
import com.licenta.service.FavoriteAnnouncementService;
import com.licenta.service.dto.FavoriteAnnouncementDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {
    private final AnnouncementService announcementService;
    private final FavoriteAnnouncementService favoriteAnnouncementService;

    public AnnouncementController(AnnouncementService announcementService, FavoriteAnnouncementService favoriteAnnouncementService) {
        this.announcementService = announcementService;
        this.favoriteAnnouncementService = favoriteAnnouncementService;
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

    @GetMapping(value = "/favorite")
    @ResponseBody
    public ResponseEntity<Page<AnnouncementVO>> getFavoriteAnnouncements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<Announcement> favoriteAnnouncementsOfUser = favoriteAnnouncementService.getFavoriteAnnouncementsOfCurrentUser();
        Pageable pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(announcementService.getFavoriteAnnouncements(favoriteAnnouncementsOfUser, pageRequest));
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
