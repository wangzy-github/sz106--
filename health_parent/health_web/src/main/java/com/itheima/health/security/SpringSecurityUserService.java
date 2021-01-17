package com.itheima.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @PackageName: com.itheima.health.controller
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/13
 * @Time: 20:14
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {
    @Reference
    private UserService userService;

    /**
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 认证
        User user = userService.findByUsername(username);
        if (user != null) {
            // 获取用户密码
            String password = user.getPassword();
            // 授权
            List<GrantedAuthority> authorities = new ArrayList<>();
            SimpleGrantedAuthority sga = null;
            Set<Role> roles = user.getRoles();
            if (roles != null) {
                for (Role role : roles) {
                    // 授予角色
                    sga = new SimpleGrantedAuthority(role.getKeyword());
                    authorities.add(sga);
                    Set<Permission> permissions = role.getPermissions();
                    if (permissions!=null) {
                        for (Permission permission : permissions) {
                            // 授予权限
                            sga = new SimpleGrantedAuthority(permission.getKeyword());
                            authorities.add(sga);
                        }
                    }
                }
            }
            return new org.springframework.security.core.userdetails.User(username, password, authorities);
        }
        return null;
    }

    /*public static void main(String[] args) {
        String encode = new BCryptPasswordEncoder().encode("1234");
        System.out.println(encode);
    }*/
}
