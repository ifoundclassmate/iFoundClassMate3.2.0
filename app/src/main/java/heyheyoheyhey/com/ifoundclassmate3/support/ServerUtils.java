package heyheyoheyhey.com.ifoundclassmate3.support;

/**
 * Created by Av23 on 2015-02-25.
 */
public class ServerUtils {

    // SERVER/LOCAL ACCESS FLAG
    public static boolean BYPASS_SERVER = false;

    public final static String TEMP_IP = "99.236.119.157";
    public final static int PORT_FRIEND = 3454;
    public final static int TEMP_PORT = 3455;
    public final static int PORT_COURSE = 3456;
    public final static int PORT_GROUP = 3457;

    public final static String TASK_ADD_COURSE = "ac";
    public final static String TASK_RETRIEVE_COURSES = "rmc";
    public final static String TASK_DELETE_COURSE = "dc";
    public final static String TASK_ADD_FRIEND = "af";
    public final static String TASK_RETRIEVE_FRIENDS = "rmf";
    public final static String TASK_RETRIEVE_USERS_ENROLLED_IN_COURSE = "rubc"; // needs update
    public final static String TASK_RETRIEVE_FRIEND_COURSES = "rfc";

    public final static String TASK_CREATE_GROUP = "cg";
    public final static String TASK_ADD_USER_TO_GROUP = "autg";
    public final static String TASK_RETRIEVE_GROUPS = "rg";
}
