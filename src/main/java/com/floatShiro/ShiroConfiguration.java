package com.floatShiro;


import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfiguration {


    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager DefaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean =  new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(DefaultWebSecurityManager);

        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");

        LinkedHashMap<String,String> filterChainMap = new LinkedHashMap<>();
        filterChainMap.put("/index","authc");
        filterChainMap.put("/login","anon");
        filterChainMap.put("/","anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);

        return shiroFilterFactoryBean;
    }


    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(@Qualifier("authRealm") AuthRealm authRealm){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(authRealm);
        return manager;
    }


    @Bean("authRealm")
    public AuthRealm authRealm(@Qualifier("credentialsMatcher") CredentialsMatcher credentialsMatcher){
        AuthRealm authRealm =  new AuthRealm();
        //将authRealm放进系统缓存中
        authRealm.setCacheManager(new MemoryConstrainedCacheManager());
        authRealm.setCredentialsMatcher(credentialsMatcher);
        return authRealm;
    }


    @Bean("credentialsMatcher")
    public CredentialsMatcher credentialsMatcher(){
        return new CredentialsMatcher();
    }


    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") DefaultWebSecurityManager DefaultWebSecurityManager){
      AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
      advisor.setSecurityManager(DefaultWebSecurityManager);
      return advisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

}
