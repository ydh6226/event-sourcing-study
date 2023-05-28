package com.trading.api.deposit

import com.trading.api.deposit.dto.CreateDepositRequest
import com.trading.api.deposit.dto.DecreaseDepositRequest
import com.trading.api.deposit.dto.GetCurrentBalanceResponse
import com.trading.api.deposit.dto.IncreaseDepositRequest
import com.trading.common.ApiResponse
import com.trading.deposit.service.DepositQueryServiceV2
import com.trading.deposit.service.DepositServiceV2
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RequestMapping("/api/v2/deposit")
@RestController
class DepositControllerV2(
    private val depositService: DepositServiceV2,
    private val depositQueryService: DepositQueryServiceV2,
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