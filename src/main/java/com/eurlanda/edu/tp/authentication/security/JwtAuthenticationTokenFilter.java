package com.eurlanda.edu.tp.authentication.security;

import com.eurlanda.edu.tp.Util.Utility;
import com.eurlanda.edu.tp.authentication.auth.AuthServiceImpl;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by Justin on 2017/6/3.
 */

@Service
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    static Logger log = Logger.getLogger(JwtAuthenticationTokenFilter.class.getName());

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthServiceImpl authService;

    @Value("${token.tokenHeader}")
    private String tokenHeader;

    @Value("${token.tokenHead}")
    private String tokenHead;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String str = request.getRequestURI();
            String rex = "^/record/[1-9]+/logout/[1-9]+$";
            boolean isLogoutRequest = Pattern.matches(rex, str);
            if(isLogoutRequest){
                filterChain.doFilter(request, response);
                return;
            }
            String authHeader = request.getHeader(this.tokenHeader);
            if (authHeader != null && authHeader.startsWith(tokenHead)) {
                final String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
                String username = jwtTokenUtil.getUsernameFromToken(authToken);

                logger.info("checking authentication " + username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {   //??? rain

                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                    if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                                request));
                        logger.info("authenticated user " + username + ", setting security context");
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
                //是否解析license信息
                if(!AuthServiceImpl.isCheck){
                    authService.licenseValidate();
                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e){
            logger.warn(String.format("User's token is expired. Message: %s", e.getMessage()));
            Utility.writeError(request, response, HttpStatus.OK, ErrorCode.TokenExpired);
        } catch (SignatureException e){
            logger.warn(String.format("User's token is corrupted. Message: %s", e.getMessage()));
            Utility.writeError(request, response, HttpStatus.OK, ErrorCode.NeedAuthentication);
        } catch (MalformedJwtException e){
            logger.warn(String.format("User's token is corrupted. Message: %s", e.getMessage()));
            Utility.writeError(request, response, HttpStatus.OK, ErrorCode.NeedAuthentication);
        } catch (UsernameNotFoundException e){
            logger.warn(String.format("Username in the User's token is not found. Message: %s", e.getMessage()));
            Utility.writeError(request, response, HttpStatus.OK, ErrorCode.NeedAuthentication);
        }catch (ApplicationErrorException e){
            logger.warn(String.format("User's token is expired. Message: %s", e.getMessage()));
            if(e.getErrorCode() == ErrorCode.AlreadyResetPassword.getCode()){
                Utility.writeError(request, response, HttpStatus.OK, ErrorCode.AlreadyResetPassword);
            }else if(e.getErrorCode() == ErrorCode.Offline.getCode()){
                Utility.writeError(request, response, HttpStatus.OK, ErrorCode.Offline);
            }else if(e.getErrorCode() == ErrorCode.TokenExpired.getCode()){
                Utility.writeError(request, response, HttpStatus.OK, ErrorCode.TokenExpired);
            }
        }
    }



}
