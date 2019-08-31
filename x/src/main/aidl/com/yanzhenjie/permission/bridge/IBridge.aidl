package com.yanzhenjie.permission.bridge;

interface IBridge {
    /**
     * Request for permissions.
     */
    void requestAppDetails();

    /**
     * Request for permissions.
     */
    void requestPermission(in String[] permissions);

    /**
     * Request for package install.
     */
    void requestInstall() ;

   /**
    * Request for overlay.
    */
    void requestOverlay() ;

   /**
    * Request for alert window.
    */
    void requestAlertWindow();

   /**
    * Request for notify.
    */
    void requestNotify() ;

   /**
    * Request for notification listener.
    */
    void requestNotificationListener() ;

   /**
    * Request for write system setting.
    */
    void requestWriteSetting() ;
}