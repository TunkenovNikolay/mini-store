package org.example.orderservice.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.orderservice.domain.dto.OrderDto;
import org.example.orderservice.domain.dto.OrderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Orders", description = "АПИ работы с заказами")
public interface OrderControllerDoc {

    @Operation(
        summary = "Получить заказ по ID",
        description = "Возвращает текущий статус заказа с деталями оплаты и доставки"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Заказ найден. OrderDto содержит: orderId, customerId, amount, " +
                "paymentStatus (PENDING/COMPLETED/FAILED), createdAt, updatedAt"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Заказ с orderId=%s не существует"
        )
    })
    @GetMapping("/{orderId}")
    ResponseEntity<OrderDto> get(@PathVariable String orderId);

    @Operation(
        summary = "Создать заказ",
        description = "Создать заказ в магазине"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Заказ успешно создан. Возвращается OrderDto с уникальным orderId, " +
                    "paymentId (если оплата прошла) и статусом PENDING/COMPLETED",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = OrderDto.class

                    )
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Заказ не создан, произошла ошибка"
            )
        }
    )
    @PostMapping
    ResponseEntity<OrderDto> create(@RequestBody(
        description = "Объект для создания заказа",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = OrderRequest.class)
        )) OrderRequest orderRequest);


    @Operation(
        summary = "Создать заказ",
        description = "Создать заказ в магазине"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Заказ успешно обновлен. Возвращается OrderDto.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = OrderDto.class

                    )
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Заказ не обновлен, произошла ошибка"
            )
        }
    )
    @PutMapping("/{orderId}")
    ResponseEntity<OrderDto> update(@PathVariable String orderId, @RequestBody(
        description = "Объект для обновления заказа",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = OrderDto.class)
        )) OrderDto orderDto);

    @Operation(
        summary = "Удалить заказ",
        description = "Удаляет заказ из системы."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Заказ успешно удалён."
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Заказ с orderId=%s не найден"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Неверный orderId (UUID формат)"
        )
    })
    @DeleteMapping("/{orderId}")
    ResponseEntity<Void> delete(@PathVariable String orderId);
}
