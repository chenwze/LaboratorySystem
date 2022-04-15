package com.gdufe.laboratorysystem.config;




import com.gdufe.laboratorysystem.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;


/**
 * @author cwz
 * @date 年05月21日 17:38
 */

// 开启MVC security安全支持
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启注解方法安全
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Value("${system.user.password.secret}")
    private String secret;

    @Bean //配置口令加密方式
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder(secret);
    }

    @Bean //配置持久化Token存储方式

    public PersistentTokenRepository tokenRepository() {
        return jdbcTokenRepository();
    }

    private PersistentTokenRepository jdbcTokenRepository() {
        JdbcTokenRepositoryImpl jr = new JdbcTokenRepositoryImpl();
        jr.setDataSource(dataSource);
//        jr.setCreateTableOnStartup(true); // 启动时自动创建表persistent_logins，创建成功后注释掉
        return jr;
    }

    /**
     *用户配置身份认证自定义配置
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userServiceImpl);
//        auth.userDetailsService(userServiceImpl).passwordEncoder(passwordEncoder());
    }

    /**
     * 用户自定义配置Filter链
     * @param web
     * @throws Exception
     */

    /**
     *用户授权管理自定义配置
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();

        http.headers().frameOptions().sameOrigin();

        // 自定义用户授权管理
        http.authorizeRequests()
                .antMatchers( "/register", "/userlogin","/userLogin", "/index", "/reisterPage","/","/retrievePassword"
                                ,"/verCode","/user/**").permitAll()
                .antMatchers("/test/**","/login/**","/getUserInfo").permitAll()
                .antMatchers("/student/**").hasAuthority("student")
                .antMatchers("/teacher/**").hasAuthority("teacher")
                .antMatchers("/admin/**").hasAuthority("admin")
//                .antMatchers("/detail/commom/**", "/commom/**", "/open/commom").hasRole("COMMOM")
//                .antMatchers("/detail/vip/**", "/vip/**", "/open/vip").hasRole("VIP")
//                .antMatchers("/detail/svip/**", "/svip/**", "/open/svip").hasRole("SVIP")
                .anyRequest().authenticated();
        // 自定义用户登录控制
        http.formLogin().loginPage("/userLogin").permitAll()
                .usernameParameter("username").passwordParameter("password")
                .defaultSuccessUrl("/").failureUrl("/userLogin?error");
                http.csrf().disable();
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        // 自定义用户退出控制
        http.logout().logoutUrl("/mylogout").logoutSuccessUrl("/login");
        http.sessionManagement().invalidSessionUrl("/login");
        // 开启Remember-me功能
        http.rememberMe().rememberMeParameter("rememberme") //须与登录表单勾选框中name属性值一致
                .tokenValiditySeconds(60*60*24) //设置“记住我”中Token有效期为200s
                .tokenRepository(jdbcTokenRepository()); //对Token进行持久化管理
    }

    /**
     * 静态资源设置
     */
    @Override
    public void configure(WebSecurity webSecurity) {
        // 不拦截静态资源,所有用户均可访问的资源
        webSecurity.ignoring().antMatchers("/js/**","/css/**"
        ,"/bootstrap/**","/layui/**");
    }
}
