package com.nhnacademy.orderpaymentrefund.converter;

import java.util.List;

public interface Converter<E, D>{

    default D entityToDto(E entity){
        return null;
    }

    default E dtoToEntity(D dto){
        return null;
    }

    default D entityListToDto(List<E> entityList){
        return null;
    }

    default E dtoListToEntityList(List<D> dtoList){
        return null;
    }

}
