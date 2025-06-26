package top.zymd3414.sm_springboot.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;

// 作为基础路径
@Component
@ConfigurationProperties(prefix = "spring.web.resources")
public class StaticResourceProperties {
    private String staticLocations = "";

    // Getter和Setter
    public String getStaticLocations() {
        return staticLocations;
    }
    public void setStaticLocations(List<String> staticLocations) {
        this.staticLocations = staticLocations.getFirst().split("file:")[1] + "/";
    }
}
