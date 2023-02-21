package ru.otus.library.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@ComponentScan
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Autowired
    private CustomUserDetailService userDetailsService;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    private SimpleUrlAuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

    /*

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password(encoder().encode("user"))
                .roles("USER")
                .build();
        UserDetails admin = User.withUsername("admin")
                .password(encoder().encode("admin"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

     */

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
        http.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/perform_login")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/books/**", "/books/")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.PUT,"/books/**", "/books/")
                .authenticated()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/books/**", "/books/")
                .authenticated()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.DELETE,"/books/**")
                .hasRole("ADMIN")
                .and()
                .formLogin()
                .loginProcessingUrl("/perform_login")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .and()
                .rememberMe().key("someSecret").alwaysRemember(true)
                .and()
                .logout();
    }

    @Bean
    public PasswordEncoder encoder() {
        /*
            return new BCryptPasswordEncoder();
         */
        return NoOpPasswordEncoder.getInstance();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}
