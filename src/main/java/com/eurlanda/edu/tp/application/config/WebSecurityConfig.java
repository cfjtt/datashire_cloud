package com.eurlanda.edu.tp.application.config;

import com.eurlanda.edu.tp.authentication.security.CustomHTTP403Filter;
import com.eurlanda.edu.tp.authentication.security.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by Justin on 2017/6/2.
 */

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${localhost.debug}")
    private Boolean localhostDebug;

    // 指定加密方式
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilter() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    public CustomHTTP403Filter customHTTP403Filter() {
        return new CustomHTTP403Filter();
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                // 设置UserDetailsService
                .userDetailsService(this.userDetailsService)
                // 设置passwordEncoder
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        if (localhostDebug) {
            httpSecurity.authorizeRequests().antMatchers("/**").permitAll();
        }

        httpSecurity
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()

                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                //.antMatchers("/**").permitAll()       // FOR TEST

                // 允许对于网站静态资源的无授权访问
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/common/**",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.css.map",
                        "/**/*.js",
                        "/**/*.js.map",
                        "/rs/**",
                        "/bower_components/**",
                        "/file/**",
                        "/styles/**",
                        "/image/**",
                        "/report/**",
                        "/static/**",
                        "/markdown/**",
                        "/swagger-resources/**",
                        "/webjars/springfox-swagger-ui/**",
                        "/v2/api-docs",
                        "/app/**",
                        "/core/**",
                        "/lib/**",
                        "/po/**",
                        "/utils/**",
                        "/vendor/**"
                ).permitAll()

                // 对于获取token的rest api要允许匿名访问
                .antMatchers("/auth/login").permitAll()
                .antMatchers("/previewImage/**").permitAll()
                .antMatchers("/share/**").permitAll()
                .antMatchers("/startEx/**").permitAll()
                .antMatchers("/record/*/logout/*").permitAll()
                .antMatchers("/record/getClouderaData/**").permitAll()
                .antMatchers("/record/getRealTimeOnlineData/**").permitAll()
                .antMatchers("/record/getAccumulativeOnlineData/**").permitAll()
                .antMatchers("/record/getDayAndWeekAndMonthOnlineAvgData/**").permitAll()
                .antMatchers("/record/getCharacterCloudDashboardData/**").permitAll()
                .antMatchers("/record/getRecentlyLoginUserData/**").permitAll()
                .antMatchers("/record/getSystemTime/**").permitAll()
                .antMatchers("/port/**").permitAll()
                .antMatchers("/teacher/class/export").permitAll()
                .antMatchers("/doc/downloadDocFile").permitAll()
                .antMatchers("/doc/previewProjectImage").permitAll()
                .antMatchers("/doc/previewDocImage").permitAll()
                .antMatchers("/dmdata/**").permitAll()
                .antMatchers("/webSocket/**").permitAll()
//                .antMatchers("/admin/**").permitAll()
//                .antMatchers("/student/**").permitAll()
//                .antMatchers("/services/**").permitAll()
//                .antMatchers("/teacher/**").permitAll()
//                .antMatchers("/course/**").permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();


        httpSecurity.headers()
                // 禁止缓存
                .cacheControl()

                // Disable X-frame-options
                .and()
                .frameOptions().disable();

        // 添加JWT filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        httpSecurity.exceptionHandling().authenticationEntryPoint(customHTTP403Filter());
    }
}