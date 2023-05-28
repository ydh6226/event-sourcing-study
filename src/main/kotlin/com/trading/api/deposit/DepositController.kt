package com.trading.api.deposit

import com.trading.api.deposit.dto.CreateDepositRequest
import com.trading.api.deposit.dto.DecreaseDepositRequest
import com.trading.api.deposit.dto.GetCurrentBalanceResponse
import com.trading.api.deposit.dto.IncreaseDepositRequest
import com.trading.common.ApiResponse
import com.trading.deposit.service.DepositQueryService
import com.trading.deposit.service.DepositService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RequestMapping("/api/v1/deposit")
@RestController
class DepositController(
    private val depositService: DepositService,
    private val depositQueryService: DepositQueryService,
) {

    @GetMapping
    fun getCurrentBalance(@RequestParam accountNo: String): ApiResponse<GetCurrentBalanceResponse> {
        val balance = depositQueryService.getCurrentBalance(accountNo)
        return ApiResponse.success(GetCurrentBalanceResponse(balance))
    }

    @PostMapping
    fun createDeposit(@RequestBody request: CreateDepositRequest): ApiResponse<Unit> {
        depositService.create(request.accountNo, request.balance)
        return ApiResponse.success()
    }

    @PostMapping("/increase")
    fun increase(@RequestBody request: IncreaseDepositRequest): ApiResponse<Unit> {
        depositService.increase(request.accountNo, request.amount)
        return ApiResponse.success()
    }

    @PostMapping("/decrease")
    fun decrease(@RequestBody request: DecreaseDepositRequest): ApiResponse<Unit> {
        depositService.decrease(request.accountNo, request.amount)
        return ApiResponse.success()
    }

}