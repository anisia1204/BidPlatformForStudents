package com.licenta.web;

import com.licenta.domain.vo.UserDetailsVO;
import com.licenta.domain.vo.WithdrawVO;
import com.licenta.service.WithdrawService;
import com.licenta.service.dto.UpdateUserPointsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping
    @ResponseBody
    public ResponseEntity<Page<WithdrawVO>> getWithdrawHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) String userFullName,
            @RequestParam(required = false) Double points,
            @RequestParam(required = false) String createdAt
    ) {
        Pageable pageable = PageRequest.of(page, size,
                sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());

        return ResponseEntity.ok(withdrawService.getWithdrawHistory(userFullName, points, createdAt, pageable));
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
