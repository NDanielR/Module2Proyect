package model;

//clase modelo de los clientes
public record Customer(
    String id,
    String name,
    Integer creditScore,
    String city) {

}
