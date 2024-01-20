package com.example.mapper;

import java.util.List;

public interface CommonMappable<E, D>{
    E from(D dto);
    D to(E entity);
    List<D> to (List<E> entity);
}
