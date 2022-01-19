package com.dogather.pjtserver.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import com.dogather.pjtserver.dto.UserDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtProvider {
	
	// Secret for key
	private static String secret = "DoGatherJwtTokenSecretStringDoGatherJwtTokenSecretString";
	// Key for JWT Token
	private static SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
	
	// userdto�� �̿��Ͽ� Token ���� �� ��ȯ
	public static String getToken(String userId) {
		String Subject = "Dogather";
		String payload = userId;
		int duration = 30 * 60 * 1000;
		Date expiration = new Date(System.currentTimeMillis() + duration); // 30m
//		Date expiration = new Date(System.currentTimeMillis() + 0); // 30m
		
		String token = Jwts.builder()
				.setSubject(Subject)
				.setExpiration(expiration)
				.claim("payload", payload)
				.signWith(key)
				.compact();
		
		return token;
	}
	
	public static String validateToken(String token, String userId) {
		String ret = "InvalidToken";
		try {
			
			Claims claims = Jwts.parserBuilder()
					.setSigningKey(key)
					.requireSubject("Dogather")
					.build()
					.parseClaimsJws(token)
					.getBody();
			
			ret = (String) claims.get("payload");	
			
		} catch( ExpiredJwtException e) {
			
			System.err.println("�Ⱓ�� ����� ��ū�Դϴ�.");
			ret = "�ٽ÷α����ϼ���!";
			
		} catch (Exception e) {
			System.err.println("�߸��� ��ū�Դϴ�.");
			
		}
	
		return ret;
	}
	
}
