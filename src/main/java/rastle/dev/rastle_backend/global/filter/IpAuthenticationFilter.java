package rastle.dev.rastle_backend.global.filter;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import jakarta.servlet.*;
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
        String ipAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        InetAddress inetAddress = InetAddress.getByName(ipAddress);
        String country = null;
        try {
            country = databaseReader.country(inetAddress).getCountry().getName();
        } catch (GeoIp2Exception e) {
            log.warn(e.getMessage());
        }
        if(country == null || !country.equals("South Korea")){
            log.warn("Access Rejected : {}, {}", ipAddress, country);
            return;
        }
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
