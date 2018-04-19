package com.gzedu.xlims.common;

import java.net.InetAddress;
import java.security.SecureRandom;


/**
 * Generate a unique identifier.
 *
 * @since 3.0
 */
public class SequenceUUID {
	
	/**
	 * 获得Sequence的静态方法
	 * @return
	 */
	public static String getSequence() {
		return new SequenceUUID().getUUID();
	}
	
    protected static String midValue = null;

    public String getUUID() {
        long timeNow = System.currentTimeMillis();
        int timeLow = (int) timeNow & 0xFFFFFFFF;

        if (midValue == null) {
            try {
                InetAddress inet = InetAddress.getLocalHost();
                byte[] bytes = inet.getAddress();
                String hexInetAddress = hexFormat(getInt(bytes), 8);
                String thisHashCode = hexFormat(System.identityHashCode(this), 8);
                midValue = hexInetAddress + thisHashCode;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SecureRandom oRandom = new SecureRandom();
        int node = oRandom.nextInt();

        return (hexFormat(timeLow, 8) + midValue + hexFormat(node, 8));
    }

    private static int getInt(byte[] bytes) {
        int i = 0;
        int j = 24;

        for (int k = 0; j >= 0; k++) {
            int l = bytes[k] & 0xff;
            i += (l << j);
            j -= 8;
        }

        return i;
    }

    private static String hexFormat(int i, int j) {
        String s = Integer.toHexString(i);

        return padHex(s, j) + s;
    }

    private static String padHex(String s, int i) {
        StringBuffer tmpBuffer = new StringBuffer();

        if (s.length() < i) {
            for (int j = 0; j < (i - s.length()); j++) {
                tmpBuffer.append('0');
            }
        }

        return tmpBuffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(SequenceUUID.getSequence());
    }
    
}
