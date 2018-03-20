package com.olympicweightlifting.billing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.olympicweightlifting.R;
import com.olympicweightlifting.authentication.profile.ProfileActivity;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class BillingManager implements PurchasesUpdatedListener {

    public static final String SKU_TYPE_PREMIUM = "premium";
    private static final String BASE_64_ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnN01i27/yIV1m1dtSGEI0Z+BDmdCqdm9/Vwza99zJ/1n5uYG6mWpSTnSy5" +
            "7kkxT8J7ab+Om6DXzGBjufHI9Lbiwoe2krelwtn9q1WT0+O4hDNoBahD9T27rfDGaUAtCIGUwS024XIyUAaykTLJZRl7iGwhbsn24gRt5+gPm0JRn5UOwIPiX9yR1Bu69ASkkj/vhacRGvjCuUtO8wE" +
            "OLqV1pkPKPX1R/zrxVrxlBEz4fAK9CUYc8frbJ+FIvbK6x132Mm0MmUj93VhE9hRrsFAwk4KLrXpeGbumqVMg6cXQMRg3sQ04z3k1yXVc4xcS392NqXW/1ayfn6DC+tb2NeRwIDAQAB";
    private Activity activity;
    private BillingListener billingListener;
    private BillingClient billingClient;
    private boolean isServiceConnected;


    public BillingManager(Activity activity, final BillingListener updatesListener) {
        this.activity = activity;
        billingListener = updatesListener;
        billingClient = BillingClient.newBuilder(activity).setListener(this).build();

        startServiceConnection(this::queryPurchases);
    }

    public void startServiceConnection(final Runnable executeOnSuccess) {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    isServiceConnected = true;
                    if (executeOnSuccess != null) {
                        executeOnSuccess.run();
                    }
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                isServiceConnected = false;
            }
        });
    }


    public void queryPurchases() {
        Runnable queryToExecute = () -> {
            Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP);
            onQueryPurchasesFinished(purchasesResult);
        };

        executeServiceRequest(queryToExecute);
    }

    private void onQueryPurchasesFinished(Purchase.PurchasesResult result) {
        if (billingClient == null || result.getResponseCode() != BillingClient.BillingResponse.OK) {
            return;
        }
        billingListener.onPurchasesQueried(result.getPurchasesList());
    }


    private void executeServiceRequest(Runnable runnable) {
        if (isServiceConnected) {
            runnable.run();
        } else {
            startServiceConnection(runnable);
        }
    }


    @Override
    public void onPurchasesUpdated(int resultCode, List<Purchase> purchases) {
        if (resultCode == BillingClient.BillingResponse.OK) {
            for (Purchase purchase : purchases) {
                if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
                    purchases.remove(purchase);
                }
            }
            billingListener.onPurchasesUpdated(purchases);
        }
    }

    private boolean verifyValidSignature(String signedData, String signature) {
        try {
            return BillingSecurity.verifyPurchase(BASE_64_ENCODED_PUBLIC_KEY, signedData, signature);
        } catch (IOException e) {
            return false;
        }
    }

    public void initiatePurchaseFlow() {
        Runnable purchaseFlowRequest = () -> {
            BillingFlowParams.Builder params = BillingFlowParams.newBuilder().
                    setSku(SKU_TYPE_PREMIUM).setType(BillingClient.SkuType.INAPP).setOldSkus(null);
            billingClient.launchBillingFlow(activity, params.build());
        };

        executeServiceRequest(purchaseFlowRequest);
    }

    public boolean isUserPremium(List<Purchase> purchases) {
        for (Purchase purchase : purchases) {
            if (Objects.equals(purchase.getSku(), SKU_TYPE_PREMIUM)) {
                return true;
            }
        }
        return false;
    }

    public void promptUserToUpgrade(Activity activity, ViewGroup layout) {
        Snackbar snackbar = Snackbar.make(layout,
                R.string.profile_reached_free_limit, Snackbar.LENGTH_LONG);
        ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
        snackbar.setAction(R.string.all_upgrade, snackbarView -> activity.startActivity(new Intent(activity, ProfileActivity.class)));
        snackbar.show();
    }


    public void destroy() {
        if (billingClient != null && billingClient.isReady()) {
            billingClient.endConnection();
            billingClient = null;
        }
    }
}
