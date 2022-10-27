package com.eurlanda.edu.tp.authentication.security;

import com.eurlanda.edu.tp.Util.DesUtils;
import com.eurlanda.edu.tp.authentication.auth.AuthServiceImpl;
import com.eurlanda.edu.tp.dao.entity.User;
import com.eurlanda.edu.tp.dao.mapper.UserAccessRecordMapper;
import com.eurlanda.edu.tp.enums.RoleEnum;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.error.ResponseMessage;
import com.eurlanda.edu.tp.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Justin on 2017/6/2.
 */

@Service
public class JwtTokenUtil implements Serializable {
    static Logger log = Logger.getLogger(JwtTokenUtil.class.getName());

    private static final long serialVersionUID = -3301605591108950415L;

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";

    @Value("token.secret")
    private String secret;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAccessRecordMapper userAccessRecordMapper;

    private Long expiration = 3600L * 24 * 365 * 10 ;

    public String getUsernameFromToken(String token) {
        String username;
        final Claims claims = getClaimsFromToken(token);
        username = claims.getSubject();
        return username;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        final Claims claims = getClaimsFromToken(token);
        created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        final Claims claims = getClaimsFromToken(token);
        expiration = claims.getExpiration();
        return expiration;
    }

    public Claims getClaimsFromToken(String token) {
        Claims claims;
        claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }


    private Boolean validateAccountAndPassword(String token){

        return true;
    }


    private Boolean isTokenExpired(String token) throws ApplicationErrorException{
        final Date expiration = getExpirationDateFromToken(token);
        final Date createdDate = getCreatedDateFromToken(token);
        String username = getUsernameFromToken(token);
        User user = userService.selectByUserName2(username);
        Long tokenCreateTime = user.getTokenCreateTime();
        if(tokenCreateTime == null){
            if(user.getRole() == RoleEnum.MANAGER.getCode()){
                return false;
            }
            log.info("isTokenExpired检查该用户token创建时间null,token已经过期,username: " + username);
            //throw new ApplicationErrorException(ErrorCode.TokenExpired);
            return false;
        }
        return false;
        /*if(tokenCreateTime.intValue() == -1){
            throw new ApplicationErrorException(ErrorCode.AlreadyResetPassword);
        }
        if(tokenCreateTime == createdDate.getTime()){
            return false;
        }else {
            if(user.getRole() == RoleEnum.MANAGER.getCode()){
                //管理员可以同时登
                return false;
            }else {
                throw new ApplicationErrorException(ErrorCode.Offline);
            }
        }*/
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) throws ApplicationErrorException{
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && !isTokenExpired(token);
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token, UserDetails userDetails)throws ApplicationErrorException {
        JwtUser user = (JwtUser) userDetails;
        final String username = getUsernameFromToken(token);
        final Date created = getCreatedDateFromToken(token);
        //final Date expiration = getExpirationDateFromToken(token);
        return (
                username.equals(user.getUsername())
                        && !isTokenExpired(token)
                        && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate()));
    }

    public static String generateTimeToken(Integer userId){
        String v = System.currentTimeMillis()+"^"+userId;
        String encryptDes = DesUtils.encryptBasedDes(v);
        return encryptDes;
    }
}
