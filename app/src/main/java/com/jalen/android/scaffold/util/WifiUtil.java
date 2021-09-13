package com.jalen.android.magnus.vm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class WifiUtil {
    public static void clearWifiInfo(Context var0, String var1) {
        WifiManager var3;
        String var5;
        label15: {
            var3 = (WifiManager)var0.getSystemService(Context.WIFI_SERVICE);
            StringBuilder var2 = new StringBuilder();
            var2.append("\"");
            var2.append(var1);
            var2.append("\"");
            var2.toString();
            if (var1.startsWith("\"")) {
                var5 = var1;
                if (var1.endsWith("\"")) {
                    break label15;
                }
            }

            var2 = new StringBuilder();
            var2.append("\"");
            var2.append(var1);
            var2.append("\"");
            var5 = var2.toString();
        }

        WifiConfiguration var4 = getWifiConfig(var0, var5);
        var4.allowedAuthAlgorithms.clear();
        var4.allowedGroupCiphers.clear();
        var4.allowedKeyManagement.clear();
        var4.allowedPairwiseCiphers.clear();
        var4.allowedProtocols.clear();
        if (var4 != null) {
            var3.removeNetwork(var4.networkId);
            var3.saveConfiguration();
        }

    }

    public static void connectWifi(Context var0, String var1) {
        if (getWifiEnabled(var0)) {
            WifiManager var2 = (WifiManager)var0.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiConfiguration var3 = new WifiConfiguration();
            StringBuilder var4 = new StringBuilder();
            var4.append("\"");
            var4.append(var1);
            var4.append("\"");
            var3.SSID = var4.toString();
            WifiConfiguration var5 = getWifiConfig(var0, var1);
            if (var5 != null) {
                var2.disconnect();
                var2.enableNetwork(var5.networkId, true);
            }

        }
    }

    public static void connectWifi(Context var0, String var1, String var2, int var3) {
        if (getWifiEnabled(var0)) {
            WifiManager var4 = (WifiManager)var0.getSystemService(Context.WIFI_SERVICE);
            WifiConfiguration var5 = new WifiConfiguration();
            var5.allowedAuthAlgorithms.clear();
            var5.allowedGroupCiphers.clear();
            var5.allowedKeyManagement.clear();
            var5.allowedPairwiseCiphers.clear();
            var5.allowedProtocols.clear();
            StringBuilder var6 = new StringBuilder();
            var6.append("\"");
            var6.append(var1);
            var6.append("\"");
            var5.SSID = var6.toString();
            WifiConfiguration var8 = getWifiConfig(var0, var1);
            if (var8 != null) {
                var4.removeNetwork(var8.networkId);
            }

            if (var3 != 0 && var3 != 1 && var3 != 2) {
                if (var3 != 3) {
                    if (var3 == 4) {
                        var5.allowedKeyManagement.set(0);
                    }
                } else {
                    var5.hiddenSSID = true;
                    String[] var9 = var5.wepKeys;
                    StringBuilder var7 = new StringBuilder();
                    var7.append("\"");
                    var7.append(var2);
                    var7.append("\"");
                    var9[0] = var7.toString();
                    var5.allowedAuthAlgorithms.set(1);
                    var5.allowedGroupCiphers.set(3);
                    var5.allowedGroupCiphers.set(2);
                    var5.allowedGroupCiphers.set(0);
                    var5.allowedGroupCiphers.set(1);
                    var5.allowedKeyManagement.set(0);
                }
            } else {
                var6 = new StringBuilder();
                var6.append("\"");
                var6.append(var2);
                var6.append("\"");
                var5.preSharedKey = var6.toString();
                var5.hiddenSSID = true;
                var5.allowedAuthAlgorithms.set(0);
                var5.allowedGroupCiphers.set(2);
                var5.allowedGroupCiphers.set(3);
                var5.allowedKeyManagement.set(1);
                var5.allowedPairwiseCiphers.set(2);
                var5.allowedPairwiseCiphers.set(1);
                var5.status = 2;
            }

            var3 = var4.addNetwork(var5);
            if (var3 == -1) {
                connectWifi(var0, var1);
            } else {
                var4.disconnect();
                var4.enableNetwork(var3, true);
            }
        }
    }

    public static void disConnectWifi(Context var0, int var1) {
        WifiManager var2 = (WifiManager)var0.getSystemService(Context.WIFI_SERVICE);
        var2.disableNetwork(var1);
        var2.disconnect();
    }

    public static boolean disconnect(Context var0) {
        return ((WifiManager)var0.getApplicationContext().getSystemService(Context.WIFI_SERVICE)).disconnect();
    }

    public static WifiInfo getCurrentWifi(Context var0) {
        return ((WifiManager)var0.getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
    }

    private static int getItemPosition(List<ScanResult> var0, ScanResult var1) {
        for(int var2 = 0; var2 < var0.size(); ++var2) {
            if (var1.SSID.equals(((ScanResult)var0.get(var2)).SSID)) {
                return var2;
            }
        }

        return -1;
    }

    @SuppressLint("MissingPermission")
    public static WifiConfiguration getWifiConfig(Context var0, String var1) {
        if (TextUtils.isEmpty(var1)) {
            return null;
        } else {
            String var2;
            label23: {
                if (var1.startsWith("\"")) {
                    var2 = var1;
                    if (var1.endsWith("\"")) {
                        break label23;
                    }
                }

                StringBuilder var5 = new StringBuilder();
                var5.append("\"");
                var5.append(var1);
                var5.append("\"");
                var2 = var5.toString();
            }

            Iterator var3 = ((WifiManager)var0.getSystemService(Context.WIFI_SERVICE)).getConfiguredNetworks().iterator();

            WifiConfiguration var4;
            do {
                if (!var3.hasNext()) {
                    return null;
                }

                var4 = (WifiConfiguration)var3.next();
            } while(!var2.equalsIgnoreCase(var4.SSID));

            return var4;
        }
    }

    public static boolean getWifiEnabled(Context context) {
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).isWifiEnabled();
    }

    public static int getWifiEncryptType(String var0) {
        if (TextUtils.isEmpty(var0)) {
            return -1;
        } else if (var0.contains("WPA") && var0.contains("WPA2")) {
            return 0;
        } else if (var0.contains("WPA2")) {
            return 1;
        } else if (var0.contains("WPA")) {
            return 2;
        } else {
            return var0.contains("WEP") ? 3 : 4;
        }
    }

    public static String getWifiEncryptTypeStr(String var0) {
        if (TextUtils.isEmpty(var0)) {
            return null;
        } else if (var0.contains("WPA") && var0.contains("WPA2")) {
            return "WPA/WPA2 PSK";
        } else if (var0.contains("WPA2")) {
            return "WPA2 PSK";
        } else if (var0.contains("WPA")) {
            return "WPA PSK";
        } else {
            return var0.contains("WEP") ? "WEP" : "NONE";
        }
    }

    public static String getWifiIp(Context var0) {
        int var1 = ((WifiManager)var0.getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getIpAddress();
        StringBuilder var2 = new StringBuilder();
        var2.append(var1 & 255);
        var2.append(".");
        var2.append(var1 >> 8 & 255);
        var2.append(".");
        var2.append(var1 >> 16 & 255);
        var2.append(".");
        var2.append(var1 >> 24 & 255);
        return var2.toString();
    }

    public static List<ScanResult> getWifiList(Context var0) {
        WifiManager var3 = (WifiManager)var0.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List var5 = var3.getScanResults();
        WifiInfo var6 = var3.getConnectionInfo();
        ArrayList var4 = new ArrayList();

        for(int var1 = 0; var1 < var5.size(); ++var1) {
            if (!((ScanResult)var5.get(var1)).SSID.isEmpty() && (var6 == null || var6.getSSID() == null || !((ScanResult)var5.get(var1)).BSSID.equals(var6.getBSSID())) && !((ScanResult)var5.get(var1)).SSID.contains("0460")) {
                int var2 = getItemPosition(var4, (ScanResult)var5.get(var1));
                if (var2 != -1) {
                    if (((ScanResult)var4.get(var2)).level < ((ScanResult)var5.get(var1)).level) {
                        var4.remove(var2);
                        var4.add(var2, var5.get(var1));
                    }
                } else {
                    var4.add(var5.get(var1));
                }
            }
        }

        Collections.sort(var4, new Comparator<ScanResult>() {
            public int compare(ScanResult var1, ScanResult var2) {
                return var2.level - var1.level;
            }
        });
        return var4;
    }

    public static int getWifiLockType(String var0) {
        if (TextUtils.isEmpty(var0)) {
            return -1;
        } else if (var0.contains("WPA") && var0.contains("WPA2")) {
            return 0;
        } else if (var0.contains("WPA2")) {
            return 1;
        } else if (var0.contains("WPA")) {
            return 2;
        } else {
            return var0.contains("WEP") ? 3 : 4;
        }
    }
}
