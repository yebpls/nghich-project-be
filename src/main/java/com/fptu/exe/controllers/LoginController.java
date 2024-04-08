package com.fptu.exe.controllers;

import com.fptu.exe.entities.AccountEntity;
import com.fptu.exe.payload.request.LoginRequest;
import com.fptu.exe.payload.response.BaseResponse;
import com.fptu.exe.security.jwt.JwtHelper;
import com.fptu.exe.services.LoginService;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/login")
public class LoginController {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtHelper jwtHelper;

  @Autowired
  private LoginService loginService;

  private Gson gson = new Gson();

  private Logger logger = LoggerFactory.getLogger(LoginController.class);


  @Operation(summary = "Login")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "Can't found account! Bad Request!"),
      @ApiResponse(responseCode = "200", description = "Login Successfully!"),
      @ApiResponse(responseCode = "500", description = "Internal error")
  })
  @PostMapping("")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    String email = loginRequest.getEmail();
    String password = loginRequest.getPassword();
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email,
        password);
//    System.out.println(token);

    Authentication authentication = authenticationManager.authenticate(token);
    logger.info("authentication: " + authentication);
    String json = gson.toJson(authentication.getAuthorities());
    AccountEntity account = loginService.checkLogin(email, password);
    if (!account.isStatus()) {
//        String jwtToken = jwtService.generateToken(account.getRole(), account);
      BaseResponse baseResponse = new BaseResponse();
      baseResponse.setMessage("Tài khoản chưa đăng ký hoặc chưa được kích hoạt");
      return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    } else {
      String jwtToken = jwtHelper.generateToken(account.getRole(), account);
      BaseResponse baseResponse = new BaseResponse();
      baseResponse.setData(jwtToken);
      return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

  }

}
