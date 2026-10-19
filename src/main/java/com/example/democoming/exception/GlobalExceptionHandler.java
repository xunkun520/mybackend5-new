package com.example.democoming.exception;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {
@ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handle1(IllegalArgumentException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
}
   public ResponseEntity<String> handle2(IOException e) {
    return ResponseEntity.internalServerError().body("用户文件读取或保存失败");
   }
}
