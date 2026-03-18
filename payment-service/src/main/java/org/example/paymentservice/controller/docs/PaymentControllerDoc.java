package org.example.paymentservice.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.paymentservice.domain.dto.CreatePaymentRequest;
import org.example.paymentservice.domain.dto.PaymentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payments", description = "АПИ работы с оплатой")
public interface PaymentControllerDoc {

    @Operation(summary = "Получить оплату по ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Оплата найдена, PaymentDto"),
        @ApiResponse(responseCode = "404", description = "Оплата paymentId=%s не найдена")
    })
    @GetMapping("/{paymentId}")
    ResponseEntity<PaymentDto> get(@PathVariable String paymentId);

    @Operation(summary = "Создать оплату", description = "Создаёт новую оплату с PENDING статусом")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Оплата создана."),
        @ApiResponse(responseCode = "400", description = "Некорректные данные для запроса")
    })
    @PostMapping
    ResponseEntity<PaymentDto> create(@RequestBody CreatePaymentRequest createPaymentRequest);

    @Operation(summary = "Обновить оплату", description = "Обновляет оплату")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Оплата обновлена"),
        @ApiResponse(responseCode = "404", description = "Оплата paymentId=%s не найдена"),
        @ApiResponse(responseCode = "400", description = "Некорректные данные для запроса")
    })
    @PutMapping("/{paymentId}")
    ResponseEntity<PaymentDto> update(@PathVariable String paymentId, @RequestBody PaymentDto paymentDto);

    @Operation(summary = "Удалить оплату")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Оплата удалена"),
        @ApiResponse(responseCode = "404", description = "Оплата paymentId=%s не найдена"),
    })
    @DeleteMapping("/{paymentId}")
    ResponseEntity<Void> delete(@PathVariable String paymentId);
}
