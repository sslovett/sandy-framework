package com.sandy.fw.security.password;

import cn.hutool.crypto.symmetric.AES;
import com.sandy.fw.common.exception.DefaultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class PasswordManager {

    @Value("${password.sign.key:p-sandy-password}")
    public String passwordSignKey;

    @Autowired
    PasswordEncoder passwordEncoder;

    private String decrypt(String encryptedPassword) {
        AES aes = new AES(passwordSignKey.getBytes(StandardCharsets.UTF_8));
        String decryptStr;
        String decryptPassword;
        try {
            decryptStr = aes.decryptStr(encryptedPassword);
            decryptPassword = decryptStr.substring(13);
        } catch (Exception e) {
            log.error("Exception:", e);
            throw new DefaultException("AES解密错误", e);
        }
        return decryptPassword;
    }

    public String encode(String aesRawPassword) {
        String rawPassword = decrypt(aesRawPassword);
        return passwordEncoder.encode(rawPassword);
    }

    public void check(String aesRawPassword, String encryptedPassword) {
        String rawPassword = decrypt(aesRawPassword);
        if(!passwordEncoder.matches(rawPassword, encryptedPassword)) {
            throw new DefaultException("账号或密码不正确");
        }
    }
}
