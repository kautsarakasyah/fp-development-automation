
package finalproject.gopaynew.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalproject.gopaynew.dto.GenericResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper; // Spring's default JSON mapper

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Let SecurityConfig handle this. If the endpoint is protected,
            // it will be rejected later. If it's public (like /health), it will pass.
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        try {
            if (jwtUtil.validateToken(jwt)) {
                // If the token is valid, create a simple Authentication object.
                // In a real app, we might load UserDetails from a database.
                // Here, we just need to mark the request as authenticated.
                UserDetails userDetails = new User("bni_partner", "", new ArrayList<>());
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                filterChain.doFilter(request, response); // Proceed to the next filter
            } else {
                // If validateToken returns false, it's an invalid token.
                throw new Exception("Invalid token signature or claims.");
            }
        } catch (Exception e) {
            // This block catches any error during token validation (expired, malformed, etc.)
            System.err.println("[GoPay-Java] JWT validation failed: " + e.getMessage());
            
            // Create a structured JSON error response
            GenericResponse errorResponse = new GenericResponse("FAILED", "Invalid or unauthorized token.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}
