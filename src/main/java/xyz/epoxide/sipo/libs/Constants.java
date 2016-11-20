package xyz.epoxide.sipo.libs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constants {
    public static final String MODID = "sipo";
    public static final String MOD_NAME = "SIPO";
    public static final String VERSION = "0.0.0.1";
    public static final String SERVER = "xyz.epoxide.sipo.common.ProxyCommon";
    public static final String CLIENT = "xyz.epoxide.sipo.client.ProxyClient";

    public static final Logger LOG = LogManager.getLogger(MOD_NAME);
    public static final int PACKET_VERSION = 1;
}
