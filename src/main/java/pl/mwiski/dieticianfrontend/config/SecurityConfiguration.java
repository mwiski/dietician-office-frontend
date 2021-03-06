package pl.mwiski.dieticianfrontend.config;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.mwiski.dieticianfrontend.clients.user.RoleType;

@Configuration
@EnableWebSecurity
@EnableVaadin
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserPrincipalDetailService userPrincipalDetailService;

    public SecurityConfiguration(UserPrincipalDetailService userPrincipalDetailService) {
        this.userPrincipalDetailService = userPrincipalDetailService;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**/admin/**").hasRole(String.valueOf(RoleType.ADMIN))
                .antMatchers("/**/admin").hasRole(String.valueOf(RoleType.ADMIN))
                .antMatchers("/**/admin/visits/**").hasRole(String.valueOf(RoleType.ADMIN))
                .antMatchers("/**/admin/visits").hasRole(String.valueOf(RoleType.ADMIN))
                .antMatchers("/**/user/visits/**").hasRole(String.valueOf(RoleType.USER))
                .antMatchers("/**/user/visits").hasRole(String.valueOf(RoleType.USER))
                .antMatchers("/**/dietician/visits/**").hasRole(String.valueOf(RoleType.DIETICIAN))
                .antMatchers("/**/dietician/visits").hasRole(String.valueOf(RoleType.DIETICIAN))
                .and()
                .httpBasic()
                .and()
                .formLogin().permitAll().loginPage("/login").successForwardUrl("/")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("auth_code", "JSESSIONID").invalidateHttpSession(true)
                .and()
                .rememberMe().tokenValiditySeconds(2592000).key("uniqueAndSecret")
                .and()
                .csrf().disable();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailService);

        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
