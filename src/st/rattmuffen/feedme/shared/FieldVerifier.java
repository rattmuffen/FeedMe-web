package st.rattmuffen.feedme.shared;


public class FieldVerifier {

	public static boolean isValidAddress(String address) {
		if (address == null) {
			return false;
		}
		return address.startsWith("http");
	}
}
