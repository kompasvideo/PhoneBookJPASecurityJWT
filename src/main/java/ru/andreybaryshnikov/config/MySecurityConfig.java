package ru.andreybaryshnikov.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.andreybaryshnikov.config.jwt.*;

import javax.sql.DataSource;
import java.text.ParseException;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {
    DataSource dataSource;

    public MySecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public JwtAuthenticationConfigurer jwtAuthenticationConfigurer(
        @Value("${jwt.access-token-key}") String accessTokenKey,
        @Value("${jwt.refresh-token-key}") String refreshTokenKey,
        JdbcTemplate jdbcTemplate
    ) throws ParseException, JOSEException {
        return new JwtAuthenticationConfigurer()
            .accessTokenStringSerializer(new AccessTokenJwsStringSerializer(
                new MACSigner(OctetSequenceKey.parse(accessTokenKey))
            ))
            .refreshTokenStringSerializer(new RefreshTokenJweStringSerializer(
                new DirectEncrypter(OctetSequenceKey.parse(refreshTokenKey))
            ))
            .accessTokenStringDeserializer(new AccessTokenJwsStringDeserializer(
                new MACVerifier(OctetSequenceKey.parse(accessTokenKey))
            ))
            .refreshTokenStringDeserializer(new RefreshTokenJweStringDeserializer(
                new DirectDecrypter(OctetSequenceKey.parse(refreshTokenKey))
            ))
            .jdbcTemplate(jdbcTemplate);
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        return users;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((user) -> user
            .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/viewRecord")).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/deleteRecord")).hasRole("ADMIN")
            .requestMatchers(new AntPathRequestMatcher("/editRecord")).hasRole("ADMIN")
            .requestMatchers(new AntPathRequestMatcher("/editSaveRecord")).hasRole("ADMIN")
            .requestMatchers(new AntPathRequestMatcher("/viewAddRecord")).hasRole("ADMIN")
            .requestMatchers(new AntPathRequestMatcher("/addSaveRecord")).hasRole("ADMIN")
//            .requestMatchers(new AntPathRequestMatcher("/deleteRecord/**")).hasRole("MANAGER")
//            .requestMatchers(new AntPathRequestMatcher("/editRecord/**")).hasRole("HR")
            .anyRequest().authenticated())
            .formLogin((form) -> form
                .loginPage("/login")
                .permitAll()
            )
            .logout((logout) -> logout.logoutSuccessUrl("/").permitAll());
        return http.build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }

}
