package com.sandy.fw.security.util;

import com.sandy.fw.security.bean.UserInfoToken;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SecurityUtils {

    public UserInfoToken getUserInfo() {
        UserInfoToken userInfo = (UserInfoToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userInfo;
    }
}
