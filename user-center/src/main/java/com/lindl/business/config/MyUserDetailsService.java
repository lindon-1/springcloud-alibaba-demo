//package com.lindl.business.config;
//
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Service;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.util.CollectionUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class MyUserDetailsService extends UserDetailsService {
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private PermissionService permissionService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        SysUser sysUser = userService.getByUsername(username);
//        if (null == sysUser) {
//            log.warn("用户{}不存在", username);
//            throw new UsernameNotFoundException(username);
//        }
//        List<SysPermission> permissionList = permissionService.findByUserId(sysUser.getId());
//        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
//        if (!CollectionUtils.isEmpty(permissionList)) {
//            for (SysPermission sysPermission : permissionList) {
//                authorityList.add(new SimpleGrantedAuthority(sysPermission.getCode()));
//            }
//        }
//
//        MyUser myUser = new MyUser(sysUser.getUsername(), passwordEncoder.encode(sysUser.getPassword()), authorityList);
//
////        log.info("登录成功！用户: {}", JSON.toJSONString(myUser));
//
//        return myUser;
//    }
//
//}
