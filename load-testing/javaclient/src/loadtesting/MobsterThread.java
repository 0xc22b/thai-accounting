package loadtesting;

public class MobsterThread implements Runnable {
	
	public interface SignUpCallback {
	    public void result(String result);
	}
	
	private SignUpClient signUpClient;
	private SignUpCallback callback;
	private String username;
	private String email;
	private String password;
	private String repeatPassword;
	
	public MobsterThread(SignUpClient signUpClient, SignUpCallback callback, String username,
	        String email, String password, String repeatPassword) {
		this.signUpClient = signUpClient;
		this.callback = callback;
		this.username = username;
		this.email = email;
		this.password = password;
		this.repeatPassword = repeatPassword;
	}

	@Override
	public void run() {
	    String s = signUpClient.signUp(username, email, password, repeatPassword);
	    callback.result(s);
	}

}
