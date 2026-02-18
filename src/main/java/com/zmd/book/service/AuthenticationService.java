package com.zmd.book.service;

import com.zmd.book.dto.request.LoginRequestDto;
import com.zmd.book.dto.response.LoginResponseDto;


public interface AuthenticationService {

    LoginResponseDto login(LoginRequestDto loginRequestDto);

}
