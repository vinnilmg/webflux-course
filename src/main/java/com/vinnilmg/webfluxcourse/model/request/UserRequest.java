package com.vinnilmg.webfluxcourse.model.request;

public record UserRequest(
        String name,
        String email,
        String password
) { }
