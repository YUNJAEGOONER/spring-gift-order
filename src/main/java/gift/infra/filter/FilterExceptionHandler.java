package gift.infra.filter;

import gift.jwt.exception.LoginError;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
public class FilterExceptionHandler extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (LoginError e) {
            logger.info("[FilterExceptionHandlerForView]" + e.getErrorCode());
            request.setAttribute("errormsg", e.getErrorCode().getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/view/login/error");
            dispatcher.forward(request, response);
        }
    }
}