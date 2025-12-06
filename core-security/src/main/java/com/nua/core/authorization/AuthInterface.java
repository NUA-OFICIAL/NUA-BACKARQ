package com.nua.core.authorization;

import com.nua.core.base.dto.TokenResponse;
import com.nua.core.tokens.Token;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public interface AuthInterface {

    void register(RegisterRequest request);

    TokenResponse login(LoginRequest request);

    //void changePassword(ChangePassReq request);

    //void revokeAllUserTokens(Users user);

    //void saveUserToken(Users user, String jwtToken);

    TokenResponse refreshToken(final String authHeader);


    Token obtainUser(String token);

    ResponseCookie accessCookie(TokenResponse token);

    ResponseCookie refreshCookie(TokenResponse token);

}
