package com.licenta.web;

import com.licenta.domain.vo.UserDetailsVO;
import com.licenta.service.AdminFeaturesService;
import com.licenta.service.dto.UpdateUserPointsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin-features")
@CrossOrigin
public class AdminFeaturesController {

    private final AdminFeaturesService adminFeaturesService;

    public AdminFeaturesController(AdminFeaturesService adminFeaturesService) {
        this.adminFeaturesService = adminFeaturesService;
    }

    @GetMapping(value = "/details/{userId}")
    public @ResponseBody ResponseEntity<UserDetailsVO> getDetails(@PathVariable Long userId) {
        return ResponseEntity.ok(this.adminFeaturesService.getDetails(userId));
    }

    @PutMapping
    public @ResponseBody ResponseEntity<UpdateUserPointsDTO> updateUserPoints(@RequestBody UpdateUserPointsDTO updateUserPointsDTO) {
        return ResponseEntity.ok(this.adminFeaturesService.updateUserPointsAndSaveWithdraw(updateUserPointsDTO));
    }
}
