package com.example.arshit.studentattendanceapp.FragmentManagement;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public interface FragmentHandler {
    void launchFragment(ResponsiveFragment fragment);

    void popFragment(ResponsiveFragment fragment);

    void popFragmentUpTo(Class<? extends ResponsiveFragment> fragmentClass, ResponsiveFragment with);

    void popFragmentUpToNonInclusive(Class<? extends ResponsiveFragment> fragmentClass);

    class Helper {
        private static Map<Method, Object[]> pendingTransactionMap = new LinkedHashMap<>();

        public static void addPendingTransaction(Method method, Object... params) {
            pendingTransactionMap.put(method, params);
        }

        public static boolean executePendingTransactions(FragmentHandler fragmentHandler) {
            for (Method method : pendingTransactionMap.keySet()) {
                try {
                    method.invoke(fragmentHandler, pendingTransactionMap.get(method));
                } catch (Exception e) {
                    pendingTransactionMap.clear();
                    e.printStackTrace();
                    return false;
                }
            }
            pendingTransactionMap.clear();
            return true;
        }
    }
}
