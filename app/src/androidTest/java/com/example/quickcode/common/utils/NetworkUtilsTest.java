package com.example.quickcode.common.utils;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.net.ConnectivityManager;

import org.junit.Test;

public class NetworkUtilsTest {

    @Test
    public void shouldReturnNetworkNotAvailable() {
        ConnectivityManager connectivityManager = mock(ConnectivityManager.class);
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(mock(android.net.NetworkInfo.class));
        when(connectivityManager.getActiveNetworkInfo().isConnected()).thenReturn(false);
        assertFalse(NetworkUtils.isNetworkAvailable(connectivityManager));
    }
}