package org.example.deliveryservice.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.deliveryservice.domain.dto.CreateDeliveryRequest;
import org.example.deliveryservice.domain.dto.DeliveryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Delivery", description = "АПИ работы с доставкой")
public interface DeliveryControllerDoc {

    @Operation(summary = "Получить доставку по ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Доставка найдена, DeliveryDto"),
        @ApiResponse(responseCode = "404", description = "Доставка deliveryId=%s не найдена")
    })
    @GetMapping("/{deliveryId}")
    ResponseEntity<DeliveryDto> get(@PathVariable String deliveryId);

    @Operation(summary = "Получить доставку по orderRefId")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Доставка для заказа найдена"),
        @ApiResponse(responseCode = "404", description = "Доставка для orderRefId=%s не найдена")
    })
    @GetMapping("/order/{orderRefId}")
    ResponseEntity<DeliveryDto> get(@PathVariable UUID orderRefId);

    @Operation(summary = "Создать доставку", description = "Создаёт доставку для заказа")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Доставка создана, DeliveryDto"),
        @ApiResponse(responseCode = "400", description = "Неверный запрос: invalid address, orderRefId")
    })
    @PostMapping
    ResponseEntity<DeliveryDto> create(@RequestBody CreateDeliveryRequest createDeliveryRequest);

    @Operation(summary = "Обновить доставку", description = "Обновляет доставку")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Доставка обновлена"),
        @ApiResponse(responseCode = "404", description = "Доставка deliveryId=%s не найдена")
    })
    @PutMapping("/{deliveryId}")
    ResponseEntity<DeliveryDto> update(@PathVariable String deliveryId, @RequestBody DeliveryDto deliveryDto);

    @Operation(summary = "Удалить доставку")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Доставка удалена"),
        @ApiResponse(responseCode = "404", description = "Доставка deliveryId=%s не найдена")
    })
    @DeleteMapping("/{deliveryId}")
    ResponseEntity<Void> delete(@PathVariable String deliveryId);

}
