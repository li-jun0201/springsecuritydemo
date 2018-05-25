package org.security.dao;

import org.security.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *  jpa 接口
 *
 *  @Author Joey Li
 *  @Data 2018-05-23
 */
public interface SysUserRepository extends JpaRepository<SysUser, Long> {
    SysUser findByUsername(String username);
}
