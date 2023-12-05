package com.vinnilmg.webfluxcourse.mapper;

import com.vinnilmg.webfluxcourse.entity.User;
import com.vinnilmg.webfluxcourse.model.request.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(final UserRequest request);

}
