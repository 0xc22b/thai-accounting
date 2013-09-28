package gwt.server;

import gwt.server.user.UserManager;
import gwt.server.user.UserVerifier;
import gwt.server.user.model.Session;
import gwt.server.user.model.User;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;


public class RpcServiceImplTest {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig(),
            new LocalMemcacheServiceTestConfig());
    
    private RpcServiceImpl rpcService;
    private User user;
    private Session session;
    
    @Before
    public void setUp() {
        helper.setUp();
        
        UserVerifier.Log log = new UserVerifier.Log();
        user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        session = UserManager.addLoginSession(user.getKey());
        
        rpcService = new RpcServiceImpl();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }
    
    public void test1() {
        Assert.assertNotNull(rpcService);
        Assert.assertNotNull(user);
        Assert.assertNotNull(session);
    }
}
