package uo.ri.cws.application.persistence.client;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway;

public interface ClientGateway extends Gateway<ClientGateway.ClientRecord> {

    Optional<ClientRecord> findByNif(String nif);

    List<ClientRecord> findRecommendedByNif(String recommenderNif);

    /**
     * @return the clients with some work order invoiced in an invoice in the given
     *         state
     */
    List<ClientRecord> findWithInvoicesInState(String state);

    class ClientRecord extends Gateway.BaseRecord {
        public String nif;
        public String name;
        public String surname;
        public String email;
        public String phone;
        public String street;
        public String city;
        public String postalCode;
        public String country;
    }

}
