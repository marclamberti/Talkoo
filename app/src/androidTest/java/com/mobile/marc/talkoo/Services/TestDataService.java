//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243622        mlamberti@ust.hk

package com.mobile.marc.talkoo.Services;

import android.test.ServiceTestCase;
import android.content.Intent;

import com.mobile.marc.talkoo.NavigatorActivity;

public class TestDataService extends ServiceTestCase<DataService> {

    private Intent intent_;

    public TestDataService() {
        super(DataService.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        intent_ = new Intent(this.getSystemContext(), DataService.class);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

   /*
   ** Checks if the client receiver is created by the DataService when the NavigatorActivity
   ** specify that the user is client in a discussion.
    */
    public void testDataServiceIsClient() {
        NavigatorActivity.isClient = true;
        this.startService(intent_);
        assertTrue(getService().client_created);
    }

    /*
    ** Checks if the group owner receiver is created by the DataService when the NavigatorActivity
    ** specify that the user is a server in a discussion.
    */
    public void testDataServiceIsServer() {
        NavigatorActivity.isOwner = true;
        this.startService(intent_);
        assertTrue(getService().server_created);
    }
}