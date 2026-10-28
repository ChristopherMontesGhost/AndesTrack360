package pe.andeslink.andestrack.shipment;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;
@Component
public class RouteEstimator {
    private record Point(double lat,double lon){}
    private static final Map<String,Point> CITY=ImmutableMap.<String,Point>builder().put("Lima",new Point(-12.046,-77.043)).put("Arequipa",new Point(-16.409,-71.538)).put("Cusco",new Point(-13.532,-71.968)).put("Trujillo",new Point(-8.112,-79.029)).put("Piura",new Point(-5.195,-80.633)).put("Puno",new Point(-15.840,-70.022)).put("Ica",new Point(-14.068,-75.729)).put("Huancayo",new Point(-12.065,-75.205)).put("Chiclayo",new Point(-6.771,-79.841)).build();
    public BigDecimal distanceKm(String origin,String destination){Preconditions.checkArgument(origin!=null&&destination!=null&&!origin.equalsIgnoreCase(destination),"Origen y destino deben ser distintos");Point a=CITY.get(origin),b=CITY.get(destination);if(a==null||b==null)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Ciudad fuera del catálogo de demo");double lat1=Math.toRadians(a.lat),lat2=Math.toRadians(b.lat),dLat=lat2-lat1,dLon=Math.toRadians(b.lon-a.lon);double h=Math.pow(Math.sin(dLat/2),2)+Math.cos(lat1)*Math.cos(lat2)*Math.pow(Math.sin(dLon/2),2);return BigDecimal.valueOf(6371*2*Math.atan2(Math.sqrt(h),Math.sqrt(1-h))*1.28).setScale(1,RoundingMode.HALF_UP);}
    public LocalDateTime eta(BigDecimal distanceKm,BigDecimal averageSpeedKmh,LocalDateTime departure){Preconditions.checkArgument(averageSpeedKmh!=null&&averageSpeedKmh.signum()>0,"La velocidad promedio debe ser mayor a cero");long seconds=Math.round(distanceKm.doubleValue()/averageSpeedKmh.doubleValue()*3600);return departure.plusSeconds(seconds);}
}