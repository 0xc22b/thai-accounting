package loadtesting;


import java.util.ArrayList;

public class Main {
	
    public static final String SIGN_UP_URL = "http://127.0.0.1:8888/signup";
	public static final int N = 100;
	public static final int M = 20;
	
	static public void main(String[] args) throws Exception {
		doMobTest();
	}
	
	static private void doMobTest() throws Exception {

		MobsterThread.SignUpCallback callback = new MobsterThread.SignUpCallback() {
            @Override
            public void result(String result) {
                System.out.println(result);
                System.out.println();
            }
        };

		SignUpClient client = new SignUpClient(SIGN_UP_URL);
		
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < N; i++) {
			int j = 1 + (i % M);
			threads.add(new Thread(new MobsterThread(client, callback, "user" + j,
			        "user" + j + "@mail.com", "asdfjkl;", "asdfjkl;")));
		}

		for (int i = 0; i < N; i++) {
			threads.get(i).start();
		}
		for (int i = 0; i < N; i++) {
			threads.get(i).join();
		}
		
		System.out.println("Done.");
	}
}
