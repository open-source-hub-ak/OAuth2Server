package com.opensource.OAuth2server.security;

import java.security.KeyPair;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import com.opensource.OAuth2server.util.SecurityConstants;

import ch.qos.logback.core.net.ssl.KeyStoreFactoryBean;

//@Configuration
//@EnableAuthorizationServer
public class AuthorizationServiceConfigAuthorizationCodeGrantType extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	DataSource dataSource;

	@Value("${keyFile}")
	private String keyFile;
	@Value("${password}")
	private String password;
	@Value("${alias}")
	private String alias;

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

		endpoints.tokenStore(tokenStore()).accessTokenConverter(jwtAccessTokenConverter())
				.authenticationManager(authenticationManager).userDetailsService(userDetailsService);

	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		clients.inMemory().withClient(SecurityConstants.CLIENT_ID)
				.secret(passwordEncoder.encode(SecurityConstants.CLIENT_SECRET))
				.authorizedGrantTypes("authorization_code", "password", "refresh_token").scopes("read", "write")
				.resourceIds(SecurityConstants.RESOURCE_ID).redirectUris("http://localhost:8080/couponshow");
	}

	// to expose public key
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

		security.tokenKeyAccess("permitAll()");
	}

	//@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}

	//@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {

		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();

		jwtAccessTokenConverter.setKeyPair(
				new KeyStoreKeyFactory(new ClassPathResource(keyFile), password.toCharArray()).getKeyPair(alias));
		return jwtAccessTokenConverter;
	}
}
