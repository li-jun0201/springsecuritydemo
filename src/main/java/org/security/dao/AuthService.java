package org.security.dao;

import org.security.entity.SysUser;


/**
 *  demo service
 *
 *  @Author Joey Li
 *  @Data 2018-05-23
 */
public interface AuthService {
    String register(SysUser userToAdd);
    String login(String username, String password);
    String refresh(String oldToken);
}
