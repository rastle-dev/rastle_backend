package rastle.dev.rastle_backend.global.filter;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rastle.dev.rastle_backend.global.util.HttpReqResUtil;

import java.io.IOException;
import java.net.InetAddress;

@Slf4j
@Component
public class IpAuthenticationFilter implements Filter {

    @Autowired
    private DatabaseReader databaseReader;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        filter((HttpServletRequest) request, (HttpServletResponse) response, chain);


    }

    public void filter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String ipAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        InetAddress inetAddress = InetAddress.getByName(ipAddress);
        String country = null;
        if (request.getRequestURI().equals("/env_profile") || ipAddress.equals("127.0.0.1")) {
            chain.doFilter(request, response);
        } else {
            try {
                country = databaseReader.country(inetAddress).getCountry().getName();
            } catch (GeoIp2Exception e) {
                log.warn(e.getMessage());
            }
            if (country == null || !(country.equals("South Korea") || country.equals("United States"))) {
                log.warn("Access Rejected : {}, {}", ipAddress, country);
                return;
            }
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
