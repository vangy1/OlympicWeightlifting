package com.olympicweightlifting.billing;

import com.android.billingclient.api.Purchase;

import java.util.List;

public interface BillingListener {
    void onPurchasesQueried(List<Purchase> purchases);

    void onPurchasesUpdated(List<Purchase> purchases);
}