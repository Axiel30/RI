package uo.ri.cws.application.persistence.client;

import java.util.Optional;
import uo.ri.cws.application.persistence.Gateway;
import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;

public interface ClientGateway extends Gateway<ClientRecord> {

	Optional<ClientRecord> findByNif(String nif);

	public static class ClientRecord extends BaseRecord {
		public String nif;
		public String name;
		public String surname;
		public String email;
		public String phone;
		public String city;
		public String street;
		public String zipcode;
	}
}