package com.nhnacademy.orderpaymentrefund.converter;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.dto.order.request.CreateClientOrderRequestDto;

import java.util.List;

public interface Converter<E, D>{

    default D entityToDto(E entity){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    default D entityToDto(E entity, Object ... objects){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    default E dtoToEntity(D dto){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    default E dtoToEntity(D dto, Object ... objects){
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
