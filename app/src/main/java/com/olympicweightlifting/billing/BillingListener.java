package com.olympicweightlifting.billing;

import com.android.billingclient.api.Purchase;

import java.util.List;

/**
 * Created by vangor on 20/02/2018.
 */

public interface BillingListener {
    void onPurchasesQueried(List<Purchase> purchases);

    void onPurchasesUpdated(List<Purchase> purchases);
}