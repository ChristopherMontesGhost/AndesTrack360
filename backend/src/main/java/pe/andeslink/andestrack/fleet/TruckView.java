package pe.andeslink.andestrack.fleet;
import pe.andeslink.andestrack.domain.entity.Camion;
public record TruckView(Integer id,String plate,String model,double capacityTon,String state){public static TruckView from(Camion c){return new TruckView(c.getIdCamion(),c.getPlaca(),c.getModelo(),c.getCapacidadTon().doubleValue(),c.getEstado().name());}}