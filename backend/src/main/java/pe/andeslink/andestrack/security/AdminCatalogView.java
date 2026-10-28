package pe.andeslink.andestrack.security;
import java.util.List;
public record AdminCatalogView(List<AccountOption> clients,List<AccountOption> drivers,List<TruckOption> trucks){public record AccountOption(String email,String name){} public record TruckOption(String plate,String model,double capacityTon,String state){}}