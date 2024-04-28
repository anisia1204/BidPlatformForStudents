package com.licenta.web;

import com.licenta.domain.vo.UserDetailsVO;
import com.licenta.service.WithdrawService;
import com.licenta.service.dto.UpdateUserPointsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin-features")
@CrossOrigin
public class WithdrawController {

    private final WithdrawService withdrawService;

    public WithdrawController(WithdrawService withdrawService) {
        this.withdrawService = withdrawService;
    }

    @GetMapping(value = "/details/{userId}")
    public @ResponseBody ResponseEntity<UserDetailsVO> getUserDetails(@PathVariable Long userId) {
        return ResponseEntity.ok(this.withdrawService.getUserDetails(userId));
    }

    @PutMapping
    public @ResponseBody ResponseEntity<UpdateUserPointsDTO> saveWithdrawAndUpdateUserPoints(@RequestBody UpdateUserPointsDTO updateUserPointsDTO) {
        return ResponseEntity.ok(this.withdrawService.saveWithdrawAndUpdateUserPoints(updateUserPointsDTO));
    }
}
