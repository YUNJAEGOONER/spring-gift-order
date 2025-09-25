package gift.infra.config;

import gift.infra.LoggedInMemberArgumentResolver;
import gift.infra.interceptor.AdminCheckInterceptor;
import gift.infra.interceptor.LoginCheckInterceptor;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableAsync
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String FE_ADDRESS = "http://localhost:3000";
    private final LoginCheckInterceptor loginCheckInterceptor;
    private final AdminCheckInterceptor adminCheckInterceptor;
    private final LoggedInMemberArgumentResolver loggedInMemberArgumentResolver;

    public WebConfig(
            LoginCheckInterceptor loginCheckInterceptor,
            AdminCheckInterceptor adminCheckInterceptor,
            LoggedInMemberArgumentResolver loggedInMemberArgumentResolver
    ) {
        this.loginCheckInterceptor = loginCheckInterceptor;
        this.adminCheckInterceptor = adminCheckInterceptor;
        this.loggedInMemberArgumentResolver = loggedInMemberArgumentResolver;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/view/my/**", "/api/wishlist/**", "/api/orders/**");
        registry.addInterceptor(adminCheckInterceptor)
                .addPathPatterns("/view/admin/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loggedInMemberArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry){
        corsRegistry.addMapping("/**")
                .allowedOrigins(FE_ADDRESS)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .exposedHeaders("Location")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}