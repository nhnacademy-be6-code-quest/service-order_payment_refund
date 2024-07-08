package com.nhnacademy.orderpaymentrefund.client.client.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record BookProductGetResponseDto (
        long bookId,
        String title,
        String publisher,
        String author,
        LocalDate pubDate,
        @Length(min = 10, max =10) String isbn,
        @Length(min = 13, max =13) String isbn13,



        long productId,
        String cover,
        boolean packable,
        String productDescription,
        String productName,
        LocalDateTime productRegisterDate,
        int productState,
        long productViewCount,
        long productPriceStandard,
        long productPriceSales,
        long productInventory,

        @NotNull(message = "{must.have.category}")
        @Size(min = 1, message = "{must.have.category}")
        @Size(max =10, message = "{too.much.category}")
        Map<Long, String> categoryMapOfIdAndName,

        Map<Long, String> tagMapOfIdAndName
){
}
