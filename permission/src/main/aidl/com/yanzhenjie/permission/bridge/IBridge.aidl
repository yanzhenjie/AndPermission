package com.yanzhenjie.permission.bridge;

interface IBridge {
    /**
     * Request for permissions.
     */
    void requestAppDetails(in String suffix);

    /**
     * Request for permissions.
     */
    void requestPermission(in String suffix, in String[] permissions);

    /**
     * Request for package install.
     */
    void requestInstall(in String suffix);

   /**
    * Request for overlay.
    */
    void requestOverlay(in String suffix);

   /**
    * Request for alert window.
    */
    void requestAlertWindow(in String suffix);

   /**
    * Request for notify.
    */
    void requestNotify(in String suffix);

   /**
    * Request for notification listener.
    */
    void requestNotificationListener(in String suffix);

   /**
    * Request for write system setting.
    */
    void requestWriteSetting(in String suffix);
}