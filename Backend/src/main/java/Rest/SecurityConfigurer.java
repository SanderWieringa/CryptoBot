package Rest;

import Rest.Filters.JwtRequestFilters;
import Rest.Services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter
{
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtRequestFilters jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(myUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/account/authenticate").permitAll()
                .antMatchers("/account/register").permitAll()
                .antMatchers("/symbols/list").permitAll()
                .antMatchers("/symbols/info").permitAll()
                .antMatchers("/products/list").permitAll()
                .antMatchers("/products/setFakeProductsToTradeIn").permitAll()
                .antMatchers("/products/setProductsToTradeIn").permitAll()
                .antMatchers("/products/getProductsToTradeIn").permitAll()
                .antMatchers("/products/getProductsToTradeIn").permitAll()
                .antMatchers("/products/getAllProducts").permitAll()
                .antMatchers("/binance/serverTime").permitAll()
                .antMatchers("/binance/subscribe").permitAll()
                .antMatchers("/binance/placeMarketOrder").permitAll()
                .antMatchers("/binance/placeTestMarketOrder").permitAll()
                .antMatchers("/binance/getAllOpenOrders").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return NoOpPasswordEncoder.getInstance();
    }
}