package com.wilddiary.monitoring;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import io.netty.resolver.DefaultAddressResolverGroup;
import jakarta.servlet.DispatcherType;
import java.util.UUID;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/** Enable security. Allow access to static resources. */
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class WebSecurityConfig {
  private final AdminServerProperties adminServer;
  private final SecurityProperties security;

  public WebSecurityConfig(AdminServerProperties adminServer, SecurityProperties security) {
    this.adminServer = adminServer;
    this.security = security;
  }

  /**
   * Builds the security filter chain.
   *
   * @param http HttpSecurity instance to customise.
   * @return customized security filter chain.
   * @throws Exception any exceptions encountered during customization.
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    SavedRequestAwareAuthenticationSuccessHandler successHandler =
        new SavedRequestAwareAuthenticationSuccessHandler();
    successHandler.setTargetUrlParameter("redirectTo");
    successHandler.setDefaultTargetUrl(this.adminServer.path("/"));

    http.authorizeHttpRequests(
            (authorizeRequests) ->
                authorizeRequests //
                    .requestMatchers(
                        new AntPathRequestMatcher(this.adminServer.path("/static/img/assets/**")))
                    .permitAll()
                    .requestMatchers(
                        new AntPathRequestMatcher(this.adminServer.path("/assets/*.css")))
                    .permitAll()
                    .requestMatchers(
                        new AntPathRequestMatcher(this.adminServer.path("/assets/*.js")))
                    .permitAll()
                    .requestMatchers(
                        new AntPathRequestMatcher(this.adminServer.path("/assets/img/**")))
                    .permitAll()
                    .requestMatchers(
                        new AntPathRequestMatcher(this.adminServer.path("/actuator/info")))
                    .permitAll()
                    .requestMatchers(
                        new AntPathRequestMatcher(adminServer.path("/actuator/health")))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher(this.adminServer.path("/login")))
                    .permitAll()
                    .dispatcherTypeMatchers(DispatcherType.ASYNC)
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(
            (formLogin) ->
                formLogin.loginPage(this.adminServer.path("/login")).successHandler(successHandler))
        .logout((logout) -> logout.logoutUrl(this.adminServer.path("/logout")))
        .httpBasic(Customizer.withDefaults());

    http.addFilterAfter(new CustomCsrfFilter(), BasicAuthenticationFilter.class)
        .csrf(
            (csrf) ->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                    .ignoringRequestMatchers(
                        new AntPathRequestMatcher(
                            this.adminServer.path("/instances"), POST.toString()),
                        new AntPathRequestMatcher(
                            this.adminServer.path("/instances/*"), DELETE.toString()),
                        new AntPathRequestMatcher(this.adminServer.path("/actuator/**"))));

    http.rememberMe(
        (rememberMe) -> rememberMe.key(UUID.randomUUID().toString()).tokenValiditySeconds(1209600));

    return http.build();
  }

  /** Required to provide UserDetailsService for "remember functionality". */
  @Bean
  public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
    UserDetails user =
        User.withUsername(security.getUser().getName())
            .password(passwordEncoder.encode(security.getUser().getPassword()))
            .roles(security.getUser().getRoles().toArray(new String[0]))
            .build();
    return new InMemoryUserDetailsManager(user);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Primary
  public WebClient webClient() {
    HttpClient httpClient = HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);
    return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
  }
}
