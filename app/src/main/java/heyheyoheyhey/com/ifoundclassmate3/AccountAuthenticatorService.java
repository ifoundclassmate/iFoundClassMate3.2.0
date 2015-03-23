package heyheyoheyhey.com.ifoundclassmate3;

/**
 * Created by yunfei on 3/5/2015.
 */
import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
/**
 * Authenticator service that returns a subclass of AbstractAccountAuthenticator in onBind().
 */
public class AccountAuthenticatorService extends Service {
    /**
     * The tag used for the logs.
     */
    private static final String LOG_TAG = AccountAuthenticatorService.class.getSimpleName();
    /**
     * The implementation of the class |AccountAuthenticatorImpl|.
     * It is implemented as a singleton
     */
    // Instance field that stores the authenticator object
    private AccountAuthenticatorImpl mAuthenticator;
    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new AccountAuthenticatorImpl(this);
    }
    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();

    }
    /**
     * The class which implements the class |AbstractAccountAuthenticator|.
     * It is the one which the Android system calls to perform any action related with the account
     */
        public class AccountAuthenticatorImpl extends AbstractAccountAuthenticator {
        /**
         * The Context used.
         */
        private final Context mContext;
        /**
         * The main constructor of the class.
         * @param context The context used
         */
        public AccountAuthenticatorImpl(Context context) {
            super(context);
            mContext = context;
        }
        @Override
        public Bundle addAccount(AccountAuthenticatorResponse response,
                                 String accountType,
                                 String authTokenType,
                                 String[] requiredFeatures,
                                 Bundle options) throws NetworkErrorException {
            System.out.println("accountType: " + accountType);
            final Intent intent = new Intent(mContext, LoginActivity.class);
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
            final Bundle bundle = new Bundle();
            bundle.putParcelable(AccountManager.KEY_INTENT, intent);
            return bundle;
        }
        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse arg0, Account arg1,
                                         Bundle arg2) throws NetworkErrorException {
            return null;
        }
        @Override
        public Bundle editProperties(AccountAuthenticatorResponse arg0, String arg1) {
            return null;
        }
        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse arg0, Account arg1,
                                   String arg2, Bundle arg3) throws NetworkErrorException {
            return null;
        }
        @Override
        public String getAuthTokenLabel(String arg0) {
            return null;
        }
        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse arg0, Account arg1,
                                  String[] arg2) throws NetworkErrorException {
            return null;
        }
        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse arg0, Account arg1,
                                        String arg2, Bundle arg3) throws NetworkErrorException {
            return null;
        }
    }

    public class MyBinder extends Binder {
        AccountAuthenticatorService getService() {
            return AccountAuthenticatorService.this;
        }
    }
}